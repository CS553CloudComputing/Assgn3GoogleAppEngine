/**
 * 
 */
package com.ksingh14.gae.gcs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author KaranJeet
 *
 */
@SuppressWarnings("serial")
public class Remove extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	        throws ServletException, IOException
	{
		String key =  req.getParameter("remove_txt");
		if(Util.Check(key))
		{
			if(Util.Remove(key))
				res.sendRedirect("index.jsp?removeresult=Key "+key+" removed");
		}
		else
		{	
			res.sendRedirect("index.jsp?removeresult=Nothing to remove");
		}
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	        throws ServletException, IOException
	{
		String key =  req.getParameter("remove_txt");
		if(Util.Check(key))
		{
			if(Util.Remove(key))
				res.sendRedirect("index.jsp?removeresult=Key "+key+" removed");
		}
		else
		{	
			res.sendRedirect("index.jsp?removeresult=Nothing to remove");
		}
	}
}