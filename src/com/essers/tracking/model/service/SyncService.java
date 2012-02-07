package com.essers.tracking.model.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.essers.tracking.model.processor.Processor.ProcessorException;
import com.essers.tracking.model.processor.ProcessorFactory;
import com.essers.tracking.model.webservice.Executor;
import com.essers.tracking.model.webservice.RESTExecutor;

public class SyncService extends IntentService {
	
	private static final String TAG = "SyncService";
	
	public static final String EXTRA_STATUS_RECEIVER = "com.essers.tracking.STATUS_RECEIVER";
	public static final int STATUS_RUNNING = 0x1;
    public static final int STATUS_ERROR = 0x2;
    public static final int STATUS_FINISHED = 0x3;
	private Executor mExecutor;
	
	
	
	public SyncService() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate called");
		// TODO Auto-generated method stub
		super.onCreate();
		
		ContentResolver resolver = getContentResolver();
		mExecutor = new RESTExecutor(getApplicationContext(), resolver);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "onHandleIntent(intent=" + intent.toString()+")");
		
		ResultReceiver receiver = intent.getParcelableExtra(EXTRA_STATUS_RECEIVER);
		if (receiver != null) {
			receiver.send(STATUS_RUNNING, Bundle.EMPTY);
		}
		
			final long startREST = System.currentTimeMillis();
			
			try {
				mExecutor.execute(intent.getExtras().getString("api_url"), ProcessorFactory.createProcessor(intent.getExtras().getInt("token")));
			final long stopREST = System.currentTimeMillis();
			
			Log.d(TAG, "Remote syncing took " + (stopREST - startREST) + "ms");
			
			} catch (Exception e) {
				Log.e(TAG, "Problem while syncing", e);
				
				if (receiver != null) {
					Bundle bundle = new Bundle();
					bundle.putString(Intent.EXTRA_TEXT, e.toString());
					receiver.send(STATUS_ERROR, bundle);
				}
			}
			
			Log.d(TAG, "Syncing finished.");
			
			if (receiver != null) {
				receiver.send(STATUS_FINISHED, Bundle.EMPTY);
			}
				

		
	}

}
