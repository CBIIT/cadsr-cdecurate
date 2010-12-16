<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="org.codehaus.jettison.json.JSONObject"%>
<%@page import="org.codehaus.jettison.json.JSONArray"%>
  <%
  
  
  	ArrayList<String> headers = (ArrayList<String>) session.getAttribute("headers");
  	ArrayList<String> types = (ArrayList<String>) session.getAttribute("types");
  	ArrayList<String[]> rows = (ArrayList<String[]>) session.getAttribute("rows");

  	JSONArray jArray = new JSONArray();
	
	for (int rowLoop = 0; rowLoop < rows.size(); rowLoop++) {	    
		JSONObject row=new JSONObject();
  		for (int colLoop = 0; colLoop < headers.size(); colLoop++) {
  			
  			//Take Column Name from headers and take correct column from current row
		    row.put(headers.get(colLoop),rows.get(rowLoop)[colLoop]);
		    
    	}
    	jArray.put(row);
	
    }
    
    //Done constructing array, printing it:
    out.print(jArray.toString());
    out.flush();
  %>
