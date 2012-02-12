package com.essers.tracking.ui;

import android.content.Intent;
import android.os.Bundle;

public class HomeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent myIntent = new Intent(this, LoginActivity.class);
		startActivity(myIntent);
	}
	
	

}
