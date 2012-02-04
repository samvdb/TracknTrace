package com.essers.tracking.model.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

public class TrackingContract {

	public static final String CONTENT_AUTHORITY = "com.essers.tracking.provider";
	private static final Uri BASE_URI = Uri.parse("content://"
			+ CONTENT_AUTHORITY);

	public static final UriMatcher URI_MATCHER = buildUriMatcher();

	public interface Tables {
		public String ORDERS = "orders";
		public String CUSTOMERS = "customers";
		public String ADDRESSES = "addresses";
	}

	/**
	 * All REFERENCES to other tables are declared here.
	 * @author Sam
	 *
	 */
	public interface References {
		public String CUSTOMER_ID = "REFERENCES " + Tables.CUSTOMERS + "("
				+ Customer.Columns.ID + ")";
		public String PICKUP_ADDRESS = "REFERENCES " + Tables.ADDRESSES + "("
				+ Address.Columns.ID + ")";
		public String DELIVERY_ADDRESS = "REFERENCES " + Tables.ADDRESSES + "(" + Address.Columns.ID + ")";
	}

	public static class Address {

		private static final String NAME = "address";

		public static final String PATH = "addresses";
		public static final String PATH_FOR_ID = "addresses/*";

		public static final int PATH_TOKEN = 0x300;
		public static final int PATH_FOR_ID_TOKEN = 0x301;
		
		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.essers.address";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.essers.address";
		
		public static class Columns {
			public static final String ID = BaseColumns._ID;
			public static final String STREET = "street";
			public static final String HOUSENUMBER = "housenumber";
			public static final String COUNTRY = "country";
			public static final String ZIPCODE = "zipcode";
			public static final String CITY = "city";
		}

	}

	public static class Customer {

		private static final String NAME = "customer";

		public static final String PATH = "customers";
		public static final String PATH_FOR_ID = "customers/*";

		public static final int PATH_TOKEN = 0x200;
		public static final int PATH_FOR_ID_TOKEN = 0x201;

		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.essers.customer";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.essers.customer";

		public static class Columns {
			public static final String ID = BaseColumns._ID;
			public static final String DESCRIPTION = "description";
		}
	}

	public static class Order {

		public static final String NAME = "order";

		public static final String PATH = "orders";
		public static final String PATH_FOR_ID = "orders/*";

		public static final int PATH_TOKEN = 0x100;
		public static final int PATH_FOR_ID_TOKEN = 0x101;

		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.essers.order";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.essers.order";

		/**
		 * Describes the columns of the reference data
		 * 
		 * @author Sam
		 * 
		 */
		public static class Columns {
			public static final String ID = BaseColumns._ID;
			public static final String REFERENCE = "reference";
			public static final String STATE = "state";
			public static final String PICKUP_DATE = "pickup_date";
			public static final String DELIVERY_DATE = "delivery_date";
		}
	}

	private TrackingContract() {
	}

	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = CONTENT_AUTHORITY;

		matcher.addURI(authority, Order.PATH, Order.PATH_TOKEN);
		matcher.addURI(authority, Order.PATH, Order.PATH_FOR_ID_TOKEN);
		
		matcher.addURI(authority, Customer.PATH, Customer.PATH_TOKEN);
		matcher.addURI(authority, Customer.PATH_FOR_ID, Customer.PATH_FOR_ID_TOKEN);
		
		matcher.addURI(authority, Address.PATH, Address.PATH_TOKEN);
		matcher.addURI(authority, Address.PATH_FOR_ID, Address.PATH_FOR_ID_TOKEN);
		return matcher;
	};

}
