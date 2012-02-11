package com.essers.tracking.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * This class is used to define an interface for Activities or Fragments to implement their own listener when the service finishes.
 * @author Sam
 *
 */
public class MyResultReceiver extends ResultReceiver {

	private static final String TAG = "MyResultReceiver";
	private Receiver mReceiver;
	
	public MyResultReceiver(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Clears the current registered receiver.
	 */
	public void clearReceiver() {
		mReceiver = null;
	}
	
	/**
	 * Registeres a new receiver.
	 * @param receiver
	 */
	public void setReceiver(Receiver receiver) {
		mReceiver = receiver;
	}
	
	/**
	 * Callback interface to implement own method on how to handle the result.
	 * @author Sam
	 *
	 */
	public interface Receiver {
		/**
		 * Callback method wich needs to be implement to handle the sync result.
		 * @param resultCode
		 * @param resultData
		 */
		public void onReceiveResult(int resultCode, Bundle resultData);
		
		public void registerReceiver();
	}

	/** {@inheritDoc} */
	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData) {
		if (mReceiver != null) {
			mReceiver.onReceiveResult(resultCode, resultData);
		} else {
			Log.w(TAG, "No receiver registered for resultCode="+resultCode+ ": " + resultData.toString());
		}
	}
}
