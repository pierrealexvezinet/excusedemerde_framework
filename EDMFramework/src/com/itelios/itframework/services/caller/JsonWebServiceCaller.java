package com.itelios.itframework.services.caller;

import java.util.List;

import com.itelios.itframework.exception.ITException;
import com.itelios.itframework.exception.ITException.ITExceptionsType;
import com.itelios.itframework.services.engine.WebServiceEngine;
import com.itelios.itframework.utils.JsonUtils;
import com.itelios.itframework.utils.StringUtils;

/**
 * Json webservice caller
 * @author marcduvignaud
 *
 */
public class JsonWebServiceCaller {
	
	/**
	 * Call a POST Web service that needs a request object and send back a response object
	 * @param urlToCall : The URL of the web service
	 * @param requestObject : The object to send to the Web service
	 * @param returnTypeClass : The type of object that is sent back by the web service
	 * @return The object sent back by the web service
	 * @throws ITException : A web service exception
	 */
	public  <RequestType, ReturnType> ReturnType callPostWebService(String urlToCall, RequestType requestObject, Class<ReturnType> returnTypeClass) throws ITException
	{
		String jsonRequest = JsonUtils.serializeJson(requestObject);		
		String jsonResponse = WebServiceEngine.callWebServicePost(urlToCall, jsonRequest);
		if(StringUtils.isNullOrEmpty(jsonResponse))
		{
			throw new ITException("No response message sent back by the server for the POST call to " + urlToCall, ITExceptionsType.NO_RESPONSE_MESSAGE_ERROR);
		}
		return JsonUtils.deserializeJson(jsonResponse, returnTypeClass);
	}
	
	/**
	 * Call a POST Web service that needs a request object and send back a response list containing several object
	 * @param urlToCall : The URL of the web service
	 * @param requestObject : The object to send to the Web service
	 * @param returnTypeClass : The type of object that is sent back in a list by the web service
	 * @return The list of object sent back by the web service
	 * @throws ITException : A web service exception
	 */
	public <RequestType, ReturnType> List<ReturnType> callPostWebServiceForList(String urlToCall, RequestType requestObject,
			Class<ReturnType> returnTypeClass) throws ITException
	{
		String jsonRequest = JsonUtils.serializeJson(requestObject);		
		String jsonResponse = WebServiceEngine.callWebServicePost(urlToCall, jsonRequest);
		if(StringUtils.isNullOrEmpty(jsonResponse))
		{
			throw new ITException("No response message sent back by the server for the POST call to " + urlToCall, ITExceptionsType.NO_RESPONSE_MESSAGE_ERROR);
		}
		return JsonUtils.deserializeJsonList(jsonResponse, returnTypeClass);
	}

	/**
	 * Call a POST Web service that doesn't need a request message but send back a response message
	 * @param urlToCall : The URL of the web service
	 * @param returnTypeClass : The type of object that is sent back by the web service
	 * @return The object sent back by the web service
	 * @throws ITException : A web service exception
	 */
	public <ReturnType> ReturnType callPostWebService(String urlToCall, Class<ReturnType> returnTypeClass) throws ITException
	{	
		String jsonResponse = WebServiceEngine.callWebServicePost(urlToCall, null);
		if(StringUtils.isNullOrEmpty(jsonResponse))
		{
			throw new ITException("No response message sent back by the server for the POST call to " + urlToCall, ITExceptionsType.NO_RESPONSE_MESSAGE_ERROR);
		}
		return JsonUtils.deserializeJson(jsonResponse, returnTypeClass);
	}
	
	/**
	 * Call a POST Web service that doesn't need a request message but send back a response list containing several object
	 * @param urlToCall : The URL of the web service
	 * @param returnTypeClass : The type of object that is sent back in a list by the web service
	 * @return The list of object sent back by the web service
	 * @throws ITException : A web service exception
	 */
	public <ReturnType> List<ReturnType> callPostWebServiceForList(String urlToCall, Class<ReturnType> returnTypeClass) throws ITException
	{
		String jsonResponse = WebServiceEngine.callWebServicePost(urlToCall, null);
		if(StringUtils.isNullOrEmpty(jsonResponse))
		{
			throw new ITException("No response message sent back by the server for the POST call to " + urlToCall, ITExceptionsType.NO_RESPONSE_MESSAGE_ERROR);
		}
		return JsonUtils.deserializeJsonList(jsonResponse, returnTypeClass);
	}

	/**
	 * Call a POST Web service that doesn't need a request object and doesn't send back a response object
	 * @param urlToCall : The URL of the web service
	 * @throws ITException: A web service exception
	 */
	public void callPostWebService(String urlToCall) throws ITException
	{
		WebServiceEngine.callWebServicePost(urlToCall, null);
	}

	/**
	 * Call a GET Web service that send back a response object
	 * @param urlToCall : The URL of the web service
	 * @param returnTypeClass : The type of object that is sent back by the web service
	 * @return The object sent back by the web service
	 * @throws ITException : A web service exception
	 */
	public <ReturnType> ReturnType callGetWebService(String urlToCall, Class<ReturnType> returnTypeClass) throws ITException
	{
		String jsonResponse = WebServiceEngine.callWebServiceGet(urlToCall);
		if(StringUtils.isNullOrEmpty(jsonResponse))
		{
			throw new ITException("No response message sent back by the server for the GET call to " + urlToCall, ITExceptionsType.NO_RESPONSE_MESSAGE_ERROR);
		}
		return JsonUtils.deserializeJson(jsonResponse, returnTypeClass);
	}
	
	/**
	 * Call a GET Web service that send back a response list containing several object
	 * @param urlToCall : The URL of the web service
	 * @param returnTypeClass : The type of object that is sent back in a list by the web service
	 * @return The list of object sent back by the web service
	 * @throws ITException : A web service exception
	 */
	public <ReturnType> List<ReturnType> callGetWebServiceForList(String urlToCall, Class<ReturnType> returnTypeClass) throws ITException
	{
		String jsonResponse = WebServiceEngine.callWebServiceGet(urlToCall);
		if(StringUtils.isNullOrEmpty(jsonResponse))
		{
			throw new ITException("No response message sent back by the server for the GET call to " + urlToCall, ITExceptionsType.NO_RESPONSE_MESSAGE_ERROR);
		}
		return JsonUtils.deserializeJsonList(jsonResponse, returnTypeClass);
	}
	
	/**
	 * Call a GET Web service that doesn't send back a response object
	 * @param urlToCall : The URL of the web service
	 * @throws ITException: A web service exception
	 */
	public void callGetWebService(String urlToCall) throws ITException
	{
		WebServiceEngine.callWebServiceGet(urlToCall);
	}
}
