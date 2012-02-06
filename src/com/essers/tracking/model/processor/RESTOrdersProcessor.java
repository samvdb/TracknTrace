package com.essers.tracking.model.processor;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.util.Log;

import com.essers.tracking.model.provider.TrackingContract;

public class RESTOrdersProcessor extends Processor{
	
	private static final String TAG = "OrdersProcessor";

	public RESTOrdersProcessor() {
		super(TrackingContract.CONTENT_AUTHORITY);
	}

	@Override
	protected ArrayList<ContentProviderOperation> parse(JSONObject data,
			ContentResolver resolver) throws JSONException, IOException {
		Log.d(TAG, "Response code: " + data.getInt("code"));
		JSONArray orders = data.getJSONObject("data").getJSONArray("orders");
		
		for (int i=0; i < orders.length(); i++) {
			JSONObject order = (JSONObject) orders.get(i);
			Log.d(TAG, "Order: " + order.toString());
		}
		
		return null;
	}

}
