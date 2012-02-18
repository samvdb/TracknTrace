package com.essers.tracking.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.essers.tracking.R;

public class OrderDetailActivity extends FragmentActivity {
	
	private static final String TAG = "OrderDetailActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, getIntent().getLongExtra("order_id", -1) + "");
		
		setContentView(R.layout.activity_order_detail);
		
	}

}
