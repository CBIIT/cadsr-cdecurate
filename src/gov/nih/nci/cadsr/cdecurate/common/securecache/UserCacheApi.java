/*L
 * Copyright Leidos
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */
package gov.nih.nci.cadsr.cdecurate.common.securecache;

import org.apache.log4j.Logger;

public class UserCacheApi
{

    static private final SecureCacheDao secureCacheDao = SecureCacheDao.getInstance();
    private static final Logger logger = Logger.getLogger(UserCacheApi.class.getName());

    public static boolean credentialValidate( String loginName, String rawCredential )
    {
        // TODO DAO call to get this users data as a CacheHashData

        // Do we have this user in the SecureCache table
        UserCacheData userCacheData = secureCacheDao.cacheEntryRead( loginName );
        if( userCacheData == null)
        {
            System.out.println( "MHL  userCacheData==null");
            // If CacheHashData is null
            return false;
        }

        // TODO check user's cached credentials
        System.out.println( "MHL  userCacheData: " + userCacheData);

        // TODO give return value meaning
        return false;
    }



    private static boolean credentialUpdate(UserCacheData userCacheData)
    {

        // TODO give return value meaning
        return false;
    }

    public static boolean credentialUpdateLoginDate(String loginName)
    {

        // TODO give return value meaning
        return false;
    }

    public static boolean credentialSave(String loginName, String rawCredential)
    {

        // TODO give return value meaning
        return false;
    }

}
