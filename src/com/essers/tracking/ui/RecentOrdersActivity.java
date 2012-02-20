package com.essers.tracking.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.essers.tracking.R;
import com.essers.tracking.model.service.ServiceHelper;
import com.essers.tracking.model.service.SyncService;
import com.essers.tracking.ui.fragment.OrderDetailFragment;
import com.essers.tracking.ui.fragment.OrderListFragment;
import com.essers.tracking.ui.fragment.OrderListFragment.ListListener;
import com.essers.tracking.util.MyResultReceiver;
import com.essers.tracking.util.WebserviceHelper;

public class RecentOrdersActivity extends BaseActivity implements ListListener,
		MyResultReceiver.Receiver {

	private static final String TAG = "RecentOrdersActivity";
	public static final int REQUEST_ORDERS = 100;
	public static final int REQUEST_CLEAN_ORDERS = 101;

	private MyResultReceiver mReceiver;

	/** Index of the page parameter to pass onto the request for recent orders */
	private int lastPageRequest = 1;
	private OrderListFragment mOrderList;
	private boolean mReady = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recent_orders);

		getActivityHelper().setupActionBar(getTitle(), 0);

		mOrderList = (OrderListFragment) getSupportFragmentManager().findFragmentById(
				R.id.fragment_order_list);

		registerReceiver();

		triggerRefresh();

	}

	private void triggerRefresh() {

		Log.d(TAG, "triggerRefresh() called");
		mReady = false;
		int token;
		if (lastPageRequest == 1) {
			token = REQUEST_CLEAN_ORDERS;
		} else {
			token = REQUEST_ORDERS;
		}

		String url = WebserviceHelper.prepareCall(
				this.getString(R.string.remote_get_recent_orders),
				new String[] { String.valueOf(lastPageRequest) });
		ServiceHelper.execute(this, mReceiver, token, url);
		
		

	}

	public void registerReceiver() {
		mReceiver = new MyResultReceiver(new Handler());
		mReceiver.setReceiver(this);

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

	public void onListItemSelected(long orderBaseId) {

		/*OrderDetailFragment detailView = (OrderDetailFragment) this
				.getFragmentManager().findFragmentById(
						R.id.fragment_order_detail);

		if (detailView == null || !detailView.isInLayout()) {
			Intent showContent = new Intent(getApplication(),
					OrderDetailActivity.class);
			// showContent.setData(Uri.p)
			startActivity(showContent);
		} else {
			detailView.updateOrder(orderBaseId);
		}*/
		
		/*Intent showContent = new Intent(getApplication(),
				OrderDetailActivity.class);
		Bundle bundle = new Bundle();
		int order = (int) orderBaseId;
		bundle.putInt("order_id", order);
		showContent.putExtras(bundle);
		// showContent.setData(Uri.p)
		startActivity(showContent);*/

	}

	public void onLastIndexReached() {

		// If we are already fetching new data dont trigger another refresh
		if (!mReady) {
			return;
		}

		this.lastPageRequest++;

		triggerRefresh();
		// triggerLoadMoreData();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.recent_orders, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected called");
		switch (item.getItemId()) {

		case R.id.menu_refresh:
			this.lastPageRequest = 1;
			triggerRefresh();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "onStart");
	}

}
