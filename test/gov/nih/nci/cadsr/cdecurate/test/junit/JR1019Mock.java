package gov.nih.nci.cadsr.cdecurate.test.junit;

import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cdecurate.test.helpers.DBUtil;
import gov.nih.nci.cadsr.cdecurate.test.helpers.EasyMockAdapter;
import gov.nih.nci.cadsr.common.TestUtil;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
  * https://tracker.nci.nih.gov/browse/CURATNTOOL-1016
  * 
  * Setup: Enter userId and password in the VM argument (NOT program argument!!!) in the following format:
  * 
  * -Du=SBREXT -Dp=[replace with the password]
  * 
  */
public class JR1019Mock extends EasyMockAdapter {
	
	public JR1019Mock() {
		initEasyMock();
	}

	@Override
	public HttpSession initEasyMock() {
		// TODO Auto-generated method stub
		return super.initEasyMock();
	}

	public HttpSession getSession() {
		return session;
	}

	public HttpServletRequest getServletRequest() {
		return request;
	}

	public HttpServletResponse getServletResponse() {
		return response;
	}
	
}