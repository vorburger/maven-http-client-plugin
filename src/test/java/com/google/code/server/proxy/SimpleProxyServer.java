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
package com.google.code.server.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Semaphore;

import org.springframework.util.Assert;

import com.google.code.server.Server;

/**
 * 
 * @author 797409
 * 
 */
public class SimpleProxyServer implements Server {
	
	private int proxyPort = 8080;
	
	private String remoteHost;
	
	private int remotePort = 80;
	
	private ServerSocket server;
	
	private final Semaphore lock = new Semaphore(1);
	
	private boolean running = false;
	
	private Thread serverThread;
	
	private final Thread shutDownHook;
	
	public SimpleProxyServer() {
		this(8080, "localhost", 80);
	}
	
	public SimpleProxyServer(int proxyPort, String remoteHost, int remotePort) {
		super();
		this.proxyPort = proxyPort;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		shutDownHook = new Thread("proxy-server-sh") {
			@Override
			public void run() {
				try {
					System.out.println("[INFO] running proxy server shut down hook...");
					if (running) {
						shutdown();
					}
				} catch (Exception e) {
					System.out.println("failed to shut down");
					e.printStackTrace();
				}
			}
		};
	}
	
	public void start() throws IOException, InterruptedException {
		lock.acquire();
		try {
			Assert.isTrue(!running, "already running!");
			System.out.println("[INFO] Starting proxy server on port " + proxyPort);
			serverThread = new Thread() {
				@Override
				public void run() {
					try {
						server = new ServerSocket(proxyPort);
						running = true;
						// Loop to listen for incoming connection,
						// and accept if there is one
						Socket incoming = null;
						Socket outgoing = null;
						while (running) {
							try {
								// Create the 2 sockets to transmit incoming and outgoing traffic of proxy server
								incoming = server.accept();
								System.out.println("[INFO] proxying to " + remoteHost + ":" + remotePort + " ...");
								outgoing = new Socket(remoteHost, remotePort);
								// Create the 2 threads for the incoming
								// and outgoing traffic of proxy server
								final ProxyThread thread1 = new ProxyThread(incoming, outgoing);
								thread1.start();
								
								final ProxyThread thread2 = new ProxyThread(outgoing, incoming);
								thread2.start();
								
								Thread wait = new Thread() {
									@Override
									public void run() {
										try {
											thread1.join();
											thread2.join();
											System.out.println("[INFO] fullfilled proxy request to " + remoteHost + ":" + remotePort + " ...");
										} catch (InterruptedException e) {
											System.err.println("[ERROR] redirection failed");
											e.printStackTrace();
										}
									}
								};
								wait.start();
							} catch (Exception e) {
								if (running) {
									System.err.println("redirection failed");
									e.printStackTrace();
								}
							}
						}
					} catch (Exception e1) {
						running = false;
						System.err.println("[ERROR] failed to start");
						e1.printStackTrace();
					}
				}
			};
			serverThread.start();
		} finally {
			lock.release();
		}
	}
	
	@Override
	public void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(shutDownHook);
	}
	
	@Override
	public void shutdown() throws Exception {
		lock.acquire();
		try {
			Assert.isTrue(running, "not running!");
			running = false;
			server.close();
			serverThread.join();
			System.out.println("[INFO] proxy server stopped on port " + proxyPort);
		} finally {
			lock.release();
		}
	}
	
	public boolean isRunning() {
		return running;
	}
	
	class ProxyThread extends Thread {
		Socket incoming, outgoing;
		
		ProxyThread(Socket in, Socket out) {
			incoming = in;
			outgoing = out;
		}
		
		// does the data transfers
		@Override
		public void run() {
			byte[] buffer = new byte[50];
			int size = 0;
			OutputStream toClient;
			InputStream fromClient;
			try {
				toClient = outgoing.getOutputStream();
				fromClient = incoming.getInputStream();
				while (size != -1) {
					try {
						size = fromClient.read(buffer);
					} catch (SocketException se) {
						// do nothing
						size = -1;
					}
					if (size == -1) {
						incoming.close();
						outgoing.close();
					} else {
						toClient.write(buffer, 0, size);
					}
				}
			} catch (Exception e) {
				System.err.println("[ERROR] proxy redirection failure");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return the proxyPort
	 */
	public int getProxyPort() {
		return proxyPort;
	}
	
	/**
	 * @param proxyPort
	 *            the proxyPort to set
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
	
	/**
	 * @return the remoteHost
	 */
	public String getRemoteHost() {
		return remoteHost;
	}
	
	/**
	 * @param remoteHost
	 *            the remoteHost to set
	 */
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}
	
	/**
	 * @return the remotePort
	 */
	public int getRemotePort() {
		return remotePort;
	}
	
}
