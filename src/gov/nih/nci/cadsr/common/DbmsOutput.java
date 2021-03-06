package gov.nih.nci.cadsr.common;

/**
 * Source: http://asktom.oracle.com/pls/asktom/f?p=100:11:0::::P11_QUESTION_ID:45027262935845
 */
import java.sql.*;

import org.apache.log4j.Logger;

public class DbmsOutput {
	private static final Logger logger = Logger.getLogger(DbmsOutput.class.getName());

	/*
	 * our instance variables. It is always best to use callable or prepared
	 * statements and prepare (parse) them once per program execution, rather
	 * then one per execution in the program. The cost of reparsing is very
	 * high. Also -- make sure to use BIND VARIABLES!
	 * 
	 * we use three statments in this class. One to enable dbms_output -
	 * equivalent to SET SERVEROUTPUT on in SQL*PLUS. another to disable it --
	 * like SET SERVEROUTPUT OFF. the last is to "dump" or display the results
	 * from dbms_output using system.out
	 */
	private CallableStatement enable_stmt;
	private CallableStatement disable_stmt;
	private CallableStatement show_stmt;
	private boolean enabled = false;

	/*
	 * our constructor simply prepares the three statements we plan on
	 * executing.
	 * 
	 * the statement we prepare for SHOW is a block of code to return a String
	 * of dbms_output output. Normally, you might bind to a PLSQL table type but
	 * the jdbc drivers don't support PLSQL table types -- hence we get the
	 * output and concatenate it into a string. We will retrieve at least one
	 * line of output -- so we may exceed your MAXBYTES parameter below. If you
	 * set MAXBYTES to 10 and the first line is 100 bytes long, you will get the
	 * 100 bytes. MAXBYTES will stop us from getting yet another line but it
	 * will not chunk up a line.
	 */
	public DbmsOutput(Connection conn) throws SQLException {
		enable_stmt = conn.prepareCall("begin dbms_output.enable(:1); end;");
//		enable_stmt = conn.prepareCall("begin dbms_output.enable(:1);dbms_output.enable(NULL); end;");		//set it to unlimited c.f. http://stackoverflow.com/questions/16476568/how-to-increase-dbms-output-buffer
		disable_stmt = conn.prepareCall("begin dbms_output.disable; end;");

		show_stmt = conn
				.prepareCall("declare "
//						+ "    l_line varchar2(255); "
						+ "    l_line varchar2(32000); "
						+ "    l_done number; "
						+ "    l_buffer long; "
						+ "begin "
						+ "  loop "
						+ "    exit when length(l_buffer)+255 > :maxbytes OR l_done = 1; "
						+ "    dbms_output.get_line( l_line, l_done ); "
						+ "    l_buffer := l_buffer || l_line || chr(10); "
						+ "  end loop; " + " :done := l_done; "
						+ " :buffer := l_buffer; " + "end;");
	}

	/*
	 * enable simply sets your size and executes the dbms_output.enable call
	 */
	public void enable(int size) throws SQLException {
		enable_stmt.setInt(1, size);
		enable_stmt.executeUpdate();
		enabled = true;
		LogUtil.setLogger(logger);
	}
	
	//Caused: 14:47:02,418 ERROR [STDERR] java.sql.SQLException: Missing IN or OUT parameter at index:: 1
//	public void enable() throws SQLException {
//		//NULL imply unlimited buffer (http://docs.oracle.com/cd/B19306_01/appdev.102/b14258/d_output.htm)
//		
//		enable_stmt.executeUpdate();
//		enabled = true;
//		LogUtil.setLogger(logger);
//	}
	
	/*
	 * disable only has to execute the dbms_output.disable call
	 */
	public void disable() throws SQLException {
		disable_stmt.executeUpdate();
		LogUtil.setLogger(null);
	}

	/*
	 * show does most of the work. It loops over all of the dbms_output data,
	 * fetching it in this case 32,000 bytes at a time (give or take 255 bytes).
	 * It will print this output on stdout by default (just reset what
	 * System.out is to change or redirect this output).
	 */

	public void show() throws SQLException {
		if(enabled) {
			int done = 0;
	
			show_stmt.registerOutParameter(2, java.sql.Types.INTEGER);
			show_stmt.registerOutParameter(3, java.sql.Types.VARCHAR);
	
			for (;;) {
				show_stmt.setInt(1, 1000000);
				show_stmt.executeUpdate();
				LogUtil.log(show_stmt.getString(3));
				if ((done = show_stmt.getInt(2)) == 1)
					break;
			}
		} else {
			logger.info("DBMS_OUTPUT disabled");
		}
	}

	/*
	 * close closes the callable statements associated with the DbmsOutput
	 * class. Call this if you allocate a DbmsOutput statement on the stack and
	 * it is going to go out of scope -- just as you would with any callable
	 * statement, result set and so on.
	 */
	public void close() throws SQLException {
		enable_stmt.close();
		disable_stmt.close();
		show_stmt.close();
	}
}
