/**
 * 
 */
package com.ksingh14.gae.gcs;

import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.AsyncMemcacheService;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * @author KaranJeet
 *
 */
public class Memcache {
	private static final Logger log = Logger.getLogger(Memcache.class.getName());
    
	public static boolean Insert(String key,String value)
	{
		try{
		AsyncMemcacheService asyncCache = MemcacheServiceFactory.getAsyncMemcacheService();
        asyncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
        asyncCache.put(key, value);
        return true;}
		catch(Exception ex)
		{
			log.warning("Async insert failed"+ex.getMessage());
			return false;
		}
	}

	public static boolean checkCache(String key) {
		try{
			AsyncMemcacheService asyncCache = MemcacheServiceFactory.getAsyncMemcacheService();
	        asyncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	        Future<Boolean> fb = asyncCache.contains(key);
	        Boolean b = (Boolean)fb.get();
	        if(b)
	        	return true;
	        else
	        	return false;
	        }
			catch(Exception ex)
			{
				log.warning("Async insert failed"+ex.getMessage());
				return false;
			}
	}

	public static String Find(String key) {
		try{
			byte[] value;
			AsyncMemcacheService asyncCache = MemcacheServiceFactory.getAsyncMemcacheService();
	        asyncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	        Future<Object> futureValue = asyncCache.get(key); // read from cache
	        // ... do other work in parallel to cache retrieval
	        value = (byte[]) futureValue.get();
	        if (value != null) {
	        	String val = new String(value,"UTF-8");
	        	if(!GCS.IsBlobKey(val))
	        		return val;
	        	else return GCS.Find(key);
	        }
	        else
	        	return "";
	        }
			catch(Exception ex)
			{
				log.warning("Async insert failed"+ex.getMessage());
				return "";
			}
	}

	public static boolean Remove(String key) {
		try
		{
		AsyncMemcacheService asyncCache = MemcacheServiceFactory.getAsyncMemcacheService();
        asyncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
        asyncCache.delete(key);
        if(GCS.Check(key))
        	GCS.Remove(key);
        HashMap<String, Object> hash = HashXml.GetHashMap(HashXml.HASHNAME);
        hash.remove(key);
        HashXml.SaveUpdatedHash(hash, HashXml.HASHNAME);
		return true;
		}
		catch(Exception ex)
		{
			log.warning(ex.getMessage());
			return false;
		}
	}

	public static String FetchFile(String key) {
		try
		{
			byte[] value;
		AsyncMemcacheService asyncCache = MemcacheServiceFactory.getAsyncMemcacheService();
        asyncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
        Future<Object> futureValue = asyncCache.get(key); // read from cache
        value = (byte[]) futureValue.get();
		return new String(value,"UTF-8");
		}
		catch(Exception ex)
		{
			log.warning(ex.getMessage());
			return null;
		}
	}
}
