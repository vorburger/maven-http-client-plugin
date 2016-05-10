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

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import com.google.code.maven.plugin.http.client.Proxy;
import com.google.code.maven.plugin.http.client.Request;

/**
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 */
public class HttpRequestUtils {
	
	public static HttpResponse query(DefaultHttpClient httpclient, Request request, Proxy proxy, Log log) throws ClientProtocolException, IOException,
			MojoExecutionException {
		log.debug("preparing " + request);
		if (proxy != null) {
			log.info("setting up " + proxy + " for request " + request);
			proxy.prepare(httpclient);
		}
		HttpRequestBase httpRequest = request.buildHttpRequestBase(httpclient, log);
		HttpHost targetHost = request.buildHttpHost(log);
		log.debug("HTTP " + request.getMethod() + " url=" + request.getFinalUrl());
		long responseTime = System.currentTimeMillis();
		HttpResponse response = httpclient.execute(targetHost, httpRequest);
		log.debug("received response (time=" + (System.currentTimeMillis() - responseTime) + "ms) for request [" + "HTTP " + request.getMethod() + " url="
				+ request.getFinalUrl() + "]");
		return response;
	}
}
