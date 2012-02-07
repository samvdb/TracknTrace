package com.essers.tracking.ui.phone;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.essers.tracking.ui.BaseSinglePaneActivity;

public class SearchActivity extends BaseSinglePaneActivity {

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
	
	

}
