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

import org.apache.commons.codec.EncoderException;
import org.apache.http.NameValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link Parameter} test case
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 */
public class RequestParameterTest {
	
	Parameter parameter;
	
	@Before
	public void before() {
		parameter = new Parameter();
	}
	
	@Test
	public void testRequestParameter() {
		Assert.assertNull(parameter.getName());
		Assert.assertNull(parameter.getValue());
	}
	
	@Test
	public void testRequestParameterStringString() {
		String expectedName = "a name";
		String expectedValue = "a value";
		parameter = new Parameter(expectedName, expectedValue);
		Assert.assertEquals(expectedName, parameter.getName());
		Assert.assertEquals(expectedValue, parameter.getValue());
	}
	
	@Test
	public void testGetName() {
		Assert.assertNull(parameter.getName());
	}
	
	@Test
	public void testSetName() {
		String expected = "a name";
		parameter.setName(expected);
		Assert.assertEquals(expected, parameter.getName());
		parameter.setName(null);
		Assert.assertNull(parameter.getName());
	}
	
	@Test
	public void testGetValue() {
		Assert.assertNull(parameter.getValue());
	}
	
	@Test
	public void testSetValue() {
		String expected = "a value";
		parameter.setValue(expected);
		Assert.assertEquals(expected, parameter.getValue());
		parameter.setValue(null);
		Assert.assertNull(parameter.getValue());
	}
	
	@Test
	public void testToNameValuePair() {
		String expectedName = "a name";
		String expectedValue = "a value";
		parameter = new Parameter(expectedName, expectedValue);
		NameValuePair nameValuePair = parameter.toNameValuePair();
		Assert.assertNotNull(nameValuePair);
		Assert.assertEquals(expectedName, nameValuePair.getName());
		Assert.assertEquals(expectedValue, nameValuePair.getValue());
	}
	
	@Test
	public void testToUrlParameter() throws EncoderException {
		String expectedName = "name";
		String expectedValue = "value";
		parameter = new Parameter(expectedName, expectedValue);
		String urlParameter = parameter.toUrlParameter();
		Assert.assertNotNull(urlParameter);
		Assert.assertEquals(urlParameter, expectedName + "=" + expectedValue);
		
		expectedName = "name ";
		expectedValue = "valu&";
		parameter = new Parameter(expectedName, expectedValue);
		urlParameter = parameter.toUrlParameter();
		Assert.assertNotNull(urlParameter);
		Assert.assertEquals("name+=valu%26", urlParameter);
	}
	
	@Test
	public void testToString() {
		Assert.assertEquals("Parameter[null=null]", parameter.toString());
		parameter = new Parameter("name", "value");
		Assert.assertEquals("Parameter[name=value]", parameter.toString());
	}
	
}
