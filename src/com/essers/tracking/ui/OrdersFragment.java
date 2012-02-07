package com.essers.tracking.ui;



import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.essers.tracking.R;
import com.essers.tracking.model.provider.TrackingContract.Order;
import com.essers.tracking.util.NotifyingAsyncQueryHandler;

public class OrdersFragment extends ListFragment implements NotifyingAsyncQueryHandler.AsyncQueryListener {
	
	private static final String TAG = "OrdersFragment";
	
	private Cursor mCursor;
	private CursorAdapter mAdapter;
	
	private NotifyingAsyncQueryHandler mHandler;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mHandler = new NotifyingAsyncQueryHandler(getActivity().getContentResolver(), this);
		mAdapter = new OrderAdapter(getActivity());
		String[] projection = RecentOrdersQuery.PROJECTION;
		
		setListAdapter(mAdapter);
		mHandler.startQuery(RecentOrdersQuery.TOKEN, Order.CONTENT_URI, projection);
	}


	public void onQueryComplete(int token, Object cookie, Cursor cursor) {
		
		Log.d(TAG, "onQueryComplete(token=" +token+ ")");
		if (getActivity() == null) {
			return;
		}
		
		if (token == RecentOrdersQuery.TOKEN) {
			mCursor = cursor;
			getActivity().startManagingCursor(cursor);
			mAdapter.changeCursor(cursor);
		}
		
	}
	
	private class OrderAdapter extends CursorAdapter {
    	public OrderAdapter(Context context) {
    		super(context, null);
    	}

		@Override
		public void bindView(View arg0, Context arg1, Cursor cursor) {
		
			((TextView)arg0.findViewById(R.id.order_order_id)).setText(cursor.getString(RecentOrdersQuery.ORDER_ID));
			((TextView)arg0.findViewById(R.id.order_reference)).setText(cursor.getString(RecentOrdersQuery.REFERENCE));
			((TextView)arg0.findViewById(R.id.order_state)).setText(cursor.getString(RecentOrdersQuery.STATE));
			
		}

		@Override
		public View newView(Context arg0, Cursor cursor, ViewGroup parent) {
			
			return getActivity().getLayoutInflater().inflate(R.layout.list_item_order, parent, false);
		}
    	
    	
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
