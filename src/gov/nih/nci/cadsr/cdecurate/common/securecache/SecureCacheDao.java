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

public class SecureCacheDao
{

    private static final SecureCacheDao instance = new SecureCacheDao();
    private Connection connection = null;
    private static final Logger logger = Logger.getLogger( SecureCacheDao.class.getName() );

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
     * @param userCacheData
     * @return false on sql exception, else true.
     * @TODO Check for good values in userCacheData
     */
    private boolean updateEntry( UserCacheData userCacheData )
    {
        // TODO Check here for good values in userCacheData

        // Get current date for LAST_LOGIN and DATE_MODIFIED
        java.sql.Date currentDate = new java.sql.Date( Calendar.getInstance().getTimeInMillis() );

        String sql = "UPDATE  SBREXT.USER_CACHE " +
                " set CREDENTIALS = '" + userCacheData.getCacheHashData().getCredential() + "'," +
                " USER_SALT = '" + userCacheData.getCacheHashData().getSalt() + "', " +
                " LAST_LOGIN = ? ," +
                " DATE_MODIFIED = ? " +
                " WHERE UA_NAME = '" + userCacheData.getLoginName() + "'";

        PreparedStatement ps = null;
        try
        {
            ps = connection.prepareStatement( sql );
            ps.setDate( 1, currentDate ); // LAST_LOGIN
            ps.setDate( 2, currentDate ); // DATE_MODIFIED
            ps.execute();
        } catch( Exception e )
        {
            logger.error( "Secure Cache ERROR - updateEntry(" + userCacheData.getLoginName() + ") : " + e.toString(), e );
            return false;
        }
        return true;
    }


    /**
     * Add a new record to the SBREXT.USER_CACHE table.
     *
     * @param userCacheData
     * @return false if SQL exception, otherwise true.
     * @TODO Check for good values in userCacheData
     */
    private boolean addNewEntry( UserCacheData userCacheData )
    {

        // TODO Check here for good values in userCacheData
        // If salt was not provided, set it here
        if( ( userCacheData.getCacheHashData().getSalt() == null ) || ( userCacheData.getCacheHashData().getSalt().isEmpty() ) )
        {
            userCacheData.getCacheHashData().setSalt( SecureCacheAlgorithm.generateSalt() );
        }

        java.sql.Date currentDate = new java.sql.Date( Calendar.getInstance().getTimeInMillis() );

        // Get the highest value in the USER_CACHE_ID column in the SBREXT.USER_CACHE table. We will add one to this value for the new ID.
        int highId = getHighestId();

        String sql = "INSERT INTO SBREXT.USER_CACHE (UA_NAME, USER_CACHE_ID, CREDENTIALS, USER_SALT, LAST_LOGIN, DATE_CREATED, DATE_MODIFIED ) " +
                " VALUES('" + userCacheData.getLoginName() + "'," + ( highId + 1 ) +
                ", '" + userCacheData.getCacheHashData().getCredential() + "','" + userCacheData.getCacheHashData().getSalt() + "', ?, ?, ? )";
        PreparedStatement ps = null;
        try
        {
            ps = connection.prepareStatement( sql );
            ps.setDate( 1, currentDate ); //LAST_LOGIN
            ps.setDate( 2, currentDate ); //DATE_CREATED
            ps.setDate( 3, currentDate ); //DATE_MODIFIED
            ps.execute();
        } catch( Exception e )
        {
            logger.error( "Secure Cache ERROR - addNewEntry(" + userCacheData.getLoginName() + ") : " + e.toString(), e );
            return false;
        }
        return true;
    }


    /**
     * With only a username and password, creates a UserCacheData, then calls cacheEntryRecord( userCacheData )
     *
     * @param loginName
     * @param password
     * @return
     */
    public boolean cacheEntryRecord( String loginName, String password )
    {
        String salt = "";
        String cred = "";

        try
        {
            salt = SecureCacheAlgorithm.generateSalt();
            cred = SecureCacheAlgorithm.cacheCalculate( password, salt );
        } catch( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        } catch( InvalidKeySpecException e )
        {
            e.printStackTrace();
        }
        CacheHashData cacheHashData = new CacheHashData( cred, salt );
        UserCacheData userCacheData = new UserCacheData( loginName, cacheHashData );

        return cacheEntryRecord( userCacheData );
    }

    /**
     * Populate a UserCacheData for this user
     *
     * @param loginName
     * @return
     */
    public UserCacheData cacheEntryRead( String loginName )
    {
        UserCacheData userCacheData = null;

        String sql = "SELECT *" +
                " FROM SBREXT.USER_CACHE " +
                " WHERE UA_NAME = '" + loginName + "'";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = connection.prepareStatement( sql );
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

    public boolean validate( String loginName, String password )
    {
        UserCacheData userCacheData = cacheEntryRead( loginName );
        String credentials;

        try
        {
            credentials = SecureCacheAlgorithm.cacheCalculate( password, userCacheData.getCacheHashData().getSalt() );
        } catch( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
            return false;
        } catch( InvalidKeySpecException e )
        {
            e.printStackTrace();
            return false;
        }

        if( !credentials.equals( userCacheData.getCacheHashData().getCredential() ) )
        {
            logger.info( "Secure Cache - Bad validate: " + loginName );
            return false;
        }

        // If we get here, we have a good login.
        return true;
    }

    /**
     * @param loginName
     * @return Return true if the user "loginName" is in the SBREXT.USER_CACHE table, false if not, or there was an SQL exception.
     */
    private boolean isUserInCache( String loginName )
    {
        String sql = "SELECT *" +
                " FROM SBREXT.USER_CACHE " +
                " WHERE UA_NAME = '" + loginName + "'";

        PreparedStatement ps = null;
        try
        {
            ps = connection.prepareStatement( sql );
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

    /**
     * Deletes an entry in the SBREXT.USER_CACHE table, by UA_NAME.
     *
     * @param loginName
     * @return false if there was an SQL exception, else return true.
     */
    public boolean cacheEntryDelete( String loginName )
    {
        String sql = "delete FROM SBREXT.USER_CACHE  " +
                " WHERE UA_NAME = '" + loginName + "'";

        PreparedStatement ps = null;
        try
        {
            ps = connection.prepareStatement( sql );
            ps.execute();
        } catch( SQLException e )
        {
            logger.error( "Secure Cache ERROR - cacheEntryRead(" + loginName + ") : " + e.toString(), e );
            return false;
        }
        return true;
    }

    /**
     * Sets LAST_LOGIN and DATE_MODIFIED to the current date for user "loginName"
     *
     * @param loginName
     * @return Return false if there is an SQL exception, else return true.
     */
    public boolean cacheUpdateLoginDate( String loginName )
    {
        java.sql.Date currentDate = new java.sql.Date( Calendar.getInstance().getTimeInMillis() );

        String sql = "UPDATE  SBREXT.USER_CACHE " +
                " set  LAST_LOGIN = ? ," +
                " DATE_MODIFIED = ? " +
                " WHERE UA_NAME = '" + loginName + "'";

        PreparedStatement ps = null;
        try
        {
            ps = connection.prepareStatement( sql );
            ps.setDate( 1, currentDate );
            ps.setDate( 2, currentDate );
            ps.execute();
        } catch( Exception e )
        {
            logger.error( "ERROR - cacheUpdateLoginDate(" + loginName + ") : " + e.toString(), e );
            return false;
        }
        return true;
    }

    /**
     * Returns the highest ID in use, will return 0 if there are no entries in the USER_CACHE table.
     *
     * @return the highest value in the USER_CACHE_ID column of the USER_CACHE table.
     */
    private int getHighestId()
    {
        String sql = "SELECT MAX(USER_CACHE_ID) as max FROM SBREXT.USER_CACHE";
        int max = -1;
        PreparedStatement ps;
        ResultSet rs;
        try
        {
            ps = connection.prepareStatement( sql );
            ps.execute();
            rs = ps.getResultSet();
            if( rs.next() )
            {
                max = rs.getInt( "max" );
            }
            else
            {
                // I think if there are no rows in the table, rs.getInt( "max" ) will return 0, so we should never get here.
                logger.info( "No value returned from getHighestId" );
            }
        } catch( Exception e )
        {
            logger.error( "ERROR - getHighestId " + e.toString(), e );
        }
        return max;
    }


    public Connection getConnection()
    {
        return connection;
    }


    public void setConnection( Connection connection )
    {
        this.connection = connection;
    }
}
