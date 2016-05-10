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
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.springframework.util.Assert;

import com.google.code.server.Server;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * simple http server with some common handlers
 * 
 * @author Nadim Benabdenbi
 * 
 */
public class SimpleHttpServer implements Server {
	
	private final int port;
	
	private final Map<String, HttpHandler> httpHandlers = new HashMap<String, HttpHandler>();
	
	private final Semaphore lock = new Semaphore(1);
	
	private transient HttpServer server;
	
	private boolean running = false;
	
	private final Thread shutDownHook;
	
	public SimpleHttpServer() {
		this(80, null);
	}
	
	public SimpleHttpServer(int port) {
		this(port, null);
	}
	
	public SimpleHttpServer(int port, Map<String, HttpHandler> httpHandlers) {
		super();
		this.port = port;
		if (httpHandlers != null) {
			this.httpHandlers.putAll(httpHandlers);
		}
		withHttpHandler("/server/shutdown.html", new HttpHandler() {
			@Override
			public void handle(HttpExchange exchange) throws IOException {
				try {
					Headers responseHeaders = exchange.getResponseHeaders();
					responseHeaders.set("Content-Type", "text/plain;charset=UTF-8");
					exchange.sendResponseHeaders(200, 0);
					OutputStream responseBody = exchange.getResponseBody();
					responseBody.write("shutting down".getBytes("UTF-8"));
					responseBody.close();
					shutdown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		shutDownHook = new Thread("http-server-sh") {
			@Override
			public void run() {
				try {
					System.out.println("[INFO] running http server shut down hook...");
					if (running) {
						shutdown();
					}
				} catch (InterruptedException e) {
					System.out.println("failed to shut down");
					e.printStackTrace();
				}
			}
		};
	}
	
	public SimpleHttpServer withHttpHandler(String context, HttpHandler httpHandler) {
		httpHandlers.put(context, httpHandler);
		return this;
	}
	
	public void start() throws IOException, InterruptedException {
		lock.acquire();
		try {
			Assert.isTrue(!running, "already running!");
			System.out.println("[INFO] Starting Http server on port " + port);
			InetSocketAddress addr = new InetSocketAddress(port);
			server = HttpServer.create(addr, 0);
			for (String context : httpHandlers.keySet()) {
				server.createContext(context, httpHandlers.get(context)).getFilters().add(new ParameterFilter());
			}
			server.setExecutor(Executors.newCachedThreadPool());
			server.start();
			System.out.println("[INFO] Http Server is listening on port " + port);
			running = true;
		} finally {
			lock.release();
		}
	}
	
	public void shutdown() throws InterruptedException {
		lock.acquire();
		try {
			Assert.isTrue(running, "not running!");
			server.stop(0);
			System.out.println("[INFO] Http server stopped on port " + port);
			running = false;
		} finally {
			lock.release();
		}
	}
	
	public void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(shutDownHook);
	}
	
	public boolean isRunning() {
		return running;
	}
}
