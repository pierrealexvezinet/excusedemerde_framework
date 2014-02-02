package com.itelios.itframework.utils;

import java.util.List;


public class StringUtils {

	/**
	 * Simple function testing if a string is null or empty
	 * @param theString
	 * @return True if null or empty, false otherwise
	 */
	public static boolean isNullOrEmpty(String theString)
	{
		return (theString == null || theString.length() == 0);
	}
	
	/**
	 * Concat every entry of the list delimited by the delimiter
	 * @param listToConcat
	 * @param delimiter
	 * @return
	 */
	public static String concatToString(List<String> listToConcat, String delimiter)
	{
		StringBuilder sb = new StringBuilder();
		for(String strToConcat : listToConcat)
		{
			sb.append(strToConcat).append(delimiter);
		}
		String concatStr = sb.toString();
		if(concatStr.length() > delimiter.length())
			concatStr = concatStr.substring(0, (concatStr.length() - delimiter.length()));
			
		return concatStr;
	}
	
	/**
   * Do a lpad of a string
   * @param originalString : The number to format
   * @param padChar : The padding character
   * @param totalLength : The total number of characters 
   * @return The formatted string
   */
  public static String lPad(String originalString, String padChar, int totalLength)
  {
  	if(originalString.length() == totalLength)
  		return originalString;
  	return String.format("%" + (totalLength - originalString.length()) + "s", "").replace(" ", String.valueOf(padChar)) + originalString;
  }
  
  /**
   * Do a rpad of a string
   * @param originalString : The number to format
   * @param padChar : The padding character
   * @param totalLength : The total number of characters 
   * @return The formatted string
   */
  public static String rPad(String originalString, String padChar, int totalLength)
  {
  	if(originalString.length() == totalLength)
  		return originalString;
  	
  	return originalString + 
        String.format("%" + (totalLength - originalString.length()) + "s", " ").replace(" ", String.valueOf(padChar));
  }
}
