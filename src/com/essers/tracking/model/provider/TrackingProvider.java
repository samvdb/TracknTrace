package com.essers.tracking.model.provider;

import java.util.ArrayList;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.essers.tracking.model.provider.TrackingContract.Address;
import com.essers.tracking.model.provider.TrackingContract.Customer;
import com.essers.tracking.model.provider.TrackingContract.Order;

public class TrackingProvider extends ContentProvider {
	
	private static final String TAG = "TrackingProvider";
	private static final UriMatcher mUriMatcher = TrackingContract.URI_MATCHER;
	private TrackingDatabase mDatabase;

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.v(TAG, "insert(uri=" + uri + ", values=" + values.toString() + ")");
		
		final SQLiteDatabase db = mDatabase.getWritableDatabase();
		final int match = mUriMatcher.match(uri);
		
		switch(match) {
		case Order.PATH_TOKEN: {
			db.insertOrThrow(Order.NAME, null, values);
			Log.d(TAG, "Setting content uri notification on insert= " + uri.toString());
			getContext().getContentResolver().notifyChange(uri, null);
			return buildUri(Order.CONTENT_URI, values.getAsString(Order.Columns.ORDER_ID));
		}
		case Address.PATH_TOKEN: {
			db.insertOrThrow(Address.NAME, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return buildUri(Address.CONTENT_URI, values.getAsString(Address.Columns.ADDRESS_ID));
		}
		case Customer.PATH_TOKEN: {
			db.insertOrThrow(Customer.NAME, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return buildUri(Customer.CONTENT_URI, values.getAsString(Customer.Columns.CUSTOMER_ID));
		}
		default: 
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	@Override
	public boolean onCreate() {
		final Context context = getContext();
		mDatabase = new TrackingDatabase(context);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "query(" + uri + ", " + selection + ")");
		
		final int match = mUriMatcher.match(uri);
		final SQLiteDatabase db = mDatabase.getReadableDatabase();
		final SQLiteQueryBuilder builder = buildExpandedSelection(uri, match);
		Cursor c = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		Log.d(TAG, "Setting notififaction Uri on cursor=" + uri.toString());
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String getId(Uri uri) {
		return uri.getPathSegments().get(1);
	}

	public static Uri buildUri(Uri uri, String id) {
		return uri.buildUpon().appendPath(id).build();
	}
	
	private SQLiteQueryBuilder buildExpandedSelection(Uri uri, int match) {
		
		Log.d(TAG, "buildExpandedSelection(uri="+uri+", match="+match+ ")");
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		
		String id = null;
		switch(match) {
		case Address.PATH_TOKEN:
			builder.setTables(Address.NAME);
			break;
		case Customer.PATH_TOKEN:
			builder.setTables(Customer.NAME);
			break;
		case Order.PATH_TOKEN:
			builder.setTables(Order.NAME);
			break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri + " match=" + match);
			
		}
		return builder;
		
	}
	
	/**
     * Apply the given set of {@link ContentProviderOperation}, executing inside
     * a {@link SQLiteDatabase} transaction. All changes will be rolled back if
     * any single one fails.
     */
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
    	
    	if (operations == null) { throw new OperationApplicationException("Operations had a null value while trying to apply them in batch."); }
    	
        final SQLiteDatabase db = mDatabase.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }


}
