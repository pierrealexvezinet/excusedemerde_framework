package com.itelios.itframework;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.itelios.itframework.utils.Log.LogLevel;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Itelios framework application class
 * Must be extended by the applications using the framework
 * @author marcduvignaud
 *
 */
public abstract class ITApplication extends Application {	
	
	private static Context itApplicationContext = null;
	/**
	 * Get the application context from anywhere in the application
	 * @return The application context
	 */
	public static Context getItApplicationContext()
	{
		return itApplicationContext;
	}
		
	/**
	 * The directory where files will be stored in cache (relative directory, don't set the full path) 
	 * Default is "/ITCache/" (don't forget the "/")
	 */
	public static String CACHE_ROOT_DIRECTORY = "/ITCache/";
	/**
	 * Returns the image cache directory
	 */
	public static final String getImageCacheDirectory() 
	{
		return CACHE_ROOT_DIRECTORY + "image/";
	}
	
	/**
	 * Singleton object to store the image loader. Can be retrieved with <code>getImageLoader()</code>
	 */
	private static ImageLoader imgLoader;
	/**
	 * Return the ImageLoader singleton used to download and cache images.
	 * itApplicationContext must be initialized before calling this method.
	 * @return
	 */
	public static ImageLoader getImageLoader()
	{
		if(imgLoader == null)
		{
			// Create global configuration and initialize ImageLoader with this configuration
			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.EXACTLY)
		        .build();
	        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getItApplicationContext())
	        	.threadPoolSize(5)
	        	.denyCacheImageMultipleSizesInMemory()
	        	.memoryCache(new WeakMemoryCache())
	        	.defaultDisplayImageOptions(defaultOptions)
	            .build();
	        ImageLoader.getInstance().init(config);
	        imgLoader = ImageLoader.getInstance();
		}
		
		return imgLoader;
	}
	
	/**
   * The max size that the DOWNLOADED_IMAGE_CACHE_DIR will reach before being flushed for new cache
   */
  public static final long CACHE_IMAGE_MAX_MB_SIZE = 10 * 1024 * 1024; //Correspond to 10 MB
	
	/**
	 * The log level used by the application
	 */
	public static final LogLevel LOG_LEVEL = LogLevel.Info;
	
	/**
	 * The timeout of the WS call (60 seconds by default)
	 */
	public static final int HTTP_TIMEOUT = 60 * 1000;	

	@Override
	public void onCreate()
	{
		super.onCreate();
		itApplicationContext = getApplicationContext();
	}
}
