package com.essers.tracking.ui;

import java.util.Calendar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.essers.tracking.R;
import com.essers.tracking.ui.fragment.DateDialogFragment;
import com.essers.tracking.ui.fragment.DateDialogFragment.DateDialogFragmentListener;
import com.essers.tracking.ui.fragment.SearchFragment;

public class SearchActivity extends BaseActivity {
	
	private SearchFragment mSearchFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		getActivityHelper().setupActionBar(getTitle(), 0);
		
		/*mSearchFragment = (SearchFragment) getFragmentManager().findFragmentById(R.id.fragment_search);

		// create new DateDialogFragment
		DateDialogFragment ddf = DateDialogFragment.newInstance(this,
				R.string.search_pickup_date_title, Calendar.getInstance());

		ddf.setDateDialogFragmentListener(new DateDialogFragmentListener() {

			public void dateDialogFragmentDateSet(Calendar date) {
				// update the fragment
				mSearchFragment.updatePickupDate(date);
			}
		});*/

		//ddf.show(getFragmentManager(), "date picker dialog fragment");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	

}
