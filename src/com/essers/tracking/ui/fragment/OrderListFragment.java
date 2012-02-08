package com.essers.tracking.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;


public class OrderListFragment extends ListFragment {
	
	private OnListItemSelectedListener listItemSelectedListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initListData(savedInstanceState);
		
		listItemSelectedListener = (OnListItemSelectedListener) getActivity();
	}

	private void initListData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}

	public interface OnListItemSelectedListener {
		public void onListItemSelected();
	}

}
