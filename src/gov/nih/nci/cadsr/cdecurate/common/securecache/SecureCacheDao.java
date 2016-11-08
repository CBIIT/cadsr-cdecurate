/*L
 * Copyright Leidos
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */
package gov.nih.nci.cadsr.cdecurate.common.securecache;


import org.apache.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
/**
 * We use cache time span of 7 days.
 * 
 * @author lernerm, asafievan
 *
 */
public class SecureCacheDao
{

    private static final SecureCacheDao instance = new SecureCacheDao();
    private static final Logger logger = Logger.getLogger( SecureCacheDao.class.getName() );
    private static String _jndiName = "java:jboss/datasources/CDECurateDS";
    private static String containerUser;
    private static String containerCredential;
    private static final Object LOCK_1 = new Object() {};

    /**
     * Set up a value only if it was not set before.
     * 
     * @param containerCredential
     */
	public static void setContainerCredential(String containerCredential) {
		if (SecureCacheDao.containerCredential == null)
			synchronized(LOCK_1) {
				SecureCacheDao.containerCredential = containerCredential;
			}
	}
	/**
	 * Set up a value only if it was not set before.
	 * 
	 * @param containerUser
	 */
	public static void setContainerUser(String containerUser) {
		if (SecureCacheDao.containerUser == null)
			synchronized(LOCK_1) {
				SecureCacheDao.containerUser = containerUser;
			}
	}
	
	private static Context envContext ;
    private static DataSource dataSource;
    static {
    	initDataSource();
    }
    private static void initDataSource() {
		try {
			envContext = new InitialContext();
		} catch (NamingException e) {
			logger.fatal("Error getting InitialContext");
		}
        try {
        	if (envContext != null) 
        		dataSource = (DataSource)envContext.lookup(_jndiName);
        	else {
        		logger.fatal("InitialContext is NULL");
        	}
		} catch (NamingException e) {
			logger.fatal("Error getting DataSource");
		}
    }
    private SecureCacheDao()
    {
    }


    public static SecureCacheDao getInstance()
    {
        return instance;
    }

    /**
     * If user is in the table update, if not, add a new user record to SBREXT.USER_CACHE.
     *
     * @param userCacheData
     * @return true if there where no SQL exceptions from updateEntry or addNewEntry
     */
    public boolean cacheEntryRecord( UserCacheData userCacheData )
    {
        boolean retValue;

        // Is this user in the table?
        if( isUserInCache( userCacheData.getLoginName() ) )
        {
            retValue = updateEntry( userCacheData );
        }
        else
        {
            retValue = addNewEntry( userCacheData );
        }
        return retValue;
    }


    /**
     * Modify an existing row in the table
     * Will update credentials and salt, also sets last login and date modified to current date.
     *
     * @param userCacheData expected not null
     * @return false on sql exception, else true.
     * @TODO Check for good values in userCacheData
     */
    private boolean updateEntry( UserCacheData userCacheData )
    {
    	Connection connection = getDbConnection();
        if (connection != null) {
	        try {
		    	//Check here for good values in userCacheData
		
		        String sql = "UPDATE  SBREXT.USER_CACHE" +
		                " set CREDENTIALS = ?, " +
		                " USER_SALT = ?, " +
		                " LAST_LOGIN = sysdate," +
		                " LAST_MODIFIED_BY = user," +
		                " DATE_MODIFIED = sysdate" +
		                " WHERE UPPER(UA_NAME) = UPPER(?)";
		        
		        logger.debug("updateEntry SQL: " + sql);
		        
		        PreparedStatement ps = null;
		        try
		        {
		            ps = connection.prepareStatement( sql );
		            ps.setString( 1, userCacheData.getCacheHashData().getCredential() );
		            ps.setString( 2, userCacheData.getCacheHashData().getSalt() );
		            ps.setString( 3, userCacheData.getLoginName() );
	    			int res = ps.executeUpdate();
	    			logger.debug("addNewEntry # of updated records: " + res);
		        } catch( Exception e )
		        {
		            logger.error( "Secure Cache ERROR - updateEntry(" + userCacheData.getLoginName() + ") : " + e.toString(), e );
		            return false;
		        }
		        return true;
	        }
	        finally {
	        	try {
	        		connection.close();
	        	}
	        	catch (Exception e) {
	        		logger.error("Error in updateEntry on DB Connection closing" , e);
	        	}
	        }
        }
        else {
        	logger.error("Error in updateEntry cannot get DB Connection for user cache");
        	return false;
        }
    }


    /**
     * Add a new record to the SBREXT.USER_CACHE table.
     *
     * @param userCacheData expected not null all instance members shall be not null
     * @return false if SQL exception, otherwise true.
     * @TODO Check for good values in userCacheData
     */
    private boolean addNewEntry( UserCacheData userCacheData )
    {
        Connection connection = getDbConnection();
        if (connection != null) {
	        try {
	        //Check here for good values in userCacheData
	        // If salt was not provided, set it here
		        if( ( userCacheData.getCacheHashData().getSalt() == null ) || ( userCacheData.getCacheHashData().getSalt().isEmpty() ) )
		        {
		            userCacheData.getCacheHashData().setSalt( SecureCacheAlgorithm.generateSalt() );
		        }
		
		        String sql = "INSERT INTO SBREXT.USER_CACHE (UA_NAME, USER_CACHE_ID, CREDENTIALS, USER_SALT, LAST_LOGIN, DATE_CREATED, DATE_MODIFIED ) " +
		                " VALUES(?, SBREXT.USER_CACHE_SEQ.NEXTVAL, ?, ?, sysdate, sysdate, sysdate)";
		        
		        logger.debug("addNewEntry SQL: " + sql);
		        
		        PreparedStatement ps = null;
		        try
		        {
		            ps = connection.prepareStatement( sql );
		            ps.setString( 1, userCacheData.getLoginName().toUpperCase() );
		            ps.setString( 2, userCacheData.getCacheHashData().getCredential() );
		            ps.setString( 3, userCacheData.getCacheHashData().getSalt() );
	    			int res = ps.executeUpdate();
	    			logger.debug("addNewEntry # of inserted records: " + res);
		        } catch( Exception e )
		        {
		            logger.error( "Secure Cache ERROR - addNewEntry(" + userCacheData.getLoginName() + ") : " + e.toString(), e );
		            return false;
		        }
		        return true;
	        }
	        finally {
	        	try {
	        		connection.close();
	        	}
	        	catch (Exception e) {
	        		logger.error("Error in updateEntry on DB Connection closing" , e);
	        	}
	        }
        }
        else {
        	logger.error("Error in updateEntry cannot get DB Connection for user cache");
        	return false;
        }
    }


    /**
     * With only a username and credentials, creates a UserCacheData, then calls cacheEntryRecord( userCacheData )
     *
     * @param loginName
     * @param credentials
     * @return
     */
    public boolean cacheEntryRecord( String loginName, String pwd )
    {

        CacheHashData cacheHashData;
        try
        {
        	cacheHashData = SecureCacheAlgorithm.cacheGenerateNew(pwd);
        } catch(Exception e )
        {
        	logger.error("Error in cacheEntryRecord", e);
            return false;
        } 
        

        UserCacheData userCacheData = new UserCacheData( loginName, cacheHashData );

        return cacheEntryRecord( userCacheData );
    }

    /**
     * Populate a UserCacheData for this user
     *
     * @param loginName
     * @return UserCacheData
     */
    public UserCacheData cacheEntryRead( String loginName )
    {
        Connection connection = getDbConnection();
        if (connection != null) {
	        try {
		        UserCacheData userCacheData = null;
		        
		        String sql = "SELECT *" +
		                " FROM SBREXT.USER_CACHE" +
		                " WHERE upper(UA_NAME) = UPPER(?) and LAST_LOGIN > sysdate - 7";
		        
		        logger.debug("cacheEntryRead SQL: " + sql);
		        
		        PreparedStatement ps = null;
		        ResultSet rs = null;
		        try
		        {
		            ps = connection.prepareStatement( sql );
		            ps.setString(1, loginName);
		            ps.execute();
		            rs = ps.getResultSet();
		
		            // Populate the UserCacheData
		            if( rs.next() )
		            {
		                String credentials = rs.getString( "CREDENTIALS" );
		                String salt = rs.getString( "USER_SALT" );
		                CacheHashData cacheHashData = new CacheHashData( credentials, salt );
		                userCacheData = new UserCacheData( loginName, cacheHashData );
		                userCacheData.setLoginName( loginName );
		                userCacheData.setId( rs.getInt( "USER_CACHE_ID" ) );
		                userCacheData.setLastLogin( rs.getDate( "LAST_LOGIN" ) );
		                userCacheData.setDateCreated( rs.getDate( "DATE_CREATED" ) );
		                userCacheData.setDateModified( rs.getDate( "DATE_MODIFIED" ) );
		                logger.debug("cacheEntryRead: " + userCacheData);;
		            }
		            else
		            {
		                // This user is not in the user cache table
		                logger.info( "SecureCacheDao.cacheEntryRead did not find user \"" + loginName + "\" in SBREXT.USER_CACHE." );
		                userCacheData = null;
		            }
		        } catch( Exception e )
		        {
		            logger.error( "Secure Cache ERROR - cacheEntryRead(" + loginName + ") : " + e.toString(), e );
		        }
		        return userCacheData;
	        }
			finally {
	        	try {
	        		connection.close();
	        	}
	        	catch (Exception e) {
	        		logger.error("Error in updateEntry on DB Connection closing" , e);
	        	}
	        }
        }
        else {
        	logger.error("Error in updateEntry cannot get DB Connection for user cache");
        	return null;
        }
        
    }

    /**
     * @param loginName expected not null
     * @return Return true if the user "loginName" is in the SBREXT.USER_CACHE table, false if not, or there was an SQL exception.
     */
    private boolean isUserInCache( String loginName )
    {
    	Connection connection = getDbConnection();
    	if (connection != null) {
	    	try {
	    	String sql = "SELECT *" +
	                " FROM SBREXT.USER_CACHE" +
	                " WHERE UPPER(UA_NAME) = UPPER(?)";
	    	
	        logger.debug("isUserInCache SQL: " + sql);
	        
	        PreparedStatement ps = null;
	        try
	        {
	            ps = connection.prepareStatement( sql );
	            ps.setString(1, loginName);
	            ps.execute();
	            if( ps.getResultSet().next() )
	            {
	                return true;
	            }
	            else
	            {
	                return false;
	            }
	        } catch( Exception e )
	        {
	            logger.error( "Secure Cache ERROR - isUserInCache(" + loginName + ") : " + e.toString(), e );
	            return false;
	        }
	    	}
	    	finally {
	        	try {
	        		connection.close();
	        	}
	        	catch (Exception e) {
	        		logger.error("Error in isUserInCache on DB Connection closing" , e);
	        	}
	    	}
    	}
    	else {
	        logger.error("Error in isUserInCache cannot get DB Connection for user cache");
        	return false;
    	}
    }

    /**
     * Deletes an entry in the SBREXT.USER_CACHE table, by UA_NAME.
     *
     * @param loginName expected not null
     * @return false if there was an SQL exception, else return true.
     */
    public boolean cacheEntryDelete( String loginName )
    {
    	Connection connection = getDbConnection();
    	if (connection != null) {
	    	try {
	    		String sql = "delete FROM SBREXT.USER_CACHE  " +
                " WHERE UPPER(UA_NAME) = UPPER(?)";

	    		PreparedStatement ps = null;
	    		try
	    		{
	    			ps = connection.prepareStatement( sql );
	    			ps.setString(1, loginName);
	    			int res = ps.executeUpdate();
	    			logger.debug("cacheEntryDelete # of deleted records: " + res);
	    		} catch( SQLException e )
	    		{
	    			logger.error( "Secure Cache ERROR - cacheEntryDelete(" + loginName + ") : " + e.toString(), e );
	    			return false;
	    		}
	    		return true;
	    	}
	    	finally {
	        	try {
	        		connection.close();
	        	}
	        	catch (Exception e) {
	        		logger.error("Error in updateEntry on DB Connection closing" , e);
	        	}
	    	}
    	}
    	else {
    		logger.error("Error in cacheEntryDelete cannot get DB Connection for user cache");
        	return false;
    	}
    }

    /**
     * Sets LAST_LOGIN and DATE_MODIFIED to the current date for user "loginName"
     *
     * @param loginName
     * @return Return false if there is an SQL exception, else return true.
     */
    public boolean cacheUpdateLoginDate( String loginName )
    {
        Connection connection = getDbConnection();
        if (connection != null) {
	        try {
			    String sql = "UPDATE  SBREXT.USER_CACHE " +
			            " set LAST_LOGIN = sysdate," +
			            " LAST_MODIFIED_BY = user" +
			            " WHERE UPPER(UA_NAME) = UPPER(?)";
			    
			    logger.debug("cacheUpdateLoginDate SQL: " + sql);
			    
			    PreparedStatement ps = null;
			    try
			    {
			        ps = connection.prepareStatement( sql );
			        ps.setString( 1, loginName );
			        int res = ps.executeUpdate();
			        logger.debug("cacheUpdateLoginDate # of updated records: " + res);
			    } catch( Exception e )
			    {
			        logger.error( "ERROR - cacheUpdateLoginDate(" + loginName + ") : " + e.toString(), e );
			        return false;
			    }
			    return true;
	        	}
	finally {
	        	try {
	        		connection.close();
	        	}
	        	catch (Exception e) {
	        		logger.error("Error in updateEntry on DB Connection closing" , e);
	        	}
	        }
        }
        else {
        	logger.error("Error in updateEntry cannot get DB Connection for user cache");
        	return false;
        }      
    }
    
    public Connection getDbConnection()
    {
        try 
        {
        	if (dataSource != null)
        		return dataSource.getConnection(containerUser, containerCredential);
        	else {
        		synchronized(this) {
	        		initDataSource();
	        		if (dataSource != null) {
	        			return dataSource.getConnection();
	        		}
	        		else {
						Thread.yield();
	        			initDataSource();
	            		if (dataSource != null) {
	            			return dataSource.getConnection();
	            		}
	        		}
        		}
        	}
        	logger.error("getDbConnection - cannot get DB connection from the container; datasource received is NULL");
        	return null;
        }
        catch (Exception e) {
        	 logger.error( "ERROR - getDbConnection", e );
        	 return null;
        }
    }
}
