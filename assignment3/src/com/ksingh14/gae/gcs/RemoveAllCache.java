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
public class RemoveAllCache extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	        throws ServletException, IOException
	{
		int i=Util.RemoveAllCache();
		if(i==1)
			res.sendRedirect("index.jsp?removeallcacheresult=Everything deleted&removeallcacheretry=0");
		else if(i==2)
			res.sendRedirect("index.jsp?removeallcacheretry=1");
		else
			res.sendRedirect("index.jsp?removeallcacheresult=Key not found");
	}
}