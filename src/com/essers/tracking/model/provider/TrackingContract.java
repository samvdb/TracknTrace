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
				+ Customer.Columns.CUSTOMER_ID + ")";
		public String PICKUP_ADDRESS = "REFERENCES " + Tables.ADDRESSES + "("
				+ Address.Columns.ADDRESS_ID + ")";
		public String DELIVERY_ADDRESS = "REFERENCES " + Tables.ADDRESSES + "(" + Address.Columns.ADDRESS_ID + ")";
	}

	public static class Address {

		public static final String NAME = "address";

		public static final String PATH = "addresses";
		public static final String PATH_FOR_ID = "addresses/*";

		public static final int PATH_TOKEN = 300;
		public static final int PATH_FOR_ID_TOKEN = 301;
		
		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.essers.address";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.essers.address";
		
		public static class Columns {
			public static final String ADDRESS_ID = "address_id";
			public static final String STREET = "street";
			public static final String HOUSENUMBER = "housenumber";
			public static final String COUNTRY = "country";
			public static final String ZIPCODE = "zipcode";
			public static final String CITY = "city";
		}

	}

	public static class Customer {

		public static final String NAME = "customer";

		public static final String PATH = "customers";
		public static final String PATH_FOR_ID = "customers/*";

		public static final int PATH_TOKEN = 200;
		public static final int PATH_FOR_ID_TOKEN = 201;

		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.essers.customer";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.essers.customer";

		public static class Columns {
			public static final String CUSTOMER_ID = "customer_id";
			public static final String DESCRIPTION = "description";
		}
	}

	public static class Order {

		public static final String NAME = "orders";

		public static final String PATH = "orders";
		public static final String PATH_FOR_CUSTOMER_ID = "customer";
		//public static final String PATH_FOR_ID = "orders/*";
		//public static final String PATH_FOR_CUSTOMER_ID = "orders/customer/*";

		public static final int PATH_TOKEN = 100;
		public static final int PATH_FOR_ID_TOKEN = 101;
		public static final int PATH_FOR_CUSTOMER_ID_TOKEN = 102;

		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.essers.order";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.essers.order";
		
		public static final String DEFAULT_SORT = Order.Columns.ORDER_ID + " ASC";
		
		public static Uri buildCustomerOrdersUri(String customerId) {
			return CONTENT_URI.buildUpon().appendPath(PATH_FOR_CUSTOMER_ID).appendPath(customerId).build();
		}
		
		/** Read {@link #Order.Columns.CUSTOMER_ID} */
		public static String getCustomerId(Uri uri) {
			return uri.getPathSegments().get(2);
		}

		/**
		 * Describes the columns of the reference data
		 * 
		 * @author Sam
		 * 
		 */
		public static class Columns {
			public static final String ORDER_ID = "order_id";
			public static final String CUSTOMER_ID = "customer_id";
			public static final String REFERENCE = "reference";
			public static final String STATE = "state";
			public static final String PICKUP_ADDRESS = "pickup_address";
			public static final String PICKUP_DATE = "pickup_date";
			public static final String DELIVERY_ADDRESS = "delivery_address";
			public static final String DELIVERY_DATE = "delivery_date";
		}
	}

	private TrackingContract() {
	}

	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = CONTENT_AUTHORITY;

		matcher.addURI(authority, "orders", Order.PATH_TOKEN);
		matcher.addURI(authority, "orders/customer/*", Order.PATH_FOR_CUSTOMER_ID_TOKEN);
		matcher.addURI(authority, "orders/*", Order.PATH_FOR_ID_TOKEN);
		
		
		matcher.addURI(authority, Customer.PATH, Customer.PATH_TOKEN);
		matcher.addURI(authority, Customer.PATH_FOR_ID, Customer.PATH_FOR_ID_TOKEN);
		
		matcher.addURI(authority, Address.PATH, Address.PATH_TOKEN);
		matcher.addURI(authority, Address.PATH_FOR_ID, Address.PATH_FOR_ID_TOKEN);
		return matcher;
	};
	
	public static String getType(int match) {
		switch(match) {
		case Order.PATH_TOKEN:
			return Order.CONTENT_TYPE;
		case Order.PATH_FOR_ID_TOKEN:
			return Order.CONTENT_ITEM_TYPE;
		case Order.PATH_FOR_CUSTOMER_ID_TOKEN:
			return Order.CONTENT_TYPE;
		case Address.PATH_TOKEN:
			return Address.CONTENT_TYPE;
		case Address.PATH_FOR_ID_TOKEN:
			return Address.CONTENT_ITEM_TYPE;
		case Customer.PATH_TOKEN:
			return Customer.CONTENT_TYPE;
		case Customer.PATH_FOR_ID_TOKEN:
			return Customer.CONTENT_ITEM_TYPE;
			default:
				throw new UnsupportedOperationException("Unknown match: " + match);
		}
	}

}
