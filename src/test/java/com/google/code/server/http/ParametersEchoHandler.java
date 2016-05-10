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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * 
 * @author Nadim Benabdenbi
 * 
 */
public class ParametersEchoHandler implements HttpHandler {
	
	@SuppressWarnings("unchecked")
	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		if (requestMethod.equalsIgnoreCase("GET")) {
			Headers responseHeaders = exchange.getResponseHeaders();
			Map<String, Object> params = (Map<String, Object>) exchange.getAttribute("parameters");
			responseHeaders.set("Content-Type", "text/xml");
			exchange.sendResponseHeaders(200, 0);
			OutputStream out = exchange.getResponseBody();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write("<html>\n\t<body>Parameters:\n");
			writer.write("<table><tr><hr>name</hr><hr>value</hr></tr>\n");
			for (String key : params.keySet()) {
				writer.write("<tr><td>");
				writer.write(key);
				writer.write("</td><td>");
				writer.write("" + params.get(key));
				writer.write("</td></td>\n");
			}
			writer.write("</table></body>\n</html>");
			out.close();
		}
	}
}
