package gov.nih.nci.cadsr.cdecurate.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloadRowsArrayDataLoader implements ValueLoader {
	final int TOTAL = 2;	//total members
	public static final int ROWS_INDEX = 0;
	public static final int ARRAY_DATA_INDEX = 1;
	ArrayList<String[]> rows;									//1
	ArrayList<HashMap<String,List<String[]>>> arrayData;	//2

    public DownloadRowsArrayDataLoader(
    		ArrayList<String[]> rows,
    		ArrayList<HashMap<String,List<String[]>>> arrayData
    		) {
        this.rows = rows;
        this.arrayData = arrayData;
    }

	public Object load() {
    	List data = new ArrayList(TOTAL);
    	data.add(rows);
    	data.add(arrayData);
        
    	return data;
    }
}
