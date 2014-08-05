package gov.nih.nci.cadsr.cdecurate.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloadedDataLoader implements ValueLoader {
	final int TOTAL = 3;	//total members
	public static final int ID_INDEX = 0;
	public static final int TYPE_INDEX = 1;
	public static final int LIMIT_INDEX = 2;
	ArrayList<String> downloadID;		//1
	String downloadType;				//2
	String downloadLimit;				//3

    public DownloadedDataLoader(
    		ArrayList<String> downloadID,
    		String downloadType,
    		String downloadLimit
    		) {
        this.downloadID = downloadID;
        this.downloadType = downloadType;
        this.downloadLimit = downloadLimit;
    }

	public Object load() {
    	List data = new ArrayList(TOTAL);
    	data.add(downloadID);
    	data.add(downloadType);
    	data.add(downloadLimit);
        
    	return data;
    }
}
