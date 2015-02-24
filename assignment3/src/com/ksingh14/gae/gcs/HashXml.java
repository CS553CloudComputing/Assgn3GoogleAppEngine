/**
 * 
 */
package com.ksingh14.gae.gcs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.HashMap;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author KaranJeet
 *
 */
public class HashXml {
	
	private final static GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
    .initialRetryDelayMillis(10)
    .retryMaxAttempts(10)
    .totalRetryPeriodMillis(15000)
    .build());
	private static String BUCKETNAME = "ass3gcs";
	public static final boolean SERVE_USING_BLOBSTORE_API = false;
	private static final int BUFFER_SIZE = 31 * 1024 * 1024;
	public static final String HASHNAME = "hash.xml";
	private static final Logger log = Logger.getLogger(HashXml.class.getName());
    
	public static String GetHashXmlString(String hashName) throws IOException
	{
		try
		{
		GcsFilename fileName = new GcsFilename(BUCKETNAME, hashName);
	    GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
	    //copy(Channels.newInputStream(readChannel), os);
	    //String xml = os.toString();
	    String xml = IOUtils.toString(Channels.newInputStream(readChannel),"UTF-8");
	    return xml;
		}
		catch(FileNotFoundException ex)
		{
			return null;
		}
	}
	
	public static HashMap<String, Object> GetHashMap(String hashName) throws IOException
	{
		String xml = GetHashXmlString(hashName);
		if(xml!=null)
		{
		XStream xStream = new XStream();
		xStream.alias("hash", java.util.HashMap.class);
		@SuppressWarnings("unchecked")
		HashMap<String, Object> hash = (HashMap<String,Object>) xStream.fromXML(xml);
		return hash;
		}
		else return new HashMap<String, Object>();
	}
	
	public static HashMap<String,Object> CombineHashXml() throws IOException
	{
		HashMap<String,Object> hash0 = GetHashMap("hash0.xml");
		HashMap<String,Object> hash1 = GetHashMap("hash1.xml");
		HashMap<String,Object> hash2 = GetHashMap("hash2.xml");
		HashMap<String,Object> hash3 = GetHashMap("hash3.xml");
		HashMap<String,Object> hash = new HashMap<String, Object>();
		hash.putAll(hash0);
		hash.putAll(hash1);
		hash.putAll(hash2);
		hash.putAll(hash3);
		return hash;
	}
	
	public static void SaveHash(HashMap<String,Object> _hash, String hashName) throws IOException
	{
		try{
			HashMap<String,Object> hash=new HashMap<String, Object>();
			if(GetHashXmlString(HASHNAME)!=null)
			{
				hash.putAll(GetHashMap(HASHNAME));
			}
			hash.putAll(_hash);
		XStream xStream = new XStream(new DomDriver());
    	xStream.alias("hash", java.util.HashMap.class);
    	String xml = xStream.toXML(hash);
    	GcsOutputChannel outputChannel =
    	        gcsService.createOrReplace(new GcsFilename(BUCKETNAME, hashName), GcsFileOptions.getDefaultInstance());
    	copy(IOUtils.toInputStream(xml,"UTF-8"), Channels.newOutputStream(outputChannel));
		}
		catch(Exception ex)
		{
			log.warning("SaveHash - "+ex.getMessage());
		}
	}
	
	public static void SaveUpdatedHash(HashMap<String,Object> _hash, String hashName) throws IOException
	{
		try{
			
		XStream xStream = new XStream(new DomDriver());
    	xStream.alias("hash", java.util.HashMap.class);
    	String xml = xStream.toXML(_hash);
    	GcsOutputChannel outputChannel =
    	        gcsService.createOrReplace(new GcsFilename(BUCKETNAME, hashName), GcsFileOptions.getDefaultInstance());
    	copy(IOUtils.toInputStream(xml,"UTF-8"), Channels.newOutputStream(outputChannel));
		}
		catch(Exception ex)
		{
			log.warning("SaveUpdatedHash - "+ex.getMessage());
		}
	}

	private static void copy(InputStream input, OutputStream output) throws IOException {
    try {
      byte[] buffer = new byte[BUFFER_SIZE];
      int bytesRead = input.read(buffer);
      while (bytesRead != -1) {
        output.write(buffer, 0, bytesRead);
        bytesRead = input.read(buffer);
      }
    } finally {
      input.close();
      output.close();
    }
	}
}
