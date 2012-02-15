package com.essers.tracking.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.essers.tracking.R;
import com.essers.tracking.model.provider.TrackingContract.Order;
import com.essers.tracking.model.service.ServiceHelper;
import com.essers.tracking.model.service.SyncService;
import com.essers.tracking.ui.fragment.OrderDetailFragment;
import com.essers.tracking.ui.fragment.OrderListFragment.ListListener;
import com.essers.tracking.util.MyResultReceiver;
import com.essers.tracking.util.WebserviceHelper;

public class RecentOrdersActivity extends BaseActivity implements ListListener, MyResultReceiver.Receiver {
	
	private static final String TAG = "RecentOrdersActivity";
	
	private MyResultReceiver mReceiver;
	
	/** Index of the page parameter to pass onto the request for recent orders */
	private int lastPageRequest = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recent_orders);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setupHomeActivity();
		
		
		registerReceiver();
		
		triggerRefresh();
		
	}
	
	
	private void triggerRefresh() {

		Log.d(TAG, "triggerRefresh() called");
		
		String url = WebserviceHelper.prepareCall(this.getString(R.string.remote_get_recent_orders), new String[]{this.getUsername(), String.valueOf(lastPageRequest)});
		//String url = this.getString(R.string.remote_test_oauth);
		ServiceHelper.execute(this, mReceiver, Order.PATH_TOKEN, url);
		
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


	public void registerReceiver() {
		mReceiver = new MyResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		
	}


	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.d(TAG, "onReceiveResult(resultCode=" + resultCode + ", resultData=" + resultData.toString());  
		
		TextView progressStatus = (TextView) this.findViewById(R.id.progress_status);
		TextView timeUpdated = (TextView) this.findViewById(R.id.progress_update_time);
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
		
		switch(resultCode) {
		case SyncService.STATUS_RUNNING:
			
			progressStatus.setText(R.string.fetching_orders);
			break;
		case SyncService.STATUS_FINISHED:
			progressStatus.setText(R.string.data_uptodate);
			timeUpdated.setText(formattedDate);
			ProgressBar bar = (ProgressBar)this.findViewById(R.id.progressBar1);
			bar.setVisibility(ProgressBar.INVISIBLE);
			break;
		case SyncService.STATUS_ERROR:
			this.lastPageRequest--;
			break;
			
		}
		
	}


	public void onListItemSelected(long orderBaseId) {
		
		OrderDetailFragment detailView = (OrderDetailFragment) this.getFragmentManager().findFragmentById(R.id.fragment_order_detail);
		
		if (detailView == null || !detailView.isInLayout()) {
			Intent showContent = new Intent(getApplication(), OrderDetailActivity.class);
			//showContent.setData(Uri.p)
			startActivity(showContent);
		} else {
			detailView.updateOrder(orderBaseId);
		}
		
	}


	public void onLastIndexReached() {
		this.lastPageRequest++;
		triggerRefresh();
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.recent_orders, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected called");
		switch(item.getItemId()) {
		case R.id.menu_search:
			
			return true;
		case R.id.menu_refresh:
			triggerRefresh();
			return true;
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	


	

}

