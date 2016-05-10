/**
 *    Copyright (C) 2011 Nadim Benabdenbi <nadim.benabdenbi@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.google.code.maven.plugin.http.client.utils;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.maven.plugin.logging.Log;

/**
 * 
 * @author 797409
 * 
 */
public class HttpEntityUtils {
	
	public static final Charset DEFAULT_PLATFORM_ENCODING = Charset.defaultCharset();
	
	public static final Pattern CONTENT_TYPE_CHARSET_PATTERN = Pattern.compile("[cC][hH][aA][rR][sS][eE][tT]=");
	
	public static final Pattern CONTENT_TYPE_CHARSET_DELIMITER_PATTERN = Pattern.compile("[|,;\\s]");
	
	public static Charset getEncoding(HttpEntity entity, Log log) {
		return getEncoding(entity, DEFAULT_PLATFORM_ENCODING, log);
	}
	
	public static Charset getEncoding(HttpEntity entity, Charset defaultCharset, Log log) {
		try {
			if (entity.getContentEncoding() != null) {
				return Charset.forName(entity.getContentEncoding().getName());
			} else if (entity.getContentType() != null) {
				String type = entity.getContentType().getValue();
				if (type != null) {
					Matcher charsetMatcher = CONTENT_TYPE_CHARSET_PATTERN.matcher(type);
					if (charsetMatcher.find()) {
						Matcher delimiterMatcher = CONTENT_TYPE_CHARSET_DELIMITER_PATTERN.matcher(type);
						String charsetName = null;
						if (delimiterMatcher.find(charsetMatcher.end())) {
							charsetName = type.substring(charsetMatcher.end(), delimiterMatcher.start());
						} else {
							charsetName = type.substring(charsetMatcher.end());
						}
						return Charset.forName(charsetName);
					}
					
				}
			} else {
				log.warn("encoding not defined in content encoding nor in content type");
			}
		} catch (IllegalCharsetNameException icne) {
			log.warn("failed to determine response encoding", icne);
		} catch (UnsupportedCharsetException uce) {
			log.warn("failed to determine response encoding", uce);
		}
		log.warn("back to default platform encoding " + DEFAULT_PLATFORM_ENCODING);
		return defaultCharset;
	}
}
