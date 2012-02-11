package com.essers.tracking.ui.fragment;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import com.essers.tracking.R;
import com.essers.tracking.model.provider.TrackingContract;
import com.essers.tracking.model.provider.TrackingContract.Order;

public class OrderListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final String TAG = "OrderListFragment";
	private ListListener mListListener;
	private Cursor mCursor;
	private SimpleCursorAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// initListData(savedInstanceState);

		mListListener = (ListListener) getActivity();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.list_item_order, null, new String[] {
						Order.Columns.ORDER_ID, Order.Columns.REFERENCE,
						Order.Columns.STATE }, new int[] { R.id.textView1,
						R.id.textView2, R.id.textView3 }, 0);

		setListAdapter(mAdapter);
		setListShown(false);
		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);

	}

	public interface ListListener {
		public void onListItemSelected(String orderId);

		public void onLastIndexReached();
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		//Uri baseUri = Uri.parse(args.getString("content_uri"));
		Uri baseUri = TrackingContract.Order.CONTENT_URI;
		return new CursorLoader(getActivity(), baseUri,
				RecentOrdersQuery.PROJECTION, null, null, null);

	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in. (The framework will take care of closing the
		// old cursor once we return.)
		
		Log.d(TAG, "onLoadFinished called");
		mAdapter.swapCursor(data);

		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}

	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
		Log.d(TAG, "onLoaderReset called");
        mAdapter.swapCursor(null);

	}

	private interface RecentOrdersQuery {
		int TOKEN = 2;

		String[] PROJECTION = { BaseColumns._ID, Order.Columns.ORDER_ID,
				Order.Columns.REFERENCE, Order.Columns.STATE };

		int ORDER_ID = 0;
		int REFERENCE = 1;
		int STATE = 2;
	}

}
