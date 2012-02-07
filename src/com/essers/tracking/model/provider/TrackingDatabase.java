package com.essers.tracking.model.provider;


import com.essers.tracking.model.provider.TrackingContract.Address;
import com.essers.tracking.model.provider.TrackingContract.Customer;
import com.essers.tracking.model.provider.TrackingContract.Order;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class TrackingDatabase extends SQLiteOpenHelper {
	
	private static final String TAG = "TrackingDatabase";
	
	private static final String DATABASE_NAME = "tracking.db";
	private static final int DATABASE_VERSION = 2;
	
	public TrackingDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static final String ID = BaseColumns._ID;
	public static final String REFERENCE = "reference";
	public static final String STATE = "state";
	public static final String PICKUP_DATE = "pickup_date";
	public static final String DELIVERY_DATE = "delivery_date";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE " + Address.NAME + " ("
				+ BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ Address.Columns.ADDRESS_ID + " INTEGER NOT NULL, " 
				+ Address.Columns.STREET + " TEXT,"
				+ Address.Columns.HOUSENUMBER + " TEXT,"
				+ Address.Columns.COUNTRY + " TEXT,"
				+ Address.Columns.ZIPCODE + " TEXT,"
				+ Address.Columns.CITY + " TEXT,"
				+ "UNIQUE (" + Address.Columns.ADDRESS_ID + ") ON CONFLICT REPLACE)"
				);
		
		db.execSQL("CREATE TABLE " + Customer.NAME + " ("
				+ BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ Customer.Columns.CUSTOMER_ID + " TEXT NOT NULL,"
				+ Customer.Columns.DESCRIPTION + " TEXT,"
				+ "UNIQUE (" + Customer.Columns.CUSTOMER_ID + ") ON CONFLICT REPLACE)"
				);
		
		
		db.execSQL("CREATE TABLE " + Order.NAME + " ("
				+ BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ Order.Columns.ORDER_ID + " TEXT NOT NULL,"
				+ Order.Columns.CUSTOMER_ID + " TEXT " + TrackingContract.References.CUSTOMER_ID + ","
				+ Order.Columns.REFERENCE + " TEXT,"
				+ Order.Columns.STATE + " INTEGER NOT NULL,"
				+ Order.Columns.PICKUP_ADDRESS + " INTEGER " + TrackingContract.References.PICKUP_ADDRESS  + ","
				+ Order.Columns.PICKUP_DATE + " INTEGER NOT NULL,"
				+ Order.Columns.DELIVERY_ADDRESS + " INTEGER " + TrackingContract.References.DELIVERY_ADDRESS + ","
				+ Order.Columns.DELIVERY_DATE + " INTEGER NOT NULL,"
				+ "UNIQUE (" + Order.Columns.ORDER_ID + ") ON CONFLICT REPLACE)" );
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		if(oldVersion<newVersion){
			Log.d(TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);
			db.execSQL("DROP TABLE IF EXISTS " + Customer.NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Address.NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Order.NAME);
			// add more execs for more tables
			onCreate(db);
		}
		
	}
	
	
	

}
