package com.essers.tracking.ui.helper;

import com.essers.tracking.R;
import com.essers.tracking.ui.OrdersFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
public class MyActionTab {
	
	private Activity mActivity;
	
	public MyActionTab(Activity activity) {
		mActivity = activity;
	}
	
	public void createActionTab(Activity activity) {
			
		final ActionBar bar = activity.getActionBar();
		createTabs(bar);
		
		if (bar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS) {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
        } else {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        }	
	}
	
	private void createTabs(ActionBar bar) {
		
		bar.addTab(bar.newTab()
				.setText(R.string.title_recent_orders)
				.setTabListener(new TabListener(new OrdersFragment()))
				);	
	}

	private class TabListener implements ActionBar.TabListener {
		
		private Fragment mFragment;
		
		public TabListener(Fragment fragment) {
			mFragment = fragment;
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.add(R.id.fragment_content, mFragment, mFragment.getTag());
			
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(mFragment);
			
		}

		
	}

}
