package com.essers.tracking;


import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.essers.tracking.model.provider.TrackingContract.Order;
import com.essers.tracking.model.service.SyncService;
import com.essers.tracking.ui.OrdersFragment;

public class HomeActivity extends FragmentActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		OrdersFragment fr = new OrdersFragment();
		fragmentTransaction.add(R.id.fragment1, fr);
		fragmentTransaction.commit();

		triggerRefresh();
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
}