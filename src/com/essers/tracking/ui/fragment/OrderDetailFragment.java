package com.essers.tracking.ui.fragment;

import java.sql.Timestamp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.essers.tracking.R;
import com.essers.tracking.model.provider.TrackingContract.Addresses;
import com.essers.tracking.model.provider.TrackingContract.Orders;
import com.essers.tracking.ui.MyMapActivity;
import com.essers.tracking.util.LocalActivityManagerFragment;
import com.essers.tracking.util.UIUtils;

public class OrderDetailFragment extends LocalActivityManagerFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final String TAG = "OrderDetailFragment";

	private static final int ORDER_LOADER = 1;
	private static final int PICKUP_LOADER = 2;
	private static final int DELIVERY_LOADER = 3;

	private static final String TAG_TEXT = "text";
	private static final String TAG_MAP = "map";

	private ViewGroup mRootView;
	private Cursor mOrderCursor;
	private Cursor mPickupCursor;
	private Cursor mDeliveryCursor;
	
	private String orderId;

	private TabHost mTabHost;

	public static OrderDetailFragment newInstance(String orderId) {
		OrderDetailFragment page = new OrderDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putString("order_id", orderId);
		page.setArguments(bundle);
		return page;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.orderId = this.getArguments().getString("order_id");
		Log.d(TAG, "onCreate: orderId=" + this.orderId);

	}

	private void setupTabs() {

		mTabHost.addTab(mTabHost.newTabSpec(TAG_TEXT)
				.setIndicator(getString(R.string.menu_detail_text))
				.setContent(R.id.tab_detail_text));
		
		Intent mapIntent = new Intent(getActivity(), MyMapActivity.class);
		mapIntent.putExtra("order_id", orderId);
		
		mTabHost.addTab(mTabHost.newTabSpec(TAG_MAP)
				.setIndicator(getString(R.string.menu_detail_map))
				.setContent(mapIntent));

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated called");

		mTabHost = (TabHost) mRootView.findViewById(android.R.id.tabhost);
		mTabHost.setup(getLocalActivityManager());

		setupTabs();

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(ORDER_LOADER, getArguments(), this);

	}

	public void updateOrder(long orderId) {
		//
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		// TODO Auto-generated method stub
		switch (id) {
		case ORDER_LOADER: {

			String orderId = arg1.getString("order_id");
			Uri baseUri = Orders.buildOrdersUri(orderId);

			return new CursorLoader(getActivity(), baseUri,
					OrderQuery.PROJECTION, null, null, null);
		}
		case PICKUP_LOADER: {
			String addressId = arg1.getString("pickup_id");
			Uri baseUri = Addresses.buildAddressUri(addressId);
			return new CursorLoader(getActivity(), baseUri,
					AddressQuery.PROJECTION, null, null, null);
		}
		case DELIVERY_LOADER: {
			String addressId = arg1.getString("delivery_id");
			Uri baseUri = Addresses.buildAddressUri(addressId);
			return new CursorLoader(getActivity(), baseUri,
					AddressQuery.PROJECTION, null, null, null);
		}
		default:
			return null;
		}
	}

	/**
	 * Start new queries to fetch the address information. This should only
	 * happen when the order was found.
	 * 
	 * {@inheritDoc}
	 */
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case ORDER_LOADER: {
			onOrderLoadComplete(data);
			break;
		}
		case PICKUP_LOADER: {

			onPickupLoadComplete(data);
			break;
		}
		case DELIVERY_LOADER: {
			onDeliveryLoadComplete(data);
			break;
		}
		}

	}

	private void onDeliveryLoadComplete(Cursor data) {

		mDeliveryCursor = data;
		data.moveToFirst();
		updateAddressView(data, R.id.include_delivery, Orders.DELIVERY_DATE);
	}

	private void onPickupLoadComplete(Cursor data) {

		mPickupCursor = data;
		mPickupCursor.moveToFirst();
		updateAddressView(data, R.id.include_pickup, Orders.PICKUP_DATE);
	}

	private void onOrderLoadComplete(Cursor data) {

		if (data.getCount() != 1) {
			onOrderLoadError();
			return;
		}

		mOrderCursor = data;
		data.moveToFirst();

		String pickup = data.getString(data
				.getColumnIndex(Orders.PICKUP_ADDRESS));
		String delivery = data.getString(data
				.getColumnIndex(Orders.DELIVERY_ADDRESS));

		Bundle bundle = new Bundle();
		bundle.putString("pickup_id", pickup);
		bundle.putString("delivery_id", delivery);

		getLoaderManager().initLoader(PICKUP_LOADER, bundle, this);
		getLoaderManager().initLoader(DELIVERY_LOADER, bundle, this);
		updateOrderView();

	}

	private void updateOrderView() {

		String reference = mOrderCursor.getString(mOrderCursor
				.getColumnIndex(Orders.REFERENCE));
		String orderId = mOrderCursor.getString(mOrderCursor
				.getColumnIndex(Orders.ORDER_ID));
		int pickupdate = mOrderCursor.getInt(mOrderCursor
				.getColumnIndex(Orders.PICKUP_DATE));
		int deliverydate = mOrderCursor.getInt(mOrderCursor
				.getColumnIndex(Orders.DELIVERY_DATE));
		int state = mOrderCursor.getInt(mOrderCursor
				.getColumnIndex(Orders.STATE));
		int problem = mOrderCursor.getInt(mOrderCursor
				.getColumnIndex(Orders.PROBLEM));
		String description = mOrderCursor.getString(mOrderCursor
				.getColumnIndex(Orders.PROBLEM_DESCRIPTION));

		UIUtils.setText(R.id.detail_reference, reference, mRootView);
		UIUtils.setText(R.id.detail_order_id, orderId, mRootView);

		// Address - pickup subview
		ViewGroup container = (ViewGroup) mRootView
				.findViewById(R.id.include_pickup);
		UIUtils.setDate(R.id.detail_content_date, pickupdate, container);
		UIUtils.setTime(R.id.detail_content_time, pickupdate, container);

		// Address - delivery subview
		container = (ViewGroup) mRootView.findViewById(R.id.include_delivery);
		UIUtils.setDate(R.id.detail_content_date, deliverydate, container);
		UIUtils.setTime(R.id.detail_content_time, deliverydate, container);

		UIUtils.setLevel(R.id.detail_order_state, state, mRootView);
		UIUtils.setLevel(R.id.detail_order_problem, problem, mRootView);

		UIUtils.setStateDescription(getActivity(),
				R.id.detail_state_description, state, mRootView);
		if (problem == 1) {
			UIUtils.setText(R.id.detail_problem_description, description,
					mRootView);
		}

		UIUtils.setStateBackground(state, R.id.detail_state_layout, mRootView);

	}

	private void updateAddressView(Cursor cursor, int resource, String dateField) {

		String name = cursor.getString(cursor.getColumnIndex(Addresses.NAME));
		String city = cursor.getString(cursor.getColumnIndex(Addresses.CITY));
		String number = cursor.getString(cursor
				.getColumnIndex(Addresses.HOUSENUMBER));
		String street = cursor.getString(cursor
				.getColumnIndex(Addresses.STREET));
		String zip = cursor.getString(cursor.getColumnIndex(Addresses.ZIPCODE));
		String country = cursor.getString(cursor
				.getColumnIndex(Addresses.COUNTRY));
		int date = mOrderCursor.getInt(mOrderCursor.getColumnIndex(dateField));

		ViewGroup container = (ViewGroup) mRootView.findViewById(resource);
		UIUtils.setDate(R.id.detail_content_date, date, container);
		UIUtils.setTime(R.id.detail_content_time, date, container);

		UIUtils.setText(R.id.detail_content_name, name, container);
		UIUtils.setText(R.id.detail_content_street, street + " " + number,
				container);
		UIUtils.setText(R.id.detail_content_city, country + " " + zip + " "
				+ city, container);

	}

	private void onOrderLoadError() {
		Log.e(TAG,
				"onOrderLoadError there was a problem finding the order in the database.");

	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		mOrderCursor = null;
		mPickupCursor = null;
		mDeliveryCursor = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mRootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_order_detail, null);
		return mRootView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Interface that defines all columns to get when requesting an order.
	 * 
	 * @author Sam
	 * 
	 */
	private interface OrderQuery {
		String[] PROJECTION = new String[] { Orders.ORDER_ID, Orders.REFERENCE,
				Orders._ID, Orders.DELIVERY_ADDRESS, Orders.DELIVERY_DATE,
				Orders.PICKUP_ADDRESS, Orders.PICKUP_DATE, Orders.PROBLEM,
				Orders.PROBLEM_DESCRIPTION, Orders.STATE };
	}

	private interface AddressQuery {
		String[] PROJECTION = new String[] { Addresses._ID,
				Addresses.ADDRESS_ID, Addresses.NAME, Addresses.CITY,
				Addresses.HOUSENUMBER, Addresses.STREET, Addresses.ZIPCODE,
				Addresses.COUNTRY };
	}

}
