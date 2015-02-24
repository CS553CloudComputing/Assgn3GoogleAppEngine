/**
 * 
 */
package com.ksingh14.gae.gcs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

/**
 * @author KaranJeet
 *
 */
public class GCS {
	
	private static String BUCKETNAME = "ass3gcs";
	private static GcsService gcsService = GcsServiceFactory.createGcsService();
	private static final Logger log = Logger.getLogger(GCS.class.getName());
	private static final int BUFFER_SIZE = 31 * 1024 * 1024;
	private static BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	
	public static boolean Insert(BlobInfo blob,byte[] b) throws IOException
	{
		log.warning("writing byte array of size : "+b.length);
		GcsFilename filename = new GcsFilename(BUCKETNAME, "test/"+blob.getFilename());
		GcsFileOptions options = new GcsFileOptions.Builder()
        .mimeType(blob.getContentType())
        .acl("authenticated-read")
        .addUserMetadata("myfield1", "my field value")
        .build();
		gcsService.createOrReplace(filename, options,ByteBuffer.wrap(b));
        Memcache.Insert(blob.getFilename(),blob.getBlobKey().getKeyString());
		return false;
	}
	
	public static boolean Insert(BlobInfo blob,byte[] b,int i) throws IOException
	{
		log.warning("writing byte array of size : "+b.length);
		GcsFilename filename = new GcsFilename(BUCKETNAME, "test/"+blob.getFilename()+"part"+i);
		GcsFileOptions options = new GcsFileOptions.Builder()
        .mimeType(blob.getContentType())
        .acl("authenticated-read")
        .addUserMetadata("myfield1", "my field value")
        .build();
		gcsService.createOrReplace(filename, options,ByteBuffer.wrap(b));
        Memcache.Insert(blob.getFilename(),blob.getBlobKey().getKeyString());
		return false;
	}
	
	public static boolean Insert(BlobInfo blob,String[] _s) throws IOException
	{
		String s;
		int sCount = _s.length;
		for(int i=0;i<sCount;i++)
		{
			s=_s[i];
		GcsFilename filename = new GcsFilename(BUCKETNAME, "test/"+blob.getFilename()+".part"+i);
		GcsOutputChannel outputChannel =
    	        gcsService.createOrReplace(filename, GcsFileOptions.getDefaultInstance());
    	copy(IOUtils.toInputStream(s,"UTF-8"), Channels.newOutputStream(outputChannel));
        Memcache.Insert(blob.getFilename(),blob.getBlobKey().getKeyString());
		}
		return false;
	}


	public static boolean Check(String key) throws IOException {
		HashMap<String,Object> hash = new HashMap<String, Object>();
		try{
		hash.putAll(HashXml.GetHashMap(HashXml.HASHNAME));
		}
		catch(FileNotFoundException ex)
		{
			return false;
		}
		String value;
		try{
		GcsFilename fileName = new GcsFilename(BUCKETNAME, "test/"+key);
	    GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
	    value = IOUtils.toString(Channels.newInputStream(readChannel),"UTF-8");
		}
		catch(Exception ex)
		{
			log.warning(ex.getMessage());
			return false;
		}
		return ((value!=null)&&hash.containsKey(key))?true:false;
	}

	public static String Find(String key) throws IOException {
		String value;
		HashMap<String,Object> hash = HashXml.GetHashMap(HashXml.HASHNAME);
		Object hashValue = null;
		if(hash.containsKey(key))
			hashValue = hash.get(key);
		if(hashValue!=null&&IsBlobKey((String)hashValue))
		try
		{
			GcsFilename fileName = new GcsFilename(BUCKETNAME, "test/"+key);
		    GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
		    value = IOUtils.toString(Channels.newInputStream(readChannel),"UTF-8");
		    return value;
		}
			catch(Exception ex)
			{
				log.warning(ex.getMessage());
				return null;
			}
		return (String)hashValue;
	}


	public static boolean IsBlobKey(String val) {
		try
		{
			BlobKey blobKey = new BlobKey(val);
			return true;
		}
		catch (Exception ex)
		{
			log.warning("isblob - "+ex.getMessage());
			return false;
		}
	}


	@SuppressWarnings("null")
	public static String[] Split(byte[] b) throws UnsupportedEncodingException {
		int chunk = 25*1024*1024;
		log.warning("splitting stream");
		String s = new String(b,"UTF-8");
		double l = b.length/(chunk);
		String[] _s = null;
		int start=0;
		for(int i =0;i<l+1;i++)
		{
			_s[i]=s.substring(start,start+chunk);
			start+=chunk;
		}
		return _s;
	}
	
	public static byte[][] Splitbyte(byte[] b) {
		log.warning("splitting stream");
		int chunk = 25*1024*1024;
		log.warning("chunk = "+chunk);
		int l = b.length/(chunk);
		l++;
		log.warning("no. of parts = "+l);
		byte[][] bt = new byte[l][chunk];
		int start =0;
		for(int i=0;i<l;i++)
		{
			log.warning("i = "+i);
			bt[i] = Arrays.copyOfRange(b, start, start+chunk);
			start+=chunk;
			log.warning("byte array no. "+i+" array size - "+bt[i].length);
		}
		return bt;
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

	public static String[] Split(String string) {
		int chunk = 25*1024*1024;
		log.warning("splitting stream");
		//String s = new String(b,"UTF-8");
		int l = string.length()/(chunk);
		String[] _s = null;
		int start=0;
		for(int i =0;i<l+1;i++)
		{
			_s[i]=string.substring(start,start+chunk);
			start+=chunk;
		}
		return _s;
	}

	public static String[] Split(BlobstoreInputStream in) throws IOException {
		int chunk = 25*1024*1024;
		log.warning("splitting stream");
		//String s = new String(b,"UTF-8");
		//int l = in.available()/(chunk);
		String[] _s = new String[5];
		int start=0;
		for(int i =0;i<5;i++)
		{
			_s[i]=IOUtils.toString(in,"UTF-8").substring(start,start+chunk);
			start+=chunk;
		}
		return _s;
	}

	public static boolean Remove(String key) throws IOException {
		//Memcache.Remove(key);
		String value;
		HashMap<String,Object> hash = HashXml.GetHashMap(HashXml.HASHNAME);
		Object hashValue = null;
		if(hash.containsKey(key))
			hashValue = hash.get(key);
		if(hashValue!=null)
		try
		{
			GcsFilename fileName = new GcsFilename(BUCKETNAME, "test/"+key);
		    //GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
		    //value = IOUtils.toString(Channels.newInputStream(readChannel),"UTF-8");
		    gcsService.delete(fileName);
		    if(Memcache.checkCache(key))
		    	Memcache.Remove(key);
		    return true;
		}
			catch(Exception ex)
			{
				log.warning(ex.getMessage());
				return false;
			}
		return false;
	}

	public static String FetchFile(String key) throws IOException {
		HashMap<String,Object> hash = HashXml.GetHashMap(HashXml.HASHNAME);
		Object hashValue = null;
		if(hash.containsKey(key))
			hashValue = hash.get(key);
		if(hashValue!=null&&IsBlobKey((String)hashValue))
		try
		{
			GcsFilename fileName = new GcsFilename(BUCKETNAME, key);
		    GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
		    String value = IOUtils.toString(Channels.newInputStream(readChannel),"UTF-8");
		    //gcsService.delete(fileName);
		    //Memcache.Remove(key);
		    return value;
		}
			catch(Exception ex)
			{
				log.warning(ex.getMessage());
				return null;
			}
		return null;
	}
	
	
}
