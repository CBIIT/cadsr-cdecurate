/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

// Copyright (c) 2006 ScenPro, Inc.

// $Header: /cvsshare/content/cvsroot/cdecurate/src/gov/nih/nci/cadsr/cdecurate/database/TreeNodeAlt.java,v 1.36 2007-09-10 17:18:20 hebell Exp $
// $Name: not supported by cvs2svn $

package gov.nih.nci.cadsr.cdecurate.database;

import gov.nih.nci.cadsr.cdecurate.ui.AltNamesDefsServlet;
import gov.nih.nci.cadsr.cdecurate.util.Tree;
import gov.nih.nci.cadsr.cdecurate.util.TreeNode;

/**
 * The specific TreeNode for Alternate Names and Definitions
 * 
 * @author lhebel
 *
 */
public class TreeNodeAlt extends TreeNode
{
    private TreeNodeAlt(TreeNodeAlt old_)
    {
        super(old_);
        _classType = AltNamesDefsServlet._classTypeCS;
    }
    
    public TreeNodeAlt dupl()
    {
        TreeNodeAlt temp = new TreeNodeAlt(this);
        temp._alt = _alt.dupl();
        
        return temp;
    }

    public TreeNodeAlt(Alternates alt_, String idseq_)
    {
        // Because an Alternate may appear multiple times within a hierarchy
        // the "value" managed by the parent class, TreeNode, must concatenate
        // the referenced CSI idseq and the Alternate idseq to keep it unique.

        super(alt_.getName(), idseq_ + " " + alt_.getAltIdseq(), false);
        _alt = alt_;
        _classType = AltNamesDefsServlet._classTypeCS;
    }
    
    public Alternates getAlt()
    {
        return _alt;
    }
    
    public String getID()
    {
        return _alt.getAltIdseq();
    }
    
    public String toHTML(Tree branch_, int indent_, String format_)
    {
        return _alt.toHTML2(indent_);
    }

    public void clear()
    {
        super.clear();
        
        _alt = null;
    }
    
    private Alternates _alt;
}
