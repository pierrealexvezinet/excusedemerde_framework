package com.itelios.itframework.utils;

import java.util.Date;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.itelios.itframework.ITApplication;
import com.itelios.itframework.exception.ITException;

/**
 * Utils class used to manage shared preferences
 * @author marcduvignaud
 *
 */
public class PreferencesUtils {
	
	private static final String LOG_TAG = "PreferencesUtils";
	
	/*
	 ***************************** Get Methods *****************************
	 */
	
	/**
	 * Get the string value stored in preferences for the given key
	 * @param key : The key of the value
	 * @param defaultValue : The value to return if the given key isn't found in preferences
	 * @return : The string value associated to the key, default value if not found
	 */
	public static String getStringValueFromPreferences(String key, String defaultValue)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ITApplication.getItApplicationContext());
		return prefs.getString(key, defaultValue);
	}
	/**
	 * Get the boolean value stored in preferences for the given key
	 * @param key : The key of the value
	 * @param defaultValue : The value to return if the given key isn't found in preferences
	 * @return : The boolean value associated to the key, default value if not found
	 */
	public static boolean getBooleanValueFromPreferences(String key, boolean defaultValue)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ITApplication.getItApplicationContext());
		return prefs.getBoolean(key, defaultValue);
	}
	/**
	 * Get the int value stored in preferences for the given key
	 * @param key : The key of the value
	 * @param defaultValue : The value to return if the given key isn't found in preferences
	 * @return : The int value associated to the key, default value if not found
	 */
	public static int getIntValueFromPreferences(String key, int defaultValue)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ITApplication.getItApplicationContext());
		return prefs.getInt(key, defaultValue);
	}
	/**
	 * Get the float value stored in preferences for the given key
	 * @param key : The key of the value
	 * @param defaultValue : The value to return if the given key isn't found in preferences
	 * @return : The float value associated to the key, default value if not found
	 */
	public static float getFloatValueFromPreferences(String key, float defaultValue)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ITApplication.getItApplicationContext());
		return prefs.getFloat(key, defaultValue);
	}
	/**
	 * Get the long value stored in preferences for the given key
	 * @param key : The key of the value
	 * @param defaultValue : The value to return if the given key isn't found in preferences
	 * @return : The long value associated to the key, default value if not found
	 */
	public static long getLongValueFromPreferences(String key, long defaultValue)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ITApplication.getItApplicationContext());
		return prefs.getLong(key, defaultValue);
	}
	/**
	 * Get the date value stored in preferences for the given key
	 * @param key : The key of the value
	 * @param defaultValue : The value to return if the given key isn't found in preferences
	 * @return : The date value associated to the key, default value if not found
	 */
	public static Date getDateValueFromPreferences(String key, Date defaultValue)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ITApplication.getItApplicationContext());
		long dateTime = prefs.getLong(key, 0);
		if(dateTime == 0)
			return defaultValue;
		else
			return new Date(dateTime);
	}
	/**
	 * Get the serializable value stored in preferences for the given key
	 * WARNING : Use only for single objects, not for list
	 * @param key : The key of the value
	 * @param objectClass : The class of the serialized object
	 * @param defaultValue : The value to return if the given key isn't found in preferences or if there's an error during deserialization
	 * @return The serializable value associated to the key, default value if not found or if there's an error
	 */
	public static <ObjectClass> ObjectClass getSerializableValueFromPreferences(String key, Class<ObjectClass> objectClass, ObjectClass defaultValue)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ITApplication.getItApplicationContext());
		String jsonObject = prefs.getString(key, null);
		if(StringUtils.isNullOrEmpty(jsonObject))
			return defaultValue;
		
		try
		{
			ObjectClass objectToReturn = JsonUtils.deserializeJson(jsonObject, objectClass);
			return objectToReturn;
		}
		catch (ITException e)
		{
			//If there is an error we log and we return the default value
			Log.e(LOG_TAG, String.format("Error while retrieving from preference - %s", e.getITExceptionMessage()));
			return defaultValue;
		}
	}
	
	/*
	 ***************************** End Get Methods *****************************
	 */
	
	
	/*
	 ***************************** Set Methods *****************************
	 */
	
	/**
	 * Set the string value stored in preferences for the given key
	 * @param key : The key of the value
	 * @param value : The string value to associate with the given key in preferences
	 */
	public static void setStringValueToPreferences(String key, String value)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ITApplication.getItApplicationContext());
		prefs.edit().putString(key, value).commit();
	}
	/**
	 * Set the boolean value stored in preferences for the given key
	 * @param key : The key of the value
	 * @param value : The boolean value to associate with the given key in preferences
	 */
	public static void setBooleanValueToPreferences(String key, boolean value)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ITApplication.getItApplicationContext());
		prefs.edit().putBoolean(key, value).commit();
	}
	/**
	 * Set the int value stored in preferences for the given key
	 * @param key : The key of the value
	 * @param value : The int value to associate with the given key in preferences
	 */
	public static void setIntValueToPreferences(String key, int value)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ITApplication.getItApplicationContext());
		prefs.edit().putInt(key, value).commit();
	}
	/**
	 * Set the float value stored in preferences for the given key
	 * @param key : The key of the value
	 * @param value : The float value to associate with the given key in preferences
	 */
	public static void setFloatValueToPreferences(String key, float value)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ITApplication.getItApplicationContext());
		prefs.edit().putFloat(key, value).commit();
	}
	/**
	 * Set the long value stored in preferences for the given key
	 * @param key : The key of the value
	 * @param value : The long value to associate with the given key in preferences
	 */
	public static void setLongValueToPreferences(String key, long value)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ITApplication.getItApplicationContext());
		prefs.edit().putLong(key, value).commit();
	}
	/**
	 * Set the date value stored in preferences for the given key
	 * @param key : The key of the value
	 * @param value : The date value to associate with the given key in preferences
	 */
	public static void setDateValueToPreferences(String key, Date value)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ITApplication.getItApplicationContext());
		long time = value == null ? 0 : value.getTime(); 
		prefs.edit().putLong(key, time).commit();
	}
	/**
	 * Set the serializable value stored in preferences for the given key
	 * @param key : The key of the value
	 * @param value : The serializable value to associate with the given key in preferences. Null if you want to clear the preference
	 */
	public static <ObjectClass> void setSerializableValueToPreference(String key, ObjectClass value)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ITApplication.getItApplicationContext());
		if(value == null)
		{
			prefs.edit().putString(key, null).commit();
			return;
		}
		try
		{
			String jsonObject = JsonUtils.serializeJson(value);
			prefs.edit().putString(key, jsonObject).commit();
		}
		catch (ITException e)
		{
			//If there is an error we log and don't store the object in preference
			Log.e(LOG_TAG, String.format("Error while storing preference - %s", e.getITExceptionMessage()));
		}
	}
	
	/*
	 ***************************** End Set Methods *****************************
	 */
}
