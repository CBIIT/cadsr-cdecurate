/*L
 * Copyright ScenPro Inc, SAIC-F
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */

// Copyright (c) 2006 ScenPro, Inc.

// $Header: /cvsshare/content/cvsroot/cdecurate/src/gov/nih/nci/cadsr/cdecurate/database/Alternates.java,v 1.37 2007-09-26 16:51:36 chickerura Exp $
// $Name: not supported by cvs2svn $

package gov.nih.nci.cadsr.cdecurate.database;

import gov.nih.nci.cadsr.cdecurate.ui.AltNamesDefsServlet;
import gov.nih.nci.cadsr.cdecurate.ui.AltNamesDefsSession;
import gov.nih.nci.cadsr.cdecurate.util.ToolException;
import gov.nih.nci.cadsr.cdecurate.util.Tree;
import gov.nih.nci.cadsr.cdecurate.util.TreeNode;

/**
 * This class describes a single Alternate Name or Alternate Definition.
 * 
 * @author lhebel
 *
 */
public class Alternates
{
    
    private int _instance;
    private String _title;
    private boolean _display;
    private String _name;
    private String _type;
    private String _language;
    private String _altIdseq;
    private String _acIdseq;
    private String _conteIdseq;
    private String _conteName;
    private String _HTMLformat1;
    private String _HTMLformat2;
    private boolean _changed;
    private boolean _delete;
    private boolean _editable;
    private Tree _root;

    private static final String _defHTMLformat1 = "<tr " + TreeNode._nodeName + "=\"{[NAME]}\" " + TreeNode._nodeLevel + "=\"{[NODELEVEL]}\" " + TreeNode._nodeValue + "=\"{[NODEVALUE]}\">\n"
        + "<td class=\"alt0\" title=\"{[NTITLE]}\">"
            + "<table><tr>"
            + "<td class=\"alt9\">{[EDITGLYPH]}</td>"
            + "<td class=\"alt9\">{[DELGLYPH]}</td>"
            + "<td><span style=\"text-decoration: {[DELFLAG]}\"><b>{[NAME]}</b></span></td>"
            + "</tr></table></td>\n"
        + "<td class=\"alt1\" title=\"Context\"><b>{[CONTEXT]}</b></td>\n"
        + "<td class=\"alt1\" title=\"Alternate Type\"><b>{[TYPE]}</b></td>\n"
        + "<td class=\"alt1\" title=\"Language\"><b>{[LANG]}</b></td>\n"
        + "</tr>\n{[CSI]}";
    private static final String _defHTMLformat2 = "<tr " + TreeNode._nodeName + "=\"{[NAME]}\" " + TreeNode._nodeLevel + "=\"{[NODELEVEL]}\" " + TreeNode._nodeValue + "=\"{[NODEVALUE]}\">\n"
        + "<td class=\"ind{[MARGIN]}\" title=\"{[NTITLE]}\">"
            + "<table><tr>"
            + "<td class=\"alt9\">{[EDITGLYPH]}</td>"
            + "<td class=\"alt9\">{[DELGLYPH]}</td>"
            + "<td>{[INSTANCE]}:&nbsp;<span style=\"text-decoration: {[DELFLAG]}\">{[NAME]}</span></td>"
            + "</tr></table></td>\n"
        + "<td class=\"alt2\" title=\"Context\">{[CONTEXT]}</td>\n"
        + "<td class=\"alt2\" title=\"Alternate Type\">{[TYPE]}</td>\n"
        + "<td class=\"alt3\" title=\"Language\">{[LANG]}</td>\n"
        + "</tr>\n";
    
    private static final String EDITGLYPH = "<img src=\"images/edit.gif\" title=\"Edit\" onclick=\"doEdit(this);\"/>";
    private static final String DELGLYPH1 = "<span class=\"restore\" title=\"Restore\" onclick=\"doRestore(this);\"/>&#81;</span>";
    private static final String DELGLYPH2 = "<img src=\"images/delete.gif\" title=\"Delete\" onclick=\"doDelete(this);\"/>";
    
    public static final String _HTMLprefix = "<table>\n";
    public static final String _HTMLsuffix = "</table>\n";

    public static final int _MISSINGAC = -1;
    public static final int _MISSINGCONTE = -2;
    public static final int _MISSINGTYPE = -3;
    public static final int _MISSINGNAME = -4;
    public static final int _INSTANCENAME = 0;
    public static final int _INSTANCEDEF = 1;
    private static final String _INSTANCEDEFTITLE = "<tr><td colspan=\"4\"><p style=\"margin: 0.3in 0in 0in 0in\"><b>Definitions</b></p></td></tr>\n";
    private static final String _INSTANCENAMETITLE = "<tr><td colspan=\"4\"><p style=\"margin: 0.3in 0in 0in 0in\"><b>Names</b></p></td></tr>\n";
    // private static final String _INSTANCEUNKTITLE = "<tr><td colspan=\"4\"><p style=\"margin: 0.3in 0in 0in 0in\"><b>UNKNOWN</b></p></td></tr>\n";

    /**
     * Local test entry.
     * 
     * @param args_ [0] is the database URL for connectivity as needed.
     */
    public static void main(String[] args_)
    {
    }
    
    /**
     * Duplicate the object. All references are to new instances and not existing
     * object pointers.
     * 
     * @return the copied object
     */
    public Alternates dupl()
    {
        Alternates temp = new Alternates(this);
        
        return temp;
    }
    
    public boolean equals(Alternates other_)
    {
        // The Alternate must be a name to be equal - we don't do definitions/descriptions
        if (!this.isName())
            return false;
        if (!other_.isName())
            return false;
        
        // The primary key when present must not be the same or we are comparing
        // to ourself.
        if (this._altIdseq == null && other_._altIdseq != null)
            return false;
        
        if (this._altIdseq != null && other_._altIdseq == null)
            return false;
        
        // When comparing to ourself we will lie and say the objects aren't equal because
        // the calling method expects that behavior.
        if (this._altIdseq.equals(other_._altIdseq))
            return false;

        // The AC owner when present must be the same to be equal.
        if (this._acIdseq == null && other_._acIdseq != null)
            return false;
        
        if (this._acIdseq != null && other_._acIdseq == null)
            return false;

        // The Context when present must be the same to be equal.
        if (this._conteIdseq == null && other_._conteIdseq != null)
            return false;
        
        if (this._conteIdseq != null && other_._conteIdseq == null)
            return false;

        // The Alt Name Type when present must be the same to be equal.
        if (this._type == null && other_._type != null)
            return false;
        
        if (this._type != null && other_._type == null)
            return false;

        // The Alt Name Text (ie name) when present must be the same to be equal.
        if (this._name == null && other_._name != null)
            return false;
        
        if (this._name != null && other_._name == null)
            return false;

        // Now check all the required fields for equality. The name is compared case
        // insensitive.
        boolean aFlag = (this._acIdseq == null && other_._acIdseq == null) || this._acIdseq.equals(other_._acIdseq);
        boolean cFlag = (this._conteIdseq == null && other_._conteIdseq == null) || this._conteIdseq.equals(other_._conteIdseq);
        boolean tFlag = (this._type == null && other_._type == null) || this._type.equals(other_._type);
        boolean nFlag = (this._name == null && other_._name == null) || this._name.compareToIgnoreCase(other_._name) == 0;
        return (aFlag && cFlag && tFlag && nFlag);
    }

    /**
     * A private constructor that copies values not references from another Alternates object.
     * 
     * @param old_ the old object
     */
    private Alternates(Alternates old_)
    {
        _instance = old_._instance;
        _name = old_._name;
        _type = old_._type;
        _language = old_._language;
        _altIdseq = old_._altIdseq;
        _acIdseq = old_._acIdseq;
        _conteIdseq = old_._conteIdseq;
        _conteName = old_._conteName;
        _HTMLformat1 = old_._HTMLformat1;
        _HTMLformat2 = old_._HTMLformat2;
        _changed = old_._changed;
        _delete = old_._delete;
        _editable = old_._editable;
        _root = old_._root.dupl();
        _title = old_._title;
        _display = old_._display;
    }

    /**
     * Default constructor
     *
     */
    public Alternates()
    {
        clear();
    }
    
    /**
     * Constructor
     * 
     * @param instance_ either _INSTANCENAME or _INSTANCEDEF
     * @param name_ the text either an Alternate Name or Alternate Definition
     * @param type_ the alternate type as defined in the LOV database table for Names and Definitions
     * @param lang_ the language as defined in the LANGUAGE database table
     * @param ac_ the idseq for the parent Administered Component
     * @param desig_ the idseq of the Alternate Name or Alternate Definition
     * @param conte_ the idseq of the Context
     * @param conteName_ the name of the Context
     */
    public Alternates(int instance_, String name_, String type_, String lang_, String ac_, String desig_, String conte_, String conteName_)
    {
        setInstance(instance_);
        setName(name_);
        setType(type_);
        setLanguage(lang_);
        setConteIdseq(conte_);
        setConteName(conteName_);
        _acIdseq = ac_;
        _altIdseq = desig_;
        _HTMLformat1 = _defHTMLformat1;
        _HTMLformat2 = _defHTMLformat2;
        _root = new Tree(new TreeNode("root", null, false));
        _changed = false;
        _delete = false;
        _editable = true;
        _display = true;
    }

    /**
     * Blank the object content.
     *
     */
    public void clear()
    {
        _name = "";
        _type = "";
        _language = "";
        _conteName = "";
        _conteIdseq = "";
        _language = "ENGLISH";
        _HTMLformat1 = _defHTMLformat1;
        _HTMLformat2 = _defHTMLformat2;
        _root = new Tree(new TreeNode("root", null, false));
        _changed = false;
        _delete = false;
        _editable = true;
        _display = true;
    }

    /**
     * Get the CSI Tree
     * 
     * @return the CSI Tree
     */
    public Tree getCSITree()
    {
        return _root;
    }
    
    /**
     * Set the text, either a Name or Definition, see the Constructor.
     * 
     * @param name_ the Alternate text.
     */
    public void setName(String name_)
    {
        if (_name != null && _name.equals(name_))
            return;

        _name = (name_ == null) ? "" : name_;
        _changed = true;
    }

    /**
     * Set the alternate type, see the Constructor.
     * 
     * @param type_ the type as defined in the LOV database table
     */
    public void setType(String type_)
    {
        if (_type != null && _type.equals(type_))
            return;

        _type = (type_ == null) ? "" : type_;
        _changed = true;
    }

    /**
     * Set the Language
     * 
     * @param lang_ the language as defined in the database
     */
    public void setLanguage(String lang_)
    {
        if (_language != null && _language.equals(lang_))
            return;

        _language = (lang_ == null) ? "" : lang_;
        _changed = true;
    }
    
    /**
     * The parent AC idseq.
     * 
     * @param idseq_ the database id
     */
    public void setACIdseq(String idseq_)
    {
        _acIdseq = idseq_;
    }
    
    /**
     * Set the Context idseq.
     * 
     * @param idseq_ the database id
     */
    public void setConteIdseq(String idseq_)
    {
        if (_conteIdseq != null && _conteIdseq.equals(idseq_))
            return;

        _conteIdseq = idseq_;
        _changed = true;
    }
    
    /**
     * Set the Context name.
     * 
     * @param name_ the context name
     */
    public void setConteName(String name_)
    {
        _conteName = (name_ == null) ? "" : name_;
    }
    
    /**
     * Mark this object to be deleted from the database.
     *
     */
    public void toBeDeleted()
    {
        _delete = true;
    }

    /**
     * Mark this object to keep, i.e. undelete.
     *
     */
    public void markToKeep()
    {
        _delete = false;
    }
    
    /**
     * Make this Alternate read only - user can't edit or delete it.
     *
     */
    public void makeReadOnly()
    {
        _editable = false;
    }

    /**
     * Test the object type.
     * 
     * @return true if this object is an Alternate Name.
     */
    public boolean isName()
    {
        return (_INSTANCENAME == _instance);
    }
    
    /**
     * Compare the argument to the Alternate Name/Def database id
     * 
     * @param idseq_ the test database id
     * @return true if the argument matches the Alternate id
     */
    public boolean isIdseq(String idseq_)
    {
        return _altIdseq.equals(idseq_);
    }

    /**
     * Test the object type.
     * 
     * @return true if this object is an Alternate Definition.
     */
    public boolean isDef()
    {
        return (_INSTANCEDEF == _instance);
    }

    /**
     * Test the persisted state.
     * 
     * @return true if this object is marked as a new object, i.e. not yet saved to the database.
     */
    public boolean isNew()
    {
        return (_altIdseq == null || _altIdseq.charAt(0) == AltNamesDefsSession._newPrefix.charAt(0));
    }
    
    /**
     * Test the persisted state.
     * 
     * @return true if this object exists in the database but has been changed and not yet saved back.
     */
    public boolean isChanged()
    {
        return _changed;
    }

    /**
     * Test the persisted state.
     * 
     * @return true if this object is marked to be deleted from the database
     */
    public boolean isDeleted()
    {
        return _delete;
    }

    /**
     * Check all the database id's for this Alternate.
     * 
     * @return 0 if everything is good, otherwise an error code for the problem.
    private int validateCheck()
    {
        if (_acIdseq == null)
        {
            return _MISSINGAC;
        }
        if (_conteIdseq == null)
        {
            return _MISSINGCONTE;
        }
        if (_type == null)
        {
            return _MISSINGTYPE;
        }
        if (_name == null)
        {
            return _MISSINGNAME;
        }
        return 0;
    }
     */
    
    /**
     * Validate the required data in this object.
     * 
     * @throws SQLException
    private void validate() throws SQLException
    {
        switch (validateCheck())
        {
        case _MISSINGAC:
            throw new SQLException(this.getClass().getName() + ": AC IDSEQ is null, use method setACIdseq().");
        case _MISSINGCONTE:
            throw new SQLException(this.getClass().getName() + ": Context IDSEQ is null, use method setConteIdseq().");
        case _MISSINGTYPE:
            throw new SQLException(this.getClass().getName() + ": Type is null, use method setType().");
        case _MISSINGNAME:
            throw new SQLException(this.getClass().getName() + ": Name is null, use method setName().");
        }
    }
     */
    
    /**
     * Get the Alternate text, either the name or the definition.
     * 
     * @return the text of this Alternate
     */
    public String getName()
    {
        return _name;
    }
    
    /**
     * Get the Alternate type.
     * 
     * @return the value as defined in the database type LOV.
     */
    public String getType()
    {
        return _type;
    }
    
    /**
     * Get the language.
     * 
     * @return the language indication for the text stored in this alternate.
     */
    public String getLanguage()
    {
        return _language;
    }
    
    /**
     * Get the object database id.
     * 
     * @return the idseq for this alternate name/definition
     */
    public String getAltIdseq()
    {
        return _altIdseq;
    }
    
    /**
     * Set the object database id.
     * 
     * @param idseq_ the alternated name/definition database id.
     */
    public void setAltIdseq(String idseq_)
    {
        _altIdseq = idseq_;
    }
    
    /**
     * Get the parent administered component database id.
     * 
     * @return the AC idseq.
     */
    public String getAcIdseq()
    {
        return (_acIdseq == null || _acIdseq.length() == 0) ? null : _acIdseq;
    }
    
    /**
     * Get the associated Context database id.
     * 
     * @return the Context idseq
     */
    public String getConteIdseq()
    {
        return (_conteIdseq == null || _conteIdseq.length() == 0) ? null : _conteIdseq;
    }
    
    /**
     * Get the Context Name
     * 
     * @return the Context Name for the Context idseq
     */
    public String getConteName()
    {
        return _conteName;
    }

    /**
     * Set the HTML format1 string.
     * 
     * @param format_ the string used by the toHTML() method to format the object output.
     */
    public void setHTMLformat1(String format_)
    {
        _HTMLformat1 = format_;
    }

    /**
     * Set the HTML format2 string.
     * 
     * @param format_ the string used by the toHTML2() method to format the object output.
     */
    public void setHTMLformat2(String format_)
    {
        _HTMLformat1 = format_;
    }

    /**
     * Get the HTML format1 string.
     * 
     * @return the HTML format string used by the toHTML() method.
     */
    public String getHTMLformat1()
    {
        return _HTMLformat1;
    }
    
    /**
     * Get the HTML format2 string.
     * 
     * @return the HTML format string used by the toHTML2() method.
     */
    public String getHTMLformat2()
    {
        return _HTMLformat2;
    }

    /**
     * Get the instance indicator.
     * 
     * @return either _INSTANCENAME or _INSTANCEDEF
     */
    public int getInstance()
    {
        return _instance;
    }
    
    /**
     * Get the object title.
     * 
     * @return the HTML title string
     */
    public String getHtmlTitle()
    {
        return _title;
    }
    
    /**
     * Set the instance indicator.
     * 
     * @param instance_ either _INSTANCENAME or _INSTANCEDEF
     */
    public void setInstance(int instance_)
    {
        _instance = instance_;
        if (isName())
            _title = _INSTANCENAMETITLE;
        else
            _title = _INSTANCEDEFTITLE;
    }
    
    /**
     * Mark this Alternate as "new"
     * 
     * @param idseq_ the new Alternate database id
     */
    public void markNew(String idseq_)
    {
        _altIdseq = idseq_;
        _root.markNew();
    }
    
    /**
     * Set the alternate as visible in a UI depending on the type being a manually
     * curated definition. This effectively determines if the alternate
     * returns anything from the toHTML methods.
     * 
     * @param showMC_ the show control value, true when the Alternate is visible, false when it should be hidden
     */
    public void display(boolean showMC_)
    {
        // Is this an Alternate Definition type of "manually curated"?
        boolean typeMC = (isName()) ? false : DBAccess._manuallyCuratedDef.equals(_type);

        // The show control will be "true" to display all Alt Defs, when it is "false" and the
        // object is a manually curated definition (true == false), don't display otherwise
        // show it.
        _display = showMC_ || (typeMC == showMC_);
    }

    /**
     * Format the Alternate for output using HTML tags.
     * 
     * @return the HTML formatted string with appropriate substitutions.
     */
    public String toHTML()
    {
        // Don't return anything for a hidden Alternate
        if (!_display)
            return "";

        String[] formats = new String[3];
        formats[AltNamesDefsServlet._classTypeAlt] = "(not used)";
        formats[AltNamesDefsServlet._classTypeCSI] = AltNamesDefsServlet._formatHTMLcsiView;
        formats[AltNamesDefsServlet._classTypeCS] = AltNamesDefsServlet._formatHTMLcsView;

        String text = _HTMLformat1;
        text = text.replace("{[NTITLE]}", (_instance == _INSTANCENAME) ? "Alternate Name" : "Alternate Definition");
        text = text.replace("{[NAME]}", _name.trim().replaceAll("[\\n]", "<br/>"));		//alt definition
        text = text.replace("{[TYPE]}", _type.replaceAll(" ", "&nbsp;"));
        text = text.replace("{[LANG]}", _language.replaceAll(" ", "&nbsp;"));
        text = text.replace("{[CONTEXT]}", _conteName.trim().replaceAll(" ", "&nbsp;"));
        text = text.replace("{[CSI]}", _root.toHTML(formats));
        text = text.replace("{[NODELEVEL]}", "0");
        text = text.replace("{[NODEVALUE]}",_altIdseq);
        text = text.replace("{[CLASSTYPE]}", String.valueOf(_instance));
        if (_editable)
        {
            text = text.replace("{[EDITGLYPH]}", EDITGLYPH);
        }
        else
        {
            text = text.replace("{[EDITGLYPH]}", "&nbsp;");
            text = text.replace("{[DELGLYPH]}", "&nbsp;");
            _delete = false;
        }
        if(_delete)
        {
            text = text.replace("{[DELFLAG]}", "line-through");
            text = text.replace("{[DELGLYPH]}", DELGLYPH1);
        }
        else
        {
            text = text.replace("{[DELFLAG]}", "none");
            text = text.replace("{[DELGLYPH]}", DELGLYPH2);
        }
        
        return text;
    }
    
    /**
     * Format the Alternate for output using HTML tags.
     * 
     * @return the HTML formatted string with appropriate substitutions.
     */
    public String toHTML2(int indent_)
    {
        // Don't return anything for a hidden Alternate
        if (!_display)
            return "";

        String instance = (_instance == _INSTANCENAME) ? "Name" : "Definition";
        String text = _HTMLformat2;
        text = text.replace("{[NTITLE]}", (_instance == _INSTANCENAME) ? "Alternate Name" : "Alternate Definition");
        text = text.replace("{[INSTANCE]}", instance);
        text = text.replace("{[NAME]}", _name.trim().replaceAll("[\\n]", "<br/>"));		//alt definition
        text = text.replace("{[TYPE]}", _type.replaceAll(" ", "&nbsp;"));
        text = text.replace("{[LANG]}", _language.replaceAll(" ", "&nbsp;"));
        text = text.replace("{[CONTEXT]}", _conteName.trim().replaceAll(" ", "&nbsp;"));
        text = text.replace("{[MARGIN]}",String.valueOf(indent_));
        text = text.replace("{[NODELEVEL]}",String.valueOf(indent_));
        text = text.replace("{[NODEVALUE]}",_altIdseq);
        text = text.replace("{[CLASSTYPE]}", String.valueOf(_instance));
        if (_editable)
        {
            text = text.replace("{[EDITGLYPH]}", EDITGLYPH);
        }
        else
        {
            text = text.replace("{[EDITGLYPH]}", "&nbsp;");
            text = text.replace("{[DELGLYPH]}", "&nbsp;");
            _delete = false;
        }
        if(_delete)
        {
            text = text.replace("{[DELFLAG]}", "line-through");
            text = text.replace("{[DELGLYPH]}", DELGLYPH1);
        }
        else
        {
            text = text.replace("{[DELFLAG]}", "none");
            text = text.replace("{[DELGLYPH]}", DELGLYPH2);
        }

        return text;
    }

    /**
     * Add Class Scheme Items heirarchy information for this Alternate.
     * 
     * @param nodes_ the TreeNode objects describing the CS and CSI entries
     * @param levels_ the child levels, each higher value is associated to the nearest lower value, e.g 1, 2, 2, 3 indicates [3] is a child of [2] which is a child of [0], [1] has no children
     * @throws ToolException
     */
    public void addCSI(TreeNode[] nodes_, int[] levels_) throws ToolException
    {
        _root.addHierarchy(nodes_, levels_);
    }
}