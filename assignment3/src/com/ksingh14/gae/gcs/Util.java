/**
 * 
 */
package com.ksingh14.gae.gcs;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobstoreInputStream;

/**
 * @author KaranJeet
 *
 */
public class Util {
	private static final Logger log = Logger.getLogger(Memcache.class.getName());
    
	public static HashMap<String,Object> Insert(BlobInfo blob) throws IOException
	{
		HashMap<String, Object> hash = new HashMap<String, Object>();
		if(!Check(blob.getFilename()))
        if(blob.getSize()>100*1024)
    	{
        	log.warning("file larger than 100 KB");
        	if(blob.getSize()<25*1024*1024)
        	{
        		log.warning("file smaller than 25MB");
        		byte[] b = new byte[(int)blob.getSize()];
        		BlobstoreInputStream in = new BlobstoreInputStream(blob.getBlobKey());
        		in.read(b);
        		hash.put(blob.getFilename(), blob.getBlobKey().toString());
        		GCS.Insert(blob,b);
        		in.close();
        	}
        	else 
        	{
        		BlobstoreInputStream in = new BlobstoreInputStream(blob.getBlobKey());
        		log.warning("file larger than 25MB");
        	
        		for(int i=0;i<5;i++)
        		{
        			byte[] b = new byte[25*1024*1024];
        			in.read(b);
        			hash.put(blob.getFilename()+"part"+i, blob.getBlobKey().toString());
        			GCS.Insert(blob, b, i);
        		}
        		in.close();
        	}
    	}
    	else
    	{
    		log.warning("file smaller than 100 KB");
    		byte[] b = new byte[(int)blob.getSize()];
            BlobstoreInputStream in = new BlobstoreInputStream(blob.getBlobKey());
            in.read(b);
            hash.put(blob.getFilename(), new String(b,"UTF-8"));
            GCS.Insert(blob,b);
            Memcache.Insert(blob.getFilename(), new String(b,"UTF-8"));
            in.close();
    	}
		
		log.warning("cannot write file");
		return hash;
	}
	
	public static boolean Check(String key) throws IOException
	{
		return (Memcache.checkCache(key))?true:(GCS.Check(key))?true:false;
	}

	public static String Find(String key) throws IOException {
		return (Memcache.checkCache(key))?Memcache.Find(key):(GCS.Check(key))?GCS.Find(key):null;
	}

	public static boolean Remove(String key) throws IOException {
		return (Memcache.checkCache(key))?Memcache.Remove(key):(GCS.Check(key))?GCS.Remove(key):null;
	}
	
	public static String[] Listing() throws IOException
	{
		if(HashXml.GetHashXmlString(HashXml.HASHNAME)!=null)
		{	
			HashMap<String, Object> hash =  HashXml.GetHashMap(HashXml.HASHNAME);
			int size = hash.size();
			String[] s =  new String[size];
			int i=0;
			for(String key:hash.keySet())
				s[i++]=key;
			return s;
	}
		else return null;
	}
	
	public static int RemoveAllCache() throws IOException
	{
		String[] _s = Listing();
		try{
			int i=0;
		for(String s:_s)
		{
			i++;
			if(Memcache.checkCache(s))
				Memcache.Remove(s);
			if(i>10) return 2;
		}
		return 1;
		}
		catch (Exception ex)
		{
			return 0;
		}
	}
	
	public static int RemoveAll() throws IOException
	{
		String[] _s = Listing();
		try{
			int i=0;
		for(String s:_s)
		{
			i++;
			Remove(s);
			if(i>10)return 2;
		}
		return 1;
		}
		catch (Exception ex)
		{
			return 0;
		}
	}
	
	public static String FetchFile(String key) throws IOException
	{
		return (Memcache.checkCache(key))?Memcache.Find(key):(GCS.Check(key))?GCS.Find(key):null;
	}
	
	public static boolean FindInFile(String key,String string) throws IOException
	{
		String value = Find(key);
		if(value.matches(string))
			return true;
		return false;
	}
	
	public static String[] Listing(String string) throws IOException
	{
		String[] _s =  Listing();
		String[] s = new String[_s.length];
		int i=0;
		for(String key:_s)
		{
			if(key.matches(string))
				s[i++] = key;
		}
		return (s.length>0)?s:null;
	}
	
	public static double CacheSizeElem() throws IOException
	{
		String[] keys = Listing();
		double i=0;
		for(String key:keys)
		if(Memcache.checkCache(key))
			i++;
		return i;
	}
	
	public static double StorageSizeElem() throws IOException
	{
		double totalSize = Listing().length;
		return totalSize;
	}
	
	public static double CacheSizeMB() throws IOException
	{
		double size=0;
		String[] keys=Listing();
		for(String key:keys)
			if(Memcache.checkCache(key))
				size+=Memcache.Find(key).length();
		return size/(1024*1024);
	}
	
	public static double StorageSizeMB() throws IOException
	{
		double size=0;
		String[] keys=Listing();
		for(String key:keys)
			if(GCS.Check(key))
				size+=GCS.Find(key).length();
		return size/(1024*1024);
	}
}
