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

import org.springframework.util.Assert;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Authenticator.Result;

/**
 * 
 * @author Nadim Benabdenbi
 * 
 */
public class AuthenticationHttpHandler implements HttpHandler {
	
	private final BasicAuthenticator authenticator;
	
	private final HttpHandler successAuthenticationHandler;
	
	private final HttpHandler failedAuthenticationHandler;
	
	public AuthenticationHttpHandler(BasicAuthenticator authenticator, HttpHandler successAuthenticationHandler) {
		this(authenticator, successAuthenticationHandler, new HttpErrorHandler(403));
	}
	
	public AuthenticationHttpHandler(BasicAuthenticator authenticator, HttpHandler successAuthenticationHandler, HttpHandler failedAuthenticationHandler) {
		Assert.notNull(authenticator);
		Assert.notNull(successAuthenticationHandler);
		Assert.notNull(failedAuthenticationHandler);
		this.authenticator = authenticator;
		this.successAuthenticationHandler = successAuthenticationHandler;
		this.failedAuthenticationHandler = failedAuthenticationHandler;
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Result authenticate = authenticator.authenticate(exchange);
		if (authenticate instanceof Authenticator.Success) {
			successAuthenticationHandler.handle(exchange);
		}
		if (authenticate instanceof Authenticator.Retry) {
			exchange.sendResponseHeaders(((Authenticator.Retry) authenticate).getResponseCode(), 0);
			exchange.getResponseBody().close();
		} else {
			failedAuthenticationHandler.handle(exchange);
		}
	}
}
