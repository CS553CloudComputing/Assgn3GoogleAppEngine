/**
 * 
 */
package com.ksingh14.gae.gcs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author KaranJeet
 *
 */
@SuppressWarnings("serial")
public class Find extends HttpServlet {

	private static final Logger log = Logger.getLogger(Find.class.getName());
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	        throws ServletException, IOException
	{
		log.warning("doGet downloading file");
		String key =  req.getParameter("find_txt");
		String response = null;
		if(Util.Check(key))
		{
			response = Util.Find(key);
			res.setContentType("text/plain");
			res.setHeader("Content-Disposition", "attachment; filename=\""+key+"\"");
		    try
		    {
		        OutputStream outputStream = res.getOutputStream();
		        outputStream.write(response.getBytes());
		        outputStream.flush();
		        outputStream.close();
		    }
		    catch(Exception e)
		    {
		        log.warning("error downloading file");
		    }
		}
		else
		{	
			res.sendRedirect("index.jsp?findresult=Key not found");
		}
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	        throws ServletException, IOException
	{
		log.warning("doGet downloading file");
		String key =  req.getParameter("find_txt");
		String response = null;
		if(Util.Check(key))
		{
			response = Util.Find(key);
			res.setContentType("text/plain");
			res.setHeader("Content-Disposition", "attachment; filename=\""+key+"\"");
		    try
		    {
		        OutputStream outputStream = res.getOutputStream();
		        outputStream.write(response.getBytes());
		        outputStream.flush();
		        outputStream.close();
		    }
		    catch(Exception e)
		    {
		        log.warning("error downloading file");
		    }
		}
		else
		{	
			res.sendRedirect("index.jsp?findresult=Key not found");
		}
	}
}