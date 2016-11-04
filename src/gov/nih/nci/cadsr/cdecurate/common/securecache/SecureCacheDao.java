package gov.nih.nci.cadsr.cdecurate.common.securecache;


import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SecureCacheDao
{

    private static final SecureCacheDao instance = new SecureCacheDao();

    private SecureCacheDao()
    {
    }

    private static final Logger logger = Logger.getLogger( SecureCacheDao.class.getName() );

    public static SecureCacheDao getInstance()
    {
        return instance;
    }


    private Connection connection = null;

    public boolean cacheEntryRecord( UserCacheData userCacheData )
    {

        // TODO - return false is a place holder for now
        return false;
    }

    /**
     * Populate a UserCacheData for this user
     *
     * @param loginName
     * @return
     */
    public UserCacheData cacheEntryRead( String loginName )
    {
        String sql = "SELECT *" +
                " FROM SBREXT.USER_CACHE " +
                " WHERE (UA_NAME = '" + loginName + "')";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            ps = connection.prepareStatement( sql );
            ps.execute();
            rs = ps.getResultSet();
            while( rs.next() )
            {
                System.out.println( "MHL rs: " + rs.getString( "CREDENTIALS" ) );
            }

        } catch( Exception e )
        {
            System.err.println( "ERROR - cacheEntryRead(" + loginName + ") : " + e.toString() );
            logger.error( "ERROR - cacheEntryRead(" + loginName + ") : " + e.toString(), e );
        }
        return null;
    }

    public boolean cacheEntryDelete( String loginName )
    {

        // TODO - return false is a place holder for now
        return false;
    }

    public boolean cacheUpdateLoginDate( String loginName )
    {

        // TODO - return false is a place holder for now
        return false;
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
