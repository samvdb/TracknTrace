package com.essers.tracking.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.essers.tracking.R;
import com.essers.tracking.model.service.ServiceHelper;
import com.essers.tracking.model.service.SyncService;
import com.essers.tracking.ui.fragment.OrderListFragment;
import com.essers.tracking.ui.fragment.OrderListFragment.ListListener;
import com.essers.tracking.util.MyResultReceiver;
import com.essers.tracking.util.MyResultReceiver;
import com.essers.tracking.util.UIUtils;
import com.essers.tracking.util.WebserviceHelper;

public class SearchResultActivity extends BaseActivity implements ListListener,
		MyResultReceiver.Receiver {

	private static final String TAG = "SearchResultActivity";

	private MyResultReceiver mReceiver;
	private OrderListFragment mOrderList;
	private int lastPageRequest = 1;
	private boolean mReady = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);

		getActivityHelper().setupActionBar(getTitle(), 0);
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}



	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);

		mOrderList = (OrderListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_order_list);
		mOrderList.updateSyncTime();
		mOrderList.updateHeader(R.string.data_uptodate, false);
	}

	public void onListItemSelected(long orderBaseId) {
		// TODO Auto-generated method stub

	}

	public void onLastIndexReached() {
		// If we are already fetching new data dont trigger another refresh
		if (!mReady) {
			return;
		}

		this.lastPageRequest++;

		triggerSearchMore();

	}

	private void triggerSearchMore() {
		Log.v(TAG, "triggerSearchMore called");
		mReady = false;

		String orderId = this.getIntent().getStringExtra("order_id");

		String url = WebserviceHelper.prepareCall(
				this.getString(R.string.remote_search_by_order), new String[] {
						orderId, String.valueOf(lastPageRequest) });
		ServiceHelper.execute(this, mReceiver,
				RecentOrdersActivity.REQUEST_ORDERS, url);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_result, menu);
		return true;
	}

	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.d(TAG, "onReceiveResult(resultCode=" + resultCode + ", resultData="
				+ resultData.toString());

		switch (resultCode) {
		case SyncService.STATUS_RUNNING:

			mOrderList.updateHeader(R.string.fetching_orders, true);
			break;
		case SyncService.STATUS_FINISHED:

			mReady = true;
			mOrderList.updateSyncTime();
			mOrderList.updateHeader(R.string.data_uptodate, false);

			break;
		case SyncService.STATUS_ERROR:

			mOrderList.updateSyncTime();
			mOrderList.updateHeader(R.string.no_more_data, false);
			this.lastPageRequest--;
			break;

		}

	}

	public void registerReceiver() {
		mReceiver = new MyResultReceiver(new Handler());
		mReceiver.setReceiver(this);

	}

}
