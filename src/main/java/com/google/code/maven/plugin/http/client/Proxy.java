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
/**
 * 
 */
package com.google.code.maven.plugin.http.client;

import java.io.Serializable;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.util.Assert;

/**
 * Proxy bean definition
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 * @see Credentials for authentication
 * @see HttpClientMojo for usage
 */
public class Proxy implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the id
	 */
	private String id;
	
	/**
	 * the proxy host (host IP address or host name).
	 * 
	 * @parameter
	 * @required
	 */
	private String host;
	
	/**
	 * the proxy port
	 * 
	 * @parameter
	 * @required
	 */
	private int port;
	
	/**
	 * the proxy credential (use when proxy requires authentication)
	 * 
	 * @parameter
	 */
	private Credentials credentials;
	
	/**
	 * host getter.
	 * 
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * host setter.
	 * 
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * port getter.
	 * 
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * port setter.
	 * 
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		Assert.isTrue(port > 0 && port < 65536, "port number should be strictly positive and less than 65536");
		this.port = port;
	}
	
	/**
	 * credentials getter.
	 * 
	 * @return the credentials
	 */
	public Credentials getCredentials() {
		return credentials;
	}
	
	/**
	 * credentials setter.
	 * 
	 * @param credentials
	 *            the credentials to set
	 */
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	
	/**
	 * id getter.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * id setter.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * prepares the httpclient for using this proxy configuration when opening http connection
	 * 
	 * @param httpclient
	 */
	public void prepare(DefaultHttpClient httpclient) {
		HttpHost proxyHost = new HttpHost(host, port);
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost);
		if (credentials != null) {
			httpclient.getCredentialsProvider().setCredentials(//
					new AuthScope(host, port),//
					credentials.toUsernamePasswordCredentials());
		}
	}
	
	@Override
	public String toString() {
		return new StringBuilder("Proxy[").append(host).append(":").append(port).append(" authentication=").append(credentials != null).append("]").toString();
	}
}
