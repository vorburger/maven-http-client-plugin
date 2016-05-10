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

import java.io.Serializable;

import org.apache.http.auth.UsernamePasswordCredentials;

/**
 * <h1>Authentication credentials</h1> used for proxy authentication, HTTP basic authentication and form authentication
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 * @see Proxy for concrete usage
 * @see Request for concrete usage
 */
public class Credentials implements Serializable {
	/**
	 * unique serial version identifier.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the id
	 */
	private String id;
	
	/**
	 * the authentication login.
	 * 
	 * @parameter
	 * @required
	 */
	private String login;
	
	/**
	 * the authentication password.
	 * 
	 * @parameter
	 * @required
	 */
	private String password;
	
	/**
	 * login getter
	 * 
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	
	/**
	 * login setter
	 * 
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * password getter.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * password setter.
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * converts to {@link UsernamePasswordCredentials}
	 * 
	 * @return
	 */
	public UsernamePasswordCredentials toUsernamePasswordCredentials() {
		return new UsernamePasswordCredentials(login, password);
	}
}
