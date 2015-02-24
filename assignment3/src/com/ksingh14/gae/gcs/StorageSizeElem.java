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
public class StorageSizeElem extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	        throws ServletException, IOException
	{
		double size=0;
		size=Util.StorageSizeElem();
		res.sendRedirect("index.jsp?storagesizeelemresult="+size);
	}
}
