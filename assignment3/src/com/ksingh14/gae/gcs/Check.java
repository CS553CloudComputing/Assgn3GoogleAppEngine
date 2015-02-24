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
public class Check extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	        throws ServletException, IOException
	{
		String key =  req.getParameter("check_txt");
		if(Util.Check(key))
			res.sendRedirect("index.jsp?checkResult=Key found");
		else
			res.sendRedirect("index.jsp?checkResult=Key not found");
	}
}
