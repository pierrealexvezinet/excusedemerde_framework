package com.itelios.itframework.services.caller;

import java.util.List;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import com.itelios.itframework.exception.ITException;
import com.itelios.itframework.exception.ITException.ITExceptionsType;
import com.itelios.itframework.services.engine.KSoapWebServiceEngine;
import com.itelios.itframework.utils.CastUtils;
import com.itelios.itframework.utils.SoapUtils;


/**
 * SOAP webservice caller
 * @author marcduvignaud
 *
 */
public class SoapWebServiceCaller  {

	/**
	 * Call a SOAP Web service that needs a request object and send back a response object
	 * @param urlServer : The URL of the web service
	 * @param relativeUrlWsdl : The URL of the WSDL relative to the urlServer
	 * @param namespace : The namespace used by the soap web service
	 * @param methodName : The name of the method called
	 * @param requestObject : The object to send to the Web service
	 * @param returnTypeClass : The type of object that is sent back by the web service
	 * @return The object sent back by the web service
	 * @throws ITException : A web service exception
	 */
	public static <RequestType, ReturnType> ReturnType callWebService(String urlServer, String relativeUrlWsdl, String namespace, 
			String methodName, RequestType requestObject, Class<ReturnType> returnTypeClass) throws ITException
	{
		SoapObject soapRequest = SoapUtils.serializeBusinessObject(namespace, methodName, requestObject);
		if(soapRequest == null)
			throw new ITException("Error while serializing the object - The serialized object is null", ITExceptionsType.PARSE_ERROR);
		
		Object result = KSoapWebServiceEngine.callWebService(urlServer, relativeUrlWsdl, namespace, methodName, soapRequest);
		if(result == null)
		{
			throw new ITException("No response message sent back by the server for the call to " + urlServer + methodName, ITExceptionsType.NO_RESPONSE_MESSAGE_ERROR);
		}
		if(result instanceof SoapObject)
		{
			SoapObject soapResult = (SoapObject) result;
			return SoapUtils.deserializeBusinessObject(soapResult, returnTypeClass);
		}
		else if(result instanceof SoapPrimitive)
		{
			SoapPrimitive soapPrimitiveResult = (SoapPrimitive) result;
			try
			{
				return CastUtils.castPrimitiveTypeValue(soapPrimitiveResult.toString(), returnTypeClass);
			}
			catch (ClassCastException ex)
			{
				throw new ITException("Error while casting result '"+soapPrimitiveResult.toString()+"' to primitive type '"+returnTypeClass.getSimpleName()+"'", ITExceptionsType.PARSE_ERROR);
			}
		}
		else
		{
			throw new ITException("Unknown response type sent back by the server for the call to " + urlServer + methodName, ITExceptionsType.NO_RESPONSE_MESSAGE_ERROR);
		}
	}
	
	/**
	 * Call a SOAP Web service that needs a request object and send back a response list containing several object
	 * @param urlServer : The URL of the web service
	 * @param relativeUrlWsdl : The URL of the WSDL relative to the urlServer
	 * @param namespace : The namespace used by the soap web service
	 * @param methodName : The name of the method called
	 * @param requestObject : The object to send to the Web service
	 * @param returnTypeClass : The type of object that is sent back by the web service
	 * @return The list of objects sent back by the web service
	 * @throws ITException : A web service exception
	 */
	public static <RequestType, ReturnType> List<ReturnType> callWebServiceForList(String urlServer, String relativeUrlWsdl, String namespace, 
			String methodName, RequestType requestObject, Class<ReturnType> returnTypeClass) throws ITException
	{
		SoapObject soapRequest = SoapUtils.serializeBusinessObject(namespace, methodName, requestObject);
		if(soapRequest == null)
			throw new ITException("Error while serializing the object - The serialized object is null", ITExceptionsType.PARSE_ERROR);
		
		//For the lists, the returned object is always a soap object
		SoapObject soapResult = (SoapObject) KSoapWebServiceEngine.callWebService(urlServer, relativeUrlWsdl, namespace, methodName, soapRequest);
		if(soapResult == null)
		{
			throw new ITException("No response message sent back by the server for the call to " + urlServer + methodName, ITExceptionsType.NO_RESPONSE_MESSAGE_ERROR);
		}
		return SoapUtils.deserializeBusinessObjectList(soapResult, returnTypeClass);
	}
	
	/**
	 * Call a SOAP Web service that doesn't need a request object and send back a response object
	 * @param urlServer : The URL of the web service
	 * @param relativeUrlWsdl : The URL of the WSDL relative to the urlServer
	 * @param namespace : The namespace used by the soap web service
	 * @param methodName : The name of the method called
	 * @param returnTypeClass : The type of object that is sent back by the web service
	 * @return The object sent back by the web service
	 * @throws ITException : A web service exception
	 */
	public static <ReturnType> ReturnType callWebService(String urlServer, String relativeUrlWsdl, String namespace, 
			String methodName, Class<ReturnType> returnTypeClass) throws ITException
	{
		SoapObject soapRequest = new SoapObject(urlServer, methodName);
		
		Object result = KSoapWebServiceEngine.callWebService(urlServer, relativeUrlWsdl, namespace, methodName, soapRequest);
		if(result == null)
		{
			throw new ITException("No response message sent back by the server for the call to " + urlServer + methodName, ITExceptionsType.NO_RESPONSE_MESSAGE_ERROR);
		}
		if(result instanceof SoapObject)
		{
			SoapObject soapResult = (SoapObject) result;
			return SoapUtils.deserializeBusinessObject(soapResult, returnTypeClass);
		}
		else if(result instanceof SoapPrimitive)
		{
			SoapPrimitive soapPrimitiveResult = (SoapPrimitive) result;
			try
			{
				return CastUtils.castPrimitiveTypeValue(soapPrimitiveResult.toString(), returnTypeClass);
			}
			catch (ClassCastException ex)
			{
				throw new ITException("Error while casting result '"+soapPrimitiveResult.toString()+"' to primitive type '"+returnTypeClass.getSimpleName()+"'", ITExceptionsType.PARSE_ERROR);
			}
		}
		else
		{
			throw new ITException("Unknown response type sent back by the server for the call to " + urlServer + methodName, ITExceptionsType.NO_RESPONSE_MESSAGE_ERROR);
		}
	}
	
	/**
	 * Call a SOAP Web service that doesn't need a request object and send back a response list containing several object
	 * @param urlServer : The URL of the web service
	 * @param relativeUrlWsdl : The URL of the WSDL relative to the urlServer
	 * @param namespace : The namespace used by the soap web service
	 * @param methodName : The name of the method called
	 * @param returnTypeClass : The type of object that is sent back by the web service
	 * @return The list of objects sent back by the web service
	 * @throws ITException : A web service exception
	 */
	public static <ReturnType> List<ReturnType> callWebServiceForList(String urlServer, String relativeUrlWsdl, String namespace, 
			String methodName, Class<ReturnType> returnTypeClass) throws ITException
	{
		SoapObject soapRequest = new SoapObject(urlServer, methodName);
		
		//For the lists, the returned object is always a soap object
		SoapObject soapResult = (SoapObject) KSoapWebServiceEngine.callWebService(urlServer, relativeUrlWsdl, namespace, methodName, soapRequest);
		if(soapResult == null)
		{
			throw new ITException("No response message sent back by the server for the call to " + urlServer + methodName, ITExceptionsType.NO_RESPONSE_MESSAGE_ERROR);
		}
		return SoapUtils.deserializeBusinessObjectList(soapResult, returnTypeClass);
	}
	
}
