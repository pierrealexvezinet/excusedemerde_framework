package com.itelios.itframework.exception;

/**
 * Exception class that handle exception types to be easier
 * @author marcduvignaud
 *
 */
public class ITException extends Exception {
	private static final long serialVersionUID = 3660603386264740486L;
	
	private ITExceptionsType exceptionType;
	
	public ITExceptionsType getExceptionType()
	{
		return exceptionType;
	}
	public void setExceptionType(ITExceptionsType exceptionType)
	{
		this.exceptionType = exceptionType;
	}
	
	public ITException(Throwable throwable)
	{
		super(throwable);
	}
	public ITException(String exError, ITExceptionsType exType)
	{
		super(exError);
		this.exceptionType = exType;
	}
	
	/**
	 * Get the preformated exception message
	 * @return An exception message containing the exception type and the error
	 */
	public String getITExceptionMessage()
	{
		return String.format("ITException of type '%s' has been raised. Error : %s", exceptionType.toString(), getMessage());
	}
	
	public static ITException generateInternetConnectivityException()
	{
		return new ITException("Unable to connect to the internet", ITExceptionsType.INTERNET_CONNECTIVITY_ERROR);
	}
	
	public enum ITExceptionsType
	{
		CALL_ERROR, //Error during WS Call
		NO_RESPONSE_MESSAGE_ERROR, //Error, no response message from the server
		PARSE_ERROR, //Error while parsing data
		RESULT_ERROR, //Error in the result
		INTERNET_CONNECTIVITY_ERROR, //No internet connection
		DATABASE_ERROR //Error from the database
	}
}
