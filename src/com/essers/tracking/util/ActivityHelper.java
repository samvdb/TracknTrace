package com.essers.tracking.util;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

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
	
	public void setupHomeActivity() {
	      
        // NOTE: there needs to be a content view set before this is called, so this method
        // should be called in onPostCreate.
        if (isTablet(mActivity)) {
        	mActivity.getActionBar().setDisplayOptions(
                    0,
                    ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        } else {
        	mActivity.getActionBar().setDisplayOptions(
                    ActionBar.DISPLAY_USE_LOGO,
                    ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_TITLE);
        }
    }

	public final boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
	
	
	
}
