package com.essers.tracking.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class HomeActivity extends BaseActivity {
	
	private static final String TAG = "HomeActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent myIntent = new Intent(this, LoginActivity.class);
		startActivity(myIntent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		Log.d(TAG, "onDestroy called");
	}
	
	
	
	

}
