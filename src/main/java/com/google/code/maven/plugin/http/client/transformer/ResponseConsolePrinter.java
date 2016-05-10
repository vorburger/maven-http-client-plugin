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
package com.google.code.maven.plugin.http.client.transformer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.http.HttpResponse;
import org.apache.maven.plugin.MojoExecutionException;
import org.springframework.util.Assert;

/**
 * <h1>prints the {@link BufferedReader} content to the console</h1>
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 * @see AbstractTransformer
 * @see BufferedReader
 * @see System#out
 * @see System#err
 */
public class ResponseConsolePrinter extends AbstractResponseTransformer<Object> {
	
	/**
	 * the print console. default value is {@link System#out}
	 */
	private PrintStream console = System.out;
	
	/**
	 * 
	 */
	@Override
	protected Object transform(HttpResponse response) throws MojoExecutionException {
		try {
			BufferedReader reader = getContentReader(response);
			String line;
			while ((line = reader.readLine()) != null) {
				console.println(line);
			}
			return null;
		} catch (IllegalStateException ise) {
			throw new MojoExecutionException("failed to write to system out", ise);
		} catch (IOException ioe) {
			throw new MojoExecutionException("failed to write to system out", ioe);
		}
		
	}
	
	/**
	 * @param console
	 *            the console to set. valid values in {out,err}.
	 */
	public void setConsole(String console) {
		Assert.notNull(console, "the console can not be null");
		if ("out".equals(console)) {
			this.console = System.out;
		} else if ("err".equals(console)) {
			this.console = System.err;
		} else {
			throw new IllegalArgumentException("invalid console value [" + console + "]: should be equal to err or out.");
		}
	}
	
	/**
	 * @return the console
	 */
	public String getConsole() {
		return console == System.out ? "out" : "err";
	}
	
}
