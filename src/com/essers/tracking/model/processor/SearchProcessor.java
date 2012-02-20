package com.essers.tracking.model.processor;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.essers.tracking.model.provider.TrackingContract.Addresses;
import com.essers.tracking.model.provider.TrackingContract.Orders;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;

public class SearchProcessor extends OrdersProcessor {

	@Override
	public ArrayList<ContentProviderOperation> parse(JSONObject parser,
			ContentResolver resolver) throws IOException, JSONException {
		// TODO Auto-generated method stub
		
		ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
		
		ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(Addresses.CONTENT_URI);
		batch.add(builder.build());
		
		builder = ContentProviderOperation.newDelete(Orders.CONTENT_URI);
		batch.add(builder.build());
		
		
		
		batch.addAll(super.parse(parser, resolver));
		
		return batch;
	}

}
