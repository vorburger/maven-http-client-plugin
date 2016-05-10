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
package com.google.code.maven.plugin.http.client.config;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Unique Bean Identifier: Utility class for generating unique bean id
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 */
public abstract class UniqueBeanIdentifier {
	private static final AtomicInteger id = new AtomicInteger();
	
	/**
	 * 
	 * @return a unique numeric id
	 */
	public static String getNextId() {
		return getNextId((String) null);
	}
	
	/**
	 * 
	 * @param javaType
	 *            the javaType
	 * @return when javaType is not null : a string starting with the javaType simple name followed by a dot and ending
	 *         with a unique numeric id. otherwise a unique numeric id
	 */
	public static String getNextId(Class<?> javaType) {
		return (javaType == null ? "" : javaType.getName() + ".") + id.incrementAndGet();
	}
	
	/**
	 * 
	 * @param javaType
	 *            the javaType
	 * @return when javaType is not null : a string starting with the javaType simple name followed by a dot and ending
	 *         with a unique numeric id. otherwise a unique numeric id
	 */
	public static String getNextId(String javaType) {
		return (javaType == null ? "" : javaType + ".") + id.incrementAndGet();
	}
}
