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

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Parameter
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 */
public class Parameter implements Serializable {
	
	/**
	 * unique serial version identifier.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the id
	 */
	private String id;
	
	/**
	 * the parameter name.
	 * 
	 * @parameter
	 * @required
	 */
	private String name;
	
	/**
	 * the parameter value.
	 * 
	 * @parameter
	 * @required
	 */
	private String value;
	
	/**
	 * the url codec (used to converts parameter name & value to valid url parameter.
	 */
	private final transient URLCodec codec = new URLCodec();
	
	/**
	 * @param name
	 * @param value
	 */
	public Parameter() {
		this(null, null);
	}
	
	/**
	 * @param name
	 * @param value
	 */
	public Parameter(String name, String value) {
		super();
		this.name = name;
		this.value = value;
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
	 * name getter.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * name setter.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * value getter.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * value setter.
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @param beanResolver
	 * @return
	 */
	public NameValuePair toNameValuePair() {
		return new BasicNameValuePair(name, value);
	}
	
	/**
	 * @param beanResolver
	 * @return
	 * @throws EncoderException
	 */
	public String toUrlParameter() throws EncoderException {
		return new StringBuilder(codec.encode(name)).append("=")//
				.append(codec.encode(value)).toString();
	}
	
	@Override
	public String toString() {
		return new StringBuilder("Parameter[").append(name).append("=").append(value).append("]").toString();
	}
}
