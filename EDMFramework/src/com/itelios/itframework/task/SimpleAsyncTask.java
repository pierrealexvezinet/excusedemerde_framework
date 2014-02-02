package com.itelios.itframework.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.itelios.itframework.exception.ITException;
import com.itelios.itframework.listener.SimpleTaskListener;

/**
 * Simple async task which :
 * 	<ul>
 *  <li>Show a progress dialog with the given message in parameter (if you want to show one).</li>
 *  <li>Execute the SimpleTaskListener retrieveObjectInBackground() in a background thread.</li>
 *  <li>Execute the SimpleTaskListener onTaskCompleted() if success or onTaskRaisedError() if exception raised.</li>
 *  </ul>
 * @author marcduvignaud
 *
 * @param <ReturnType> : The class of the object type in the listener
 */
public class SimpleAsyncTask<ReturnType> extends AsyncTask<String, Void, ReturnType>{
	
	private Activity theActivity;	
	private ProgressDialog progressDialog;
	private ITException raisedException;
	private boolean needProgDialog;
	private SimpleTaskListener<ReturnType> taskListener;
	private String progressMessage;
	private String progressTitle;
	private boolean isStillShowing;
	
	public boolean isStillShowing()
	{
		return isStillShowing;
	}
	public void setStillShowing(boolean isStillShowing)
	{
		this.isStillShowing = isStillShowing;
	}

	public SimpleAsyncTask(Activity activity, SimpleTaskListener<ReturnType> taskListener, String progressTitle, String progressMessage) {
		this.needProgDialog = true;
		this.theActivity = activity;
		this.taskListener = taskListener;
		this.progressMessage = progressMessage;
		this.progressTitle = progressTitle;
		this.isStillShowing = true;
	}
	
	public SimpleAsyncTask(Activity activity, SimpleTaskListener<ReturnType> taskListener) {
		this.needProgDialog = false;
		this.theActivity = activity;
		this.taskListener = taskListener;
		this.progressMessage = "";
		this.progressTitle = "";
		this.isStillShowing = true;
	}
	
	@Override
	protected ReturnType doInBackground(String... params) {
		if(!isStillShowing)
			return null;
		
		ReturnType object = null;
		try
		{
			object = taskListener.retrieveObjectInBackground();
		}
		catch(ITException ex)
		{
			this.raisedException = ex;
			object = null;
		}
		
		return object;
	}

	@Override
  protected void onPreExecute() {
		super.onPreExecute();
		if(!this.needProgDialog || !this.isStillShowing)
			return;		
		
		if(progressDialog != null && progressDialog.isShowing())
			return;
		
		progressDialog = ProgressDialog.show(theActivity, progressTitle, progressMessage);
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				// User canceled by pressing back button, so we cancel the task and inform the listener
				cancel(true);
				taskListener.onTaskCancelled();
			}
        });
  }
	
	@Override
	protected void onPostExecute(ReturnType result) {
		super.onPostExecute(result);
		
		if(!this.isStillShowing)
			return;
		
		
		if(this.needProgDialog && progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		
		if(this.raisedException != null)
			taskListener.onTaskRaisedError(raisedException);
		else
			taskListener.onTaskCompleted(result);
	}	
}
