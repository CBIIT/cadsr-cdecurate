/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

// Copyright (c) 2006 ScenPro, Inc.

// $Header: /cvsshare/content/cvsroot/cdecurate/src/gov/nih/nci/cadsr/cdecurate/database/DBAccess.java,v 1.47 2009-02-10 19:54:00 chickerura Exp $
// $Name: not supported by cvs2svn $

package gov.nih.nci.cadsr.cdecurate.database;

import gov.nih.nci.cadsr.cdecurate.common.NO_SQL_CHECK;
import gov.nih.nci.cadsr.cdecurate.tool.AC_Bean;
import gov.nih.nci.cadsr.cdecurate.util.ToolException;
import gov.nih.nci.cadsr.cdecurate.util.Tree;
import gov.nih.nci.cadsr.cdecurate.util.TreeNode;
import gov.nih.nci.cadsr.common.StringUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;


/**
 * This class encapsulates the caDSR database access.
 *
 * @author lhebel
 */
public class DBAccess
{
	 public static final Logger  logger  = Logger.getLogger(DBAccess.class.getName());

    /**
     * A class test method during development.
     *
     * @param args [0] the database URL
     */
    public static void main( String[] args )
    {
        // Check the arguments.
        if( args.length != 1 )
        {
            System.err.println( "DBAccess: Missing database connection URL." );
            return;
        }

        // This is a test so it is not necessary to create a connection pool or other more elaborate obfuscations.
        try
        {
            FileOutputStream fout = new FileOutputStream( "c:/temp/dbaccessout.txt" );

            DriverManager.registerDriver( new oracle.jdbc.driver.OracleDriver() );

            Connection conn = DriverManager.getConnection( "jdbc:oracle:thin:@" + args[0], "guest", "guest" );
            DBAccess db = new DBAccess( conn );

            // Set the test AC IDSEQ.
            String ac = "F8E452D6-E410-4E12-E034-0003BA0B1A09";

            // Get the AC title.
            String title = db.getACtitle( "UNKNOWN", ac );
            fout.write( title.getBytes() );
            fout.write( Alternates._HTMLprefix.getBytes() );

            // Get the Alternate Names and Definitions for the AC.
            Alternates[] alts = db.getAlternates( new String[]{ ac }, true, true );

            // Output each with the desired related information.
            int flag = -1;
            for( Alternates temp : alts )
            {
                String html;
                if( flag != temp.getInstance() )
                {
                    flag = temp.getInstance();
                    switch( temp.getInstance() )
                    {
                        case Alternates._INSTANCEDEF:
                            fout.write( "<tr><td style=\"border-top: 2px solid black\" colspan=\"4\"><p style=\"margin: 0.3in 0in 0in 0in\"><b>Definitions</b></p></td></tr>\n".getBytes() );
                            break;
                        case Alternates._INSTANCENAME:
                            fout.write( "<tr><td style=\"border-top: 2px solid black\" colspan=\"4\"><p style=\"margin: 0.3in 0in 0in 0in\"><b>Names</b></p></td></tr>\n".getBytes() );
                            break;
                        default:
                            fout.write( "<tr><td style=\"border-top: 2px solid black\" colspan=\"4\"><p style=\"margin: 0.3in 0in 0in 0in\"><b>UNKNOWN</b></p></td></tr>\n".getBytes() );
                            break;
                    }
                }
                html = temp.toHTML();
                fout.write( html.getBytes() );
            }

            Tree csi;
            csi = db.getAlternates( ac, true );
            title = Alternates._HTMLsuffix + "<hr/>\n" + Alternates._HTMLprefix + csi.toHTML( null );
            fout.write( title.getBytes() );

            // Show the full CSI tree.
            csi = db.getCSI();
            title = Alternates._HTMLsuffix + "<hr/>\n" + Alternates._HTMLprefix + csi.toHTML( null ) + Alternates._HTMLsuffix;
            fout.write( title.getBytes() );

            // Close the database connection.
            conn.close();
        } catch( SQLException ex )
        {
            ex.printStackTrace();
        } catch( ToolException ex )
        {
            ex.printStackTrace();
        } catch( FileNotFoundException ex )
        {
            ex.printStackTrace();
        } catch( IOException ex )
        {
            ex.printStackTrace();
        }
    }

    /**
     * Constructor. This class doesn't care how a connection is acquired, e.g. connection pool or dedicated, it
     * relies on the caller to perform the necessary calls.
     *
     * @param conn_ the established database connection
     */
    public DBAccess( Connection conn_ )
    {
        _conn = conn_;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // There is special processing around the UML_PACKAGE_NAME and UML_PACKAGE_ALIAS
        if( _packageAlias == null || _packageName == null )
        {
            // Get the CSI type values from the tool options. We don't care what they are called as long as we have the
            // right value.
            String select = "select property, value from sbrext.tool_options_view_ext where tool_name = 'CURATION' and property like 'CSI.%' ";
            try
            {
                pstmt = _conn.prepareStatement( select );
                rs = pstmt.executeQuery();

                // Set the static variables appropriately.
                while( rs.next() )
                {
                    String property = rs.getString( 1 );
                    String value = rs.getString( 2 );
                    if( property.equals( "CSI.PACKAGE.ALIAS" ) )
                        _packageAlias = value;
                    else if( property.equals( "CSI.PACKAGE.NAME" ) )
                        _packageName = value;
                }

            } catch( SQLException ex )
            {
                // Log the error but keep going.
                _log.error( ex.toString() );
            } finally
            {
                rs = SQLHelper.closeResultSet( rs );
                pstmt = SQLHelper.closePreparedStatement( pstmt );
            }

            // This only has to be done once. And either both are read or both are set blank.
            if( _packageAlias == null || _packageName == null )
            {
                _packageAlias = "";
                _packageName = "";
            }
        }
    }

    /**
     * A container class to simplify the data retrieval.
     *
     * @author lhebel
     */
    private static class CSIData
    {
        /**
         * Constructor
         *
         * @param node_  a tree node
         * @param level_ the hierarchy level
         */
        public CSIData( TreeNode node_, int level_ )
        {
            _level = level_;
            _node = node_;
        }

        public int _level;
        public TreeNode _node;
    }

    /**
     * Get the Class Scheme Item hierarchy for the Alternate Name or Definition provided by the caller.
     *
     * @param alt_ the Alternate Name or Definition for which to retrieve the Class Scheme Item hierarchy.
     * @throws ToolException
     */
    private void getAlternatesCSI( Alternates alt_ ) throws ToolException
    {
        Vector<CSIData> test = getAlternatesCSI( alt_.getAltIdseq(), null );

        // Take the content of the Vector and turn it into arrays for use by the Tree class.
        TreeNode[] nodes = new TreeNode[test.size()];
        int[] levels = new int[nodes.length];
        for( int i = 0; i < nodes.length; ++i )
        {
            CSIData temp = test.get( i );
            nodes[i] = temp._node;
            levels[i] = temp._level;
        }

        // Create the CSI tree for this Alternate Name/Def
        alt_.addCSI( nodes, levels );
    }

    /**
     * This convenience class holds composite information.
     *
     * @author lhebel
     */
    private class ATTData
    {
        public ATTData( String aidseq_, String cidseq_ )
        {
            _aidseq = aidseq_;
            _cidseq = cidseq_;
        }

        /**
         * The ATT_IDSEQ, database id for the record associating the CSI with the Alternate Name/Definition
         */
        public String _aidseq;

        /**
         * The CS_CSI_IDSEQ
         */
        public String _cidseq;
    }

    /**
     * Get the CSI references for the Alternate specified.
     *
     * @param idseq_ the Alternate Name or Definition
     * @return the associated CSI's
     * @throws ToolException
     */
    private Vector<ATTData> getAlternatesCSI1( String idseq_ ) throws ToolException
    {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String select = "select za.aca_idseq, za.cs_csi_idseq from sbrext.ac_att_cscsi_view_ext za where za.att_idseq = ?";

        try
        {
            // A simple query and storage into a vector.
            pstmt = _conn.prepareStatement( select );
            pstmt.setString( 1, idseq_ );
            rs = pstmt.executeQuery();
            Vector<ATTData> adata = new Vector<ATTData>();
            while( rs.next() )
            {
                adata.add( new ATTData( rs.getString( 1 ), rs.getString( 2 ) ) );
            }
            return adata;
        } catch( SQLException ex )
        {
            _log.error( "SQL: " + select, ex );
            throw new ToolException( ex );
        } finally
        {
            rs = SQLHelper.closeResultSet( rs );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
        }
    }

    /**
     * Get the Class Scheme Item hierarchy for the Alternate Name or Definition provided by the caller.
     *
     * @param idseq_ the Alternate Name or Definition database id
     * @return the CSI hierarchy
     * @throws ToolException
     */
    private Vector<CSIData> getAlternatesCSI( String idseq_, Alternates alt_ ) throws ToolException
    {
        // Get the SQL select statement
        String select = SQLSelectCSI.getAlternatesCSISelect( _packageAlias );

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector<CSIData> list = new Vector<CSIData>();
        try
        {

            // First retrieve the intersection records.
            Vector<ATTData> adata = getAlternatesCSI1( idseq_ );
            if( adata.size() == 0 )
            {
                if( alt_ == null )
                    return list;

                // No related CSI so create an artificial group called "unclassified"
                CSIData temp = new CSIData( new TreeNodeCS( "(unclassified)", "(unclassified)", "Unclassified Alternate Names and Definitions.", null, null, false ), 0 );
                list.add( temp );
                list.add( new CSIData( new TreeNodeAlt( alt_, alt_.getAltIdseq() ), 1 ) );
                return list;
            }

            // Because the retrieval is bottom-up (starts with the leaf and works up to the parent Classification Scheme) and
            // we want to show it to the user top-down we have to flip the results around. To do this we use a Vector and
            // always add the new record at the top (like a LIFO stack).
            CSIData lastStop = null;
            pstmt = _conn.prepareStatement( select );

            for( ATTData tdata : adata )
            {
                // Retrieve the CSI data for this Alternate Name/Def
                pstmt.setString( SQLSelectCSI._ARGCSCSIIDSEQ, tdata._cidseq );
                rs = pstmt.executeQuery();

                int level = 0;
                int prevLevel = 0;
                TreeNodeCSI prevTnc = null;
                String prevCSValue = "";
                String prevCSName = "";
                String prevCSDef = null;
                String prevCSVers = null;
                String prevCSConte = null;
                String csValue = "";
                String acaValue = tdata._aidseq;
                boolean extra = false;
                while( true )
                {
                    // If we didn't read anything, set the loop to terminate.
                    boolean loop = rs.next();
                    if( loop )
                    {
                        level = rs.getInt( SQLSelectCSI._LEVEL );
                        csValue = rs.getString( SQLSelectCSI._CSIDSEQ );

                        if( alt_ != null && level == 1 )
                        {
                            list.add( 0, new CSIData( new TreeNodeAlt( alt_, tdata._aidseq ), level - 1 ) );
                        }
                    }

                    // We only do this after the first time through the loop OR said another way, skip
                    // this the first time here.
                    if( extra )
                    {
                        // Whenever the level is 1 it means we are at a new leaf. If we are at a leaf and
                        // the Classification Scheme has changed, record a new CS record. If this is
                        // the last time through the loop be sure to write a new CS record for any data
                        // holding in the stack.
                        if( ( level == 1 && prevCSValue.equals( csValue ) == false ) || loop == false )
                        {
                            ++prevLevel;
                            CSIData stop = new CSIData( new TreeNodeCS( prevCSName, prevCSValue, prevCSDef, prevCSVers, prevCSConte, false ), prevLevel );
                            list.add( 0, stop );

                            // To be properly represented as levels in a hierarchy, 0 is the top (the CS record)
                            // and 'N' is a child. To sequence the numbers we use the max previous level
                            // and subtract the recorded level.
                            for( CSIData temp : list )
                            {
                                // When a level is zero, we can stop because a previous CS group is starting.
                                if( temp == lastStop )
                                    break;

                                // This subtract basically "flips" the level around. A variable has to be used as
                                // there's no way to predetermine how deep a hierarchy may be.
                                temp._level = prevLevel - temp._level;
                            }
                            lastStop = stop;
                        }
                    }

                    // We have finished the data retrieval so end the loop.
                    if( loop == false )
                        break;

                    // Always save the data retrieved and remember this record for reference should the
                    // next record prompt the need for a CS parent record.
                    String csiType = rs.getString( SQLSelectCSI._CSITYPE );
                    String csCsiIdseq = rs.getString( SQLSelectCSI._CSCSIIDSEQ );
                    String csiId = rs.getString( SQLSelectCSI._CSIID );
                    String csiVersion = rs.getString( SQLSelectCSI._CSIVERSION );
                    if( acaValue == null )
                        acaValue = csCsiIdseq;
                    TreeNodeCSI tnc = new TreeNodeCSI( rs.getString( SQLSelectCSI._CSINAME ), acaValue, csCsiIdseq, csiType, null, false, csiVersion, csiId );
                    list.add( 0, new CSIData( tnc, level ) );
                    prevCSValue = csValue;
                    prevCSName = rs.getString( SQLSelectCSI._CSNAME );
                    prevCSDef = rs.getString( SQLSelectCSI._CSDEFIN );
                    prevCSVers = rs.getString( SQLSelectCSI._CSVERS );
                    prevCSConte = rs.getString( SQLSelectCSI._CSCONTE );
                    prevLevel = level;

                    if( csiType.equals( _packageName ) )
                    {
                        prevTnc = tnc;
                    }
                    else if( prevTnc != null )
                    {
                        if( csiType.equals( _packageAlias ) )
                            prevTnc.setPackageAlias( acaValue );
                        prevTnc = null;
                    }
                    acaValue = null;

                    // Flag to start processing CS records.
                    extra = true;
                }//end of while
                rs = SQLHelper.closeResultSet( rs );
                pstmt.clearParameters();
            }//end of for loop

        } catch( SQLException ex )
        {
            _log.error( "SQL: " + select, ex );
            rs = SQLHelper.closeResultSet( rs );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
            throw new ToolException( ex );
        } finally
        {
            rs = SQLHelper.closeResultSet( rs );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
        }
        return list;
    }

    // new method to be called at  VMAction.java line 1360

    public ArrayList<Alternates> getAlternatesFastCS( String vmIdseq ) throws ToolException
    {
        boolean sortByName_ = true; // replaces parameter that was set to true
        boolean showMC_ = true; // replaces parameter that was set to true

        HashMap<String, Alternates> vmAlternates = new HashMap<String, Alternates>();
//        ArrayList<String> altIdseqs = new ArrayList<String>(); // don't need this now that we have a hashmap
        HashMap<String, ArrayList<CSIData>> csiDataForAlts;

        // the code I'm replacing loops through the alternates 2 or 3 times and
        // gets the CSI data for each one.  bad.
        // what we want to do is turn the result set into an Alternates object
        // and then add the CSI data to it
        String selectSQL = SQLSelectAlts.getAlternates( sortByName_ );
        PreparedStatement selectDesigsAndDefins = null; //I don't know why this makes my IDE happy, totally redundant as far as I know.
        ResultSet alternatesRS = null; //I don't know why this makes my IDE happy, totally redundant as far as I know.
        try
        {
            selectDesigsAndDefins = _conn.prepareStatement( selectSQL );
            alternatesRS = getAlternates( selectDesigsAndDefins, vmIdseq );
            while( alternatesRS.next() )
            {
                Alternates currentAlternates = SQLSelectAlts.copyFromRS( alternatesRS, showMC_ );
//              altIdseqs.add(currentAlternates.getIdseq());
                vmAlternates.put( currentAlternates.getAltIdseq(), currentAlternates );
            }

            // Now that we have an ArrayList of the alternates' ids and an array of the Alternates
            // Get all the CSI data for all the Alternates and put it in a hashmap of ArrayList<CSIData>
            csiDataForAlts = getCSIDataForAlternatesList( vmAlternates );

            CSIData unclassifiedCSI = new CSIData( new TreeNodeCS( "(unclassified)", "(unclassified)", "Unclassified Alternate Names and Definitions.", null, null, false ), 0 );
            ArrayList<CSIData> unclassifiedCSIList = new ArrayList<CSIData>();
            unclassifiedCSIList.add( unclassifiedCSI );


            // now loop through altIdseqs.  If there is no entry in output, add the unclassified entry for that altIdseq
            for( String altIdseq : vmAlternates.keySet() )
            {
                if( csiDataForAlts.get( altIdseq ) == null )
                {
                    csiDataForAlts.put( altIdseq, unclassifiedCSIList );
                    csiDataForAlts.get( altIdseq ).add( new CSIData( new TreeNodeAlt( vmAlternates.get( altIdseq ), altIdseq ), 1 ) );
                }
                // reverse all the ArrayLists of CSIData to be consistent with original code
                Collections.reverse( csiDataForAlts.get( altIdseq ) );

                //Turn the ArrayList of CSIData into node and level arrays for use by Tree
                int csiDataListSize = csiDataForAlts.get( altIdseq ).size();
                TreeNode[] nodes = new TreeNode[csiDataListSize];
                int[] levels = new int[csiDataListSize];
                for( int i = 0; i < nodes.length; ++i )
                {
                    CSIData csiData = csiDataForAlts.get( altIdseq ).get( i );
                    nodes[i] = csiData._node;
                    levels[i] = csiData._level;
                }

                // Create the CSI tree for this Alternate Name/Def
                vmAlternates.get( altIdseq ).addCSI( nodes, levels );
            }

        } catch( SQLException ex )
        {
            _log.error( "SQL: " + selectSQL, ex );
            throw new ToolException( ex );
        } catch( ToolException ex )
        {
            _log.error( "SQL: " + selectSQL, ex );
            throw ex;
        } finally
        {
            if( alternatesRS != null )
            {
                SQLHelper.closeResultSet( alternatesRS );
            }
            if( selectDesigsAndDefins != null )
            {
                SQLHelper.closePreparedStatement( selectDesigsAndDefins );
            }
        }
        return new ArrayList<Alternates>( vmAlternates.values() );
    }

    // I can break this into two steps insetad of three by making one call to get all the alternates with all their data,
    // then one call to get the att data for all alternates, that's fewer calls to get att data

    /**
     * @param vmAlternates a hashmap of format Alternates objects keyed off the Alternates's idseq
     * @return A hashmap of array's of CSIData, key'd off the idseq of the Alternates object the list of CSIData belongs to
     * @throws ToolException
     */
    public HashMap<String, ArrayList<CSIData>> getCSIDataForAlternatesList( HashMap<String, Alternates> vmAlternates ) throws ToolException
    {
        HashMap<String, ArrayList<CSIData>> output = new HashMap<String, ArrayList<CSIData>>();
        // empty CSIData for AltIdseqs with no CSI's
        CSIData unclassifiedCSI = new CSIData( new TreeNodeCS( "(unclassified)", "(unclassified)", "Unclassified Alternate Names and Definitions.", null, null, false ), 0 );
        ArrayList<CSIData> unclassifiedCSIList = new ArrayList<CSIData>();
        unclassifiedCSIList.add( unclassifiedCSI );
        String attSQL = "SELECT za.att_idseq, za.aca_idseq, za.cs_csi_idseq FROM sbrext.ac_att_cscsi_view_ext za WHERE za.att_idseq in (?)";
        String csCsiSQL = "SELECT level, cc.cs_idseq, cs.long_name, cc.cs_csi_idseq, csi.long_name, cs.preferred_definition, " +
                "cs.version, c.name, csi.csitl_name, cs.cs_id, csi.csi_id,  csi.version  " +
                "FROM sbr.cs_csi_view cc, sbr.cs_items_view csi, sbr.classification_schemes_view cs, sbr.contexts_view c  " +
                "WHERE csi.csi_idseq = cc.csi_idseq  AND cs.cs_idseq(+) = cc.cs_idseq  AND c.conte_idseq(+) = cs.conte_idseq  " +
                "CONNECT BY prior cc.p_cs_csi_idseq = cc.cs_csi_idseq  " +
                "START WITH cc.cs_csi_idseq = ?  AND csi.csi_idseq = cc.csi_idseq  AND csi.csitl_name <> ?";
        PreparedStatement attStatement = null; //I don't know why this makes my IDE happy, totally redundant as far as I know.
        PreparedStatement csCsiStatement;
        ResultSet attRS = null; //I don't know why this makes my IDE happy, totally redundant as far as I know.
        ResultSet csCsiRS;
        // ATT result set management fields
        String currentALtIdseq;
        String acaIdseq;
        String csCsiIdseq;
        int attRsSize;
        //CSIData management fields

        try
        {
            // prep new array list for the CSIData
            ArrayList<CSIData> alternatesCSIData = new ArrayList<CSIData>();
            // get the cs_csi_idseq's from sbrext.ac_att_cscsi_view_ext
            attStatement = _conn.prepareStatement( attSQL );
            StringBuilder altIdseqs = new StringBuilder();
            for( String str : vmAlternates.keySet() )
            {
                altIdseqs.append( str ).append( ',' );// argh, end up with a comma at the end, remove with substring when used
            }
            // set list of alternates idseqs into prepared statement
            if( altIdseqs.length() > 1 )
            {
                attStatement.setString( 1, altIdseqs.toString().substring( 0, altIdseqs.length() - 1 ) );
            }
            else
            {
                attStatement.setString( 1, "" );
            }
            attRS = attStatement.executeQuery();
            while( attRS.next() )
            {
                currentALtIdseq = attRS.getString( 1 );
                acaIdseq = attRS.getString( 2 );
                csCsiIdseq = attRS.getString( 3 );
                // if the hashmap doesn't have an entry for currentALtIdseq, make it
                if( output.get( currentALtIdseq ) == null )
                {
                    output.put( currentALtIdseq, new ArrayList<CSIData>() );
                }
                // go get the CSIData for this ATT entry
                csCsiStatement = _conn.prepareStatement( csCsiSQL );
                csCsiStatement.setString( 1, csCsiIdseq );
                csCsiStatement.setString( 2, _packageAlias );
                csCsiRS = csCsiStatement.executeQuery();
                boolean firstTimeThrough = true;
                int level = 0;
                int prevLevel = 0;
                TreeNodeCSI prevTnc = null;
                String prevCSValue = "csIdseq"; //initialize to non-working value for contrast
                String prevCSName = "";
                String prevCSDef = "";
                String prevCSVers = "";
                String prevCSConte = "";
                String csValue = "csIdseq"; //initialize to non-working value for contrast
                CSIData prevTreeNodeCSRecord = null;
                while( true )
                {
                    boolean weHaveARow = csCsiRS.next();
                    if( weHaveARow )
                    {
                        level = csCsiRS.getInt( SQLSelectCSI._LEVEL );
                        csValue = csCsiRS.getString( SQLSelectCSI._CSIDSEQ );
                        if( vmAlternates.get( currentALtIdseq ) != null && level == 1 )
                        {
                            output.get( currentALtIdseq ).add( new CSIData( new TreeNodeAlt( vmAlternates.get( currentALtIdseq ), currentALtIdseq ), level - 1 ) );
                        }
                    }

                    if( !firstTimeThrough )
                    { // this conditional could probably be accommodated other ways but it's not worth the trouble at this point
                        // Whenever the level is 1 it means we are at a new leaf. If we are at a leaf and
                        // the Classification Scheme has changed, record a new CS record.
                        // Or if we are out of results,  be sure to write a new CS record for any data
                        // holding in the stack.
                        if( ( level == 1 && !prevCSValue.equals( csValue ) ) || !weHaveARow )
                        {
                            CSIData treeNodeCSRecord = new CSIData( new TreeNodeCS( prevCSName, prevCSValue, prevCSDef, prevCSVers, prevCSConte, false ), prevLevel );
                            output.get( currentALtIdseq ).add( treeNodeCSRecord );

                            // To be properly represented as levels in a hierarchy, 0 is the top (the CS record)
                            // and 'N' is a child. To sequence the numbers we use the max previous level
                            // and subtract the recorded level.
                            // this is all very expensive and I don't know that it is
                            //    A: necessary
                            //    B: going to work because I'm not adding everything to the start of the list
                            for( CSIData temp : output.get( currentALtIdseq ) )
                            {
                                // When a level is zero, we can stop because a previous CS group is starting.
                                if( temp == prevTreeNodeCSRecord )
                                    break;

                                // This subtract basically "flips" the level around. A variable has to be used as
                                // there's no way to predetermine how deep a hierarchy may be.
                                temp._level = prevLevel - temp._level;//todo: confirm this still works with the current insert strategy
                            }
                            prevTreeNodeCSRecord = treeNodeCSRecord;
                        }
                    }

                    if( !weHaveARow )
                    {
                        break; // terminate while loop on csCsriRS rows
                    }

                    // build a new TreeNodeCSI CSIData object
                    String csiType = csCsiRS.getString( SQLSelectCSI._CSITYPE );
                    String nodeCsCsiIdseq = csCsiRS.getString( SQLSelectCSI._CSCSIIDSEQ );
                    String csiId = csCsiRS.getString( SQLSelectCSI._CSIID );
                    String csiVersion = csCsiRS.getString( SQLSelectCSI._CSIVERSION );
                    if( acaIdseq == null )
                        acaIdseq = nodeCsCsiIdseq;
                    TreeNodeCSI tnc = new TreeNodeCSI( csCsiRS.getString( SQLSelectCSI._CSINAME ), acaIdseq, nodeCsCsiIdseq, csiType, null, false, csiVersion, csiId );
                    // add new TreeNodeCSI CSIData object to the list for this alternate
                    output.get( currentALtIdseq ).add( 0, new CSIData( tnc, level ) );
                    // collect prev values for next loop's comparisons
                    prevCSValue = csValue;
                    prevCSName = csCsiRS.getString( SQLSelectCSI._CSNAME );
                    prevCSDef = csCsiRS.getString( SQLSelectCSI._CSDEFIN );
                    prevCSVers = csCsiRS.getString( SQLSelectCSI._CSVERS );
                    prevCSConte = csCsiRS.getString( SQLSelectCSI._CSCONTE );
                    prevLevel = level;
                    // This if/else block is either pointless or ill conceived.  Todo: verify which it is, if time exists
                    if( csiType.equals( _packageName ) )
                    {
                        prevTnc = tnc;
                    }
                    else if( prevTnc != null )
                    {
                        if( csiType.equals( _packageAlias ) )
                        {
                            prevTnc.setPackageAlias( acaIdseq ); //?? Why would I want to set this to an IDSEQ???
                            // and anyway, aren't I just updating the tnc object that's in the list, why wait to do
                            // this after assigning the object to the prevTnc variable??
                        }
                        prevTnc = null;
                    }

                    // hurray, we finished the first time through
                    firstTimeThrough = false;
                }
                SQLHelper.closeResultSet( csCsiRS );
                csCsiStatement.clearParameters();
                SQLHelper.closePreparedStatement( csCsiStatement );
            }

        } catch( SQLException ex )
        {
            _log.error( "SQL: " + attSQL, ex );
            throw new ToolException( ex );
//        } catch (ToolException ex) {
//            _log.error("SQL: " + attSQL, ex);
//            throw ex;
        } finally
        {
            if( attRS != null )
            {
                SQLHelper.closeResultSet( attRS );
            }
            if( attStatement != null )
            {
                SQLHelper.closePreparedStatement( attStatement );
            }
        }

        // now loop through altIdseqs.  If there is no entry in output, add the unclassified entry for that altIdseq
        for( String altIdseq : vmAlternates.keySet() )
        {
            if( output.get( altIdseq ) == null )
            {
                output.put( altIdseq, unclassifiedCSIList );
                output.get( altIdseq ).add( new CSIData( new TreeNodeAlt( vmAlternates.get( altIdseq ), altIdseq ), 1 ) );
            }
            // reverse all the arraylists of CSIData to be consistent with original code
            Collections.reverse( output.get( altIdseq ) );
        }

        return output;
    }

    /**
     * Get the CSI Lineage for the CSI specified
     *
     * @param idseq_ the CS_CSI_IDSEQ for the desired CS
     * @param root_  the Tree to hold the query results
     * @throws ToolException
     */
    public void getCSILineage( String idseq_, Tree root_ ) throws ToolException
    {
        // Get the SQL select
        String select = SQLSelectCSI.getAlternatesCSISelect( _packageAlias );

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            // Get the lineage (child to parent) from CSI to CS.
            pstmt = _conn.prepareStatement( select );
            pstmt.setString( SQLSelectCSI._ARGCSCSIIDSEQ, idseq_ );
            rs = pstmt.executeQuery();

            // Because this returns "bottom up" each entry is added at the
            // top of the vector to change the order to "top down"
            Vector<CSIData> lineage = new Vector<CSIData>();
            String csName = null;
            String csValue = null;
            String csDef = null;
            String csVers = null;
            String csConte = null;
            String csIdVer = null;
            int csId;
            TreeNodeCSI prevTnc = null;
            while( rs.next() )
            {
                csName = rs.getString( SQLSelectCSI._CSNAME );
                csValue = rs.getString( SQLSelectCSI._CSIDSEQ );
                csDef = rs.getString( SQLSelectCSI._CSDEFIN );
                csVers = rs.getString( SQLSelectCSI._CSVERS );
                csConte = rs.getString( SQLSelectCSI._CSCONTE );
                csId = rs.getInt( SQLSelectCSI._CSID );
                csIdVer = csId + "v" + csVers;
                String csiName = rs.getString( SQLSelectCSI._CSINAME );
                String csiValue = rs.getString( SQLSelectCSI._CSCSIIDSEQ );
                String csiType = rs.getString( SQLSelectCSI._CSITYPE );
                String csiVersion = rs.getString( SQLSelectCSI._CSIVERSION );
                String csiId = rs.getString( SQLSelectCSI._CSIID );
                int level = rs.getInt( SQLSelectCSI._LEVEL );
                TreeNodeCSI tnc = new TreeNodeCSI( csiName, csiValue, csiValue, csiType, null, true, csiVersion, csiId );
                lineage.add( 0, new CSIData( tnc, level ) );
                if( level == 2 && csiType.equals( _packageAlias ) )
                    prevTnc.setPackageAlias( csiValue );
                prevTnc = tnc;
            }
            if( csValue != null )
                lineage.add( 0, new CSIData( new TreeNodeCS( csName, csValue, csDef, csIdVer, csConte, false ), 0 ) );

            // Take the content of the Vector and turn it into arrays for use by the Tree class. The level
            // is reset because the smallest value must appear first. Being a direct route child-to-parent only
            // 1 parent exists for each child and only 1 child exists for each parent in the result set.
            TreeNode[] nodes = new TreeNode[lineage.size()];
            int[] levels = new int[nodes.length];
            for( int i = 0; i < nodes.length; ++i )
            {
                CSIData temp = lineage.get( i );
                nodes[i] = temp._node;
                levels[i] = i;
            }

            // Add the hierarchy to the object Tree
            root_.addHierarchy( nodes, levels );
        } catch( SQLException ex )
        {
            _log.error( "SQL: " + select, ex );
            rs = SQLHelper.closeResultSet( rs );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
            throw new ToolException( ex );
        } finally
        {
            rs = SQLHelper.closeResultSet( rs );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
        }
    }

    /**
     * Execute the SQL query to retrieve the Alternate Names and Definitions.
     *
     * @param pstmt_ the SQL select statement
     * @param idseq_ the parent AC idseq
     * @return the results set of all Alternate Names and Definitions associated to the specified AC
     * @throws ToolException
     */
    private ResultSet getAlternates( PreparedStatement pstmt_, String idseq_ ) throws ToolException
    {
        ResultSet rs = null;
        try
        {
            // It is important for the data to match the values expected in the logic. This also provides a means to
            // sort the results and ensure the order is guaranteed.
            pstmt_.setInt( SQLSelectAlts._ARGDESINST, Alternates._INSTANCENAME );
            pstmt_.setString( SQLSelectAlts._ARGDESACIDSEQ, idseq_ );
            pstmt_.setInt( SQLSelectAlts._ARGDEFINST, Alternates._INSTANCEDEF );
            pstmt_.setString( SQLSelectAlts._ARGDEFACIDSEQ, idseq_ );
            rs = pstmt_.executeQuery();        //JR1016 more rows than it should be!
        } catch( SQLException ex )
        {
            rs = SQLHelper.closeResultSet( rs );
            throw new ToolException( ex );
        }
        return rs;
    }

    /**
     * Get the Alternate Names and Definitions for a specific Administered Component. The result is a
     * Tree with the Alt Name/Def subordinate to the CSI to which it is related. This is a more traditional
     * "file system" view of the Alt Name/Def with the CS as the progenator.
     *
     * @param idseq_ the AC database it
     * @return the CSI and Alt Name/Def hierarchy.
     * @throws ToolException
     */
    public Tree getAlternates( String idseq_, boolean showMC_ ) throws ToolException
    {
        Tree root = new Tree( new TreeNode( "Alternate Names & Definitions", null, false ) );
        String select = SQLSelectAlts.getAlternates( true );
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            // It is important for the data to match the values expected in the logic. This also provides a means to
            // sort the results and ensure the order is guaranteed.
            pstmt = _conn.prepareStatement( select );
            rs = getAlternates( pstmt, idseq_ );

            // Read the results.
            Vector<Alternates> altList = new Vector<Alternates>();
            while( rs.next() )
            {
                altList.add( SQLSelectAlts.copyFromRS( rs, showMC_ ) );
            }

            for( Alternates alt : altList )
            {
                // Get the CSI for each Alternate.
                Vector<CSIData> test = getAlternatesCSI( alt.getAltIdseq(), alt );

                // Take the content of the Vector and turn it into arrays for use by the Tree class.
                TreeNode[] nodes = new TreeNode[test.size()];
                int[] levels = new int[nodes.length];
                for( int i = 0; i < nodes.length; ++i )
                {
                    CSIData temp = test.get( i );
                    nodes[i] = temp._node;
                    levels[i] = temp._level;
                }
                // Add the hierarchy to the Alternate CSI tree.
                root.addHierarchy( nodes, levels );
            }
        } catch( SQLException ex )
        {
            _log.error( "SQL: " + select, ex );
            rs = SQLHelper.closeResultSet( rs );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
            throw new ToolException( ex );
        } catch( ToolException ex )
        {
            _log.error( "SQL: " + select, ex );
            throw ex;
        } finally
        {
            rs = SQLHelper.closeResultSet( rs );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
        }
        // Return the Tree (This is normally viewed on the UI from the "View by CS/CSI" tab.
        return root;
    }

    /**
     * Get the Alternate Names and Definitions for a specific Administered Component. The result is a
     * list which contains the Alternate and the hierarchy of CSI to which it is related.
     *
     * @param idseq_      the AC database id
     * @param sortByName_ true to sort the results by the text for "name" and false to sort by type;
     *                    in either case the results are grouped by Alt Name first then Alt Definition.
     * @return the array of Alternate Names and Definitions
     * @throws ToolException
     */
    public Alternates[] getAlternates( String[] idseq_, boolean sortByName_, boolean showMC_ ) throws ToolException
    {
        Alternates[] list = new Alternates[0];
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String select = SQLSelectAlts.getAlternates( sortByName_ );  //CURATNTOOL-1064
        try
        {
            // It is important for the data to match the values expected in the logic. This also provides a means to
            // sort the results and ensure the order is guaranteed.
            pstmt = _conn.prepareStatement( select );
            Vector<Alternates> temp = new Vector<Alternates>();
            for( int i = 0; i < idseq_.length; ++i )
            {
                rs = getAlternates( pstmt, idseq_[i] );

                // Because the ORDER BY clause ensures the order, we only need to copy the data as it's
                // read.
                while( rs.next() )
                {
                    temp.add( SQLSelectAlts.copyFromRS( rs, showMC_ ) );
                }
                rs = SQLHelper.closeResultSet( rs );
            }
            // Convert the result Vector to an array and get the CSI hierarchy for each Alternate Name
            // or Definition.
            list = new Alternates[temp.size()];
            for( int i = 0; i < list.length; ++i )
            {
                list[i] = temp.get( i );
                getAlternatesCSI( list[i] );
            }
        } catch( SQLException ex )
        {
            _log.error( "SQL: " + select, ex );
            rs = SQLHelper.closeResultSet( rs );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
            throw new ToolException( ex );
        } catch( ToolException ex )
        {
            _log.error( "SQL: " + select, ex );
            rs = SQLHelper.closeResultSet( rs );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
            throw ex;
        } finally
        {
            rs = SQLHelper.closeResultSet( rs );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
        }

        // Return the result list complete with CSI information.
        return list;
    }

    /**
     * Delete all Alternates for the AC.
     *
     * @param idseq_ the AC database id
     * @throws SQLException
     */
    public void deleteAlternates( String idseq_ ) throws SQLException
    {
        String delete;
        PreparedStatement pstmt = null;

        try
        {
            delete = "delete from sbr.designations_view where ac_idseq = ?";
            pstmt = _conn.prepareStatement( delete );
            pstmt.setString( 1, idseq_ );
            pstmt.executeUpdate();
            pstmt = SQLHelper.closePreparedStatement( pstmt );

            delete = "delete from sbr.definitions_view where ac_idseq = ?";
            pstmt = _conn.prepareStatement( delete );
            pstmt.setString( 1, idseq_ );
            pstmt.executeUpdate();
        } catch( SQLException ex )
        {
            pstmt = SQLHelper.closePreparedStatement( pstmt );
            throw ex;
        } finally
        {
            pstmt = SQLHelper.closePreparedStatement( pstmt );
        }
    }

    /**
     * Retrieve the formatted title for the specified AC. Currently the format is
     * <label>: <name> [<public id>v<version>]
     * This method ensures a consistency in display and allows for easy maintenance should
     * the format need to change.
     *
     * @param idseq_ the AC database id
     * @return the formatted title string as described above
     * @throws ToolException
     */
    public String getACtitle( String defName_, String idseq_ ) throws ToolException
    {
        if( idseq_ == null || idseq_.length() == 0 )
            return "NEW " + ACTypes.valueOf( defName_ ).getName();

        // All AC records appear in the admin_components_view in addition to the individual tables, e.g. data_elements_view. The
        // information needed for the output is contained in the one view which simplifies the SQL.
        String select = "select actl_name, long_name, public_id, version from sbr.admin_components_view where ac_idseq = ?";
        String title = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = _conn.prepareStatement( select );
            pstmt.setString( 1, idseq_ );
            rs = pstmt.executeQuery();

            // There will be only 1 result if any. Format and return to the caller.
            if( rs.next() )
            {
                String type = rs.getString( 1 );
                ACTypes acType = ACTypes.valueOf( type );
                String name = rs.getString( 2 );
                String pid = rs.getString( 3 );
                String vers = rs.getString( 4 );
                if( vers.indexOf( '.' ) < 0 )
                    vers += ".0";
                title = acType.getName() + ": <b>" + StringUtil.escapeHtmlEncodedValue(name) + "</b><br/> [" + pid + "v" + vers + "]";
            }
        } catch( SQLException ex )
        {
            _log.error( "SQL: " + select, ex );
            SQLHelper.closeResultSet( rs );
            SQLHelper.closePreparedStatement( pstmt );
            throw new ToolException( ex );
        } finally
        {
            rs = SQLHelper.closeResultSet( rs );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
        }
        // Return a formatted result or null if the AC was not found.
        return title;
    }

    /**
     * Delete the Alternate Name from the database.
     *
     * @param idseq_ the database id
     * @throws ToolException
     */
    public void deleteAltName( String idseq_ ) throws ToolException
    {
        String select = "delete from sbr.designations_view where ac_idseq = ?";
        PreparedStatement pstmt = null;
        try
        {
            pstmt = _conn.prepareStatement( select );
            pstmt.setString( 1, idseq_ );
            pstmt.executeUpdate();
        } catch( SQLException ex )
        {
            _log.error( "SQL: " + select, ex );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
            throw new ToolException( ex );
        } finally
        {
            pstmt = SQLHelper.closePreparedStatement( pstmt );
        }
    }

    /**
     * Delete the Alternate Definition from the database.
     *
     * @param idseq_ the database id.
     * @throws ToolException
     */
    public void deleteAltDef( String idseq_ ) throws ToolException
    {
        String select = "delete from sbr.definitions_view where ac_idseq = ?";
        PreparedStatement pstmt = null;
        try
        {
            pstmt = _conn.prepareStatement( select );
            pstmt.setString( 1, idseq_ );
            pstmt.executeUpdate();
        } catch( SQLException ex )
        {
            _log.error( "SQL: " + select, ex );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
            throw new ToolException( ex );
        } finally
        {
            pstmt = SQLHelper.closePreparedStatement( pstmt );
        }
    }

    /**
     * Retrieve the Class Scheme hierarchy from the caDSR.
     *
     * @return the CSI tree for the entire caDSR
     */
    public Tree getCSI() throws ToolException
    {
        return getCSI( null );
    }

    /**
     * Retrieve the Class Scheme hierarchy from the caDSR qualified by specific Contexts.
     *
     * @return the CSI tree for the entire caDSR
     */
    @NO_SQL_CHECK
    public Tree getCSI( String[] contexts_ ) throws ToolException
    {
        // Because the CSI is a hierarchy we can not sort it in the SQL. The Tree.add() methods will ensure the
        // syblings are sorted case insensitive.
        String select = SQLSelectCSIAll.getCSIHierarchyNoContextsViewName( contexts_ );
        Statement stmt = null;
        ResultSet rs = null;
        HashMap<String, String> contextNameMap = null;

        Tree root = new Tree( new TreeNode( "Class Scheme Items", null, false ) );


        try
        {
            /*
              JIRA 1134
              On some tiers selecting "level" and the context name caused an Oracle error.
              Here we query for the Context name separately without level and match them up later by cs_idseq, cs_csi_idseq, and long_name
             */
            String selectContextName = SQLSelectCSIAll.getCSIHierarchyNoLevel( contexts_ );
            stmt = _conn.createStatement();
            contextNameMap = new HashMap<String, String>();
            rs = stmt.executeQuery( selectContextName );
            while( rs.next() )
            {
                // Save the value which is the Context name by the key cs_idseq:cs_csi_idseq:long_name
                String key = rs.getString( 2) + ":" + rs.getString( 3 ) + ":" + rs.getString( 4 );
                contextNameMap.put( key, rs.getString( 1 ) );
            }

            // Retrieve the hierarchy and add a CS record at the top of each new branch.
            stmt = _conn.createStatement();
            rs = stmt.executeQuery( select );        //JR1046 checked

            Vector<CSIData> test = new Vector<CSIData>();
            String lastCS = "";
            String prevValue = null;
            while( rs.next() )
            {
                int level = rs.getInt( SQLSelectCSIAll._LEVEL );
                if( level == 1 )
                {
                    // This may be the beginning of a new branch.
                    String csIdseq = rs.getString( SQLSelectCSIAll._CSIDSEQ );
                    if( !lastCS.equals( csIdseq ) )
                    {
                        // It is a new branch so make the level 1 less than the current data.
                        String csName = rs.getString( SQLSelectCSIAll._CSNAME );
                        String csDefin = rs.getString( SQLSelectCSIAll._CSDEFIN );
                        String csVers = rs.getString( SQLSelectCSIAll._CSVERS );

                        String csConte = contextNameMap.get( rs.getString( SQLSelectCSIAll._CSIDSEQ ) + ":" + rs.getString( SQLSelectCSIAll._CSCSIIDSEQ ) + ":" + rs.getString( SQLSelectCSIAll._CSINAME ) );
                        //String csConte =  rs.getString(SQLSelectCSIAll._CSCONTE);

                        int csId = rs.getInt( SQLSelectCSIAll._CSID );
                        String csIdVer = csId + "v" + csVers;
                        TreeNodeCS tnc = new TreeNodeCS( csName, csIdseq, csDefin, csIdVer, csConte, false );
                        test.add( new CSIData( tnc, level - 1 ) );
                        lastCS = csIdseq;
                    }
                }

                // Add the CSI record to the cache.
                String csiName = rs.getString( SQLSelectCSIAll._CSINAME );
                String csiValue = rs.getString( SQLSelectCSIAll._CSCSIIDSEQ );
                String csiType = rs.getString( SQLSelectCSIAll._CSITYPE );
                String csiId = rs.getString( SQLSelectCSIAll._CSIID );
                String csiVersion = rs.getString( SQLSelectCSIAll._CSIVERSION );
                TreeNodeCSI tnc = new TreeNodeCSI( csiName, csiValue, csiValue, csiType, prevValue, false, csiVersion, csiId );
                test.add( new CSIData( tnc, level ) );
                prevValue = ( csiType.equals( _packageAlias ) ) ? csiValue : null;
            }

            // Convert to arrays to add to the Tree
            TreeNode[] nodes = new TreeNode[test.size()];
            int[] levels = new int[nodes.length];
            for( int i = 0; i < nodes.length; ++i )
            {
                CSIData temp = test.get( i );
                nodes[i] = temp._node;
                levels[i] = temp._level;
            }

            // Build the hierarchy in the Tree
            root.addHierarchy( nodes, levels );
        } catch( SQLException ex )
        {
            _log.error( "SQL: " + select );
            rs = SQLHelper.closeResultSet( rs );
            stmt = SQLHelper.closeStatement( stmt );
            throw new ToolException( ex );
        } finally
        {
            rs = SQLHelper.closeResultSet( rs );
            stmt = SQLHelper.closeStatement( stmt );
        }
        // Return the Tree root.
        return root;
    }

    /**
     * Insert an Alternate Name or Definition into the database.
     *
     * @param alt_ the Alternate
     * @param sql_ the SQL insert
     * @throws SQLException
     */
    private void insertAlt( Alternates alt_, String sql_ ) throws SQLException
    {
        CallableStatement cstmt = null;
        try
        {
            cstmt = _conn.prepareCall( sql_ );
            cstmt.setString( 1, alt_.getAcIdseq() );
            cstmt.setString( 2, alt_.getConteIdseq() );
            cstmt.setString( 3, StringUtil.unescapeHtmlEncodedValue(alt_.getName() ));
            cstmt.setString( 4, alt_.getType() );
            cstmt.setString( 5, alt_.getLanguage() );
            cstmt.registerOutParameter( 6, java.sql.Types.VARCHAR );
            cstmt.executeUpdate();
            alt_.setAltIdseq( cstmt.getString( 6 ) );
        } catch( SQLException ex )
        {
            SQLHelper.closeCallableStatement( cstmt );
            throw ex;
        } finally
        {
            SQLHelper.closeCallableStatement( cstmt );
        }

    }

    /**
     * Insert an Alternate Name into the database
     *
     * @param alt_ the Alternate object
     * @throws SQLException
     */
    private void insertAltName( Alternates alt_ ) throws SQLException
    {
        String insert = "begin insert into sbr.designations_view "
                + "(ac_idseq, conte_idseq, name, detl_name, lae_name) "
                + "values (?, ?, ?, ?, ?) return desig_idseq into ?; end;";

        insertAlt( alt_, insert );
    }

    /**
     * Insert an Alternate Defintion into the database
     *
     * @param alt_ the Alternate object
     * @throws SQLException
     */
    private void insertAltDef( Alternates alt_ ) throws SQLException
    {
        String insert = "begin insert into sbr.definitions_view "
                + "(ac_idseq, conte_idseq, definition, defl_name, lae_name) "
                + "values (?, ?, ?, ?, ?) return defin_idseq into ?; end;";

        insertAlt( alt_, insert );
    }

    /**
     * Insert an Alternate into the database
     *
     * @param alt_ the Alternate
     * @throws SQLException
     */
    private void insert( Alternates alt_ ) throws SQLException
    {
        if( alt_.isName() )
            insertAltName( alt_ );
        else
            insertAltDef( alt_ );
    }

    /**
     * Update an Alternate
     *
     * @param alt_ the Alternate
     * @param sql_ the SQL update
     * @throws SQLException
     */
    private void updateAlt( Alternates alt_, String sql_ ) throws SQLException
    {
        PreparedStatement pstmt = null;
        try
        {
            pstmt = _conn.prepareStatement( sql_ );
            pstmt.setString( 1, StringUtil.unescapeHtmlEncodedValue(alt_.getName() ));
            pstmt.setString( 2, alt_.getType() );
            pstmt.setString( 3, alt_.getLanguage() );
            pstmt.setString( 4, alt_.getConteIdseq() );    //JR1099 added and offset the subsequent index by one
            pstmt.setString( 5, alt_.getAltIdseq() );
            pstmt.executeUpdate();

        } catch( SQLException ex )
        {
            SQLHelper.closePreparedStatement( pstmt );
            throw ex;
        } finally
        {
            SQLHelper.closePreparedStatement( pstmt );
        }

    }

    /**
     * Update an Alternate Name
     *
     * @param alt_ the Alternate
     * @throws SQLException
     */
    private void updateAltName( Alternates alt_ ) throws SQLException
    {
        String update = "update sbr.designations_view set name = ?, detl_name = ?, lae_name = ?, conte_idseq = ? where desig_idseq = ?";    //JR1099 added conte_idseq

        updateAlt( alt_, update );
    }

    /**
     * Update an Alternate Definition
     *
     * @param alt_ the Alternate
     * @throws SQLException
     */
    private void updateAltDef( Alternates alt_ ) throws SQLException
    {
        String update = "update sbr.definitions_view set definition = ?, defl_name = ?, lae_name = ?, conte_idseq = ? where defin_idseq = ?";

        updateAlt( alt_, update );
    }

    /**
     * Update an Alternate
     *
     * @param alt_ the Alternate
     * @throws SQLException
     */
    private void update( Alternates alt_ ) throws SQLException
    {
        if( alt_.isName() )
            updateAltName( alt_ );
        else
            updateAltDef( alt_ );
    }

    /**
     * Save CSI and Alternate Name/Definition associations
     *
     * @param alt_ the Alternate
     * @throws SQLException
     */
    private void saveCSI( Alternates alt_ ) throws SQLException
    {
        Vector<TreeNode> list;
        PreparedStatement pstmt = null;
        PreparedStatement stmt = null;
        try
        {
            // Add new ones first.
            list = alt_.getCSITree().findNew();
            if( list.size() > 0 )
            {
                String insert = "insert into sbrext.ac_att_cscsi_view_ext (cs_csi_idseq, att_idseq, atl_name) values (?, ?, ?)";

                pstmt = _conn.prepareStatement( insert );

                // Prepare and insert the new associations
                for( TreeNode node : list )
                {
                    if( node instanceof TreeNodeCSI )
                    {
                        // Only do if we have a CSI node.
                        String atlName = ( alt_.isName() ) ? "DESIGNATION" : "DEFINITION";
                        pstmt.setString( 1, node.getValue() );
                        pstmt.setString( 2, alt_.getAltIdseq() );
                        pstmt.setString( 3, atlName );

                        pstmt.executeUpdate();

                        // For UML_PACKAGE_NAME also associate to the UML_PACKAGE_ALIAS
                        TreeNodeCSI temp = ( TreeNodeCSI ) node;
                        if( temp.isPackageName() )
                        {
                            String alias = temp.getPackageAlias();
                            pstmt.setString( 1, alias );
                            pstmt.setString( 2, alt_.getAltIdseq() );
                            pstmt.setString( 3, atlName );
                        }
                    }
                }

                // Done with the new ones.
                pstmt = SQLHelper.closePreparedStatement( pstmt );
            }

            // Remove ones we don't want to keep.
            list = alt_.getCSITree().findDeleted();
            if( list.size() > 0 )
            {
                String insert = "delete from sbrext.ac_att_cscsi_view_ext where aca_idseq = ?";

                pstmt = _conn.prepareStatement( insert );

                // Prepare and delete unwanted associations.
                for( TreeNode node : list )
                {
                    if( node instanceof TreeNodeCSI )
                    {
                        pstmt.setString( 1, node.getValue() );

                        pstmt.executeUpdate();

                        // Again UML_PACKAGE_NAME is extra work.
                        TreeNodeCSI temp = ( TreeNodeCSI ) node;
                        if( temp.isPackageName() )
                        {
                            stmt = _conn.prepareStatement( "delete from sbrext.ac_att_cscsi_view_ext where cs_csi_idseq = ? and att_idseq = ? and atl_name = ?" );
                            stmt.setString( 1, temp.getPackageAlias() );
                            stmt.setString( 2, alt_.getAltIdseq() );
                            stmt.setString( 3, "DESIGNATION" );
                            stmt.executeUpdate();
                            stmt = SQLHelper.closePreparedStatement( stmt );
                        }
                    }
                }

            }
        } finally
        {
            pstmt = SQLHelper.closePreparedStatement( pstmt );
            stmt = SQLHelper.closePreparedStatement( stmt );
        }
    }

    /**
     * Delete an Alternate Name/Definition
     *
     * @param sql_   the SQL delete
     * @param idseq_ the Alternate database id
     * @throws SQLException
     */
    private void deleteAlt( String sql_, String idseq_ ) throws SQLException
    {
        PreparedStatement pstmt = null;
        try
        {
            pstmt = _conn.prepareStatement( sql_ );
            pstmt.setString( 1, idseq_ );
            pstmt.executeUpdate();
        } finally
        {
            pstmt = SQLHelper.closePreparedStatement( pstmt );
        }
    }

    /**
     * Delete an Alternate Name
     *
     * @param alt_ the Alternate
     * @throws SQLException
     */
    private void deleteAltName( Alternates alt_ ) throws SQLException
    {
        String delete = "delete from sbr.designations_view where desig_idseq = ?";

        deleteAlt( delete, alt_.getAltIdseq() );
    }

    /**
     * Delete an Alternate Definition
     *
     * @param alt_ the Alternate
     * @throws SQLException
     */
    private void deleteAltDef( Alternates alt_ ) throws SQLException
    {
        String delete = "delete from sbr.definitions_view where defin_idseq = ?";

        deleteAlt( delete, alt_.getAltIdseq() );
    }

    /**
     * Delete an Alternate
     *
     * @param alt_ the Alternate
     * @throws SQLException
     */
    private void delete( Alternates alt_ ) throws SQLException
    {
        if( alt_.isName() )
            deleteAltName( alt_ );
        else
            deleteAltDef( alt_ );
    }

    /**
     * Insert a USED_BY Alternate Name into the database. It doesn't matter
     * if the cause of this is an Alternate Name or Alternate Definition.
     *
     * @param alt_ the Alternate object
     * @throws SQLException
     */
    private void insertUsedBy( Alternates alt_ ) throws SQLException
    {
        String insert = "begin insert into sbr.designations_view "
                + "(ac_idseq, conte_idseq, name, detl_name, lae_name) "
                + "values (?, ?, ?, ?, ?) return desig_idseq into ?; end;";

        CallableStatement cstmt = null;
        try
        {
            // Add the Designation.
            cstmt = _conn.prepareCall( insert );
            cstmt.setString( 1, alt_.getAcIdseq() );    //JR1099 _altIdseq is $-1 ???
            cstmt.setString( 2, alt_.getConteIdseq() );
            cstmt.setString( 3, alt_.getName() );        //JR1099 the fix!
            //cstmt.setString(4, _addDesigType);	//JR1099 "USED_BY" ? Not even sure what type is this!!!
            cstmt.setString( 4, alt_.getType() );
            cstmt.setString( 5, alt_.getLanguage() );
            cstmt.registerOutParameter( 6, java.sql.Types.VARCHAR );
            cstmt.executeUpdate();

            cstmt.setString( 1, alt_.getAcIdseq() );
            cstmt.setString( 2, alt_.getConteIdseq() );
            cstmt.setString( 3, alt_.getName() );
            cstmt.setString( 4, alt_.getType() );
            cstmt.setString( 5, alt_.getLanguage() );
            cstmt.registerOutParameter( 6, java.sql.Types.VARCHAR );


            // At this time we don't really care about the generated desig_idseq
            // but we may in the future.
        } catch( SQLException ex )
        {
            ex.printStackTrace();

            cstmt = SQLHelper.closeCallableStatement( cstmt );    //JR1099 caused: java.sql.SQLIntegrityConstraintViolationException: ORA-00001: unique constraint (SBR.DESIG_UK) violated ORA-06512: at line 1
            // If the record already exists then great, just ignore the duplicate, otherwise
            // it's not good.
            if( ex.getErrorCode() != 1 )
                throw ex;
        } finally
        {
            cstmt = SQLHelper.closeCallableStatement( cstmt );
        }
    }

    /**
     * Save a USED_BY Alternate Name
     *
     * @param alt_ the Alternate
     * @throws SQLException
     */
    public void saveUsedBy( Alternates alt_ ) throws SQLException
    {
        // If it's deleted, don't need to do anything. We can automatically add
        // new USED_BY types but we can't delete them automatically.
        if( alt_.isDeleted() )
        {
            return;
        }

        // If it's new or changed it doesn't matter if it's a Name or Definition.
        if( alt_.isNew() /*|| alt_.isChanged() JR1099 - avoid duplicate!*/ )
        {
            insertUsedBy( alt_ );
        }
        else
        {    //JR1099
            update( alt_ );
        }
    }

    /**
     * Save changes to an Alternate.
     *
     * @param alt_ the Alternate
     * @return true the alternate is inserted/updated, false the alternate is deleted.
     * @throws SQLException
     */
    public boolean save( Alternates alt_ ) throws SQLException
    {
        // If it's deleted, don't need to do anything extra.
        if( alt_.isDeleted() )
        {
            delete( alt_ );
            return false;
        }

        //GF32723 check to make sure it does not exist in the database by name + type
        try
        {
//			if(DesignationHelper.isAlternateNameExists(alt_, _conn)) {	//JR1099 no need to check against database anymore based on the last requirement communicated via QA
//			    update(alt_);
//			} else
            // If it's new or changed be sure to record CSI changes also.
            if( alt_.isNew() )
                insert( alt_ );
            else if( alt_.isChanged() )
                update( alt_ );
        } catch( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        saveCSI( alt_ );
        return true;
    }

    /**
     * Save block changes to Alternates.
     *
     * @param alt_        the Alternate
     * @param idseq_      the AC id's being block edited
     * @param conteIdseq_ the matching Context ID's
     * @throws SQLException
     */
    public void save( Alternates alt_, String[] idseq_, String[] conteIdseq_ ) throws SQLException
    {
        // If it's deleted, don't need to do anything extra.
        if( alt_.isDeleted() )
        {
            delete( alt_ );
            return;
        }

        // If it's new, add to all AC's and save changes to CSI associations
        if( alt_.isNew() )
        {
            for( int i = 0; i < idseq_.length; ++i )
            {
                alt_.setACIdseq( idseq_[i] );
                alt_.setConteIdseq( conteIdseq_[i] );
                insert( alt_ );
                saveCSI( alt_ );
            }
            return;
        }

        // If it's an update, remember to save CSI changes
        if( alt_.isChanged() )
        {
            update( alt_ );
        }

        saveCSI( alt_ );
    }

    /**
     * Save version block changes to Alternates.
     *
     * @param alt_        the Alternate
     * @param beans_      the AC_Bean parents of the Alternates
     * @param idseq_      the AC id's being block edited
     * @param conteIdseq_ the matching Context ID's
     * @throws SQLException
     */
    public void save( Alternates alt_, AC_Bean[] beans_, String[] idseq_, String[] conteIdseq_ ) throws SQLException
    {
        // If it's deleted there's nothing to do because this is really a massive add and delete just means
        // don't add it. And the above loop has already deleted the Alternates that may have been copied
        // outside this method.
        if( alt_.isDeleted() )
        {
            return;
        }

        // If it's new, add to all new AC's and save changes to CSI associations
        if( alt_.isNew() )
        {
            for( int i = 0; i < idseq_.length; ++i )
            {
                // If the AC was not Versioned for any reason, don't add the Alternates.
                String newIdseq = beans_[i].getIDSEQ();
                if( idseq_[i].equals( newIdseq ) )
                    continue;

                alt_.setACIdseq( newIdseq );
                alt_.setConteIdseq( beans_[i].getContextIDSEQ() );
                insert( alt_ );
                saveCSI( alt_ );
            }
            return;
        }

        // Processing a single Alternate, first determine if it's Original AC has been versioned.
        int acNdx;
        String newIdseq = null;
        for( acNdx = 0; acNdx < idseq_.length; ++acNdx )
        {
            // Find the original AC for this ALT.
            String oldIdseq = alt_.getAcIdseq();
            if( oldIdseq != null && idseq_[acNdx].equals( oldIdseq ) )
            {
                // Now verify the new AC is not null and is not the same as the old one.
                newIdseq = beans_[acNdx].getIDSEQ();
                if( newIdseq != null && newIdseq.length() > 0 && !newIdseq.equals( oldIdseq ) )
                {
                    break;
                }
            }
        }

        // If we don't find a match there was an error and a new AC wasn't created for this one.
        if( acNdx == idseq_.length )
            return;

        // If it's an update or nothing has been done it really means just add it to a single AC and not all AC's
        alt_.setACIdseq( newIdseq );
        alt_.setConteIdseq( beans_[acNdx].getContextIDSEQ() );
        insert( alt_ );
        alt_.getCSITree().markNew();
        saveCSI( alt_ );

        return;
    }

    /**
     * For a simple select, return results as a String list.
     *
     * @param select_ the SQL select
     * @return the result set
     * @throws ToolException
     */
    private String[] getList( String select_ ) throws ToolException
    {
        String[] list = new String[0];

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = _conn.prepareStatement( select_ );
            rs = pstmt.executeQuery();

            // There will be only 1 result if any. Format and return to the caller.
            Vector<String> temp = new Vector<String>();
            while( rs.next() )
            {
                temp.add( rs.getString( 1 ) );
            }
            // Convert to an array
            list = new String[temp.size()];
            for( int i = 0; i < list.length; ++i )
                list[i] = temp.get( i );
        } catch( SQLException ex )
        {
            _log.error( "SQL: " + select_, ex );
            rs = SQLHelper.closeResultSet( rs );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
            throw new ToolException( ex );
        } finally
        {
            rs = SQLHelper.closeResultSet( rs );
            pstmt = SQLHelper.closePreparedStatement( pstmt );
        }
        // Return the result set
        return list;
    }

    /**
     * Get the Alternate Name Types.
     *
     * @return the Alternate Name Types.
     * @throws ToolException
     */
    public String[] getDesignationTypes() throws ToolException
    {
        String select = "select detl_name from sbr.designation_types_lov_view where detl_name not in ( "
                + "select value from sbrext.tool_options_view_ext where property like 'EXCLUDE.DESIGNATION_TYPE.%' "
                + ") order by upper(detl_name)";
        return getList( select );
    }

    /**
     * Get the Languages
     *
     * @return the Languages
     * @throws ToolException
     */
    public String[] getLangs() throws ToolException
    {
        String select = "select name from sbr.languages_lov_view order by (name)";
        return getList( select );
    }

    /**
     * Get the Alternate Definition Types.
     *
     * @return the Alternate Definition Types.
     * @throws ToolException
     */
    public String[] getDefinitionTypes( boolean showMC_ ) throws ToolException
    {
        String select = "select defl_name from sbrext.definition_types_lov_view_ext where defl_name not in ( "
                + "select value from sbrext.tool_options_view_ext where property like 'EXCLUDE.DEFINITION_TYPE.%' "
                + ")";
        if( !showMC_ )
            select += " and defl_name <> '" + _manuallyCuratedDef + "'";
        return getList( select + " order by upper(defl_name)" );
    }

    /**
     * Test if the CSI type provided is the special UML_PACKAGE_NAME
     *
     * @param name_ the CSI type to test
     * @return true if this is the UML_PACKAGE_NAME type
     */
    public static boolean isPackageName( String name_ )
    {
        return _packageName.equals( name_ );
    }

    /**
     * Test if the CSI type provided is the special UML_PACKAGE_ALIAS
     *
     * @param alias_ the CSI type to test
     * @return true if this is the UML_PACKAGE_ALIAS type
     */
    public static boolean isPackageAlias( String alias_ )
    {
        return _packageAlias.equals( alias_ );
    }

    /**
     * Get the name of the CSI type for a UML Package Alias
     *
     * @return the package alias type name
     */
    public static String getPackageAliasName()
    {
        return _packageAlias;
    }

    /**
     * Get the default language for the caDSR
     *
     * @return the default language
     * @throws ToolException
     */
    public String getDefaultLanguage() throws ToolException
    {
        String select = "select value from sbrext.tool_options_view_ext where tool_name = 'caDSR' and property = 'DEFAULT.LANGUAGE'";
        String[] results = getList( select );
        switch( results.length )
        {
            case 0:
                results = getLangs();
                switch( results.length )
                {
                    case 0:
                        return "";
                    case 1:
                        return results[0];
                    default:
                        return "";
                }

            case 1:
                return results[0];

            default:
                return "";
        }
    }

    private Connection _conn;

    public static final int _MAXNAMELEN = 255;
    public static final int _MAXDEFLEN = 2000;

    public static final String _addDesigType = "USED_BY";
    public static final String _manuallyCuratedDef = "Manually-curated";

    private static String _packageName = null;
    private static String _packageAlias = null;
    private static final Logger _log = Logger.getLogger( DBAccess.class );
}
