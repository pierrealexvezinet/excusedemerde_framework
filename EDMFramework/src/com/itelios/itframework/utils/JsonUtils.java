package com.itelios.itframework.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itelios.itframework.exception.ITException;
import com.itelios.itframework.exception.ITException.ITExceptionsType;

/**
 * Utils class used to manipulation JSON (serialize / deserialize)
 * @author marcduvignaud
 *
 */
public class JsonUtils {

	//Global Json mapper object
	private static ObjectMapper jsonMapper;
	private static ObjectMapper getJsonMapper()
	{
		if(jsonMapper == null)
		{
			jsonMapper = new ObjectMapper();
			// We indicate to the parser not to fail in case of unknown properties, for backward compatibility reasons
	    // See http://stackoverflow.com/questions/6300311/java-jackson-org-codehaus-jackson-map-exc-unrecognizedpropertyexception
			jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		
		return jsonMapper;
	}
	
	public static <ObjectClass> ObjectClass deserializeJson(InputStream inputStream, Class<? extends ObjectClass> valueType)
		throws ITException
	{
    try
    {
      final ObjectClass businessObject = getJsonMapper().readValue(inputStream, valueType);
      return businessObject;
    }
    catch (Exception exception)
    {
      throw new ITException(exception.getMessage(), ITExceptionsType.PARSE_ERROR);
    }
	}
	
	public static <ObjectClass> ObjectClass deserializeJson(String inputJson, Class<? extends ObjectClass> valueType)
		throws ITException
	{
    try
    {
      final ObjectClass businessObject = getJsonMapper().readValue(inputJson, valueType);
      return businessObject;
    }
    catch (Exception exception)
    {
      throw new ITException(exception.getMessage(), ITExceptionsType.PARSE_ERROR);
    }
	}
	
	public static <ObjectClass> List<ObjectClass> deserializeJsonList(String inputJson, Class<? extends ObjectClass> valueType)
			throws ITException
	{
    try
    {
    	final List<ObjectClass> businessObject = getJsonMapper().readValue(inputJson, getJsonMapper().getTypeFactory().constructCollectionType(List.class,  valueType));
      return businessObject;
    }
    catch (Exception exception)
    {
      throw new ITException(exception.getMessage(), ITExceptionsType.PARSE_ERROR);
    }
	}
	
	public static <KeyClass, ObjectClass> HashMap<KeyClass, ObjectClass> deserializeJsonHashMap(String inputJson, Class<? extends KeyClass> keyType, Class<? extends ObjectClass> valueType)
			throws ITException
	{
    try
    {
    	final HashMap<KeyClass, ObjectClass> businessObject = getJsonMapper().readValue(inputJson, getJsonMapper().getTypeFactory().constructMapType(HashMap.class, keyType, valueType));
      return businessObject;
    }
    catch (Exception exception)
    {
      throw new ITException(exception.getMessage(), ITExceptionsType.PARSE_ERROR);
    }
	}
	
	public static String serializeJson(Object theObject) throws ITException
	{
		try
		{
			return getJsonMapper().writeValueAsString(theObject);
		}
		catch(Exception ex)
		{
			ITException parseRequestEx = new ITException("Error while serializing the object of type \""+theObject.getClass().getName()+"\" to JSON", ITExceptionsType.PARSE_ERROR);
			throw parseRequestEx;
		}
	}
}
