package com.essers.tracking.model.processor;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.essers.tracking.model.provider.TrackingContract;
import com.essers.tracking.model.provider.TrackingContract.Addresses;
import com.essers.tracking.model.provider.TrackingContract.Customers;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;

public class CustomerProcessor  extends Processor {

	public CustomerProcessor() {
		super(TrackingContract.CONTENT_AUTHORITY);
	}

	@Override
	public ArrayList<ContentProviderOperation> parse(JSONObject parser,
			ContentResolver resolver) throws IOException, JSONException {
		ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
		//Log.d(TAG, "Statuscode: " + parser.getInt("code"));
		return batch;
	}
	
	public ArrayList<ContentProviderOperation> parseSingle(JSONObject parser,
			ContentResolver resolver) throws IOException, JSONException {
		// TODO Auto-generated method stub
		ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
		final ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(Customers.CONTENT_URI);
		builder.withValue(Customers.CUSTOMER_ID, parser.get("customer_id"));
		builder.withValue(Customers.DESCRIPTION, parser.get("description"));
		batch.add(builder.build());
		return batch;
		
		
	}

}
