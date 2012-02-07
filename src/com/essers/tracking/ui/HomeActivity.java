package com.essers.tracking.ui;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.essers.tracking.R;
import com.essers.tracking.ui.phone.RecentOrdersActivity;
import com.essers.tracking.ui.tablet.RecentOrdersMultiPaneActivity;
import com.essers.tracking.util.UIUtils;

public class HomeActivity extends BaseActivity {
	
	private static final String TAG = "HomeActivity";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		//testTabs();
		launchRecentOrders(arg0);
		
	}

	private void testTabs() {
		setContentView(R.layout.action_bar_tabs);

		
	}

	private void launchRecentOrders(Bundle arg0) {
		
		Intent intent = null;
		
		if (UIUtils.isHoneycombTablet(this)) {
			intent = new Intent(this, RecentOrdersMultiPaneActivity.class);
		} else {
			intent = new Intent(this, RecentOrdersActivity.class);
		}
		Log.d(TAG, "starting activity: " + intent.toString());
		startActivity(intent);
		
	}
	
	public void onAddTab(View v) {
        final ActionBar bar = getActionBar();
        final int tabCount = bar.getTabCount();
        final String text = "Tab " + tabCount;
        bar.addTab(bar.newTab()
                .setText(text)
                .setTabListener(new TabListener(new TabContentFragment(text))));
    }

    public void onRemoveTab(View v) {
        final ActionBar bar = getActionBar();
        bar.removeTabAt(bar.getTabCount() - 1);
    }

    public void onToggleTabs(View v) {
        final ActionBar bar = getActionBar();

        if (bar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS) {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
        } else {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        }
    }

    public void onRemoveAllTabs(View v) {
        getActionBar().removeAllTabs();
    }
	
	private class TabListener implements ActionBar.TabListener {
        private TabContentFragment mFragment;

        public TabListener(TabContentFragment fragment) {
            mFragment = fragment;
        }

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            ft.add(R.id.fragment_content, mFragment, mFragment.getText());
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            ft.remove(mFragment);
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            Toast.makeText(HomeActivity.this, "Reselected!", Toast.LENGTH_SHORT).show();
        }

		public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

    }

    private class TabContentFragment extends Fragment {
        private String mText;

        public TabContentFragment(String text) {
            mText = text;
        }

        public String getText() {
            return mText;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View fragView = inflater.inflate(R.layout.action_bar_tab_content, container, false);

            TextView text = (TextView) fragView.findViewById(R.id.text);
            text.setText(mText);

            return fragView;
        }

    }
	
	
	
	
	/** Called when the activity is first created. */
	/**@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		OrdersFragment fr = new OrdersFragment();
		fragmentTransaction.add(R.id.fragment1, fr);
		fragmentTransaction.commit();

		triggerRefresh();
	}

	private void triggerRefresh() {

		final Intent syncIntent = new Intent(Intent.ACTION_SYNC, null, this,
				SyncService.class);
		syncIntent.putExtra("api_url",
				"http://apify.hibernia.be/customer/cust1/orders.json");
		syncIntent.putExtra("token", Order.PATH_TOKEN);
		startService(syncIntent);
	}

	public interface RecentOrdersQuery {
		int TOKEN = 2;

		String[] PROJECTION = { 
				BaseColumns._ID,
				Order.Columns.ORDER_ID,
				Order.Columns.REFERENCE, Order.Columns.STATE };

		int ORDER_ID = 0;
		int REFERENCE = 1;
		int STATE = 2;
	}*/
}