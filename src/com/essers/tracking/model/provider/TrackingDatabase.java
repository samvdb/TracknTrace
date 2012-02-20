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
import com.essers.tracking.model.provider.TrackingContract.DeliveryColumns;
import com.essers.tracking.model.provider.TrackingContract.Orders;
import com.essers.tracking.model.provider.TrackingContract.OrdersColumns;
import com.essers.tracking.model.provider.TrackingContract.PickupColumns;

public class TrackingDatabase extends SQLiteOpenHelper {
	
	private static final String TAG = "TrackingDatabase";
	
	private static final String DATABASE_NAME = "tracking.db";
	private static final int DATABASE_VERSION = 15;
	
	public TrackingDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	interface Tables {
		String ORDERS = "orders";
		String ADDRESSES = "addresses";
		String CUSTOMERS = "customers";
	}
	
	interface Views {
		String CUSTOM_ORDERS = "custom_orders";
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
				+ AddressesColumns.NAME + " TEXT, "
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
				+ OrdersColumns.PICKUP_DATE + " INTEGER NOT NULL,"
				+ OrdersColumns.DELIVERY_ADDRESS + " TEXT " + References.DELIVERY_ADDRESS_ID + ","
				+ OrdersColumns.DELIVERY_DATE + " INTEGER NOT NULL,"
				+ OrdersColumns.PROBLEM + " INTEGER NOT NULL DEFAULT 0,"
				+ OrdersColumns.PROBLEM_DESCRIPTION + " TEXT,"
				+ "UNIQUE (" + OrdersColumns.ORDER_ID + ") ON CONFLICT REPLACE)" );
		
		db.execSQL("CREATE VIEW " + Views.CUSTOM_ORDERS + " AS SELECT "
				+ "o._id, o.order_id, o.reference, o.state, o.pickup_date, o.delivery_date, o.problem, "
				+ "p.name AS " + PickupColumns.NAME + "," 
				+ "p.country AS " + PickupColumns.COUNTRY + ","
				+ "p.zipcode AS " + PickupColumns.ZIPCODE + ","
				+ "p.city AS " + PickupColumns.CITY + ","
				+ "d.name AS " + DeliveryColumns.NAME + ","
				+ "d.country AS " + DeliveryColumns.COUNTRY + ","
				+ "d.zipcode AS " + DeliveryColumns.ZIPCODE + ", "
				+ "d.city AS " + DeliveryColumns.CITY
				+ " FROM " + Tables.ORDERS + " AS o"
				+ " LEFT JOIN " + Tables.ADDRESSES + " AS p ON o." + Orders.PICKUP_ADDRESS + "=p." + Addresses.ADDRESS_ID
				+ " LEFT JOIN " + Tables.ADDRESSES + " AS d ON o." + Orders.DELIVERY_ADDRESS + "=d." + Addresses.ADDRESS_ID);
		
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
			db.execSQL("DROP VIEW IF EXISTS " + Views.CUSTOM_ORDERS);
			db.execSQL("DROP TRIGGER IF EXISTS " + Triggers.ADDRESS_DELIVERY_DELETE);
			db.execSQL("DROP TRIGGER IF EXISTS " + Triggers.ADDRESS_PICKUP_DELETE);
			
			
			// add more execs for more tables
			onCreate(db);
		}
		
	}
	
	
	

}
