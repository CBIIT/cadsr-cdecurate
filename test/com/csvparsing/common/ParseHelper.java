package com.csvparsing.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * @author Sharoon
 * @source http://interlan.blogspot.com/2012/12/simple-csv-parsing-example-with-pojo.html
 */
public class ParseHelper {

    public static String[] getColumns(BufferedReader br) {
        
        String record = "";
        StringTokenizer st = null;
        String[] columns = null;
        int totalColumns = 0;
        
        try {
            if((record = br.readLine()) != null){
                st = new StringTokenizer(record, ",");
                columns = new String[st.countTokens()];
                while(st.hasMoreTokens())
                {
                    
                    columns[totalColumns] = st.nextToken();
                    totalColumns++;
                }                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return columns;
    }
       

    /**
     * Method to get typed value
     * @param type
     * @param value 
     * @return
     */
    public static Object getTypedValue(Class<?> type, String value) {
        Object typedValue = null;
        if(type == int.class){
            value = !"".equals(value)?value:"0";
            typedValue = Integer.parseInt(value);
        } else if(type == Date.class){
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");                
            try {
                typedValue = (Date)(formatter.parse(value));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if(type == Double.class){
            value = !"".equals(value)?value:"0.0";
            typedValue = Double.parseDouble(value);
        } else if(type == Float.class){
            value = !"".equals(value)?value:"0.0f";
            typedValue = Double.parseDouble(value);
        } else if(type == boolean.class){
            value = !"".equals(value)?value:"0.0f";
            typedValue = Boolean.parseBoolean(value);
        } else if(type == String.class){
            typedValue = value;
        }
        return typedValue;
    }    

}
