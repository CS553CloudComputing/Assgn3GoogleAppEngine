package com.ksingh14.gae.gcs;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class Upload1Thread extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    private static final Logger log = Logger.getLogger(Upload1Thread.class.getName());
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    	try
    	{
    	Map<String, List<BlobInfo>> blobsData = blobstoreService.getBlobInfos(req);
    	HashMap<String, Object> hash = new HashMap<String, Object>();
    	log.warning("starting blob insertion");
        for (String key : blobsData.keySet())
    	{
        	List<BlobInfo> blobList = blobsData.get(key);
        	log.warning("bloblist size - "+blobList.size());
    	        for(BlobInfo blob:blobList)
    	        {
    	        	log.warning("writing "+blob.getFilename());
    	        	hash.putAll(Util.Insert(blob));
    	        }
    	    }
    	log.warning("completed blob insertion");
    	HashXml.SaveHash(hash,HashXml.HASHNAME);
    	Date d = new Date();
    	HttpSession session = req.getSession();
    	 if (session!=null) {
             log.warning("session received");
             session.setAttribute("endTime", d);
             session.setAttribute("threadCnt", "1");
          }
          else{
             log.warning("session not received");
          }
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