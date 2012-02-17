package com.essers.tracking.model.provider;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.essers.tracking.model.provider.TrackingContract.Addresses;
import com.essers.tracking.model.provider.TrackingContract.AddressesColumns;
import com.essers.tracking.model.provider.TrackingContract.Customers;
import com.essers.tracking.model.provider.TrackingContract.CustomersColumns;
import com.essers.tracking.model.provider.TrackingContract.OrdersColumns;

public class TrackingDatabase extends SQLiteOpenHelper {
	
	private static final String TAG = "TrackingDatabase";
	
	private static final String DATABASE_NAME = "tracking.db";
	private static final int DATABASE_VERSION = 9;
	
	public TrackingDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	interface Tables {
		String ORDERS = "orders";
		String ADDRESSES = "addresses";
		String CUSTOMERS = "customers";
		
		String ORDER_JOIN = "orders " 
				+ "LEFT OUTER JOIN pickup_addresses ON orders.pickup_address=adddresses.address_id "
				+ "LEFT OUTER JOIN delivery_addresses ON orders.delivery_address=addresses.address_id";
	}
	
	private interface Triggers {
		String ADDRESS_PICKUP_DELETE = "address_pickup_delete";
		String ADDRESS_DELIVERY_DELETE = "address_delivery_delete";
	}
	
	private interface References {
		String PICKUP_ADDRESS_ID = "REFERENCES " + Tables.ADDRESSES + "(" + Addresses.ADDRESS_ID + ")";
		String DELIVERY_ADDRESS_ID = "REFERENCES " + Tables.ADDRESSES + "(" + Addresses.ADDRESS_ID + ")";
		String CUSTOMER_ID = "REFERENCES " + Tables.CUSTOMERS + "(" + Customers.CUSTOMER_ID + ")";
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE " + Tables.ADDRESSES + " ("
				+ BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ AddressesColumns.ADDRESS_ID + " TEXT NOT NULL, " 
				+ AddressesColumns.STREET + " TEXT,"
				+ AddressesColumns.HOUSENUMBER + " TEXT,"
				+ AddressesColumns.COUNTRY + " TEXT,"
				+ AddressesColumns.ZIPCODE + " TEXT,"
				+ AddressesColumns.CITY + " TEXT,"
				+ "UNIQUE (" + AddressesColumns.ADDRESS_ID + ") ON CONFLICT REPLACE)"
				);
		
		db.execSQL("CREATE TABLE " + Tables.CUSTOMERS + " ("
				+ BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ CustomersColumns.CUSTOMER_ID + " TEXT NOT NULL,"
				+ CustomersColumns.DESCRIPTION + " TEXT,"
				+ "UNIQUE (" + CustomersColumns.CUSTOMER_ID + ") ON CONFLICT REPLACE)"
				);
		
		
		db.execSQL("CREATE TABLE " + Tables.ORDERS + " ("
				+ BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ OrdersColumns.ORDER_ID + " TEXT NOT NULL,"
				+ OrdersColumns.CUSTOMER_ID + " TEXT " + References.CUSTOMER_ID + ","
				+ OrdersColumns.REFERENCE + " TEXT,"
				+ OrdersColumns.STATE + " INTEGER NOT NULL,"
				+ OrdersColumns.PICKUP_ADDRESS + " TEXT " + References.PICKUP_ADDRESS_ID  + ","
				+ OrdersColumns.PICKUP_DATE + " TEXT NOT NULL,"
				+ OrdersColumns.DELIVERY_ADDRESS + " TEXT " + References.DELIVERY_ADDRESS_ID + ","
				+ OrdersColumns.DELIVERY_DATE + " TEXT NOT NULL,"
				+ OrdersColumns.PROBLEM + " INTEGER NOT NULL DEFAULT 0,"
				+ "UNIQUE (" + OrdersColumns.ORDER_ID + ") ON CONFLICT REPLACE)" );
		
		db.execSQL("CREATE TRIGGER " + Triggers.ADDRESS_PICKUP_DELETE + " AFTER DELETE ON "
				+ Tables.ORDERS + " BEGIN DELETE FROM "
				+ Tables.ADDRESSES + " WHERE " + AddressesColumns.ADDRESS_ID
				+ "= old." + OrdersColumns.PICKUP_ADDRESS + "; END;");
		
		db.execSQL("CREATE TRIGGER " + Triggers.ADDRESS_DELIVERY_DELETE + " AFTER DELETE ON "
				+ Tables.ORDERS + " BEGIN DELETE FROM "
				+ Tables.ADDRESSES + " WHERE " + AddressesColumns.ADDRESS_ID
				+ "= old." + OrdersColumns.DELIVERY_ADDRESS + "; END;");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		if(oldVersion<newVersion){
			Log.d(TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);
			db.execSQL("DROP TABLE IF EXISTS " + Tables.ADDRESSES);
			db.execSQL("DROP TABLE IF EXISTS " + Tables.CUSTOMERS);
			db.execSQL("DROP TABLE IF EXISTS " + Tables.ORDERS);
			db.execSQL("DROP TRIGGER IF EXISTS " + Triggers.ADDRESS_DELIVERY_DELETE);
			db.execSQL("DROP TRIGGER IF EXISTS " + Triggers.ADDRESS_PICKUP_DELETE);
			
			
			// add more execs for more tables
			onCreate(db);
		}
		
	}
	
	
	

}
