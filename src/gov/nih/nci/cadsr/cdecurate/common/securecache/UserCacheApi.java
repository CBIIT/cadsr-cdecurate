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
    private static final Logger logger = Logger.getLogger( UserCacheApi.class.getName() );


    /**
     * If the user is not in the table, or name/password where not valid, return false.
     * Return true if name/password are valid
     *
     * @param loginName
     * @param rawCredential
     * @return true if valid
     */
    public static boolean credentialValidate( String loginName, String rawCredential )
    {
        // Do we have this user in the SecureCache table
        UserCacheData userCacheData = secureCacheDao.cacheEntryRead( loginName );
        if( userCacheData == null )
        {
            // If CacheHashData is null, we do not have this user in the cache yet.
            return false;
        }

        // The user exists, now validate
        return secureCacheDao.validate( loginName, rawCredential );
    }


    /**
     * Calls the DAO's cacheEntryRecord method, which updates the table SBREXT.USER_CACHE, if the user exists in the table, if not,
     * adds a new entry.
     *
     * @param userCacheData
     * @return true if there where no SQL exceptions from updateEntry or addNewEntry
     */
    public static boolean credentialUpdate( UserCacheData userCacheData )
    {
        return secureCacheDao.cacheEntryRecord( userCacheData );
    }

    public static boolean credentialUpdateLoginDate( String loginName )
    {
        secureCacheDao.cacheUpdateLoginDate( loginName );

        // TODO give return value meaning
        return false;
    }


    /**
     * Updates an existing user's data, or, if the user does not exist, add the user
     *
     * @param loginName
     * @param rawCredential
     * @return false if there was an error from the DAO
     */
    public static boolean credentialSave( String loginName, String rawCredential )
    {
        // Return false on error
        return secureCacheDao.cacheEntryRecord( loginName, rawCredential );
    }

    /**
     * Removes this user from the database table
     *
     * @param loginName
     * @return  false if there was an error accessing the database.
     */
    public static boolean credentialDelete( String loginName )
    {
        boolean returnValue = secureCacheDao.cacheEntryDelete( loginName );
        // TODO maybe log?
        return returnValue;
    }

}
