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
import com.essers.tracking.model.provider.TrackingContract.Order;

public class OrdersProcessor extends Processor {

	private static final String TAG = "OrdersProcessor";
	private boolean cleanDB = false;

	public OrdersProcessor() {
		super(TrackingContract.CONTENT_AUTHORITY);
	}
	
	public OrdersProcessor(boolean cleanDB) {
		super(TrackingContract.CONTENT_AUTHORITY);
		this.cleanDB = cleanDB;
	}

	@Override
	public ArrayList<ContentProviderOperation> parse(JSONObject parser,
			ContentResolver resolver) throws IOException, JSONException {

		ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

		Log.d(TAG, "Statuscode: " + parser.getInt("code"));

		JSONArray orders = getData(parser).getJSONArray("orders");
		
		

		for (int index = 0; index < orders.length(); index++) {
			// Log.d(TAG, "Next Object=" + orders.getJSONObject(index));
			
			
			
			JSONObject row = orders.getJSONObject(index);
			
			if (cleanDB) {
				Log.d(TAG, "Preparing delete of all orders");
				final ContentProviderOperation.Builder deleter = ContentProviderOperation.newDelete(Order.CONTENT_URI);
				deleter.withSelection(Order.Columns.CUSTOMER_ID + "=?", new String[] {(String)row.get("customer_id")} );
				batch.add(deleter.build());
				cleanDB = false;
			}

			JSONObject pickup = getPickUpAddress(row);
			JSONObject delivery = getDeliveryAddress(row);
			
			AddressProcessor processor = new AddressProcessor();;
			batch.addAll(processor.parseSingle(pickup, resolver));
			batch.addAll(processor.parseSingle(delivery, resolver));
			
			final ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(Order.CONTENT_URI);
			builder.withValue(Order.Columns.ORDER_ID, row.get("id"));
			builder.withValue(Order.Columns.CUSTOMER_ID, row.get("customer_id"));
			builder.withValue(Order.Columns.REFERENCE, row.get("reference"));
			builder.withValue(Order.Columns.STATE, row.get("state"));
			builder.withValue(Order.Columns.PICKUP_ADDRESS, row.get("pickUpAddress"));
			builder.withValue(Order.Columns.PICKUP_DATE, row.get("pickUpDate"));
			builder.withValue(Order.Columns.DELIVERY_ADDRESS, row.get("deliveryAddress"));
			builder.withValue(Order.Columns.DELIVERY_DATE, row.get("deliveryDate"));
			batch.add(builder.build());
			

		}

		// Clear any existing values for this speaker, treating the
		// incoming details as authoritative.
		// batch.add(ContentProviderOperation.newDelete(speakerUri).build());

		return batch;
	}

	private JSONObject getPickUpAddress(JSONObject obj) throws JSONException {
		JSONObject address = new JSONObject();
		address.put("address_id", obj.getString("pickUpAddress"));
		address.put("street", obj.getString("pickup_street"));
		address.put("housenumber", obj.getString("pickup_housenumber"));
		address.put("country", obj.getString("pickup_country"));
		address.put("zipcode", obj.getString("pickup_zipcode"));
		address.put("city", obj.getString("pickup_city"));
		return address;
	}

	private JSONObject getDeliveryAddress(JSONObject obj) throws JSONException {
		JSONObject address = new JSONObject();
		address.put("address_id", obj.getString("deliveryAddress"));
		address.put("street", obj.getString("delivery_street"));
		address.put("housenumber", obj.getString("delivery_housenumber"));
		address.put("country", obj.getString("delivery_country"));
		address.put("zipcode", obj.getString("delivery_zipcode"));
		address.put("city", obj.getString("delivery_city"));
		return address;
	}

}
