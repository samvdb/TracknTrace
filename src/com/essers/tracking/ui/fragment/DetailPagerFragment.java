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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.essers.tracking.R;
import com.essers.tracking.model.provider.TrackingContract.Orders;
import com.essers.tracking.util.MyDetailPagerAdapter;

/**
 * This fragment holds a {@link ViewPager} to display Order Details.
 * The user will be able to swipe through all the orders from left to right.
 * Whenever another {@link Fragment} wants to update the users selected Order he must call {@link #updateOrder} with a valid OrderID.
 * 
 * This class already uses android.support package.
 * @author Sam
 *
 */
public class DetailPagerFragment extends Fragment  implements
		LoaderManager.LoaderCallbacks<Cursor> {
	
	private static final String TAG = "DetailPagerFragment";

	private ViewPager mViewPager;
	private MyDetailPagerAdapter mMyDetailPagerAdapter;
	private long currentOrderId = -1;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		currentOrderId = getActivity().getIntent().getLongExtra("order_id", 0);
		setHasOptionsMenu(true);
		//getActivityHelper().setupActionBar(getTitle(), 0);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_order_viewpager, container);
	}
	
	/**
	 * Other {@link Fragment} or {@link Activity} can call this method when a new order has been selected by the user.
	 * The orderId must exist in the current cursor provider by the {@link ContentProvider}
	 * @param orderId
	 */
	public void updateOrder(long orderId) {
		Log.d(TAG, "updateOrder(orderid=" + orderId + ")");
		currentOrderId = orderId;
		mViewPager.setCurrentItem(mMyDetailPagerAdapter.getCursorPosition(currentOrderId));
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

	/**{@inheritDoc}*/
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri = Orders.CONTENT_URI;

		return new CursorLoader(getActivity(), baseUri,
				SmallOrdersQuery.PROJECTION, null, null, Orders.DEFAULT_SORT);
	}

	/**{@inheritDoc}*/
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onLoadFinished called");
		mMyDetailPagerAdapter.swapCursor(data);
		mViewPager.setCurrentItem(mMyDetailPagerAdapter.getCursorPosition(currentOrderId));
	}

	/**{@inheritDoc}*/
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		

	}
	
	

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.order_detail, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}



	/**
	 * Projection of the columns the {@link ContentProvider} has to return.
	 * @author Sam
	 *
	 */
	private interface SmallOrdersQuery {

		String[] PROJECTION = { BaseColumns._ID, Orders.ORDER_ID };
	}

}
