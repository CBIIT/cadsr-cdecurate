/*
 * This file was generated - do not edit it directly !!
 * Generated by AuDAO tool, a product of Spolecne s.r.o.
 * For more information please visit http://www.spoledge.com
 */
package nci.cadsr.persist.dao.oracle;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;

import com.spoledge.audao.db.dao.AbstractDaoImpl;
import com.spoledge.audao.db.dao.DBException;
import com.spoledge.audao.db.dao.DaoException;


import nci.cadsr.persist.dao.PermissibleValuesViewDao;
import nci.cadsr.persist.dto.PermissibleValuesView;


/**
 * This is the DAO imlementation class.
 *
 * @author generated
 */
public class PermissibleValuesViewDaoImpl extends AbstractDaoImpl<PermissibleValuesView> implements PermissibleValuesViewDao {

    private static final String TABLE_NAME = "permissible_values_view";

    protected static final String SELECT_COLUMNS = "PV_IDSEQ, VALUE, SHORT_MEANING, MEANING_DESCRIPTION, BEGIN_DATE, END_DATE, HIGH_VALUE_NUM, LOW_VALUE_NUM, DATE_CREATED, CREATED_BY, DATE_MODIFIED, MODIFIED_BY, VM_IDSEQ";

    protected static final String PK_CONDITION = "PV_IDSEQ=?";

    private static final String SQL_INSERT = "INSERT INTO permissible_values_view (PV_IDSEQ, VALUE, SHORT_MEANING, MEANING_DESCRIPTION, BEGIN_DATE, END_DATE, HIGH_VALUE_NUM, LOW_VALUE_NUM, DATE_CREATED, CREATED_BY, DATE_MODIFIED, MODIFIED_BY, VM_IDSEQ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public PermissibleValuesViewDaoImpl( Connection conn ) {
        super( conn );
    }

    /**
     * Finds a record identified by its primary key.
     * @return the record found or null
     */
    public PermissibleValuesView findByPrimaryKey( String pVIDSEQ ) {
        return findOne( PK_CONDITION, pVIDSEQ);
    }

    /**
     * Finds a record.
     */
    public PermissibleValuesView findByVALUE( String vALUE ) {
        return findOne( "VALUE=?", vALUE);
    }

    /**
     * Finds records ordered by VALUE.
     */
    public PermissibleValuesView[] findAll( ) {
        return findManyArray( "1=1 ORDER BY VALUE", 0, -1);
    }

    /**
     * Deletes a record identified by its primary key.
     * @return true iff the record was really deleted (existed)
     */
    public boolean deleteByPrimaryKey( String pVIDSEQ ) throws DaoException {
        return deleteOne( PK_CONDITION, pVIDSEQ);
    }

    /**
     * Inserts a new record.
     * @return the generated primary key - pVIDSEQ
     */
    public String insert( PermissibleValuesView dto ) throws DaoException {
        PreparedStatement stmt = null;
        debugSql( SQL_INSERT, dto );

        try {
            stmt = conn.prepareStatement( SQL_INSERT );

            if ( dto.getPVIDSEQ() == null || dto.getPVIDSEQ().length() == 0 ) {
                throw new DaoException("Value of column 'PV_IDSEQ' cannot be null");
            }

            if ( dto.getPVIDSEQ() != null ) {
                checkMaxLength( "PV_IDSEQ", dto.getPVIDSEQ(), 36 );
            }
            stmt.setString( 1, dto.getPVIDSEQ() );

            if ( dto.getVALUE() == null || dto.getVALUE().length() == 0 ) {
                throw new DaoException("Value of column 'VALUE' cannot be null");
            }
            checkMaxLength( "VALUE", dto.getVALUE(), 255 );
            stmt.setString( 2, dto.getVALUE() );

            if ( dto.getSHORTMEANING() == null || dto.getSHORTMEANING().length() == 0 ) {
                throw new DaoException("Value of column 'SHORT_MEANING' cannot be null");
            }
            checkMaxLength( "SHORT_MEANING", dto.getSHORTMEANING(), 255 );
            stmt.setString( 3, dto.getSHORTMEANING() );

            if ( dto.getMEANINGDESCRIPTION() != null ) {
                checkMaxLength( "MEANING_DESCRIPTION", dto.getMEANINGDESCRIPTION(), 2000 );
            }
            stmt.setString( 4, dto.getMEANINGDESCRIPTION() );
            stmt.setDate( 5, dto.getBEGINDATE() );
            stmt.setDate( 6, dto.getENDDATE() );

            if ( dto.getHIGHVALUENUM() == null ) {
                stmt.setNull( 7, Types.BIGINT );
            }
            else {
                stmt.setLong( 7, dto.getHIGHVALUENUM() );
            }

            if ( dto.getLOWVALUENUM() == null ) {
                stmt.setNull( 8, Types.BIGINT );
            }
            else {
                stmt.setLong( 8, dto.getLOWVALUENUM() );
            }
            stmt.setDate( 9, dto.getDATECREATED() );

            if ( dto.getCREATEDBY() != null ) {
                checkMaxLength( "CREATED_BY", dto.getCREATEDBY(), 30 );
            }
            stmt.setString( 10, dto.getCREATEDBY() );
            stmt.setDate( 11, dto.getDATEMODIFIED() );

            if ( dto.getMODIFIEDBY() != null ) {
                checkMaxLength( "MODIFIED_BY", dto.getMODIFIEDBY(), 30 );
            }
            stmt.setString( 12, dto.getMODIFIEDBY() );

            if ( dto.getVMIDSEQ() != null ) {
                checkMaxLength( "VM_IDSEQ", dto.getVMIDSEQ(), 36 );
            }
            stmt.setString( 13, dto.getVMIDSEQ() );

            int n = stmt.executeUpdate();

            return dto.getPVIDSEQ();
        }
        catch (SQLException e) {
            errorSql( e, SQL_INSERT, dto );
            handleException( e );
            throw new DBException( e );
        }
        finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) {}
        }
    }

    /**
     * Updates one record found by primary key.
     * @return true iff the record was really updated (=found and any change was really saved)
     */
    public boolean update( String pVIDSEQ, PermissibleValuesView dto ) throws DaoException {
        StringBuffer sb = new StringBuffer();
        ArrayList<Object> params = new ArrayList<Object>();

        if ( dto.getPVIDSEQ() != null ) {
            checkMaxLength( "PV_IDSEQ", dto.getPVIDSEQ(), 36 );
            sb.append( "PV_IDSEQ=?" );
            params.add( dto.getPVIDSEQ());
        }

        if ( dto.getVALUE() != null ) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            checkMaxLength( "VALUE", dto.getVALUE(), 255 );
            sb.append( "VALUE=?" );
            params.add( dto.getVALUE());
        }

        if ( dto.getSHORTMEANING() != null ) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            checkMaxLength( "SHORT_MEANING", dto.getSHORTMEANING(), 255 );
            sb.append( "SHORT_MEANING=?" );
            params.add( dto.getSHORTMEANING());
        }

        if ( dto.isMEANINGDESCRIPTIONModified()) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            if ( dto.getMEANINGDESCRIPTION() == null ) {
                sb.append( "MEANING_DESCRIPTION=NULL" );
            }
            else {
                checkMaxLength( "MEANING_DESCRIPTION", dto.getMEANINGDESCRIPTION(), 2000 );
                sb.append( "MEANING_DESCRIPTION=?" );
                params.add( dto.getMEANINGDESCRIPTION());
            }
        }

        if ( dto.isBEGINDATEModified()) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            if ( dto.getBEGINDATE() == null ) {
                sb.append( "BEGIN_DATE=NULL" );
            }
            else {
                sb.append( "BEGIN_DATE=?" );
                params.add( dto.getBEGINDATE());
            }
        }

        if ( dto.isENDDATEModified()) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            if ( dto.getENDDATE() == null ) {
                sb.append( "END_DATE=NULL" );
            }
            else {
                sb.append( "END_DATE=?" );
                params.add( dto.getENDDATE());
            }
        }

        if ( dto.isHIGHVALUENUMModified()) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            if ( dto.getHIGHVALUENUM() == null ) {
                sb.append( "HIGH_VALUE_NUM=NULL" );
            }
            else {
                sb.append( "HIGH_VALUE_NUM=?" );
                params.add( dto.getHIGHVALUENUM());
            }
        }

        if ( dto.isLOWVALUENUMModified()) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            if ( dto.getLOWVALUENUM() == null ) {
                sb.append( "LOW_VALUE_NUM=NULL" );
            }
            else {
                sb.append( "LOW_VALUE_NUM=?" );
                params.add( dto.getLOWVALUENUM());
            }
        }

        if ( dto.isDATECREATEDModified()) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            if ( dto.getDATECREATED() == null ) {
                sb.append( "DATE_CREATED=NULL" );
            }
            else {
                sb.append( "DATE_CREATED=?" );
                params.add( dto.getDATECREATED());
            }
        }

        if ( dto.isCREATEDBYModified()) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            if ( dto.getCREATEDBY() == null ) {
                sb.append( "CREATED_BY=NULL" );
            }
            else {
                checkMaxLength( "CREATED_BY", dto.getCREATEDBY(), 30 );
                sb.append( "CREATED_BY=?" );
                params.add( dto.getCREATEDBY());
            }
        }

        if ( dto.isDATEMODIFIEDModified()) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            if ( dto.getDATEMODIFIED() == null ) {
                sb.append( "DATE_MODIFIED=NULL" );
            }
            else {
                sb.append( "DATE_MODIFIED=?" );
                params.add( dto.getDATEMODIFIED());
            }
        }

        if ( dto.isMODIFIEDBYModified()) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            if ( dto.getMODIFIEDBY() == null ) {
                sb.append( "MODIFIED_BY=NULL" );
            }
            else {
                checkMaxLength( "MODIFIED_BY", dto.getMODIFIEDBY(), 30 );
                sb.append( "MODIFIED_BY=?" );
                params.add( dto.getMODIFIEDBY());
            }
        }

        if ( dto.isVMIDSEQModified()) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            if ( dto.getVMIDSEQ() == null ) {
                sb.append( "VM_IDSEQ=NULL" );
            }
            else {
                checkMaxLength( "VM_IDSEQ", dto.getVMIDSEQ(), 36 );
                sb.append( "VM_IDSEQ=?" );
                params.add( dto.getVMIDSEQ());
            }
        }

        if (sb.length() == 0) {
            return false;
        }

        params.add( pVIDSEQ );

        Object[] oparams = new Object[ params.size() ];

        return updateOne( sb.toString(), PK_CONDITION, params.toArray( oparams ));
    }

    /**
     * Returns the table name.
     */
    public String getTableName() {
        return TABLE_NAME;
    }

    protected String getSelectColumns() {
        return SELECT_COLUMNS;
    }

    protected PermissibleValuesView fetch( ResultSet rs ) throws SQLException {
        PermissibleValuesView dto = new PermissibleValuesView();
        dto.setPVIDSEQ( rs.getString( 1 ));
        dto.setVALUE( rs.getString( 2 ));
        dto.setSHORTMEANING( rs.getString( 3 ));
        dto.setMEANINGDESCRIPTION( rs.getString( 4 ));
        dto.setBEGINDATE( rs.getDate( 5 ));
        dto.setENDDATE( rs.getDate( 6 ));
        dto.setHIGHVALUENUM( rs.getLong( 7 ));

        if ( rs.wasNull()) {
            dto.setHIGHVALUENUM( null );
        }

        dto.setLOWVALUENUM( rs.getLong( 8 ));

        if ( rs.wasNull()) {
            dto.setLOWVALUENUM( null );
        }

        dto.setDATECREATED( rs.getDate( 9 ));
        dto.setCREATEDBY( rs.getString( 10 ));
        dto.setDATEMODIFIED( rs.getDate( 11 ));
        dto.setMODIFIEDBY( rs.getString( 12 ));
        dto.setVMIDSEQ( rs.getString( 13 ));

        return dto;
    }

    protected PermissibleValuesView[] toArray(ArrayList<PermissibleValuesView> list ) {
        PermissibleValuesView[] ret = new PermissibleValuesView[ list.size() ];
        return list.toArray( ret );
    }

}
