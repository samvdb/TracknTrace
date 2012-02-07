package com.essers.tracking.util;

import com.essers.tracking.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityHelper {
	
	protected Activity mActivity;
	
	/**
	 * Factory method to create either a Honeycomb helper or older API helper.
	 * @param activity
	 * @return
	 */
	public static ActivityHelper createInstance(Activity activity) {
		return UIUtils.isHoneycomb() ? 
				new ActivityHelperHoneycomb(activity) : new ActivityHelper(activity);
	}
	
	protected ActivityHelper(Activity activity) {
        mActivity = activity;
    }
	
	/**
	 * The old actionbar is created here
	 * @param savedInstanceState
	 */
	public void onPostCreate(Bundle savedInstanceState) {
		// create action bar here
		throw new UnsupportedOperationException("Implement actionbar for SDK-11");
	}
	
	/**
     * Method, to be called in <code>onPostCreate</code>, that sets up this activity as a
     * sub-activity in the app.
     */
    public void setupSubActivity() {
    }

    /**
     * Returns the {@link ViewGroup} for the action bar on phones (compatibility action bar).
     * Can return null, and will return null on Honeycomb.
     */
    public ViewGroup getActionBarCompat() {
        return (ViewGroup) mActivity.findViewById(R.id.actionbar_compat);
    }

	

}
