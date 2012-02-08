package com.essers.tracking.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;


public class OrderListFragment extends ListFragment {
	
	private ListListener mListListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initListData(savedInstanceState);
		
		mListListener = (ListListener) getActivity();
	}

	private void initListData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}

	public interface ListListener {
		public void onListItemSelected(String orderId);
		public void onLastIndexReached();
	}

}
