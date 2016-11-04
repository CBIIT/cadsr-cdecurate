/*L
 * Copyright Leidos
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */
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

	@Override
	public String toString() {
		return "UserCacheData [loginName=" + loginName + ", cacheHashData=" + cacheHashData + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cacheHashData == null) ? 0 : cacheHashData.hashCode());
		result = prime * result + ((loginName == null) ? 0 : loginName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserCacheData other = (UserCacheData) obj;
		if (cacheHashData == null) {
			if (other.cacheHashData != null)
				return false;
		} else if (!cacheHashData.equals(other.cacheHashData))
			return false;
		if (loginName == null) {
			if (other.loginName != null)
				return false;
		} else if (!loginName.equals(other.loginName))
			return false;
		return true;
	}
    
}
