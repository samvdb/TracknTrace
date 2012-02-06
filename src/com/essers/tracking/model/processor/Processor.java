package com.essers.tracking.model.processor;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

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
	public void parseAndApply(JSONObject data, ContentResolver resolver) throws ProcessorException {
		Log.d(TAG, "parseAndApply(" + data + " )");
		try {
			ArrayList<ContentProviderOperation> operations = parse(data, resolver);
			resolver.applyBatch(mAuthority, operations);
		} catch(JSONException e) {
			throw new ProcessorException("Problem parsing JSON response", e);
		} catch(IOException e) {
			throw new ProcessorException("Problem reading response", e);
		} catch (RemoteException e) {
			throw new RuntimeException("Problem applying operations", e);
		} catch (OperationApplicationException e) {
			throw new RuntimeException("Problem applying operations", e);
		}
	}

	protected abstract ArrayList<ContentProviderOperation> parse(JSONObject data,
			ContentResolver resolver) throws JSONException, IOException;

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
