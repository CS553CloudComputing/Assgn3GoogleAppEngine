/**
 * 
 */
package com.ksingh14.gae.gcs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author KaranJeet
 *
 */
@SuppressWarnings("serial")
public class List extends HttpServlet {

	private static final Logger log = Logger.getLogger(ViewHash.class.getName());
	@Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	    log.warning("readched doGet of View Hash");
	    try{
	    String[] s = Util.Listing();
	    String response = "<html><body>";
	    for(String _s:s)
	    {
	    	response+=(_s+"<br />");
	    }
	    response+="</body></html>";
	    resp.setContentType("text/html; charset=windows-1252");
	    PrintWriter out = resp.getWriter();
	    out.println(response);
	    out.close();
	    }
	    catch (Exception ex)
	    {
	    	resp.sendRedirect("Error.jsp?msg=No Key Found");
	    }
	  }
	
	@Override
	  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.warning("readched doGet of View Hash");
	    try{
	    String[] s = Util.Listing();
	    String response = "<html><body>";
	    for(String _s:s)
	    {
	    	response+=(_s+"<br />");
	    }
	    response+="</body></html>";
	    resp.setContentType("text/html; charset=windows-1252");
	    PrintWriter out = resp.getWriter();
	    out.println(response);
	    out.close();
	    }
	    catch (Exception ex)
	    {
	    	resp.sendRedirect("Error.jsp?msg=No Key Found");
	    }
	  }
}