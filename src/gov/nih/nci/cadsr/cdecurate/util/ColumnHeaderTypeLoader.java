package gov.nih.nci.cadsr.cdecurate.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ColumnHeaderTypeLoader implements ValueLoader {
	final int TOTAL = 6;	//total members
	public static final int EXCLUDED_INDEX = 0;
	public static final int ALL_HEADERS_INDEX = 1;
	public static final int ALL_EXPANDED_HEADERS_INDEX = 2;
	public static final int ALL_TYPES_INDEX = 3;
	public static final int TYPEMAP_INDEX = 4;
	public static final int COLUMN_TYPES_INDEX = 5;
	ArrayList<String> excluded;									//1
	ArrayList<String> allHeaders;								//2
	ArrayList<String> allExpandedHeaders;						//3
	ArrayList<String> allTypes;									//4
	HashMap<String,ArrayList<String[]>> typeMap;				//5
	HashMap<String, String> arrayColumnTypes;					//6

    public ColumnHeaderTypeLoader(
    		ArrayList<String> excluded,
    		ArrayList<String> allHeaders,
    		ArrayList<String> allExpandedHeaders,
    		ArrayList<String> allTypes,
    		HashMap<String,ArrayList<String[]>> typeMap,
    		HashMap<String, String> arrayColumnTypes
    		) {
    	this.excluded = excluded;
        this.allHeaders = allHeaders;
        this.allExpandedHeaders = allExpandedHeaders;
        this.allTypes = allTypes;
        this.typeMap = typeMap;
        this.arrayColumnTypes = arrayColumnTypes;

    
    }

    public Object load() {
    	List data = new ArrayList(TOTAL);
    	data.add(excluded);
    	data.add(allHeaders);
    	data.add(allExpandedHeaders);
    	data.add(allTypes);
    	data.add(typeMap);
    	data.add(arrayColumnTypes);
        
    	return data;
    }
}
