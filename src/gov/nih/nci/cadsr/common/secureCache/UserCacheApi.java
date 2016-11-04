package gov.nih.nci.cadsr.common.secureCache;

/**
 * Created by lernermh on 11/4/16.
 */
public class UserCacheApi
{

    public static boolean credentialValidate( String loginName, String rawCredential )
    {
        // TODO DAO call to get this users data as a CacheHashData

        // If CacheHashData is null


        // TODO give return value meaning
        return false;
    }



    public static boolean credentialUpdate(UserCacheData userCacheData)
    {


        // TODO give return value meaning
        return false;
    }

    public static boolean credentialUpdateLoginDate(String loginName)
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
