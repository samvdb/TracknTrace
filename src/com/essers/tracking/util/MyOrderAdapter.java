package com.essers.tracking.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.essers.tracking.R;
import com.essers.tracking.model.provider.TrackingContract.Order;

public class MyOrderAdapter extends CursorAdapter {
	
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
		
		t.setText(c.getString(c.getColumnIndex(Order.Columns.PICKUP_DATE)));
		t = (TextView) arg0.findViewById(R.id.item_pickup_date);
		
		t.setText(c.getString(c.getColumnIndex(Order.Columns.DELIVERY_DATE)));
		t = (TextView) arg0.findViewById(R.id.item_delivery_date);
		
		TypedArray states = arg1.getResources().obtainTypedArray(R.array.order_state);
		int state = c.getInt(c.getColumnIndex(Order.Columns.STATE));
		t.setText(states.getText(state));

		ImageView v = (ImageView)arg0.findViewById(R.id.item_order_state);
		v.setImageLevel(state);
		
		v = (ImageView)arg0.findViewById(R.id.item_order_problem);
		v.setImageLevel(0);
		

		
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup parent) {
		final View view = LayoutInflater.from(arg0).inflate(R.layout.list_item_order, parent, false);
		return view;
	}
	
}