package com.essers.tracking.ui.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.essers.tracking.R;




public class OrderDetailFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	
	private static final String TAG = "OrderDetailFragment";
	private Cursor mCursor;
	
	public static OrderDetailFragment newInstance(long orderId) {
		OrderDetailFragment page = new OrderDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putLong("id", orderId);
		page.setArguments(bundle);
		return page;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated called");
		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		//getLoaderManager().initLoader(0, null, this);

	}
	
	public void updateOrder(long orderId) {
		//
	}

	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub

	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = (View)inflater.inflate(R.layout.fragment_order_detail, container, false);
		
		TextView t = (TextView)v.findViewById(R.id.current_page);
		t.setText(getArguments().getLong("id") + " order");
		
		return v;
	}
	
	

}
