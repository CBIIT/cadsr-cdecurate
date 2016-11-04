package gov.nih.nci.cadsr.cdecurate.common.securecache;


public class UserCacheApi
{

    static private SecureCacheDao secureCacheDao = SecureCacheDao.getInstance();
    public static boolean credentialValidate( String loginName, String rawCredential )
    {
        // TODO DAO call to get this users data as a CacheHashData

        // Do we have this user in the SecureCache table
        if( secureCacheDao.cacheEntryRead( loginName ) == null)
        {
            // If CacheHashData is null
            return false;
        }

        // TODO check user's cached credentials


        // TODO give return value meaning
        return false;
    }



    public static boolean credentialUpdate(UserCacheData userCacheData)
    {

        // TODO give return value meaning
        return false;
    }

    private static boolean credentialUpdateLoginDate(String loginName)
    {

        // TODO give return value meaning
        return false;
    }

    private boolean credentialSave(String loginName, String rawCredential)
    {

        // TODO give return value meaning
        return false;
    }
}
