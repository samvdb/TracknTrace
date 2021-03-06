package com.essers.tracking.model.processor;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;

import com.essers.tracking.model.provider.TrackingContract;
import com.essers.tracking.model.provider.TrackingContract.Addresses;

public class AddressProcessor extends Processor {

	public AddressProcessor() {
		super(TrackingContract.CONTENT_AUTHORITY);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<ContentProviderOperation> parse(JSONObject parser,
			ContentResolver resolver) throws IOException, JSONException {
		// TODO Auto-generated method stub
		ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
		//Log.d(TAG, "Statuscode: " + parser.getInt("code"));
		return batch;
	}
	
	public ArrayList<ContentProviderOperation> parseSingle(JSONObject parser,
			ContentResolver resolver) throws IOException, JSONException {
		// TODO Auto-generated method stub
		ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
		final ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(Addresses.CONTENT_URI);
		builder.withValue(Addresses.ADDRESS_ID, parser.get("address_id"));
		builder.withValue(Addresses.STREET, parser.get("street"));
		builder.withValue(Addresses.HOUSENUMBER, parser.get("housenumber"));
		builder.withValue(Addresses.COUNTRY, parser.get("country"));
		builder.withValue(Addresses.ZIPCODE, parser.get("zipcode"));
		builder.withValue(Addresses.CITY, parser.get("city"));
		builder.withValue(Addresses.NAME, parser.get("name"));
		batch.add(builder.build());
		return batch;
		
		
	}

}
