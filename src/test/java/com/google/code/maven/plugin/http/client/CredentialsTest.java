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

import org.apache.http.auth.UsernamePasswordCredentials;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link Credentials} test case
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 */
public class CredentialsTest {
	
	Credentials credentials;
	
	@Before
	public void before() {
		credentials = new Credentials();
	}
	
	@Test
	public void testGetLogin() {
		Assert.assertNull(credentials.getLogin());
	}
	
	@Test
	public void testSetLogin() {
		String expected = "a login";
		credentials.setLogin(expected);
		Assert.assertEquals(expected, credentials.getLogin());
		credentials.setLogin(null);
		Assert.assertNull(credentials.getLogin());
	}
	
	@Test
	public void testGetPassword() {
		Assert.assertNull(credentials.getPassword());
	}
	
	@Test
	public void testSetPassword() {
		String expected = "a password";
		credentials.setPassword(expected);
		Assert.assertEquals(expected, credentials.getPassword());
		credentials.setPassword(null);
		Assert.assertNull(credentials.getPassword());
	}
	
	@Test
	public void testToUsernamePasswordCredentials() {
		try {
			credentials.toUsernamePasswordCredentials();
			Assert.fail("unexpected");
		} catch (IllegalArgumentException iae) {
			//	
		}
		String expectedLogin = "a login";
		String expectedPassword = "a password";
		credentials.setLogin(expectedLogin);
		UsernamePasswordCredentials usernamePasswordCredentials = credentials.toUsernamePasswordCredentials();
		Assert.assertNotNull(usernamePasswordCredentials);
		Assert.assertEquals(expectedLogin, usernamePasswordCredentials.getUserName());
		Assert.assertNull(usernamePasswordCredentials.getPassword());
		//
		credentials.setPassword(expectedPassword);
		usernamePasswordCredentials = credentials.toUsernamePasswordCredentials();
		Assert.assertNotNull(usernamePasswordCredentials);
		Assert.assertEquals(expectedLogin, usernamePasswordCredentials.getUserName());
		Assert.assertEquals(expectedPassword, usernamePasswordCredentials.getPassword());
	}
	
}
