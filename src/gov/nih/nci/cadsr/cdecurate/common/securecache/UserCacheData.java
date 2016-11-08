/*L
 * Copyright Leidos
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */
package gov.nih.nci.cadsr.cdecurate.common.securecache;


import java.sql.Date;

public class UserCacheData
{
    private String loginName;
    private CacheHashData cacheHashData = null; //credential and salt
    private int id;
    private java.sql.Date lastLogin = null;
    private java.sql.Date dateCreated = null;
    private java.sql.Date dateModified = null;

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

    public Date getLastLogin()
    {
        return lastLogin;
    }

    public void setLastLogin( Date lastLogin )
    {
        this.lastLogin = lastLogin;
    }

    public Date getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated( Date dateCreated )
    {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified()
    {
        return dateModified;
    }

    public void setDateModified( Date dateModified )
    {
        this.dateModified = dateModified;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "UserCacheData [" +
                "loginName='" + loginName +  ", cacheHashData=" + cacheHashData + ", id=" + id + ", lastLogin=" + lastLogin + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified +
                ']';
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
