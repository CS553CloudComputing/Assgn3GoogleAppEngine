/**
 * 
 */
package com.ksingh14.gae.gcs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
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
public class DownloadAllTwice4Threads extends HttpServlet {

	private static final Logger log = Logger.getLogger(DownloadAllTwice4Threads.class.getName());
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	        throws ServletException, IOException
	{
		log.warning("doGet downloading file");
		String[] keys =  Util.Listing();
		String result=null;
		long d_start = (new Date()).getTime();
		result+="Starting at "+d_start;
		for(int i=0;i<2;i++)
		for(String key:keys)
		{
			long df_start = (new Date()).getTime();
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
		        long df_end = (new Date()).getTime();
		        result+="\nTime taken to find "+key+" is "+(df_end-df_start);
		    }
		    catch(Exception e)
		    {
		        log.warning("error downloading file");
		    }
		}
		else
		{	
			log.warning("error key not found");
		}
		}
		long d_end = (new Date()).getTime();
		result+="\nfile download finished at "+d_end;
		result+="\nTotal time taken - "+(d_end-d_start);
		res.setContentType("text/plain");
		res.setHeader("Content-Disposition", "attachment; filename=\"result.txt\"");
	    try
	    {
	        OutputStream outputStream = res.getOutputStream();
	        outputStream.write(result.getBytes());
	        outputStream.flush();
	        outputStream.close();
	    }
	    catch(Exception e)
	    {
	        log.warning("error downloading file");
	    }
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	        throws ServletException, IOException
	{
		log.warning("doGet downloading file");
		String[] keys =  Util.Listing();
		String result=null;
		long d_start = (new Date()).getTime();
		result+="Starting at "+d_start;
		for(int i=0;i<2;i++)
		for(String key:keys)
		{
			long df_start = (new Date()).getTime();
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
		        long df_end = (new Date()).getTime();
		        result+="\nTime taken to find "+key+" is "+(df_end-df_start);
		    }
		    catch(Exception e)
		    {
		        log.warning("error downloading file");
		    }
		}
		else
		{	
			log.warning("error key not found");
		}
		}
		long d_end = (new Date()).getTime();
		result+="\nfile download finished at "+d_end;
		result+="\nTotal time taken - "+(d_end-d_start);
		res.setContentType("text/plain");
		res.setHeader("Content-Disposition", "attachment; filename=\"result.txt\"");
	    try
	    {
	        OutputStream outputStream = res.getOutputStream();
	        outputStream.write(result.getBytes());
	        outputStream.flush();
	        outputStream.close();
	    }
	    catch(Exception e)
	    {
	        log.warning("error downloading file");
	    }
	}
}