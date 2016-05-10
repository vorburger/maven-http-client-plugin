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
package com.google.code.maven.plugin.http.client.transformer;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * {@link HttpEntity} {@link Transformer} returning the response content as {@link String}
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 * @see AbstractResponseTransformer
 */
public class ResponseToString extends AbstractResponseTransformer<String> {
	
	/**
	 * 
	 */
	@Override
	protected String transform(HttpResponse response) throws MojoExecutionException {
		try {
			StringBuilder result = new StringBuilder();
			BufferedReader reader = getContentReader(response);
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line).append("\n");
			}
			return result.toString();
		} catch (IllegalStateException ise) {
			throw new MojoExecutionException("failed to read the http response content", ise);
		} catch (IOException ioe) {
			throw new MojoExecutionException("failed to read the http response content", ioe);
		}
	}
	
}
