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
public class CheckCache extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	        throws ServletException, IOException
	{
		String key =  req.getParameter("checkcache_txt");
		if(Memcache.checkCache(key))
			res.sendRedirect("index.jsp?checkcacheresult=Key found");
		else
			res.sendRedirect("index.jsp?checkcacheresult=Key not found");
	}
}
