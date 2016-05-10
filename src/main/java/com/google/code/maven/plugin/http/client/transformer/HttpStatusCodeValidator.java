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

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * 
 * @author Nadim Benabdenbi
 * 
 */
public class HttpStatusCodeValidator extends AbstractResponseTransformer<HttpResponse> {
	
	private final List<Integer> statusCodes;
	
	public HttpStatusCodeValidator() {
		statusCodes = new ArrayList<Integer>();
		statusCodes.add(200);
	}
	
	@Override
	protected HttpResponse transform(HttpResponse response) throws Exception {
		int statusCode = response.getStatusLine().getStatusCode();
		for (int validCode : statusCodes) {
			if (statusCode == validCode) {
				return response;
			}
		}
		throw new MojoExecutionException("invalid response status code " + statusCode);
	}
	
	/**
	 * @return the statusCodes
	 */
	public List<Integer> getStatusCodes() {
		return new ArrayList<Integer>(statusCodes);
	}
	
	/**
	 * @param statusCodes
	 *            the statusCodes to set
	 */
	public void setStatusCodes(List<Integer> statusCodes) {
		this.statusCodes.clear();
		this.statusCodes.addAll(statusCodes);
	}
	
}
