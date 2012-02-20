package com.essers.tracking.util;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.essers.tracking.model.provider.TrackingContract.Orders;
import com.essers.tracking.ui.fragment.OrderDetailFragment;

/**
 * A PagerAdapter for the detail views. It requires a {@link Cursor} to sync its position.
 * @author Sam
 *
 */
public class MyDetailPagerAdapter extends FragmentPagerAdapter {

	private Cursor mCursor;

	public MyDetailPagerAdapter(FragmentManager fm, Cursor cursor) {
		super(fm);
		mCursor = cursor;
	}

	public void swapCursor(Cursor cursor) {
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

	public String getOrderId(int position) {

		int idColumnIndex = mCursor.getColumnIndex(Orders.ORDER_ID);
		mCursor.moveToPosition(position);
		return mCursor.getString(idColumnIndex);

	}

}
