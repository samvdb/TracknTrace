package com.essers.tracking.model.processor;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.essers.tracking.model.provider.TrackingContract;
import com.essers.tracking.model.provider.TrackingContract.Gps;
import com.essers.tracking.model.provider.TrackingContract.Orders;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;

public class GpsProcessor extends Processor {

	public GpsProcessor() {
		super(TrackingContract.CONTENT_AUTHORITY);
	}

	@Override
	public ArrayList<ContentProviderOperation> parse(JSONObject parser,
			ContentResolver resolver) throws IOException, JSONException {
		
		ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
		ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(Gps.CONTENT_URI);
		batch.add(builder.build());
		
		
		
		JSONArray coords = getData(parser).getJSONArray("gps");
		for (int index = 0; index < coords.length(); index++) {
			// Log.d(TAG, "Next Object=" + orders.getJSONObject(index));

			JSONObject row = coords.getJSONObject(index);

			builder = ContentProviderOperation
					.newInsert(Gps.CONTENT_URI);
			builder.withValue(Gps.ORDER_ID, row.get("order_id"));
			builder.withValue(Gps.LATITUDE, row.get("latitude"));
			builder.withValue(Gps.LONGITUDE, row.get("longitude"));
			batch.add(builder.build());

		}
		
		return batch;
	}

}
