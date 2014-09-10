/*
 * This file was generated - do not edit it directly !!
 * Generated by AuDAO tool, a product of Spolecne s.r.o.
 * For more information please visit http://www.spoledge.com
 */
package nci.cadsr.persist.dto;

import java.sql.Date;

import com.spoledge.audao.db.dto.AbstractDto;

/**
 * This is a DTO class.
 *
 * @author generated
 */
public class DesignationsView extends AbstractDto {

    ////////////////////////////////////////////////////////////////////////////
    // Static
    ////////////////////////////////////////////////////////////////////////////

    public static final String TABLE = "designations_view";

    ////////////////////////////////////////////////////////////////////////////
    // Attributes
    ////////////////////////////////////////////////////////////////////////////

    private String dESIGIDSEQ;
    private String aCIDSEQ;
    private String cONTEIDSEQ;
    private String nAME;
    private String dETLNAME;
    private Date dATECREATED;
    private String cREATEDBY;
    private Date dATEMODIFIED;
    private String mODIFIEDBY;
    private String lAENAME;

    private boolean isDATEMODIFIEDModified;
    private boolean isMODIFIEDBYModified;
    private boolean isLAENAMEModified;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new empty DTO.
     */
    public DesignationsView() {
    }

    ////////////////////////////////////////////////////////////////////////////
    // Public
    ////////////////////////////////////////////////////////////////////////////

    public String getDESIGIDSEQ() {
        return dESIGIDSEQ;
    }

    public void setDESIGIDSEQ( String _val) {
        this.dESIGIDSEQ = _val;
    }

    public String getACIDSEQ() {
        return aCIDSEQ;
    }

    public void setACIDSEQ( String _val) {
        this.aCIDSEQ = _val;
    }

    public String getCONTEIDSEQ() {
        return cONTEIDSEQ;
    }

    public void setCONTEIDSEQ( String _val) {
        this.cONTEIDSEQ = _val;
    }

    public String getNAME() {
        return nAME;
    }

    public void setNAME( String _val) {
        this.nAME = _val;
    }

    public String getDETLNAME() {
        return dETLNAME;
    }

    public void setDETLNAME( String _val) {
        this.dETLNAME = _val;
    }

    public Date getDATECREATED() {
        return dATECREATED;
    }

    public void setDATECREATED( java.util.Date _val ) {
        setDATECREATED((Date)( _val != null ? new Date( _val.getTime()) : null ));
    }

    public void setDATECREATED( Date _val) {
        this.dATECREATED = _val;
    }

    public String getCREATEDBY() {
        return cREATEDBY;
    }

    public void setCREATEDBY( String _val) {
        this.cREATEDBY = _val;
    }

    public Date getDATEMODIFIED() {
        return dATEMODIFIED;
    }

    public void setDATEMODIFIED( java.util.Date _val ) {
        setDATEMODIFIED((Date)( _val != null ? new Date( _val.getTime()) : null ));
    }

    public void setDATEMODIFIED( Date _val) {
        this.dATEMODIFIED = _val;
        this.isDATEMODIFIEDModified = true;
    }

    public boolean isDATEMODIFIEDModified() {
        return isDATEMODIFIEDModified;
    }

    public String getMODIFIEDBY() {
        return mODIFIEDBY;
    }

    public void setMODIFIEDBY( String _val) {
        this.mODIFIEDBY = _val;
        this.isMODIFIEDBYModified = true;
    }

    public boolean isMODIFIEDBYModified() {
        return isMODIFIEDBYModified;
    }

    public String getLAENAME() {
        return lAENAME;
    }

    public void setLAENAME( String _val) {
        this.lAENAME = _val;
        this.isLAENAMEModified = true;
    }

    public boolean isLAENAMEModified() {
        return isLAENAMEModified;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Uses 'columns' equality type.
     */
    @Override
    public boolean equals( Object _other ) {
        if (_other == this) return true;
        if (_other == null || (!(_other instanceof DesignationsView))) return false;

        DesignationsView _o = (DesignationsView) _other;

        if ( dESIGIDSEQ == null ) {
            if ( _o.dESIGIDSEQ != null ) return false;
        }
        else if ( _o.dESIGIDSEQ == null || !dESIGIDSEQ.equals( _o.dESIGIDSEQ )) return false;

        if ( aCIDSEQ == null ) {
            if ( _o.aCIDSEQ != null ) return false;
        }
        else if ( _o.aCIDSEQ == null || !aCIDSEQ.equals( _o.aCIDSEQ )) return false;

        if ( cONTEIDSEQ == null ) {
            if ( _o.cONTEIDSEQ != null ) return false;
        }
        else if ( _o.cONTEIDSEQ == null || !cONTEIDSEQ.equals( _o.cONTEIDSEQ )) return false;

        if ( nAME == null ) {
            if ( _o.nAME != null ) return false;
        }
        else if ( _o.nAME == null || !nAME.equals( _o.nAME )) return false;

        if ( dETLNAME == null ) {
            if ( _o.dETLNAME != null ) return false;
        }
        else if ( _o.dETLNAME == null || !dETLNAME.equals( _o.dETLNAME )) return false;

        if ( dATECREATED == null ) {
            if ( _o.dATECREATED != null ) return false;
        }
        else if ( _o.dATECREATED == null || dATECREATED.getTime() != _o.dATECREATED.getTime()) return false;

        if ( cREATEDBY == null ) {
            if ( _o.cREATEDBY != null ) return false;
        }
        else if ( _o.cREATEDBY == null || !cREATEDBY.equals( _o.cREATEDBY )) return false;

        if ( dATEMODIFIED == null ) {
            if ( _o.dATEMODIFIED != null ) return false;
        }
        else if ( _o.dATEMODIFIED == null || dATEMODIFIED.getTime() != _o.dATEMODIFIED.getTime()) return false;

        if ( mODIFIEDBY == null ) {
            if ( _o.mODIFIEDBY != null ) return false;
        }
        else if ( _o.mODIFIEDBY == null || !mODIFIEDBY.equals( _o.mODIFIEDBY )) return false;

        if ( lAENAME == null ) {
            if ( _o.lAENAME != null ) return false;
        }
        else if ( _o.lAENAME == null || !lAENAME.equals( _o.lAENAME )) return false;

        return true;
    }

    /**
     * Returns a hash code value for the object.
     */
    @Override
    public int hashCode() {
        int _ret = -945486783; // = "DesignationsView".hashCode()
        _ret += dESIGIDSEQ == null ? 0 : dESIGIDSEQ.hashCode();
        _ret = 29 * _ret + (aCIDSEQ == null ? 0 : aCIDSEQ.hashCode());
        _ret = 29 * _ret + (cONTEIDSEQ == null ? 0 : cONTEIDSEQ.hashCode());
        _ret = 29 * _ret + (nAME == null ? 0 : nAME.hashCode());
        _ret = 29 * _ret + (dETLNAME == null ? 0 : dETLNAME.hashCode());
        _ret = 29 * _ret + (dATECREATED == null ? 0 : (int)dATECREATED.getTime());
        _ret = 29 * _ret + (cREATEDBY == null ? 0 : cREATEDBY.hashCode());
        _ret = 29 * _ret + (dATEMODIFIED == null ? 0 : (int)dATEMODIFIED.getTime());
        _ret = 29 * _ret + (mODIFIEDBY == null ? 0 : mODIFIEDBY.hashCode());
        _ret = 29 * _ret + (lAENAME == null ? 0 : lAENAME.hashCode());

        return _ret;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Protected
    ////////////////////////////////////////////////////////////////////////////
		
    /**
     * Constructs the content for the toString() method.
     */
    protected void contentToString(StringBuffer sb) {
        append( sb, "dESIGIDSEQ", dESIGIDSEQ );
        append( sb, "aCIDSEQ", aCIDSEQ );
        append( sb, "cONTEIDSEQ", cONTEIDSEQ );
        append( sb, "nAME", nAME );
        append( sb, "dETLNAME", dETLNAME );
        append( sb, "dATECREATED", dATECREATED );
        append( sb, "cREATEDBY", cREATEDBY );
        append( sb, "dATEMODIFIED", dATEMODIFIED );
        append( sb, "mODIFIEDBY", mODIFIEDBY );
        append( sb, "lAENAME", lAENAME );
    }
}
