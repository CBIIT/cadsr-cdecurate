package gov.nih.nci.cadsr.cdecurate.common.securecache;


public class UserCacheData
{
    private String loginName;
    private CacheHashData cacheHashData = null;

    public UserCacheData( String loginName, CacheHashData cacheHashData )
    {
        this.loginName = loginName;
        this.cacheHashData = cacheHashData;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName( String loginName )
    {
        this.loginName = loginName;
    }

    public CacheHashData getCacheHashData()
    {
        return cacheHashData;
    }

    public void setCacheHashData( CacheHashData cacheHashData )
    {
        this.cacheHashData = cacheHashData;
    }
}
