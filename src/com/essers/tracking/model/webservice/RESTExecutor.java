package com.essers.tracking.model.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.format.DateUtils;
import android.util.Log;

import com.essers.tracking.model.processor.Processor;
import com.essers.tracking.model.processor.Processor.ProcessorException;
import com.essers.tracking.ui.BaseActivity;

public class RESTExecutor implements Executor {

	private static final String TAG = "RESTExecutor";
	private Context mContext;
	private final HttpClient mHttpClient;
	private final ContentResolver mResolver;
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String ENCODING_GZIP = "gzip";
	private static final int SECOND_IN_MILLIS = (int) DateUtils.SECOND_IN_MILLIS;

	public RESTExecutor(Context context, ContentResolver resolver) {
		mContext = context;
		mHttpClient = getHttpClient(context);
		mResolver = resolver;
	}

	public void execute(String url, Processor processor)
			throws ProcessorException {

		Log.d(TAG, "execute("+ url + ")");
		HttpUriRequest request = new HttpGet(url);
		sign(request);
		execute(request, processor);

	}
	
	public final String getUsername() {
		SharedPreferences prefs = mContext.getSharedPreferences("logininfo", Context.MODE_PRIVATE);
		String username = prefs.getString("username", null);
		return username;
		
	}
	
	public final String getPassword() {
		SharedPreferences prefs = mContext.getSharedPreferences("logininfo", Context.MODE_PRIVATE);
		String password = prefs.getString("password", null);
		return password;
	}

	private void sign(HttpUriRequest request) {
		
			String consumer_key = getUsername();
			String consumer_secret = getPassword();
		
			System.setProperty("debug", "true");
			OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumer_key, consumer_secret);
			consumer.setTokenWithSecret(null, "");
			
			try {
				consumer.sign(request);
			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void execute(HttpUriRequest request, Processor processor)
			throws ProcessorException {

		try {
			
			HttpResponse response = mHttpClient.execute(request);
			int status = response.getStatusLine().getStatusCode();

			if (status != HttpStatus.SC_OK) {
				throw new ProcessorException("Unexpected server response "
						+ response.getStatusLine() + " for "
						+ request.getRequestLine());
			}

			final InputStream input = response.getEntity().getContent();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(input));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				total.append(line);
			}
			Log.d(TAG, total.toString());
			JSONObject json = new JSONObject(total.toString());
			processor.parseAndApply(json, mResolver);
		} catch (ProcessorException e) {
			throw e;
		} catch (IOException e) {
			throw new ProcessorException("Problem reading remote response for "
					+ request.getRequestLine(), e);
		} catch (JSONException e) {
			throw new ProcessorException("Problem reading JSON structure "
					+ request.getRequestLine(), e);
		}

	}

	/**
	 * Generate and return a {@link HttpClient} configured for general use,
	 * including setting an application-specific user-agent string.
	 */
	public static HttpClient getHttpClient(Context context) {
		final HttpParams params = new BasicHttpParams();

		// Use generous timeouts for slow mobile networks
		HttpConnectionParams
				.setConnectionTimeout(params, 20 * SECOND_IN_MILLIS);
		HttpConnectionParams.setSoTimeout(params, 20 * SECOND_IN_MILLIS);

		HttpConnectionParams.setSocketBufferSize(params, 8192);
		HttpProtocolParams.setUserAgent(params, buildUserAgent(context));

		final DefaultHttpClient client = new DefaultHttpClient(params);

		client.addRequestInterceptor(new HttpRequestInterceptor() {
			public void process(HttpRequest request, HttpContext context) {
				// Add header to accept gzip content
				if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
					request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
				}
			}
		});

		client.addResponseInterceptor(new HttpResponseInterceptor() {
			public void process(HttpResponse response, HttpContext context) {
				// Inflate any responses compressed with gzip
				final HttpEntity entity = response.getEntity();
				final Header encoding = entity.getContentEncoding();
				if (encoding != null) {
					for (HeaderElement element : encoding.getElements()) {
						if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
							response.setEntity(new InflatingEntity(response
									.getEntity()));
							break;
						}
					}
				}
			}
		});

		return client;
	}

	/**
	 * Build and return a user-agent string that can identify this application
	 * to remote servers. Contains the package name and version code.
	 */
	private static String buildUserAgent(Context context) {
		try {
			final PackageManager manager = context.getPackageManager();
			final PackageInfo info = manager.getPackageInfo(
					context.getPackageName(), 0);

			// Some APIs require "(gzip)" in the user-agent string.
			return info.packageName + "/" + info.versionName + " ("
					+ info.versionCode + ") (gzip)";
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	/**
	 * Simple {@link HttpEntityWrapper} that inflates the wrapped
	 * {@link HttpEntity} by passing it through {@link GZIPInputStream}.
	 */
	private static class InflatingEntity extends HttpEntityWrapper {
		public InflatingEntity(HttpEntity wrapped) {
			super(wrapped);
		}

		@Override
		public InputStream getContent() throws IOException {
			return new GZIPInputStream(wrappedEntity.getContent());
		}

		@Override
		public long getContentLength() {
			return -1;
		}
	}

}
