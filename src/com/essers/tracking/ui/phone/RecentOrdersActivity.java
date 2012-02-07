package com.essers.tracking.ui.phone;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.essers.tracking.ui.BaseSinglePaneActivity;

public class RecentOrdersActivity extends BaseSinglePaneActivity {

	@Override
	protected Fragment onCreatePane() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		getActivityHelper().setupSubActivity();
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
	}

}
