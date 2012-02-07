package com.essers.tracking.model.processor;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.essers.tracking.model.provider.TrackingContract.Order;

public class ProcessorFactory {
	
	public static Processor createProcessor(int action) throws UnsupportedOperationException {
		

		/*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String customer_id = preferences.getString("customer_id", "");
		
		if (customer_id.equals("")) {
			throw new RuntimeException("Something went wrong when reading the customer id.");
		}*/
		
		switch(action) {
		case Order.PATH_TOKEN:
			return new OrdersProcessor();
			default:
				throw new UnsupportedOperationException("No processor was found to match the requested URI. " + action);
			
		}
		
		
	}

}
