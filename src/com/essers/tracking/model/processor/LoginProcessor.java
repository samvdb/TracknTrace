package com.essers.tracking.model.processor;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.essers.tracking.model.provider.TrackingContract;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;

public class LoginProcessor extends Processor {

	
	public LoginProcessor() {
		super(TrackingContract.CONTENT_AUTHORITY);
	}

	@Override
	public ArrayList<ContentProviderOperation> parse(JSONObject parser,
			ContentResolver resolver) throws IOException, JSONException {
		// TODO Auto-generated method stub
		return null;
	}

}
