/*L
 * Copyright Leidos
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */
package gov.nih.nci.cadsr.cdecurate.common.securecache;

import org.apache.log4j.Logger;

/**
 * Data as it is to be stored to DB.
 */
public class CacheHashData
{
    private static final Logger logger = Logger.getLogger(CacheHashData.class.getName());
    private String credential;
    private String salt;

    public CacheHashData( String credential, String salt )
    {
    	setCredential(credential);
        setSalt(salt);
    }
    protected CacheHashData()
    {

    }

    public String getCredential()
    {
        return credential;
    }

    public void setCredential( String credential )
    {
        if ((credential != null) && (credential.length() == SecureCacheAlgorithm.CACHE_ENCODED_LENGTH)) {
        	this.credential = credential;
        }
        else {
        	//TODO do we want just write a log here and not to RuntimeException?
        	IllegalArgumentException ex = new IllegalArgumentException("CacheHashData Error: expected credential length: " + SecureCacheAlgorithm.CACHE_ENCODED_LENGTH +
        			", received string: " + credential);
        	logger.error("CacheHashData creating error", ex);
        	throw(ex);
        }
    }

    public String getSalt()
    {
        return salt;
    }

    public void setSalt( String salt )
    {
        if ((salt != null) && (salt.length() == SecureCacheAlgorithm.SALT_ENCODED_LENGTH)) {
        	this.salt = salt;
        }
        else {
        	//TODO do we want just write a log here and not to RuntimeException?
        	IllegalArgumentException ex = new IllegalArgumentException("CacheHashData Error: expected salt length: " + SecureCacheAlgorithm.SALT_ENCODED_LENGTH +
        			", received string: " + credential);
        	logger.error("CacheHashData creating error", ex);
        	throw(ex);
        }
    }

	@Override
	public String toString() {
		return "CacheHashData [credential=" + credential + ", salt=" + salt + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((credential == null) ? 0 : credential.hashCode());
		result = prime * result + ((salt == null) ? 0 : salt.hashCode());
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
		CacheHashData other = (CacheHashData) obj;
		if (credential == null) {
			if (other.credential != null)
				return false;
		} else if (!credential.equals(other.credential))
			return false;
		if (salt == null) {
			if (other.salt != null)
				return false;
		} else if (!salt.equals(other.salt))
			return false;
		return true;
	}

}
