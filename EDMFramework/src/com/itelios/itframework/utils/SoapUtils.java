package com.itelios.itframework.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import com.itelios.itframework.exception.ITException;
import com.itelios.itframework.exception.ITException.ITExceptionsType;

/**
 * Utils class used to serialize / deserialize Soap Objects (works with ksoap2 library)
 * @author Marc Duvignaud
 */
public class SoapUtils {	
	private static final String LOG_TAG = "SoapUtils"; 
	public static final String DATE_FORMAT_WS = "yyyy-MM-dd'T'HH:mm:ss";

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface SoapPropertyElement
	{
		String name();
	}
	
	/**
	 * Serialize a single business object into a SOAPObject representation
	 * @param namespace : The namespace of the soap object
	 * @param businessObject : The business object to serialize
	 * @return The serialized object
	 * @throws ITException : Framework exception of type PARSE_ERROR
	 */
	public static <ObjectClass> SoapObject serializeBusinessObject(String namespace, String objectName, ObjectClass businessObject) throws ITException
	{
		try
		{
			SoapObject soapObject = new SoapObject(namespace, objectName);			

			Field[] fields = businessObject.getClass().getDeclaredFields();

			for (int i = 0; i < fields.length; i++) 
			{
				Field currentField = fields[i];
				Type type = currentField.getType();
				currentField.setAccessible(true);
				String fieldName = currentField.getName();
				SoapPropertyElement property = currentField.getAnnotation(SoapPropertyElement.class);
				if(property != null && !StringUtils.isNullOrEmpty(property.name()))
					fieldName = property.name();
				
				//detect String
				if (type.equals(String.class)
						|| type.equals(Integer.TYPE) || type.equals(Integer.class)
						|| type.equals(Float.TYPE) || type.equals(Float.class)
						|| type.equals(Double.TYPE) || type.equals(Double.class)
						|| type.equals(Long.TYPE) || type.equals(Long.class)
						|| type.equals(Short.TYPE) || type.equals(Short.class)
						|| type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
					//For all primitive types, we simple add the value to the soap object
					soapObject.addProperty(fieldName, currentField.get(businessObject));
				}
				else if(type.equals(Date.class))
				{
					//Special serialization for Date
					SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_WS);
					soapObject.addProperty(fieldName, dateFormat.format(currentField.get(businessObject)));
				}
				else
				{            	
					Type genericType = currentField.getGenericType();
					if(genericType instanceof ParameterizedType)
					{
						ParameterizedType parameterizedType = (ParameterizedType) genericType;
						if(parameterizedType.getRawType().equals(List.class))
						{
							//We have a list of object as a field => we add it as a soap object 
							SoapObject serializedList = serializeBusinessObjectList(namespace, 
									fieldName, (List<?>) currentField.get(businessObject));

							if(serializedList != null)
								soapObject.addSoapObject(serializedList);
						}
						else if(parameterizedType.getRawType().equals(HashMap.class))
						{
							//TODO : Handle hash map if necessary
						}
					}
					else
					{
						//We have an object as a field => we serialize it and add it to the soap property
						SoapObject serializedObject = serializeBusinessObject(namespace, fieldName, currentField.get(businessObject));
						if(serializedObject != null)
							soapObject.addSoapObject(serializedObject);
					}
				}
			}

			return soapObject;
		}
		catch (Exception e)
		{
			String errorMessage = String.format("Error while serializing the object of type %s. Error : %s", businessObject.getClass().getSimpleName(), e.getMessage());
			Log.e(LOG_TAG, errorMessage);
			throw new ITException(errorMessage, ITExceptionsType.PARSE_ERROR);
		} 
	}

	/**
	 * Serialize a single business object into a SOAPObject representation
	 * @param namespace : The namespace of the soap object
	 * @param businessObjectList : The list of business objects to serialize
	 * @return The serialized list
	 * @throws ITException : Framework exception of type PARSE_ERROR
	 */
	public static <ObjectClass> SoapObject serializeBusinessObjectList(String namespace, String listName, List<ObjectClass> businessObjectList) throws ITException
	{
		SoapObject soapObject = new SoapObject(namespace, listName);


		for(ObjectClass businessObject : businessObjectList)
		{
			Class<?> businessClass = businessObject.getClass();
			if(businessClass.equals(String.class)
					|| businessClass.equals(Integer.TYPE) || businessClass.equals(Integer.class)
					|| businessClass.equals(Double.TYPE) || businessClass.equals(Double.class)
					|| businessClass.equals(Float.TYPE) || businessClass.equals(Float.class)
					|| businessClass.equals(Long.TYPE) || businessClass.equals(Long.class)
					|| businessClass.equals(Short.TYPE) || businessClass.equals(Short.class)
					|| businessClass.equals(Boolean.TYPE) || businessClass.equals(Boolean.class))
			{
				//We have a primitive type => we simply add to property
				String propertyName = businessClass.getSimpleName().toLowerCase();
				//For integer, the property name must be int, not integer
				if(businessClass.equals(Integer.TYPE) || businessClass.equals(Integer.class))
					propertyName = "int";
				
				soapObject.addProperty(propertyName, businessObject);
			}
			else if(businessClass.equals(Date.class))
			{
				//Special serialization for Date
				SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_WS);
				soapObject.addProperty("datetime", dateFormat.format(businessObject));
			}
			else
			{
				String className = businessClass.getSimpleName();
				SoapPropertyElement property = businessClass.getAnnotation(SoapPropertyElement.class);
				if(property != null && !StringUtils.isNullOrEmpty(property.name()))
					className = property.name();
				
				SoapObject serializedObject = serializeBusinessObject(namespace, className, businessObject);
				if(serializedObject != null)
					soapObject.addSoapObject(serializedObject);
			}
		}

		return soapObject;
	}


	/**
	 * Deserialize a single SOAP object into a business object
	 * @param rootObject : The SOAP object that needs to be deserialized
	 * @param classObject : The class of the object that needs to be parsed.
	 * WARNING : The class must have a default empty constructor
	 * @return The deserialized object
	 * @throws ITException : Framework exception of type PARSE_ERROR
	 */
	public static <ObjectClass> ObjectClass deserializeBusinessObject(SoapObject rootObject, Class<ObjectClass> classObject) throws ITException
	{
		try
		{
			ObjectClass objectToReturn = classObject.getConstructor().newInstance();
			Field[] fields = classObject.getDeclaredFields();

			for (int i = 0; i < fields.length; i++) 
			{
				Field currentField = fields[i];
				Type type = currentField.getType();
				currentField.setAccessible(true);
				String fieldName = currentField.getName();
				SoapPropertyElement property = currentField.getAnnotation(SoapPropertyElement.class);
				if(property != null && !StringUtils.isNullOrEmpty(property.name()))
					fieldName = property.name();

				//detect String
				if (type.equals(String.class)) {
					//We retrieve the property safely to avoid crash when missing property
					Object objValue =  rootObject.getPropertySafely(fieldName); 
					if(objValue == null)
						continue; //We don't crash if the property is missing, we just skip
					String strValue = objValue.toString();

					if(!StringUtils.isNullOrEmpty(strValue)){
						currentField.set(objectToReturn, strValue);
					}
				}
				else if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
					//We retrieve the property safely to avoid crash when missing property
					Object objValue =  rootObject.getPropertySafely(fieldName); 
					if(objValue == null)
						continue; //We don't crash if the property is missing, we just skip
					String strValue = objValue.toString();	          
					if(!StringUtils.isNullOrEmpty(strValue)){
						currentField.setInt(objectToReturn, Integer.valueOf(strValue));
					}
				}
				else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
					//detect float or Float  
					//We retrieve the property safely to avoid crash when missing property
					Object objValue =  rootObject.getPropertySafely(fieldName); 
					if(objValue == null)
						continue; //We don't crash if the property is missing, we just skip
					String strValue = objValue.toString();
					if(!StringUtils.isNullOrEmpty(strValue)){
						currentField.setFloat(objectToReturn, Float.valueOf(strValue));
					}            	
				}
				else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
					//We retrieve the property safely to avoid crash when missing property
					Object objValue =  rootObject.getPropertySafely(fieldName); 
					if(objValue == null)
						continue; //We don't crash if the property is missing, we just skip
					String strValue = objValue.toString();
					if(!StringUtils.isNullOrEmpty(strValue)){
						currentField.setDouble(objectToReturn, Double.valueOf(strValue));
					}
				}
				else if (type.equals(Long.TYPE) || type.equals(Long.class)) {
					//We retrieve the property safely to avoid crash when missing property
					Object objValue =  rootObject.getPropertySafely(fieldName); 
					if(objValue == null)
						continue; //We don't crash if the property is missing, we just skip
					String strValue = objValue.toString();
					if(!StringUtils.isNullOrEmpty(strValue)){
						currentField.setLong(objectToReturn, Long.valueOf(strValue));
					}
				}
				else if (type.equals(Short.TYPE) || type.equals(Short.class)) {
					//We retrieve the property safely to avoid crash when missing property
					Object objValue =  rootObject.getPropertySafely(fieldName); 
					if(objValue == null)
						continue; //We don't crash if the property is missing, we just skip
					String strValue = objValue.toString();
					if(!StringUtils.isNullOrEmpty(strValue)){
						currentField.setShort(objectToReturn, Short.valueOf(strValue));
					}
				}	
				else if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
					//We retrieve the property safely to avoid crash when missing property
					Object objValue =  rootObject.getPropertySafely(fieldName); 
					if(objValue == null)
						continue; //We don't crash if the property is missing, we just skip
					String strValue = objValue.toString();
					if(!StringUtils.isNullOrEmpty(strValue)){
						currentField.setBoolean(objectToReturn, Boolean.valueOf(strValue));
					}
				}	
				else if(type.equals(Date.class))
				{
					//Special deserialization for Date
					
					Object objValue =  rootObject.getPropertySafely(fieldName); 
					if(objValue == null)
						continue; //We don't crash if the property is missing, we just skip
					String strValue = objValue.toString();
					SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_WS);
					if(!StringUtils.isNullOrEmpty(strValue)){
						currentField.set(objectToReturn, dateFormat.parse(strValue));
					}
				}
				else
				{            	
					Type genericType = currentField.getGenericType();					
					if(genericType instanceof ParameterizedType)
					{
						ParameterizedType parameterizedType = (ParameterizedType) genericType;
						if(parameterizedType.getRawType().equals(List.class) || parameterizedType.getRawType().equals(Array.class))
						{
							Type innerType = parameterizedType.getActualTypeArguments()[0];
							//We retrieve the property safely to avoid crash when missing property
							Object listProperty =  rootObject.getPropertySafely(fieldName); 
							if(listProperty == null)
								continue; //We don't crash if the property is missing, we just skip

							if(listProperty instanceof SoapObject)
							{
								SoapObject listObject = (SoapObject) listProperty;
								Object deserializedList = deserializeBusinessObjectList(listObject, (Class<?>) innerType);
								if(deserializedList != null)
									currentField.set(objectToReturn, deserializedList);
							}
						}
						else if(parameterizedType.getRawType().equals(HashMap.class))
						{
							//TODO : Handle hash map if necessary
						}
					}
					else
					{
						Object objectProperty = rootObject.getPropertySafely(currentField.getName());
						if(objectProperty == null)
							continue;
						if(objectProperty instanceof SoapObject)
						{
							SoapObject soapObject = (SoapObject) objectProperty;
							Object deserializedObject = deserializeBusinessObject(soapObject, currentField.getType());							 
							if(deserializedObject != null)
								currentField.set(objectToReturn, deserializedObject);
						}
					}
				}
			}

			return objectToReturn;
		}
		catch (Exception e)
		{
			String errorMessage = String.format("Error while deserializing the object of type %s. Error : %s", classObject.getSimpleName(), e.getMessage());
			Log.e(LOG_TAG, errorMessage);
			throw new ITException(errorMessage, ITExceptionsType.PARSE_ERROR);
		}   
	}

	/**
	 * Deserialize a list soap object into a list of business objects.
	 * @param listObject : The soap object containing the list
	 * @param classObject : The class of the object in the list that will be deserialized into.
	 * WARNING : The class must have the default empty constructor
	 * @return The list of deserialized objects
	 * @throws ITException : Framework exception of type PARSE_ERROR
	 */
	public static <ObjectClass> List<ObjectClass> deserializeBusinessObjectList(SoapObject listObject, Class<ObjectClass> classObject) throws ITException
	{
		List<ObjectClass> listOfObjects = new ArrayList<ObjectClass>();

		for(int i=0; i < listObject.getPropertyCount(); i++)
		{
			Object property = listObject.getProperty(i);
			if(classObject.equals(String.class))
			{
				String valueToCast = property.toString();
				listOfObjects.add(classObject.cast(valueToCast));
			}
			else if(classObject.equals(Integer.TYPE) || classObject.equals(Integer.class))
			{
				Integer valueToCast = Integer.valueOf(property.toString());
				listOfObjects.add(classObject.cast(valueToCast));
			}
			else if(classObject.equals(Double.TYPE) || classObject.equals(Double.class))
			{
				Double valueToCast = Double.valueOf(property.toString());
				listOfObjects.add(classObject.cast(valueToCast));
			}
			else if(classObject.equals(Float.TYPE) || classObject.equals(Float.class))
			{
				Float valueToCast = Float.valueOf(property.toString());
				listOfObjects.add(classObject.cast(valueToCast));
			}
			else if(classObject.equals(Long.TYPE) || classObject.equals(Long.class))
			{
				Long valueToCast = Long.valueOf(property.toString());
				listOfObjects.add(classObject.cast(valueToCast));
			}
			else if(classObject.equals(Short.TYPE) || classObject.equals(Short.class))
			{
				Short valueToCast = Short.valueOf(property.toString());
				listOfObjects.add(classObject.cast(valueToCast));
			}
			else if(classObject.equals(Boolean.TYPE) || classObject.equals(Boolean.class))
			{
				Boolean valueToCast = Boolean.valueOf(property.toString());
				listOfObjects.add(classObject.cast(valueToCast));
			}
			else
			{
				if(property instanceof SoapObject)
				{
					SoapObject soapObject = (SoapObject) property;
					ObjectClass deserializedObject = deserializeBusinessObject(soapObject, classObject);
					if(deserializedObject != null)
						listOfObjects.add(deserializedObject);
				}
			}
		}

		return listOfObjects;
	}
}
