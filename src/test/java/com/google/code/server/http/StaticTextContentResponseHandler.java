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
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * get handler which return a static content response as plain text
 * 
 * @author Nadim Benabdenbi
 * 
 * @see HttpHandler
 */
public class StaticTextContentResponseHandler implements HttpHandler {
	
	private final String content;
	
	private final Charset charset;
	
	private final long responseTime;
	
	public StaticTextContentResponseHandler() {
		this(Charset.forName("UTF-8"), "<html>\n\t<body>Hello World!</body>\n</html>", -1);
	}
	
	public StaticTextContentResponseHandler(String content) {
		this(Charset.forName("UTF-8"), content, -1);
	}
	
	public StaticTextContentResponseHandler(Charset charset, String content, long responseTime) {
		this.content = content;
		this.charset = charset;
		this.responseTime = responseTime;
	}
	
	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		if (requestMethod.equalsIgnoreCase("GET")) {
			if (responseTime > 0) {
				try {
					Thread.sleep(responseTime);
				} catch (InterruptedException e) {
					throw new IOException("failed to sleep", e);
				}
			}
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "text/plain;charset=" + charset.name());
			exchange.sendResponseHeaders(200, 0);
			OutputStream responseBody = exchange.getResponseBody();
			responseBody.write(content.getBytes(charset));
			responseBody.close();
		}
	}
}