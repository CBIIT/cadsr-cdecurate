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


import nci.cadsr.persist.dao.DesignationsViewDao;
import nci.cadsr.persist.dto.DesignationsView;


/**
 * This is the DAO imlementation class.
 *
 * @author generated
 */
public class DesignationsViewDaoImpl extends AbstractDaoImpl<DesignationsView> implements DesignationsViewDao {

    private static final String TABLE_NAME = "designations_view";

    protected static final String SELECT_COLUMNS = "DESIG_IDSEQ, AC_IDSEQ, CONTE_IDSEQ, NAME, DETL_NAME, DATE_CREATED, CREATED_BY, DATE_MODIFIED, MODIFIED_BY, LAE_NAME";

    protected static final String PK_CONDITION = "DESIG_IDSEQ=?";

    private static final String SQL_INSERT = "INSERT INTO designations_view (DESIG_IDSEQ, AC_IDSEQ, CONTE_IDSEQ, NAME, DETL_NAME, DATE_CREATED, CREATED_BY, DATE_MODIFIED, MODIFIED_BY, LAE_NAME) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public DesignationsViewDaoImpl( Connection conn ) {
        super( conn );
    }

    /**
     * Finds a record identified by its primary key.
     * @return the record found or null
     */
    public DesignationsView findByPrimaryKey( String dESIGIDSEQ ) {
        return findOne( PK_CONDITION, dESIGIDSEQ);
    }

    /**
     * Finds a record.
     */
    public DesignationsView findByNAME( String nAME ) {
        return findOne( "NAME=?", nAME);
    }

    /**
     * Finds records ordered by NAME.
     */
    public DesignationsView[] findAll( ) {
        return findManyArray( "1=1 ORDER BY NAME", 0, -1);
    }

    /**
     * Deletes a record identified by its primary key.
     * @return true iff the record was really deleted (existed)
     */
    public boolean deleteByPrimaryKey( String dESIGIDSEQ ) throws DaoException {
        return deleteOne( PK_CONDITION, dESIGIDSEQ);
    }

    /**
     * Inserts a new record.
     * @return the generated primary key - dESIGIDSEQ
     */
    public String insert( DesignationsView dto ) throws DaoException {
        PreparedStatement stmt = null;
        debugSql( SQL_INSERT, dto );

        try {
            stmt = conn.prepareStatement( SQL_INSERT );

            if ( dto.getDESIGIDSEQ() == null || dto.getDESIGIDSEQ().length() == 0 ) {
                throw new DaoException("Value of column 'DESIG_IDSEQ' cannot be null");
            }

            if ( dto.getDESIGIDSEQ() != null ) {
                checkMaxLength( "DESIG_IDSEQ", dto.getDESIGIDSEQ(), 36 );
            }
            stmt.setString( 1, dto.getDESIGIDSEQ() );

            if ( dto.getACIDSEQ() == null || dto.getACIDSEQ().length() == 0 ) {
                throw new DaoException("Value of column 'AC_IDSEQ' cannot be null");
            }
            checkMaxLength( "AC_IDSEQ", dto.getACIDSEQ(), 36 );
            stmt.setString( 2, dto.getACIDSEQ() );

            if ( dto.getCONTEIDSEQ() == null || dto.getCONTEIDSEQ().length() == 0 ) {
                throw new DaoException("Value of column 'CONTE_IDSEQ' cannot be null");
            }
            checkMaxLength( "CONTE_IDSEQ", dto.getCONTEIDSEQ(), 36 );
            stmt.setString( 3, dto.getCONTEIDSEQ() );

            if ( dto.getNAME() == null || dto.getNAME().length() == 0 ) {
                throw new DaoException("Value of column 'NAME' cannot be null");
            }
            checkMaxLength( "NAME", dto.getNAME(), 2000 );
            stmt.setString( 4, dto.getNAME() );

            if ( dto.getDETLNAME() == null || dto.getDETLNAME().length() == 0 ) {
                throw new DaoException("Value of column 'DETL_NAME' cannot be null");
            }
            checkMaxLength( "DETL_NAME", dto.getDETLNAME(), 20 );
            stmt.setString( 5, dto.getDETLNAME() );

            if ( dto.getDATECREATED() == null ) {
                dto.setDATECREATED( new Date( System.currentTimeMillis()));
            }
            stmt.setDate( 6, dto.getDATECREATED() );

            if ( dto.getCREATEDBY() == null || dto.getCREATEDBY().length() == 0 ) {
                throw new DaoException("Value of column 'CREATED_BY' cannot be null");
            }
            checkMaxLength( "CREATED_BY", dto.getCREATEDBY(), 30 );
            stmt.setString( 7, dto.getCREATEDBY() );

            if ( dto.getDATEMODIFIED() == null ) {
                dto.setDATEMODIFIED( new Date( System.currentTimeMillis()));
            }
            stmt.setDate( 8, dto.getDATEMODIFIED() );

            if ( dto.getMODIFIEDBY() != null ) {
                checkMaxLength( "MODIFIED_BY", dto.getMODIFIEDBY(), 30 );
            }
            stmt.setString( 9, dto.getMODIFIEDBY() );

            if ( dto.getLAENAME() != null ) {
                checkMaxLength( "LAE_NAME", dto.getLAENAME(), 30 );
            }
            stmt.setString( 10, dto.getLAENAME() );

            int n = stmt.executeUpdate();

            return dto.getDESIGIDSEQ();
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
    public boolean update( String dESIGIDSEQ, DesignationsView dto ) throws DaoException {
        StringBuffer sb = new StringBuffer();
        ArrayList<Object> params = new ArrayList<Object>();

        if ( dto.getDESIGIDSEQ() != null ) {
            checkMaxLength( "DESIG_IDSEQ", dto.getDESIGIDSEQ(), 36 );
            sb.append( "DESIG_IDSEQ=?" );
            params.add( dto.getDESIGIDSEQ());
        }

        if ( dto.getACIDSEQ() != null ) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            checkMaxLength( "AC_IDSEQ", dto.getACIDSEQ(), 36 );
            sb.append( "AC_IDSEQ=?" );
            params.add( dto.getACIDSEQ());
        }

        if ( dto.getCONTEIDSEQ() != null ) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            checkMaxLength( "CONTE_IDSEQ", dto.getCONTEIDSEQ(), 36 );
            sb.append( "CONTE_IDSEQ=?" );
            params.add( dto.getCONTEIDSEQ());
        }

        if ( dto.getNAME() != null ) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            checkMaxLength( "NAME", dto.getNAME(), 2000 );
            sb.append( "NAME=?" );
            params.add( dto.getNAME());
        }

        if ( dto.getDETLNAME() != null ) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            checkMaxLength( "DETL_NAME", dto.getDETLNAME(), 20 );
            sb.append( "DETL_NAME=?" );
            params.add( dto.getDETLNAME());
        }

        if ( dto.getDATECREATED() != null ) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            sb.append( "DATE_CREATED=?" );
            params.add( dto.getDATECREATED());
        }

        if ( dto.getCREATEDBY() != null ) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            checkMaxLength( "CREATED_BY", dto.getCREATEDBY(), 30 );
            sb.append( "CREATED_BY=?" );
            params.add( dto.getCREATEDBY());
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

        if ( dto.isLAENAMEModified()) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            if ( dto.getLAENAME() == null ) {
                sb.append( "LAE_NAME=NULL" );
            }
            else {
                checkMaxLength( "LAE_NAME", dto.getLAENAME(), 30 );
                sb.append( "LAE_NAME=?" );
                params.add( dto.getLAENAME());
            }
        }

        if (sb.length() == 0) {
            return false;
        }

        params.add( dESIGIDSEQ );

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

    protected DesignationsView fetch( ResultSet rs ) throws SQLException {
        DesignationsView dto = new DesignationsView();
        dto.setDESIGIDSEQ( rs.getString( 1 ));
        dto.setACIDSEQ( rs.getString( 2 ));
        dto.setCONTEIDSEQ( rs.getString( 3 ));
        dto.setNAME( rs.getString( 4 ));
        dto.setDETLNAME( rs.getString( 5 ));
        dto.setDATECREATED( rs.getDate( 6 ));
        dto.setCREATEDBY( rs.getString( 7 ));
        dto.setDATEMODIFIED( rs.getDate( 8 ));
        dto.setMODIFIEDBY( rs.getString( 9 ));
        dto.setLAENAME( rs.getString( 10 ));

        return dto;
    }

    protected DesignationsView[] toArray(ArrayList<DesignationsView> list ) {
        DesignationsView[] ret = new DesignationsView[ list.size() ];
        return list.toArray( ret );
    }

}
