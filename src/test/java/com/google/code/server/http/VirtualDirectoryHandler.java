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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * serves a virtual directory pages
 * 
 * @author Nadim Benabdenbi
 * 
 * @see HttpHandler
 */
public class VirtualDirectoryHandler implements HttpHandler {
	
	private final Charset charset;
	
	private final File directory;
	
	private final String context;
	
	public VirtualDirectoryHandler(File directory, String context) {
		this(Charset.forName("UTF-8"), directory, context);
	}
	
	public VirtualDirectoryHandler(Charset charset, File directory, String context) {
		this.charset = charset;
		this.directory = directory;
		this.context = context;
	}
	
	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		if (requestMethod.equalsIgnoreCase("GET")) {
			Headers responseHeaders = exchange.getResponseHeaders();
			String fileName = exchange.getHttpContext().getPath();
			File file = directory;
			if (fileName.length() > context.length() + 1) {
				fileName.substring(context.length() + 1);
				file = new File(directory, fileName);
			}
			if (file.exists() && !file.isDirectory()) {
				responseHeaders.set("Content-Type", "text/plain;charset=" + charset.name());
				exchange.sendResponseHeaders(200, 0);
				OutputStream responseBody = exchange.getResponseBody();
				InputStream in = new FileInputStream(file);
				int size;
				byte buffer[] = new byte[256];
				while ((size = in.read(buffer)) > 0) {
					responseBody.write(buffer, 0, size);
				}
				in.close();
				responseBody.close();
			} else {
				exchange.sendResponseHeaders(404, 0);
			}
		}
	}
}
