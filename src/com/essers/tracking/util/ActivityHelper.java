package com.essers.tracking.util;

import android.app.Activity;

public class ActivityHelper {
	
	protected Activity mActivity;
	
	protected ActivityHelper(Activity activity) {
		mActivity = activity;
	}
	
	public static ActivityHelper createInstance(Activity activity) {
		return new ActivityHelper(activity);
	}
	
	public void setupActionBar(CharSequence title, int color) {
		
	}

}
