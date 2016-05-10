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
package com.google.code.server.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.springframework.core.io.Resource;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * 
 * @author Nadim Benabdenbi
 * 
 */
public class StaticJiraXmlSearchHandler implements HttpHandler {
	
	public static String context = "jira/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml";
	
	private final Map<String, Resource> search;
	
	public StaticJiraXmlSearchHandler(Map<String, Resource> search) {
		this.search = search;
	}
	
	@SuppressWarnings("unchecked")
	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		if (requestMethod.equalsIgnoreCase("GET")) {
			Headers responseHeaders = exchange.getResponseHeaders();
			Map<String, Object> params = (Map<String, Object>) exchange.getAttribute("parameters");
			String jqlQuery = (String) params.get("jqlQuery");
			if (jqlQuery.startsWith("id=")) {
				jqlQuery = jqlQuery.substring(3);
			}
			Resource resource = search.get(jqlQuery);
			if (resource != null && resource.exists()) {
				responseHeaders.set("Content-Type", "text/xml");
				exchange.sendResponseHeaders(200, 0);
				InputStream in = new FileInputStream(resource.getFile());
				OutputStream out = exchange.getResponseBody();
				int size;
				byte buffer[] = new byte[256];
				while ((size = in.read(buffer)) > 0) {
					out.write(buffer, 0, size);
				}
				in.close();
				out.close();
			} else {
				exchange.sendResponseHeaders(404, 0);
				exchange.getResponseBody().close();
			}
		}
	}
}
