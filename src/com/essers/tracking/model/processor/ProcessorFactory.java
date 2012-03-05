package com.essers.tracking.model.processor;

import android.util.Log;

import com.essers.tracking.ui.MyMapActivity;
import com.essers.tracking.ui.RecentOrdersActivity;
import com.essers.tracking.ui.fragment.LoginFragment;
import com.essers.tracking.ui.fragment.SearchFragment;

public class ProcessorFactory {
	
	public static Processor createProcessor(int action) throws UnsupportedOperationException {
		

		/*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String customer_id = preferences.getString("customer_id", "");
		
		if (customer_id.equals("")) {
			throw new RuntimeException("Something went wrong when reading the customer id.");
		}*/
		
		switch(action) {
		case RecentOrdersActivity.REQUEST_ORDERS:
			return new OrdersProcessor();
		case RecentOrdersActivity.REQUEST_CLEAN_ORDERS:
			return new OrdersProcessor(true);
		case SearchFragment.SEARCH_TOKEN:
			Log.d("ProcessorFactory", "SearchProcessor called");
			return new SearchProcessor();
		case MyMapActivity.GPS_TOKEN: 
			Log.d("ProcessorFactory", "GpsProcessor called");
			return new GpsProcessor();
		case LoginFragment.LOGIN_TOKEN:
			return new LoginProcessor();
			
			default:
				throw new UnsupportedOperationException("No processor was found to match the requested URI. " + action);
			
		}
		
		
	}

}
