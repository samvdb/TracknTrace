package com.essers.tracking.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;

import com.essers.tracking.R;
import com.essers.tracking.ui.fragment.OrderListFragment.ListListener;

public class SearchResultActivity extends BaseActivity implements ListListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);

		getActivityHelper().setupActionBar(getTitle(), 0);


	}

	public void onListItemSelected(long orderBaseId) {
		// TODO Auto-generated method stub
		
	}

	public void onLastIndexReached() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_result, menu);
		return true;
	}

}
