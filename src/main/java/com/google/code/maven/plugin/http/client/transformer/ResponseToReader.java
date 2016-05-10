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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * {@link HttpEntity} {@link Transformer} returning the response content as a {@link BufferedReader}
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 * @see HttpEntity
 * @see BufferedReader
 * @see AbstractResponseTransformer
 */
public class ResponseToReader extends AbstractResponseTransformer<BufferedReader> {
	
	/**
	 * 
	 */
	@Override
	protected BufferedReader transform(HttpResponse response) throws MojoExecutionException {
		try {
			return getContentReader(response);
		} catch (Exception ise) {
			throw new MojoExecutionException("failed to get the response entity stream", ise);
		}
	}
	
}
