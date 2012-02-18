package com.essers.tracking.ui.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.essers.tracking.R;
import com.essers.tracking.model.provider.TrackingContract.Orders;

public class DetailPagerFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	
	private static final String TAG = "DetailPagerFragment";

	private ViewPager mViewPager;
	private MyDetailPagerAdapter mMyDetailPagerAdapter;
	private long currentOrderId = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, getActivity().getIntent().getLongExtra("order_id", -1) + "");
		currentOrderId = getActivity().getIntent().getLongExtra("order_id", 0);
		/*Log.d(TAG, savedInstanceState.getLong("order_id") + " order id");
		currentOrderId = savedInstanceState.getLong("order_id");*/

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_order_viewpager, container);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mViewPager = (ViewPager) getActivity().findViewById(
				R.id.order_viewpager);
		
		getLoaderManager().initLoader(0, null, this);
		
		mMyDetailPagerAdapter = new MyDetailPagerAdapter(getFragmentManager(), null);

		mViewPager.setAdapter(mMyDetailPagerAdapter);
	}

	private static class MyDetailPagerAdapter extends FragmentPagerAdapter {

		private static final String TAG = "MyDetailPagerAdapter";
		private Cursor mCursor;

		public MyDetailPagerAdapter(FragmentManager fm, Cursor cursor) {
			super(fm);
			mCursor = cursor;
		}

		public void swapCursor(Cursor cursor) {
			Log.d(TAG, "swapCursor(cursor=" + cursor.toString());
			mCursor = cursor;
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return OrderDetailFragment.newInstance(getOrderId(position));
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (mCursor == null) {
				return 0;
			}
			return mCursor.getCount();
		}

		public int getCursorPosition(long orderBaseId) {

			int idColumnIndex = mCursor.getColumnIndex(BaseColumns._ID);
			mCursor.moveToFirst();
			while (mCursor.moveToNext()) {
				if (mCursor.getLong(idColumnIndex) == orderBaseId) {
					return mCursor.getPosition();
				}
			}
			return -1;
		}

		public long getOrderId(int position) {

			int idColumnIndex = mCursor.getColumnIndex(BaseColumns._ID);
			mCursor.moveToPosition(position);
			return mCursor.getLong(idColumnIndex);

		}

	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri = Orders.CONTENT_URI;

		return new CursorLoader(getActivity(), baseUri,
				SmallOrdersQuery.PROJECTION, null, null, Orders.DEFAULT_SORT);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onLoadFinished called");
		mMyDetailPagerAdapter.swapCursor(data);
		mViewPager.setCurrentItem(mMyDetailPagerAdapter.getCursorPosition(currentOrderId));
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		

	}

	private interface SmallOrdersQuery {

		String[] PROJECTION = { BaseColumns._ID, Orders.ORDER_ID };
	}

}
