package com.essers.tracking.ui.fragment;

import java.util.Calendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.essers.tracking.R;
import com.essers.tracking.model.service.ServiceHelper;
import com.essers.tracking.model.service.SyncService;
import com.essers.tracking.ui.SearchResultActivity;
import com.essers.tracking.util.MyResultReceiver;
import com.essers.tracking.util.UIUtils;
import com.essers.tracking.util.WebserviceHelper;

public class SearchFragment extends Fragment implements
		MyResultReceiver.Receiver {

	private static final String TAG = "SearchFragment";

	public static final int SEARCH_TOKEN = 505;

	private MyResultReceiver mReceiver;
	private int lastPageRequest = 1;
	private ProgressDialog mProgressDialog; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_search, container);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		registerReceiver();
		setHasOptionsMenu(true);

		Button searchButton = (Button) getActivity().findViewById(
				R.id.search_button);
		searchButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				search();

			}

		});

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.search, menu);
	}

	private void search() {

		String orderId = getOrderId();
		Log.d(TAG, "search(order_id=" + orderId + ")");

		if (orderId.equals("")) {
			Log.d(TAG, "there is no order ID specified to search for");
			UIUtils.showToast(getActivity(), R.string.error_search_no_id);
			return;
		}

		String url = WebserviceHelper.prepareCall(
				this.getString(R.string.remote_search_by_order), new String[] {
						orderId, String.valueOf(lastPageRequest) });

		ServiceHelper.execute(getActivity(), mReceiver, SEARCH_TOKEN, url);
	}

	private String getOrderId() {

		EditText orderId = (EditText) getActivity().findViewById(
				R.id.search_order_id);
		return orderId.getText().toString();
	}

	public void updatePickupDate(Calendar date) {
		Log.v(TAG, "updatePickupDate(date=" + date + ")");
	}

	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.d(TAG, "onReceiveResult(resultCode=" + resultCode + ", resultData="
				+ resultData.toString());

		switch (resultCode) {
		case SyncService.STATUS_RUNNING:

			mProgressDialog = ProgressDialog.show(getActivity(), getActivity().getString(R.string.title_search), getActivity().getString(R.string.search_searching));

			break;
		case SyncService.STATUS_FINISHED:

			mProgressDialog.dismiss();
			launchList();

			break;
		case SyncService.STATUS_ERROR:

			mProgressDialog.dismiss();
			UIUtils.showToast(getActivity(), R.string.search_nothing_found);
			break;

		}

	}

	private void launchList() {
		OrderListFragment listView = (OrderListFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(
						R.id.fragment_order_list);

		if (listView == null || !listView.isInLayout()) {
			Intent showList = new Intent(getActivity(),
					SearchResultActivity.class);
			
			Bundle b = new Bundle();
			b.putString("order_id", getOrderId());
			showList.putExtras(b);
			startActivity(showList);
		} else {
			// listView.
		}
		
	}

	public void registerReceiver() {
		mReceiver = new MyResultReceiver(new Handler());
		mReceiver.setReceiver(this);

	}

}
