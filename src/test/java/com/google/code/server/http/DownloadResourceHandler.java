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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Download File Servlet
 * 
 * @author Nadim Benabdenbi
 * 
 */
public class DownloadResourceHandler implements HttpHandler {
	
	private final Resource resource;
	
	public DownloadResourceHandler(Resource resource) {
		Assert.notNull(resource);
		Assert.isTrue(resource.exists());
		this.resource = resource;
	}
	
	public void handle(HttpExchange exchange) throws IOException {
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "application/x-download");
		responseHeaders.set("Content-Disposition", "attachment; filename=" + resource.getFilename());
		exchange.sendResponseHeaders(200, 0);
		// Sends the resource.
		OutputStream out = exchange.getResponseBody();
		InputStream in = resource.getInputStream();
		byte buffer[] = new byte[1024];
		int size;
		while ((size = in.read(buffer)) > 0) {
			out.write(buffer, 0, size);
		}
		in.close();
		out.close();
	}
}
