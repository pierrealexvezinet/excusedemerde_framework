package com.itelios.itframework.listener;

import com.itelios.itframework.exception.ITException;

/**
 * Listener used for simple async task that will retrieve an object from a web service
 * @author marcduvignaud
 *
 * @param <ReturnType> : The type of returned object
 */
public interface SimpleTaskListener<ReturnType> {
	public ReturnType retrieveObjectInBackground() throws ITException;
	public void onTaskCompleted(ReturnType returnedObject);
	public void onTaskRaisedError(ITException exception);
	public void onTaskCancelled();
}
