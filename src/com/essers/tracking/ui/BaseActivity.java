package com.essers.tracking.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

public abstract class BaseActivity extends Activity {
	
	public final boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
