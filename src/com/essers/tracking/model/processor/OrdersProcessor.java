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
import com.essers.tracking.model.provider.TrackingContract.Orders;

public class OrdersProcessor extends Processor {

	private static final String TAG = "OrdersProcessor";


	public OrdersProcessor() {
		super(TrackingContract.CONTENT_AUTHORITY);
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
			

			JSONObject pickup = getPickUpAddress(row);
			JSONObject delivery = getDeliveryAddress(row);
			
			AddressProcessor processor = new AddressProcessor();
			batch.addAll(processor.parseSingle(pickup, resolver));
			batch.addAll(processor.parseSingle(delivery, resolver));
			
			JSONObject customer = getCustomerInfo(row);
			CustomerProcessor processor2 = new CustomerProcessor();
			batch.addAll(processor2.parseSingle(customer, resolver));
			
			final ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(Orders.CONTENT_URI);
			builder.withValue(Orders.ORDER_ID, row.get("id"));
			builder.withValue(Orders.CUSTOMER_ID, row.get("customer_id"));
			builder.withValue(Orders.REFERENCE, row.get("reference"));
			builder.withValue(Orders.STATE, row.get("state"));
			builder.withValue(Orders.PICKUP_ADDRESS, row.get("pickup_id"));
			builder.withValue(Orders.PICKUP_DATE, row.get("pickUpDate"));
			builder.withValue(Orders.DELIVERY_ADDRESS, row.get("delivery_id"));
			builder.withValue(Orders.DELIVERY_DATE, row.get("deliveryDate"));
			builder.withValue(Orders.PROBLEM, row.get("problem"));
			builder.withValue(Orders.PROBLEM_DESCRIPTION, row.get("problem_description"));
			batch.add(builder.build());
			

		}

		// Clear any existing values for this speaker, treating the
		// incoming details as authoritative.
		// batch.add(ContentProviderOperation.newDelete(speakerUri).build());

		return batch;
	}
	
	private JSONObject getCustomerInfo(JSONObject obj) throws JSONException {
		JSONObject customer = new JSONObject();
		customer.put("customer_id", obj.getString("customer_id"));
		customer.put("description", obj.getString("description"));
		return customer;
	}

	private JSONObject getPickUpAddress(JSONObject obj) throws JSONException {
		JSONObject address = new JSONObject();
		address.put("address_id", obj.getString("pickup_id"));
		address.put("street", obj.getString("pickup_street"));
		address.put("housenumber", obj.getString("pickup_housenumber"));
		address.put("country", obj.getString("pickup_country"));
		address.put("zipcode", obj.getString("pickup_zipcode"));
		address.put("city", obj.getString("pickup_city"));
		address.put("name", obj.getString("pickup_name"));
		return address;
	}

	private JSONObject getDeliveryAddress(JSONObject obj) throws JSONException {
		JSONObject address = new JSONObject();
		address.put("address_id", obj.getString("delivery_id"));
		address.put("street", obj.getString("delivery_street"));
		address.put("housenumber", obj.getString("delivery_housenumber"));
		address.put("country", obj.getString("delivery_country"));
		address.put("zipcode", obj.getString("delivery_zipcode"));
		address.put("city", obj.getString("delivery_city"));
		address.put("name", obj.getString("delivery_name"));
		return address;
	}

}
