package com.essers.tracking.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.essers.tracking.R;

public class UIUtils {

	private static final String TAG = "UIUtils";

	public static void setText(int resource, String text, View container) {

		TextView t = (TextView) container.findViewById(resource);
		if (t != null) {
			t.setText(text);
		}
	}

	public static void setDate(int resource, int timestamp, View container) {

		long time = Long.parseLong(timestamp + "");

		String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date(time*1000));
		TextView t = (TextView) container.findViewById(resource);
		if (t != null) {
			t.setText(date);
		}
	}

	public static void setTime(int resource, int timestamp, View container) {

		long time = Long.parseLong(timestamp + "");

		String ti = new SimpleDateFormat("HH:mm").format(new Date(time*1000));
		TextView t = (TextView) container.findViewById(resource);
		if (t != null) {
			t.setText(ti);
		}
	}

	public static void setLevel(int resource, int level, View container) {
		ImageView i = (ImageView) container.findViewById(resource);
		if (i != null) {
			i.setImageLevel(level);
		}
	}

	public static void showToast(Context context, int resource) {
		Toast.makeText(context, resource, Toast.LENGTH_LONG).show();
	}
	
	public static void setStateDescription(Context context, int resource, int state, View container) {
		Resources res = context.getResources();
		TypedArray levels = res.obtainTypedArray(R.array.order_state);
		TextView t = (TextView)container.findViewById(resource);
		if (t != null) {
			t.setText(levels.getString(state));
		}
	}

	public static void setStateBackground(int state, int resource,
			View container) {

		View v = container.findViewById(resource);

		if (v != null) {
			switch (state) {
			case 5:
				v.setBackgroundResource(R.color.state_1);
				break;
			case 6:
				v.setBackgroundResource(R.color.state_2);
				break;
			default:
				v.setBackgroundResource(0);
			}

		}
	}

}
