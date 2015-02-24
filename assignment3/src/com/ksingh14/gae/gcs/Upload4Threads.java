package com.ksingh14.gae.gcs;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.ThreadManager;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class Upload4Threads extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    private static final Logger log = Logger.getLogger(Upload4Threads.class.getName());
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
    	final HttpServletRequest request = req;
    	log.warning("reached insert4");
    	try
    	{
    	Map<String, List<BlobInfo>> blobsData = blobstoreService.getBlobInfos(req);
    	for (String key : blobsData.keySet())
    	{
    		final List<BlobInfo> blobList = blobsData.get(key);
    			for(int i=0;i<4;i++)
    			{
    				final int p;
    				switch(i)
    				{
    				case 0:
    					p=0;
    					break;
    				case 1:
    					p=1;
    					break;
    				case 2:
    					p=2;
    					break;
    				case 3:
    					p=3;
    					break;
    				default: p=0;
    				}
    			Thread thread = ThreadManager.createThreadForCurrentRequest(new Runnable() {
              	  public void run() {
              	    try {
              	    	int _i=blobList.size()/4;
              	    	HashMap<String, Object> hash = new HashMap<String, Object>();
              	        
              	        // Using the asynchronous cache
              	        log.warning("thread no. "+p+" picking up files from "+p*_i+" to "+
              	        ((p==3)?blobList.size():(p+1)*_i));
    	        for(BlobInfo blob:blobList.subList(p*_i, (p==3)?blobList.size():(p+1)*_i))
    	        {
    	        	log.warning("picked up file of size - "+blob.getSize());
    	        	hash.putAll(Util.Insert(blob));
    	        }
    	        HashXml.SaveHash(hash, "hash"+p+".xml");
    	    	Date d = new Date();
    	    	HttpSession session = request.getSession();
    	    	 if (session!=null) {
    	             log.warning("session received - 4 thread");
    	             session.setAttribute("endTime", d);
    	          }
    	          else{
    	             log.warning("session not received - 4 thread");
    	          }
              	    }
    	        catch (Exception ex) {
        	    	log.warning(ex.getMessage());
        	      throw new RuntimeException("Interrupted in loop:", ex);
        	    }  }
              	
            	});
            thread.start();
        	    }
    	    }
    	TimeUnit.SECONDS.sleep(25);
    	
    	HashXml.SaveUpdatedHash(HashXml.CombineHashXml(), HashXml.HASHNAME);
    	res.sendRedirect("/index.jsp?status=Successful");
    	}	
    	catch(Exception ex)
    	{
    		StringWriter sw = new StringWriter();
    		ex.printStackTrace(new PrintWriter(sw));
    		String exceptionAsString = sw.toString();
    		res.sendRedirect("/index.jsp?status=FAIL");
    		log.warning(exceptionAsString);
    	}
    }
}