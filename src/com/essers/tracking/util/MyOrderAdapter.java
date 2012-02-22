package com.essers.tracking.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.essers.tracking.R;
import com.essers.tracking.model.provider.TrackingContract.DeliveryColumns;
import com.essers.tracking.model.provider.TrackingContract.Orders;
import com.essers.tracking.model.provider.TrackingContract.PickupColumns;

public class MyOrderAdapter extends CursorAdapter {

	private Cursor mCursor;
	private Context mContext;

	public MyOrderAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor c) {

		int pdate = c.getInt(c.getColumnIndex(Orders.PICKUP_DATE));
		int ddate = c.getInt(c.getColumnIndex(Orders.DELIVERY_DATE));

		UIUtils.setText(R.id.item_order_id,
				c.getString(c.getColumnIndex(Orders.ORDER_ID)), arg0);
		UIUtils.setDate(R.id.item_delivery_date, ddate, arg0);
		UIUtils.setDate(R.id.item_pickup_date, pdate, arg0);
		UIUtils.setText(R.id.item_delivery_name,
				c.getString(c.getColumnIndex(DeliveryColumns.NAME)), arg0);
		UIUtils.setText(R.id.item_pickup_name,
				c.getString(c.getColumnIndex(PickupColumns.NAME)), arg0);
		UIUtils.setText(R.id.item_reference,
				c.getString(c.getColumnIndex(Orders.REFERENCE)), arg0);

		String pAddress = c.getString(c.getColumnIndex(PickupColumns.COUNTRY))
				+ " " + c.getString(c.getColumnIndex(PickupColumns.ZIPCODE))
				+ " " + c.getString(c.getColumnIndex(PickupColumns.CITY));
		
		String dAddress = c.getString(c.getColumnIndex(DeliveryColumns.COUNTRY))
				+ " " + c.getString(c.getColumnIndex(DeliveryColumns.ZIPCODE))
				+ " " + c.getString(c.getColumnIndex(DeliveryColumns.CITY));
		
		UIUtils.setText(R.id.item_pickup_address, pAddress, arg0);
		UIUtils.setText(R.id.item_delivery_address, dAddress, arg0);

		int state = c.getInt(c.getColumnIndex(Orders.STATE));

		ImageView v = (ImageView) arg0.findViewById(R.id.item_order_state);
		v.setImageLevel(state);

		UIUtils.setStateBackground(state, R.id.order_list_layout, arg0);

		int prob = c.getInt(c.getColumnIndex(Orders.PROBLEM));
		v = (ImageView) arg0.findViewById(R.id.item_order_problem);
		v.setImageLevel(prob);

	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup parent) {
		View view = LayoutInflater.from(arg0).inflate(R.layout.list_item_order,
				parent, false);
		return view;
	}

}