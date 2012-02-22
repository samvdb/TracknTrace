package com.essers.tracking.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.essers.tracking.R;
import com.essers.tracking.util.MyOverlays;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyMapActivity extends MapActivity {

	private MapController mMapController;
	private MapView mMapView;
	private MyOverlays mOverlays;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.tab_detail_map);

		prepareMapView();

		mMapController = mMapView.getController();
		
		
		
		
		Drawable drawable = getResources().getDrawable(R.drawable.marker);
		mOverlays = new MyOverlays(drawable);

		String coordinates[] = { "50.903845", "4.491076" };
		double lat = Double.parseDouble(coordinates[0]);
		double lng = Double.parseDouble(coordinates[1]);

		GeoPoint p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		List<Address> addresses = new ArrayList<Address>();
		try {
			addresses = geoCoder.getFromLocationName(
					"pals 22, 3202 rillaar belgie", 5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GeoPoint pp;
		String add = "";
		if (addresses.size() > 0) {
			pp = new GeoPoint((int) (addresses.get(0).getLatitude() * 1E6),
					(int) (addresses.get(0).getLongitude() * 1E6));

			
			mMapController.animateTo(pp);

			mMapController.setZoom(17);
			createMarker(pp);
			mMapView.invalidate();
		}

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
}
