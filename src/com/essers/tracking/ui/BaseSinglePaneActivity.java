package com.essers.tracking.ui;

import com.essers.tracking.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class BaseSinglePaneActivity extends BaseActivity {
	
	private Fragment mFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_singlepane_empty);
       // getActivityHelper().setupActionBar(getTitle(), 0);


        if (arg0 == null) {
            mFragment = onCreatePane();
            mFragment.setArguments(intentToFragmentArguments(getIntent()));

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.root_container, mFragment)
                    .commit();
        }
	}
	
	 /**
     * Called in <code>onCreate</code> when the fragment constituting this activity is needed.
     * The returned fragment's arguments will be set to the intent used to invoke this activity.
     */
    protected abstract Fragment onCreatePane();
	
	

}
