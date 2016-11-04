package gov.nih.nci.cadsr.cdecurate.common.securecache;

/**
 * Data as it is to be stored to DB
 */
public class CacheHashData
{
    private String credential;
    private String salt;

    public CacheHashData( String credential, String salt )
    {
        this.credential = credential;
        this.salt = salt;
    }

    public String getCredential()
    {
        return credential;
    }

    public void setCredential( String credential )
    {
        this.credential = credential;
    }

    public String getSalt()
    {
        return salt;
    }

    public void setSalt( String salt )
    {
        this.salt = salt;
    }
}
