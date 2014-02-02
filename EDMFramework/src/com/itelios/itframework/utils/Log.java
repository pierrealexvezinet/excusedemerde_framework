package com.itelios.itframework.utils;

import com.itelios.itframework.ITApplication;

/**
 * Log class that simply controls the log level
 * @author marcduvignaud
 *
 */
public class Log {
	
	public enum LogLevel 
	{
		Info(1),
		Debug(2), 
		Warning(3),
		Error(4);
		
		private int logValue;
		public int getLogValue()
		{
			return logValue;
		}

		private LogLevel(int val)
		{
			logValue = val;
		}
	}	
	
	/**
	 * Log a debug message
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 *
	 */
	public static void d(String tag, String msg){
		if(ITApplication.LOG_LEVEL.getLogValue() <= LogLevel.Debug.getLogValue()){
			android.util.Log.d(tag, msg);
		}
	}

	/**
	 * Log an info message
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 *
	 */
	public static void i(String tag, String msg){
		if(ITApplication.LOG_LEVEL.getLogValue() <= LogLevel.Info.getLogValue()){
			android.util.Log.i(tag, msg);
		}
	}
	
	/**
	 * Log an error message
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 *
	 */
	public static void e(String tag, String msg){
		if(ITApplication.LOG_LEVEL.getLogValue() <= LogLevel.Error.getLogValue()){
			android.util.Log.e(tag, msg);
		}
	}
	
	/**
	 * Log a warm message
	 * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
	 * @param msg The message you would like logged.
	 *
	 */
	public static void w(String tag, String msg){
		if(ITApplication.LOG_LEVEL.getLogValue() <= LogLevel.Warning.getLogValue()){
			android.util.Log.w(tag, msg);
		}
	}
}
