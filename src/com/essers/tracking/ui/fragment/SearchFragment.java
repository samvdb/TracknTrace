package com.essers.tracking.ui.fragment;

import java.util.Calendar;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.essers.tracking.R;
import com.essers.tracking.model.provider.TrackingContract.Order;
import com.essers.tracking.model.service.ServiceHelper;
import com.essers.tracking.ui.OrderDetailActivity;
import com.essers.tracking.ui.SearchResultActivity;
import com.essers.tracking.util.MyResultReceiver;
import com.essers.tracking.util.WebserviceHelper;

public class SearchFragment extends Fragment implements MyResultReceiver.Receiver {
	
	private static final String TAG = "SearchFragment";
	
	private MyResultReceiver mReceiver;
	private int lastPageRequest = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_search, container);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		Button searchButton = (Button)getActivity().findViewById(R.id.search_button);
		searchButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				search();
				
			}
			
		});
		
		
		
	}
	
	private void search() {
		
		String orderId = getOrderId();
		Log.d(TAG, "search(order_id=" + orderId + ")");
		
		if (orderId.equals("")) {
			Log.d(TAG, "there is no order ID specified to search for");
		}
		
		String url = WebserviceHelper.prepareCall(
				this.getString(R.string.remote_search_by_order),
				new String[] { orderId, String.valueOf(lastPageRequest) });
		if (lastPageRequest == 1) {
			ServiceHelper.execute(getActivity(), mReceiver, Order.PATH_FOR_CUSTOMER_ID_CLEAR_TOKEN, url);
		} else {
			ServiceHelper.execute(getActivity(), mReceiver, Order.PATH_FOR_CUSTOMER_ID_TOKEN, url);
		}
		
		OrderListFragment listView = (OrderListFragment) getActivity().getFragmentManager().findFragmentById(R.id.fragment_order_list);
		
		if (listView == null || !listView.isInLayout()) {
			Intent showList = new Intent(getActivity(), SearchResultActivity.class);
			startActivity(showList);
		} else {
			//listView.
		}


		
	}
	
	private String getOrderId() {
		
		EditText orderId = (EditText)getActivity().findViewById(R.id.search_order_id);
		return orderId.getText().toString();
	}

	public void updatePickupDate(Calendar date) {
		Log.v(TAG, "updatePickupDate(date=" + date + ")");
	}

	public void onReceiveResult(int resultCode, Bundle resultData) {
		// TODO Auto-generated method stub
		
	}

	public void registerReceiver() {
		mReceiver = new MyResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		
	}
	
	
	
	

}
