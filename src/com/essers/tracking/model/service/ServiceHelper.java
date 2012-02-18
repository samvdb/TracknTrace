package com.essers.tracking.model.service;

import android.content.Context;
import android.content.Intent;

import com.essers.tracking.R;
import com.essers.tracking.util.MyResultReceiver;
import com.essers.tracking.util.MyResultReceiver.Receiver;

public class ServiceHelper {
	
	/**
	 * Executes the service with the given parameters. 
	 * @param context		see {@link Context}
	 * @param receiver		In order to let the activity know the syncing is complete it needs to register itself as a Receiver.
	 * @param match			For example Order.PATH_TOKEN, this is the identifier to create a Processor to process the data.
	 * @param parameters	This is the actual URL wich the REST service has to call to get the data.
	 */
	public static void execute(Context context, MyResultReceiver receiver, int match, String parameters) {
		
		final Intent syncIntent = new Intent(
				Intent.ACTION_SYNC, 
				null, 
				context,
				SyncService.class);
		
		syncIntent.putExtra(SyncService.EXTRA_STATUS_RECEIVER, receiver);
		
		syncIntent.putExtra("api_url", parameters);
		syncIntent.putExtra("token", match);
		context.startService(syncIntent);
	}

}
