package com.essers.tracking.model.processor;

import java.io.IOException;
import java.util.ArrayList;


import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;

public abstract class Processor {
	
	private static final String TAG = "Processor";
	private final String mAuthority;

	public Processor(String authority) {
		mAuthority = authority;
	}

	/**
	 * 
	 * @param data
	 * @param resolver
	 * @throws ProcessorException
	 */
	public void parseAndApply(JSONObject parser, ContentResolver resolver) throws ProcessorException {
		try {
			ArrayList<ContentProviderOperation> operations = parse(parser, resolver);
			resolver.applyBatch(mAuthority, operations);
		}catch (ProcessorException e) {
			throw e;
		}  catch (IOException e) {
			throw new ProcessorException("Problem reading response", e);
		} catch (RemoteException e) {
			throw new RuntimeException("Problem applying batch operation", e);
		} catch (OperationApplicationException e) {
			throw new RuntimeException("Problem applying batch operation", e);
		} catch (JSONException e) {
			throw new ProcessorException("Problem reading JSON data", e);
		}
	}

	/***
	 * Parse the given {@link JsonParser}, returning a set of {@link ContentProviderOperations}
	 * that will sync the {@link COntentProvider} with the parsed data.
	 * @param parser
	 * @param resolver
	 * @return operations
	 * @throws JsonParseException
	 * @throws IOException
	 * @throws JSONException 
	 */
	public abstract ArrayList<ContentProviderOperation> parse(JSONObject parser,
			ContentResolver resolver) throws IOException, JSONException;
	
	public JSONObject getData(JSONObject fullData) throws JSONException {
		return fullData.getJSONObject("data");
	}

	/**
	 * Exception which will be thrown when an error has occured while parsing or
	 * applying a {@link JSONObject}.
	 * 
	 * @author Sam
	 * 
	 */
	public static class ProcessorException extends IOException {
		public ProcessorException(String message) {
			super(message);
		}

		public ProcessorException(String message, Throwable cause) {
			super(message);
			initCause(cause);
		}

		@Override
		public String toString() {
			if (getCause() != null) {
				return getLocalizedMessage() + ":" + getCause();
			} else {
				return getLocalizedMessage();
			}
		}
	}
}
