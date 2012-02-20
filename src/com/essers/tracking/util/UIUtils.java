package com.essers.tracking.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UIUtils {

	public static void setText(int resource, String text, ViewGroup container) {

		TextView t = (TextView) container.findViewById(resource);
		if (t != null) {
			t.setText(text);
		}
	}

	public static void setDate(int resource, int timestamp, ViewGroup container) {

		Timestamp ts = new Timestamp(timestamp);

		String date = new SimpleDateFormat("dd/MM/yyyy").format(ts);
		TextView t = (TextView) container.findViewById(resource);
		if (t != null) {
			t.setText(date);
		}
	}

	public static void setTime(int resource, int timestamp, ViewGroup container) {

		Timestamp ts = new Timestamp(timestamp);

		String time = new SimpleDateFormat("HH:mm").format(ts);
		TextView t = (TextView) container.findViewById(resource);
		if (t != null) {
			t.setText(time);
		}
	}
	
	public static void setLevel(int resource, int level, ViewGroup container) {
		ImageView i = (ImageView)container.findViewById(resource);
		if (i != null) {
			i.setImageLevel(level);
		}
	}
	

	public static void showToast(Context context, int resource) {
		Toast.makeText(context, resource, Toast.LENGTH_LONG).show();
	}

}
