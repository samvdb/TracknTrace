package com.essers.tracking.util;

import android.content.res.Resources;

public class WebserviceHelper {
	
	public static String prepareCall(String resource, String[] values) {
		
		for(int i=0; i < values.length; i++) {
			resource.replaceFirst(":param", values[i]);
		}
		
		return resource;
	}

}
