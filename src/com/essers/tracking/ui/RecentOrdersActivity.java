package com.essers.tracking.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;

import com.essers.tracking.R;
import com.essers.tracking.model.provider.TrackingContract.Order;
import com.essers.tracking.model.service.SyncService;
import com.essers.tracking.ui.fragment.OrderListFragment.ListListener;

public class RecentOrdersActivity extends BaseActivity implements ListListener {
	
	/** Index of the page parameter to pass onto the request for recent orders */
	private int lastPageRequest = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_recent_orders);
	}
	
	
	private void triggerRefresh() {

		final Intent syncIntent = new Intent(Intent.ACTION_SYNC, null, this,
				SyncService.class);
		syncIntent.putExtra("api_url",
				"http://apify.hibernia.be/customer/cust1/orders.json");
		syncIntent.putExtra("token", Order.PATH_TOKEN);
		startService(syncIntent);
	}

	public interface RecentOrdersQuery {
		int TOKEN = 2;

		String[] PROJECTION = { 
				BaseColumns._ID,
				Order.Columns.ORDER_ID,
				Order.Columns.REFERENCE, Order.Columns.STATE };

		int ORDER_ID = 0;
		int REFERENCE = 1;
		int STATE = 2;
	}

	public void onListItemSelected(String orderId) {
		// TODO Auto-generated method stub
		
	}


	public void onLastIndexReached() {
		// TODO Auto-generated method stub
		
	}
	

}
