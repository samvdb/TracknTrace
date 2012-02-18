package com.essers.tracking.model.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class TrackingContract {

	interface OrdersColumns {
		String ORDER_ID = "order_id";
		String CUSTOMER_ID = "customer_id";
		String REFERENCE = "reference";
		String STATE = "state";
		String PICKUP_ADDRESS = "pickup_address";
		String PICKUP_DATE = "pickup_date";
		String DELIVERY_ADDRESS = "delivery_address";
		String DELIVERY_DATE = "delivery_date";
		String PROBLEM = "problem";
		String PROBLEM_DESCRIPTION = "problem_description";
	}
	
	interface AddressesColumns {
		String ADDRESS_ID = "address_id";
		String STREET = "street";
		String HOUSENUMBER = "housenumber";
		String COUNTRY = "country";
		String ZIPCODE = "zipcode";
		String CITY = "city";
		String NAME = "name";
	}
	
	interface CustomersColumns {
		String CUSTOMER_ID = "customer_id";
		String DESCRIPTION = "description";
	}
	
	public interface PickupColumns {
		String COUNTRY = "pickup_country";
		String ZIPCODE = "pickup_zipcode";
		String CITY = "pickup_city";
		String NAME = "pickup_name";
	}
	
	public interface DeliveryColumns {
		String COUNTRY = "delivery_country";
		String ZIPCODE = "delivery_zipcode";
		String CITY = "delivery_city";
		String NAME = "delivery_name";
	}
	
	public static final String CONTENT_AUTHORITY = "com.essers.tracking.provider";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
	
	private static final String PATH_ORDERS = "orders";
	private static final String PATH_ADDRESSES = "addresses";
	private static final String PATH_CUSTOMERS = "customers";
	
	/**
	 * Orders contain customer information and address information along with the described fields.
	 */
	public static class Orders implements OrdersColumns, BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ORDERS).build();
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.essers.order";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.essers.order";
		
		/** Default ORDER BY */
		public static final String DEFAULT_SORT = BaseColumns._ID + " ASC";
		
		public static Uri buildOrdersUri(String orderId) {
			return CONTENT_URI.buildUpon().appendPath(orderId).build();
		}
		
		public static String getOrderId(Uri uri) {
			return uri.getPathSegments().get(1);
		}
		
	}
	
	/**
	 * Addresses contain all address information referenced by an {@link Order}
	 */
	public static class Addresses implements AddressesColumns, BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ADDRESSES).build();
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.essers.address";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.essers.address";
		
		/** Default ORDER BY */
		public static final String DEFAULT_SORT = BaseColumns._ID + " ASC";
		
		public static Uri buildAddressUri(String addressId) {
			return CONTENT_URI.buildUpon().appendPath(addressId).build();
		}
		
		public static String getAddressId(Uri uri) {
			return uri.getPathSegments().get(1);
		}
		
	}
	
	/**
	 * Addresses contain all address information referenced by an {@link Order}
	 */
	public static class Customers implements CustomersColumns, BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CUSTOMERS).build();
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.essers.customer";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.essers.customer";
		
		/** Default ORDER BY */
		public static final String DEFAULT_SORT = BaseColumns._ID + " ASC";
		
		public static Uri buildAddressUri(String customerId) {
			return CONTENT_URI.buildUpon().appendPath(customerId).build();
		}

		public static String getCustomerId(Uri uri) {
			return uri.getPathSegments().get(1);
		}

		
		
	}
	
	

	/*public static class Address {

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
		public static final int PATH_FOR_CUSTOMER_ID_CLEAR_TOKEN = 103;

		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.essers.order";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.essers.order";
		
		public static final String DEFAULT_SORT = BaseColumns._ID + " ASC";

		
		
		public static Uri buildCustomerOrdersUri(String customerId) {
			return CONTENT_URI.buildUpon().appendPath(PATH_FOR_CUSTOMER_ID).appendPath(customerId).build();
		}
		
		public static String getCustomerId(Uri uri) {
			return uri.getPathSegments().get(2);
		}

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
	}*/

	private TrackingContract() {
	}

	/*private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = CONTENT_AUTHORITY;

		matcher.addURI(authority, "orders", Order.PATH_TOKEN);
		matcher.addURI(authority, "orders/customer/*", Order.PATH_FOR_CUSTOMER_ID_TOKEN);
		matcher.addURI(authority, "orders/customer/*", Order.PATH_FOR_CUSTOMER_ID_CLEAR_TOKEN);
		matcher.addURI(authority, "orders/*", Order.PATH_FOR_ID_TOKEN);
		
		
		matcher.addURI(authority, Customer.PATH, Customer.PATH_TOKEN);
		matcher.addURI(authority, Customer.PATH_FOR_ID, Customer.PATH_FOR_ID_TOKEN);
		
		matcher.addURI(authority, Address.PATH, Address.PATH_TOKEN);
		matcher.addURI(authority, Address.PATH_FOR_ID, Address.PATH_FOR_ID_TOKEN);
		return matcher;
	};
*/

}
