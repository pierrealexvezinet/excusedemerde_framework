package com.itelios.itframework.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.itelios.itframework.ITApplication;

/**
 * Class containing methods concerning connectivity
 * @author marcduvignaud
 *
 */
public class ConnectivityUtils {
	
	/**
	 * Verify if internet connection is activated
	 * @return boolean
	 */
	public static boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = 
	    		(ConnectivityManager) ITApplication.getItApplicationContext()
	    		.getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
}
