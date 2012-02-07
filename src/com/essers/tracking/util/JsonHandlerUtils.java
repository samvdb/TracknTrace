/*
 * Copyright 2011 Peter Kuterna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.essers.tracking.util;

import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

/**
 * Various utility methods used by {@link JsonHandler} implementations.
 */
public class JsonHandlerUtils {

	private static JsonFactory sFactory;

	/**
	 * Build and return a new {@link JsonParser} with the given
	 * {@link InputStream} assigned to it.
	 */
	public static JsonParser newJsonParser(InputStream input)
			throws JsonParseException, IOException {
		if (sFactory == null) {
			sFactory = new JsonFactory();
		}
		final JsonParser parser = sFactory.createJsonParser(input);
		return parser;
	}

}
