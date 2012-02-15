package com.essers.tracking.ui.fragment;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.essers.tracking.R;
import com.essers.tracking.model.provider.TrackingContract;
import com.essers.tracking.model.provider.TrackingContract.Order;
import com.essers.tracking.ui.BaseActivity;

public class OrderListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final String TAG = "OrderListFragment";
	private ListListener mListListener;
	private Cursor mCursor;
	private CursorAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// initListData(savedInstanceState);

		mListListener = (ListListener) getActivity();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		mAdapter = new MyOrderAdapter(getActivity(), null);

		setListAdapter(mAdapter);
		//setListShown(false);
		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);

	}

	
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		//Uri baseUri = Uri.parse(args.getString("content_uri"));
		
		String customerId = ((BaseActivity)getActivity()).getUsername();
		Uri baseUri = TrackingContract.Order.buildCustomerOrdersUri(customerId);
		return new CursorLoader(getActivity(), baseUri,
				RecentOrdersQuery.PROJECTION, TrackingContract.Order.Columns.CUSTOMER_ID + " = ?", new String[] {customerId}, TrackingContract.Order.DEFAULT_SORT);

	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in. (The framework will take care of closing the
		// old cursor once we return.)
		
		Log.d(TAG, "onLoadFinished called");
		mAdapter.swapCursor(data);

		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}

	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
		Log.d(TAG, "onLoaderReset called");
        mAdapter.swapCursor(null);

	}

	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		Log.d(TAG, "onListItemClick(pos="+position+ ", id=" + id + ")");
		
		mListListener.onListItemSelected(id);
		this.
	}



	private interface RecentOrdersQuery {
		int TOKEN = 2;

		String[] PROJECTION = { BaseColumns._ID, Order.Columns.ORDER_ID,
				Order.Columns.REFERENCE, Order.Columns.STATE, Order.Columns.DELIVERY_DATE };

		int ORDER_ID = 0;
		int REFERENCE = 1;
		int STATE = 2;
		int DELIVERY_DATE = 3;
	}
	public interface ListListener {
		public void onListItemSelected(long orderBaseId);

		public void onLastIndexReached();
	}

	
	private class MyOrderAdapter extends CursorAdapter {
		
		private Cursor mCursor;
		private Context mContext;

		public MyOrderAdapter(Context context, Cursor c) {
			super(context, c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View arg0, Context arg1, Cursor c) {
			TextView t = (TextView) arg0.findViewById(R.id.item_reference);
			t.setText(c.getString(c.getColumnIndex(Order.Columns.REFERENCE)));
			t = (TextView) arg0.findViewById(R.id.item_order_id);
			
			t.setText(c.getString(c.getColumnIndex(Order.Columns.ORDER_ID)));
			t = (TextView) arg0.findViewById(R.id.item_delivery_date);
			
			t.setText(c.getString(c.getColumnIndex(Order.Columns.DELIVERY_DATE)));
			t = (TextView) arg0.findViewById(R.id.item_status);
			
			TypedArray states = arg1.getResources().obtainTypedArray(R.array.order_state);
			int state = c.getInt(c.getColumnIndex(Order.Columns.STATE))-1;
			t.setText(states.getText(state));
	
			ProgressBar b = (ProgressBar)arg0.findViewById(R.id.item_progressbar);
			b.setProgress(state);
			
			if (state == 6) {
				b.setProgressDrawable(arg1.getResources().getDrawable(R.drawable.progress_horizontal_error));
			}
			
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup parent) {
			final View view = LayoutInflater.from(arg0).inflate(R.layout.list_item_order, parent, false);
			return view;
		}
		
	}

}
