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

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.EncoderException;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/**
 * RequestConfiguration
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 */
public class Request implements Serializable {
	
	/**
	 * unique serial version identifier.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the id
	 */
	private String id;
	
	/**
	 * @parameter
	 * @required
	 */
	private String url;
	
	/**
	 * @parameter default-value="get"
	 */
	private String method = "get";
	
	/**
	 * @parameter
	 */
	private Parameter parameters[];
	/**
	 * @parameter
	 */
	private Credentials credentials;
	
	/**
	 * @parameter
	 */
	private FormCredentials formCredentials;
	
	private transient HttpHost targetHost;
	
	private transient URL finalUrl;
	
	/**
	 * url getter.
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * url setter.
	 * 
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * method getter.
	 * 
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	
	/**
	 * method setter.
	 * 
	 * @param method
	 *            the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
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
	 * formCredentials getter.
	 * 
	 * @return the formCredentials
	 */
	public FormCredentials getFormCredentials() {
		return formCredentials;
	}
	
	/**
	 * formCredentials setter.
	 * 
	 * @param formCredentials
	 *            the formCredentials to set
	 */
	public void setFormCredentials(FormCredentials formCredentials) {
		this.formCredentials = formCredentials;
	}
	
	/**
	 * parameters getter.
	 * 
	 * @return the parameters
	 */
	public Parameter[] getParameters() {
		return parameters;
	}
	
	/**
	 * parameters setter.
	 * 
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(Parameter parameters[]) {
		this.parameters = parameters;
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
	 * url getter.
	 * 
	 * @return the url
	 * @throws MalformedURLException
	 * @throws
	 */
	private void parseUrl() throws MojoExecutionException {
		if (finalUrl == null) {
			StringBuilder builder = new StringBuilder(url);
			if ("get".equalsIgnoreCase(method) && parameters != null && parameters.length > 0) {
				for (int i = 0; i < parameters.length; i++) {
					if (i == 0 && builder.indexOf("?") < 0) {
						builder.append("?");
					} else {
						builder.append("&");
					}
					try {
						builder.append(parameters[i].toUrlParameter());
					} catch (EncoderException ee) {
						throw new MojoExecutionException("malformed parameter " + parameters[i], ee);
					}
				}
			}
			try {
				finalUrl = new URL(builder.toString());
			} catch (MalformedURLException mue) {
				throw new MojoExecutionException("failed to parse url " + url, mue);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public URL getFinalUrl() {
		return finalUrl;
	}
	
	/**
	 * @return
	 * @throws MalformedURLException
	 */
	public HttpHost buildHttpHost(Log log) throws MojoExecutionException {
		if (targetHost == null) {
			parseUrl();
			targetHost = new HttpHost(finalUrl.getHost(), finalUrl.getPort(), finalUrl.getProtocol());
		}
		return targetHost;
	}
	
	/**
	 * @param httpclient
	 * @param parser
	 * @return
	 * @throws MojoExecutionException
	 * @throws IOException
	 */
	public HttpRequestBase buildHttpRequestBase(DefaultHttpClient httpclient, Log log) throws MojoExecutionException, IOException {
		buildHttpHost(log);
		// credential
		if (credentials != null) {
			// simple authentication
			httpclient.getCredentialsProvider().setCredentials(//
					new AuthScope(finalUrl.getHost(), finalUrl.getPort()),//
					credentials.toUsernamePasswordCredentials());
		}
		if (formCredentials != null) {
			// form authentication
			formCredentials.authenticate(httpclient, log);
		}
		HttpRequestBase httpRequest = null;
		if ("get".equalsIgnoreCase(method)) {
			HttpGet httpGet = new HttpGet(finalUrl.toString());
			httpRequest = httpGet;
		} else if ("post".equalsIgnoreCase(method)) {
			HttpPost httpPost = new HttpPost(finalUrl.getPath());
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (parameters != null) {
				for (Parameter parameter : parameters) {
					nvps.add(parameter.toNameValuePair());
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpRequest = httpPost;
		} else {
			throw new MojoExecutionException("unknow method " + method);
		}
		return httpRequest;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("Request[").append(method.toUpperCase());
		result.append(" url=").append(url);
		if (credentials != null) {
			result.append(" authentication=").append(formCredentials != null ? "form" : "basic");
		} else {
			result.append(" authentication=false");
		}
		result.append(" parameters:");
		if (parameters != null) {
			for (Parameter parameter : parameters) {
				result.append(parameter).append(" ");
			}
		} else {
			result.append("none");
		}
		return result.append("]").toString();
	}
}
