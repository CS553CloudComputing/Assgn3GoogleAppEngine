/**
 * 
 */
package com.ksingh14.gae.gcs;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author KaranJeet
 *
 */
@SuppressWarnings("serial")
public class RemoveAll extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	        throws ServletException, IOException
	{
		int i=Util.RemoveAll();
		if(i==1)
		{
			Date d_end = new Date();
			HttpSession session = req.getSession();
	    	 if (session!=null) {
	             //log.warning("session received");
	             session.setAttribute("endTimeRemove", d_end);
	          }
			res.sendRedirect("index.jsp?removeallresult=Everything deleted&removeallretry=0");
		}
		else if(i==2)
			res.sendRedirect("index.jsp?removeallretry=1");
		else
			res.sendRedirect("index.jsp?removeallresult=Key not found");
	}
}