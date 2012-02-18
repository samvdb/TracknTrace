package com.essers.tracking.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.essers.tracking.R;
import com.essers.tracking.model.provider.TrackingContract.DeliveryColumns;
import com.essers.tracking.model.provider.TrackingContract.Orders;
import com.essers.tracking.model.provider.TrackingContract.PickupColumns;
import com.essers.tracking.ui.BaseActivity;
import com.essers.tracking.ui.OrderDetailActivity;
import com.essers.tracking.util.MyOrderAdapter;

public class OrderListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor>, OnScrollListener{

	private static final String TAG = "OrderListFragment";
	private ListListener mListListener;
	private MyOrderAdapter mAdapter;

	private View mHeader;
	private View mFooter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mListListener = (ListListener) getActivity();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mAdapter = new MyOrderAdapter(getActivity(), null);

		mHeader = getActivity().getLayoutInflater().inflate(
				R.layout.syncing_progressbar, null);
		mFooter = getActivity().getLayoutInflater().inflate(
				R.layout.list_loading, null);
		getListView().addHeaderView(mHeader, null, false);
		getListView().setFooterDividersEnabled(true);

		setListAdapter(mAdapter);

		getListView().removeFooterView(mFooter);
		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);

		getListView().setOnScrollListener(this);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return getActivity().onOptionsItemSelected(item);
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		Uri baseUri = Orders.CONTENT_URI;
		
		return new CursorLoader(getActivity(), baseUri,
				RecentOrdersQuery.PROJECTION,
				null, null,
				Orders.DEFAULT_SORT);

	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in. (The framework will take care of closing the
		// old cursor once we return.)

		Log.d(TAG, "onLoadFinished called");
		mAdapter.swapCursor(data);

	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed. We need to make sure we are no
		// longer using it.
		Log.d(TAG, "onLoaderReset called");
		mAdapter.swapCursor(null);

	}

	/** {@inheritDoc} */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		Log.d(TAG, "onListItemClick(pos=" + position + ", id=" + id + ")");

		//mListListener.onListItemSelected(id);
		
		Intent showContent = new Intent(getActivity(),
				OrderDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putLong("order_id", id);
		showContent.putExtras(bundle);
		// showContent.setData(Uri.p)
		startActivity(showContent);

	}

	public interface RecentOrdersQuery {

		String[] PROJECTION = { BaseColumns._ID, Orders.ORDER_ID,
				Orders.REFERENCE, Orders.STATE,
				Orders.DELIVERY_DATE, Orders.PICKUP_DATE,
				Orders.PROBLEM,
				PickupColumns.NAME, PickupColumns.CITY, PickupColumns.COUNTRY, PickupColumns.ZIPCODE,
				DeliveryColumns.NAME, DeliveryColumns.CITY, DeliveryColumns.COUNTRY, DeliveryColumns.ZIPCODE};
	}

	public interface ListListener {
		public void onListItemSelected(long orderBaseId);

		public void onLastIndexReached();
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		boolean loadMore = 
	            firstVisibleItem + visibleItemCount >= totalItemCount;

	        if(loadMore) {
	        	mListListener.onLastIndexReached();
	        }

	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	public void updateHeader(int message, boolean syncing) {
		TextView progressStatus = (TextView) getActivity().findViewById(
				R.id.progress_status);

		progressStatus.setText(message);

		TextView timeUpdated = (TextView) getActivity().findViewById(
				R.id.progress_update_time);
		timeUpdated.setText(getSyncTime());

		ProgressBar bar = (ProgressBar) getActivity().findViewById(
				R.id.progressBar1);
		if (!syncing) {
			bar.setVisibility(ProgressBar.INVISIBLE);
			getListView().removeFooterView(mFooter);
		} else {
			bar.setVisibility(ProgressBar.VISIBLE);
			getListView().addFooterView(mFooter);

		}

	}

	public String getSyncTime() {
		SharedPreferences prefs = getActivity().getSharedPreferences(
				"synctime", Context.MODE_PRIVATE);
		return prefs.getString("last_time", "never");
	}

	public void updateSyncTime() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = df.format(c.getTime());

		Editor e = getActivity().getSharedPreferences("synctime",
				Context.MODE_PRIVATE).edit();
		e.putString("last_time", formattedDate);
		e.commit();

	}

}
