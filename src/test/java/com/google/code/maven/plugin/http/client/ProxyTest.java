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
package com.google.code.maven.plugin.http.client;

import static org.junit.Assert.fail;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link Proxy} test case
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 */
public class ProxyTest {
	
	Proxy proxy;
	
	@Before
	public void before() {
		proxy = new Proxy();
	}
	
	@Test
	public void testGetHost() {
		Assert.assertNull(proxy.getHost());
	}
	
	@Test
	public void testSetHost() {
		String expected = "a host";
		proxy.setHost(expected);
		Assert.assertEquals(expected, proxy.getHost());
		proxy.setHost(null);
		Assert.assertNull(proxy.getHost());
	}
	
	@Test
	public void testGetPort() {
		Assert.assertEquals(0, proxy.getPort());
	}
	
	@Test
	public void testSetPort() {
		int expected = 8080;
		proxy.setPort(expected);
		Assert.assertEquals(expected, proxy.getPort());
		try {
			proxy.setPort(0);
			fail();
		} catch (IllegalArgumentException ias) {
			Assert.assertEquals(expected, proxy.getPort());
		}
		try {
			proxy.setPort(65536);
			fail();
		} catch (IllegalArgumentException ias) {
			Assert.assertEquals(expected, proxy.getPort());
		}
	}
	
	@Test
	public void testGetCredentials() {
		Assert.assertNull(proxy.getHost());
	}
	
	@Test
	public void testSetCredentials() {
		Credentials expected = new Credentials();
		proxy.setCredentials(expected);
		Assert.assertEquals(expected, proxy.getCredentials());
		proxy.setCredentials(null);
		Assert.assertNull(proxy.getCredentials());
	}
	
	@Test
	public void testPrepare() {
		proxy.setHost("localhost");
		proxy.setPort(8080);
		DefaultHttpClient client = new DefaultHttpClient();
		proxy.prepare(client);
		HttpHost httpHost = (HttpHost) client.getParams().getParameter(ConnRoutePNames.DEFAULT_PROXY);
		Assert.assertNotNull(httpHost);
		Assert.assertEquals(httpHost.getHostName(), proxy.getHost());
		Assert.assertEquals(httpHost.getPort(), proxy.getPort());
		Assert.assertEquals(httpHost.getSchemeName(), HttpHost.DEFAULT_SCHEME_NAME);
		
		Credentials credentials = new Credentials();
		credentials.setLogin("login");
		credentials.setPassword("password");
		proxy.setCredentials(credentials);
		client = new DefaultHttpClient();
		proxy.prepare(client);
		httpHost = (HttpHost) client.getParams().getParameter(ConnRoutePNames.DEFAULT_PROXY);
		Assert.assertNotNull(httpHost);
		Assert.assertEquals(httpHost.getHostName(), proxy.getHost());
		Assert.assertEquals(httpHost.getPort(), proxy.getPort());
		Assert.assertEquals(httpHost.getSchemeName(), HttpHost.DEFAULT_SCHEME_NAME);
		org.apache.http.auth.Credentials credentials2 = client.getCredentialsProvider().getCredentials(new AuthScope("localhost", 8080));
		Assert.assertNotNull(credentials2);
		Assert.assertEquals("login", credentials2.getUserPrincipal().getName());
		Assert.assertEquals("password", credentials2.getPassword());
	}
	
	@Test
	public void testToString() {
		proxy.setHost("localhost");
		proxy.setPort(8080);
		Assert.assertEquals(proxy.toString(), "Proxy[localhost:8080 authentication=false]");
		
		Credentials credentials = new Credentials();
		credentials.setLogin("login");
		credentials.setPassword("password");
		proxy.setCredentials(credentials);
		Assert.assertEquals(proxy.toString(), "Proxy[localhost:8080 authentication=true]");
		
	}
	
}
