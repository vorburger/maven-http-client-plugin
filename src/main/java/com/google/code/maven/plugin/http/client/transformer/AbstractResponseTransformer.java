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
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.maven.plugin.MojoExecutionException;
import org.springframework.util.Assert;

import com.google.code.maven.plugin.http.client.utils.HttpEntityUtils;

/**
 * <h1>abstract class for all {@link HttpResponse} concrete {@link Transformer}</h1>
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 * @see Transformer
 * @see AbstractTransformer
 * @see HttpResponse
 */
public abstract class AbstractResponseTransformer<Target> extends AbstractTransformer<HttpResponse, Target> {
	
	private String defaultCharset = "UTF-8";
	
	@Override
	protected final Target doTransform(HttpResponse response) throws MojoExecutionException {
		Assert.notNull(response, "response can not be null");
		HttpEntity entity = response.getEntity();
		log.debug("Response protocol version: " + response.getProtocolVersion());
		log.debug("Response status line: " + response.getStatusLine());
		log.debug("Response params: " + response.getParams());
		log.debug("Response locale: " + response.getLocale());
		StringBuilder headers = new StringBuilder("{ ");
		for (Header header : response.getAllHeaders()) {
			headers.append("[").append(header.getName()).append("=").append(header.getValue()).append("] ");
		}
		headers.append("}");
		log.debug("Response headers: " + headers);
		log.debug("Response content length: " + entity.getContentLength());
		log.debug("Response content encoding: " + entity.getContentEncoding());
		log.debug("Response content type: " + entity.getContentType());
		try {
			return transform(response);
		} catch (Throwable t) {
			if (t instanceof MojoExecutionException) {
				throw (MojoExecutionException) t;
			} else {
				throw new MojoExecutionException("failed to transform", t);
			}
		}
	}
	
	/**
	 * http response reader getter
	 * 
	 * @param response
	 *            the http response
	 * @return the reader derived from the entity input stream
	 * 
	 * @throws IllegalStateException
	 *             when the entity is not resulting from a http response
	 * @throws IOException
	 *             in case of network failure
	 */
	protected final BufferedReader getContentReader(HttpResponse response) throws IllegalStateException, IOException {
		return new BufferedReader(new InputStreamReader(response.getEntity().getContent(), HttpEntityUtils.getEncoding(response.getEntity(), log)));
	}
	
	/**
	 * http content reader getter
	 * 
	 * @param entity
	 *            the http entity
	 * @return the reader derived from the entity input stream
	 * 
	 * @throws IllegalStateException
	 *             when the entity is not resulting from a http response
	 * @throws IOException
	 *             in case of network failure
	 */
	protected final InputStream getContentInputStream(HttpEntity entity) throws IllegalStateException, IOException {
		return entity.getContent();
	}
	
	/**
	 * http entity transformation
	 * 
	 * @param response
	 *            the http response to transfom
	 * @return
	 * @throws Exception
	 *             in case of an unexpected transformation error
	 */
	protected abstract Target transform(HttpResponse response) throws Exception;
	
	/**
	 * @return the defaultCharset
	 */
	public String getDefaultCharset() {
		return defaultCharset;
	}
	
	/**
	 * @param defaultCharset
	 *            the defaultCharset to set
	 */
	public void setDefaultCharset(String defaultCharset) {
		this.defaultCharset = defaultCharset;
	}
	
}
