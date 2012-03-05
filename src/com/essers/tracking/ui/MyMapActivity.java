package com.essers.tracking.ui;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.essers.tracking.R;
import com.essers.tracking.model.provider.TrackingContract.DeliveryColumns;
import com.essers.tracking.model.provider.TrackingContract.Gps;
import com.essers.tracking.model.provider.TrackingContract.Orders;
import com.essers.tracking.model.provider.TrackingContract.PickupColumns;
import com.essers.tracking.model.service.ServiceHelper;
import com.essers.tracking.model.service.SyncService;
import com.essers.tracking.ui.fragment.OrderListFragment.RecentOrdersQuery;
import com.essers.tracking.util.MyOverlays;
import com.essers.tracking.util.MyResultReceiver;
import com.essers.tracking.util.WebserviceHelper;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyMapActivity extends MapActivity implements
		MyResultReceiver.Receiver, LoaderManager.LoaderCallbacks<Cursor> {

	private static final String TAG = "MyMapActivity";

	private MapController mMapController;
	private MapView mMapView;
	private MyOverlays mOverlays;
	private MyResultReceiver mReceiver;
	private ProgressDialog mProgress;
	private Cursor cursor;

	public static final int GPS_TOKEN = 905;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_detail_map);

		registerReceiver();

		prepareMapView();

		Bundle bundle = this.getIntent().getExtras();
		String orderId = bundle.getString("order_id");

		getLatestGps(orderId);

	}

	private void getLatestGps(String id) {
		String url = WebserviceHelper.prepareCall(
				this.getString(R.string.remote_get_gps), new String[] { id });
		ServiceHelper.execute(this, mReceiver, GPS_TOKEN, url);

	}

	private void updateMap(double lat, double lng) {

		mMapController = mMapView.getController();

		Drawable drawable = getResources().getDrawable(R.drawable.marker);
		mOverlays = new MyOverlays(drawable);

		GeoPoint p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

		mMapController.animateTo(p);

		mMapController.setZoom(17);
		createMarker(p);
		mMapView.invalidate();
	}

	private void prepareMapView() {
		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setSatellite(true);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void createMarker(GeoPoint p) {
		OverlayItem overlayitem = new OverlayItem(p, "", "");
		mOverlays.addOverlay(overlayitem);
		mMapView.getOverlays().add(mOverlays);
	}

	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.d(TAG, "onReceiveResult(resultCode=" + resultCode + ", resultData="
				+ resultData.toString());

		switch (resultCode) {
		case SyncService.STATUS_RUNNING:

			mProgress = ProgressDialog.show(this, "",
					this.getText(R.string.loading_map), true);
			break;
		case SyncService.STATUS_FINISHED:

			mProgress.dismiss();
			getLoaderManager().initLoader(0, null,
					(LoaderCallbacks<Cursor>) this);
			break;
		case SyncService.STATUS_ERROR:

			mProgress.dismiss();
			Log.e(TAG, "Error fetching GPS info");
			break;

		}

	}

	public void registerReceiver() {
		mReceiver = new MyResultReceiver(new Handler());
		mReceiver.setReceiver(this);

	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri = Gps.CONTENT_URI;

		return new CursorLoader(this, baseUri,
				GpsQuery.PROJECTION, null, null, Gps.DEFAULT_SORT);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onLoadFinished called");
		cursor = data;
		processCursor(data);

	}

	private void processCursor(Cursor data) {
		if (data.moveToFirst()) {
			double lat = data.getDouble(data.getColumnIndex(Gps.LATITUDE));
			double lng = data.getDouble(data.getColumnIndex(Gps.LONGITUDE));
			this.updateMap(lat, lng);
		} else {
			Log.d(TAG, "Cursor was empty");
		}
		
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		cursor = null;
	}
	
	public interface GpsQuery {

		String[] PROJECTION = { BaseColumns._ID, Gps.ORDER_ID, Gps.LATITUDE, Gps.LONGITUDE};
	}
}
