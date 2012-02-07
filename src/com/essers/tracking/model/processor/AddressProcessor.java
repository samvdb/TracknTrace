package com.essers.tracking.model.processor;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.essers.tracking.model.provider.TrackingContract;
import com.essers.tracking.model.provider.TrackingContract.Address;
import com.essers.tracking.model.provider.TrackingContract.Order;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.util.Log;

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
		final ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(Address.CONTENT_URI);
		builder.withValue(Address.Columns.ADDRESS_ID, parser.get("address_id"));
		builder.withValue(Address.Columns.STREET, parser.get("street"));
		builder.withValue(Address.Columns.HOUSENUMBER, parser.get("housenumber"));
		builder.withValue(Address.Columns.COUNTRY, parser.get("country"));
		builder.withValue(Address.Columns.ZIPCODE, parser.get("zipcode"));
		builder.withValue(Address.Columns.CITY, parser.get("city"));
		batch.add(builder.build());
		return batch;
		
		
	}

}
