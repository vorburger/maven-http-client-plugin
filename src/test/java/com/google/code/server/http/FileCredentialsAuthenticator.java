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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.BasicAuthenticator;

/**
 * 
 * @author Nadim Benabdenbi
 * 
 */
public class FileCredentialsAuthenticator extends BasicAuthenticator {
	
	/**
	 * the credentials map
	 */
	private final Map<String, String> credentials;
	
	/**
	 * 
	 * @param realm
	 * @param credentialsFile
	 * @throws IOException
	 */
	public FileCredentialsAuthenticator(String realm, final File credentialsFile) throws IOException {
		super(realm);
		credentials = new HashMap<String, String>();
		String line;
		BufferedReader reader = new BufferedReader(new FileReader(credentialsFile));
		while ((line = reader.readLine()) != null) {
			if (!line.startsWith("#") && line.trim().length() > 0) {
				String[] credential = line.split("/");
				credentials.put(credential[0], credential[1]);
			}
		}
	}
	
	@Override
	public boolean checkCredentials(String login, String password) {
		if (login == null || password == null) {
			return false;
		}
		return password.equals(credentials.get(login));
	}
	
}
