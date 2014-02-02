package com.itelios.itframework.services.engine;

import java.util.ArrayList;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.StrictMode;

import com.itelios.itframework.exception.ITException;
import com.itelios.itframework.exception.ITException.ITExceptionsType;
import com.itelios.itframework.utils.ConnectivityUtils;
import com.itelios.itframework.utils.Log;

/**
 * Web service engine class for the ksoap library
 * @author marcduvignaud
 *
 */
public class KSoapWebServiceEngine {
	private static final String LOG_TAG = "KSoapWebServiceEngine";
	
	/**
	 * Call a soap webservice and return the soap object result
	 * @param urlServer : The URL of the web service
	 * @param relativeUrlWsdl : The URL of the WSDL relative to the urlServer
	 * @param namespace : The namespace used by the soap web service
	 * @param methodName : The name of the method called
	 * @param requestObject
	 * @return The object result (can be soap object or soap primitive)
	 * @throws ITException
	 */
	public static Object callWebService(String urlServer, String relativeUrlWsdl, String namespace, 
			String methodName, SoapObject requestObject) throws ITException
	{
		if(!ConnectivityUtils.isNetworkAvailable())
		{
			throw ITException.generateInternetConnectivityException();
		}
		
		try {			
			//CREATE THE ENVELOPE TO SEND THE REQUEST
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.implicitTypes = true;

			//ENVELOPE TAKE THE REQUEST AS PARAMETER
			if(requestObject != null)
				envelope.setOutputSoapObject(requestObject);
			envelope.setAddAdornments(true);


			//CREATION OF PROPERTIES LIST TO PUSHED ITS IN THE HEADER OF THE SOAP MESSAGE
			ArrayList<HeaderProperty> headerProperty = new ArrayList<HeaderProperty>();

			//ADDING OF PROPERTIES IN ARRAYLIST TO COMPOSE HEADEROUT OF KSOAP MESSAGE
			headerProperty.add(new HeaderProperty("User-Agent", "kSOAP/2.0"));
			headerProperty.add(new HeaderProperty("Content-Type", "text/xml;charset=utf-8"));
			headerProperty.add(new HeaderProperty("Connection", "Keep-Alive"));
			headerProperty.add(new HeaderProperty("SOAPAction", namespace + methodName));



			//LET TO CALL THE WEBSERVICE
			HttpTransportSE ht = new HttpTransportSE(urlServer + relativeUrlWsdl);
			ht.debug = true;

			System.setProperty("http.keepAlive", "true");

			//CONSTRUCT A THREAD POLICY INSTANCE
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);


			//SET XML VERSION TAG BEFORE CALLING THE WEB SERVICE A THE SPECIFIED ADRESS
			ht.setXmlVersionTag("<?xml version=\"1.0\" encoding= \"UTF-8\"?>");

			//TAKE 3 PARAM : SOAP ACTION String url, SoapSerializationEnvelope Object, HeaderProperty (MANDATORY)
			ht.call(urlServer+""+methodName, envelope, headerProperty);

			Log.d(LOG_TAG, "Soap Web service call done with request dump : " + ht.requestDump);
			Log.d(LOG_TAG, "Soap Web service call done with response dump : " + ht.responseDump);			
			
			if(envelope.bodyIn instanceof SoapFault) {
				String errorMessage = "Invalid soap message received - Error : " + ((SoapFault) envelope.bodyIn).faultstring;
				Log.e(LOG_TAG, errorMessage);
				throw new ITException(errorMessage, ITExceptionsType.CALL_ERROR);
			}
			else {
				return envelope.getResponse();				
			} 
		} 
		catch (Exception e) {
			String errorMessage =  "Error while calling the webservice '" + urlServer + methodName + "' - " + e .getMessage();
			Log.e(LOG_TAG, errorMessage);
			throw new ITException(errorMessage, ITExceptionsType.CALL_ERROR);
		}
	}
}
