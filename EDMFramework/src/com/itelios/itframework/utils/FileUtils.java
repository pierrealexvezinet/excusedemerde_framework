package com.itelios.itframework.utils;

import java.io.File;
import java.io.IOException;

import android.content.Context;

import com.itelios.itframework.ITApplication;

/**
 * Class containing tools to manipulate files & directories
 * @author marcduvignaud
 *
 */
public class FileUtils {
	
	private final static String LOG_TAG = "FileUtils";
	
	/**
	 * Function that will create the cache directory if necessary 
	 * (and will create the .nomedia file in this case)
	 * @param context
	 * @return : True if the cache directory is created, false if there's an error
	 */
	public static boolean createCacheDirectoryIfNecessary(Context context)
	{
		if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			Log.e(LOG_TAG, "Unable to retrieve the external storage directory");
			return false;
		}
		
    File cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),ITApplication.CACHE_ROOT_DIRECTORY);
    if(!cacheDir.exists())
    {
    	cacheDir.mkdirs();
    	//We create the No Media file
    	File noMediaFile = new File(cacheDir.getPath() + "/.nomedia");
    	try
			{
				noMediaFile.createNewFile();
			}
			catch (IOException e)
			{
				Log.e(LOG_TAG, "Unable to create the no media file in the cache directory");
				return false;
			}    	
    }
    
    return true;
	}		
	
	/**
	 * Function that will create a sub directory (if necessary) in the root cache directory.
	 * @param context
	 * @return : True if the cache sub directory is created, false if there's an error
	 */
	public static boolean createCacheSubDirectoryIfNecessary(Context context, String folderName)
	{
		if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			Log.e(LOG_TAG, "Unable to retrieve the external storage directory");
			return false;
		}
		
    File cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),folderName);
    if(!cacheDir.exists())
    	cacheDir.mkdirs();
    
    return true;
	}	
	
	/**
	 * Function that will delete a file if necessary
	 * @param fileName
	 * @return True if the file doesn't exists anymore, false if there's an error
	 */
	public static boolean deleteFileIfNecessary(String fileName)
	{
		File fileToDelete = new File(fileName);
		if(fileToDelete.exists())
		{
			return fileToDelete.delete();
		}
		return true;
	}
}
