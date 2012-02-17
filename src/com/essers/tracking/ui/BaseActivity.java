package com.essers.tracking.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.util.Log;
import android.view.MenuItem;

import com.essers.tracking.R;
import com.essers.tracking.util.ActivityHelper;

public abstract class BaseActivity extends Activity {
	
	private static final String TAG = "BaseActivity";
	private final ActivityHelper mActivityHelper = ActivityHelper.createInstance(this);
	
	public final boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
	
	
    public void setupHomeActivity() {
      
        // NOTE: there needs to be a content view set before this is called, so this method
        // should be called in onPostCreate.
        if (isTablet(this)) {
            this.getActionBar().setDisplayOptions(
                    0,
                    ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        } else {
           this.getActionBar().setDisplayOptions(
                    ActionBar.DISPLAY_USE_LOGO,
                    ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_TITLE);
        }
    }
    
    
    public ActivityHelper getActivityHelper() {
    	return mActivityHelper;
    }


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//saveLogin(getUsername(), null);
	}
	
	public final void saveLogin(String username, String password) {
		Log.d(TAG, "saveLogin(username=" + username + ", password=" + password + ")");
		Editor e = getSharedPreferences("logininfo", Context.MODE_PRIVATE).edit();
		e.putString("username", username);
		e.putString("password", password);
		e.commit();
	}
	
	public final String getUsername() {
		SharedPreferences prefs = getSharedPreferences("logininfo", Context.MODE_PRIVATE);
		String username = prefs.getString("username", null);
		return username;
		
	}
	
	public final String getPassword() {
		SharedPreferences prefs = getSharedPreferences("logininfo", Context.MODE_PRIVATE);
		String password = prefs.getString("password", null);
		return password;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected called in BaseActivity");
		switch (item.getItemId()) {
		case R.id.menu_search:
			Intent intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_stop:
			saveLogin(getUsername(), null);
			Intent login = new Intent(this, LoginActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			finish();

		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	

}
