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
public class FindInFile extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	        throws ServletException, IOException
	{
		String key =  req.getParameter("findinfilekey_txt");
		String string =  req.getParameter("findinfilestring_txt");
		if(Util.FindInFile(key, string))
			res.sendRedirect("index.jsp?findinfileresult=String found");
		else
			res.sendRedirect("index.jsp?findinfileresult=String not found");
	}
}