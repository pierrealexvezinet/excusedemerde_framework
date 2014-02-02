package com.itelios.itframework.services.engine;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.itelios.itframework.ITApplication;
import com.itelios.itframework.exception.ITException;
import com.itelios.itframework.exception.ITException.ITExceptionsType;
import com.itelios.itframework.utils.ConnectivityUtils;
import com.itelios.itframework.utils.Log;
import com.itelios.itframework.utils.StringUtils;

/**
 * Engine for Web Services calls (Post & Get)
 * @author marcduvignaud
 *
 */
public class WebServiceEngine {

	private final static String LOG_TAG = "WebServiceEngine";
	
	public static String CONTENT_TYPE = "application/json";
	
	/**
	 * Function used to call REST WS returning a message
	 * @param urlToCall : The URL of the WS
	 * @param requestMessage : The message to send in the request (when POST is chosen)
	 * @param requestType : The type of the request to send (POST, GET)
	 * @return The message returned by the WS
	 * @throws ITException : An exception if there's an error
	 */
	public static String callWebService (String urlToCall, String requestMessage, 
			HttpRequestType requestType) throws ITException
	{  
		if(!ConnectivityUtils.isNetworkAvailable())
		{
			throw ITException.generateInternetConnectivityException();
		}
		
		ITException callException = null;
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, ITApplication.HTTP_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, ITApplication.HTTP_TIMEOUT);
    HttpClient httpclient = new DefaultHttpClient(httpParams);      
    
    HttpUriRequest request;          
    
    if(requestType == HttpRequestType.Post)
    {
    	HttpPost postRequest = new HttpPost(urlToCall);	    
    	if(!StringUtils.isNullOrEmpty(requestMessage))
    	{
    		try
  			{
    			Log.i(LOG_TAG, "Message sent to server : " + requestMessage);
	    		StringEntity requestEntity = new StringEntity(requestMessage, HTTP.UTF_8);
	    		requestEntity.setContentType(CONTENT_TYPE);
	    		Log.d(LOG_TAG, "Request length is : " + requestEntity.getContentLength());
	    		postRequest.setEntity(requestEntity);
  			}
  			catch (Exception ex)
  			{
  				String errorMessage = "Error while adding body to request - Ex : " + ex;
  				Log.e(LOG_TAG, errorMessage);
  				throw new ITException(errorMessage, ITExceptionsType.CALL_ERROR);
  			}
    	}	    			
	    request = postRequest;
    }
    else
    {
    	request = new HttpGet(urlToCall);
    }
    
    request.setHeader("Accept", CONTENT_TYPE);
    request.setHeader("Content-type", CONTENT_TYPE);
    
    String resultMessage = "";        
    try {  
        HttpResponse response = httpclient.execute(request);  
        if(response.getEntity() != null)
        {
        	HttpEntity responseEntity = response.getEntity();
        	resultMessage = EntityUtils.toString(responseEntity);
        	Log.i(LOG_TAG, "Message received from server : " + resultMessage);
        }
        else
        	resultMessage = "";
    }
    catch (Exception e) {  
    	String errorMessage = "Error while calling the WS - Ex : " + e.getMessage(); 
    	Log.e(LOG_TAG, errorMessage);
      callException = new ITException(errorMessage, ITException.ITExceptionsType.CALL_ERROR);        
    }  
    finally
    {
    	httpclient.getConnectionManager().shutdown();
    }
    if(callException != null)
    	throw callException;
    
    return resultMessage;
	}

	/**
	 * Call a POST WebService returning a message
	 * @param urlToCall : The url of the WebService
	 * @param requestMessage : The message to send in the request
	 * @return The message returned by the WebService
	 * @throws ITException : An exception if there's an error
	 */
	public static String callWebServicePost(String urlToCall, String requestMessage) throws ITException
	{
		return callWebService(urlToCall, requestMessage, HttpRequestType.Post);
	}
	
	/**
	 * Call a GET WebService returning a message
	 * @param urlToCall
	 * @return
	 * @throws ITException
	 */
	public static String callWebServiceGet(String urlToCall) throws ITException
	{
		return callWebService(urlToCall, "", HttpRequestType.Get);
	}
	
	/**
	 * Enum used to choose the Http Request type when calling a WS
	 * @author marcduvignaud
	 *
	 */
	public enum HttpRequestType
	{
		Get,
		Post
	}
}
