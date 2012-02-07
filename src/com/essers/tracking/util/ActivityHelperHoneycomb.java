package com.essers.tracking.util;


import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class ActivityHelperHoneycomb extends ActivityHelper {
	
	protected ActivityHelperHoneycomb(Activity activity) {
        super(activity);
    }

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		// Do nothing since we already have support for it.
	}
	
	/** {@inheritDoc} */
    @Override
    public void setupSubActivity() {
        super.setupSubActivity();
        // NOTE: there needs to be a content view set before this is called, so this method
        // should be called in onPostCreate.
        if (UIUtils.isTablet(mActivity)) {
            mActivity.getActionBar().setDisplayOptions(
                    ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO,
                    ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);
        } else {
            mActivity.getActionBar().setDisplayOptions(
                    0,
                    ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO);
        }
    }

	
	

}
