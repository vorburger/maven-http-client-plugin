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
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.google.code.maven.plugin.http.client.config.BeanContext;
import com.google.code.maven.plugin.http.client.config.ProxyBeanDefinition;
import com.google.code.maven.plugin.http.client.config.RequestBeanDefinition;
import com.google.code.maven.plugin.http.client.config.TransformerBeanDefinition;
import com.google.code.maven.plugin.http.client.transformer.ResponseConsolePrinter;
import com.google.code.maven.plugin.http.client.transformer.Transformer;
import com.google.code.maven.plugin.http.client.utils.HttpRequestUtils;

/**
 * Process an http request and transforms the response from the specified flow of transformers.
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 * @goal execute
 * @requiresProject
 */
public class HttpClientMojo extends AbstractMojo {
	
	/**
	 * The Maven project
	 * 
	 * @parameter expression="${project}"
	 * @required
	 */
	private MavenProject project;
	
	/**
	 * fail safe indicator e.g. indicates if a mojo failure should break the build process.
	 * 
	 * @parameter default-value="true"
	 */
	private boolean failSafe = true;
	
	/**
	 * the request configuration
	 * 
	 * @parameter
	 * @required
	 */
	private RequestBeanDefinition request;
	
	/**
	 * the proxy configuration if any
	 * 
	 * @parameter
	 */
	private ProxyBeanDefinition proxy;
	
	/**
	 * default flow of transformers consists on printing the response content to the console
	 * 
	 * @parameter
	 */
	private TransformerBeanDefinition transformers[];
	
	/**
	 * the bean resolver
	 */
	private transient BeanContext beanContext;
	
	/**
	 * 
	 */
	public HttpClientMojo() {
		super();
		transformers = new TransformerBeanDefinition[] { new TransformerBeanDefinition(ResponseConsolePrinter.class.getName()) };
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@SuppressWarnings("unchecked")
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().debug("loading context...");
		beanContext = new BeanContext(project);
		beanContext.registerConfigFactory(proxy);
		beanContext.registerConfigFactory(request);
		getLog().debug("registering bean defintions...");
		request.setId("request");
		String requestName = this.request.registerBeanDefinition(beanContext.getBeanFactory());
		getLog().debug("request registered.");
		String proxyName = null;
		if (proxy != null) {
			proxy.setId("proxy");
			proxyName = this.proxy.registerBeanDefinition(beanContext.getBeanFactory());
			getLog().debug("proxy registered.");
		}
		List<String> transformersIds = new ArrayList<String>(transformers.length);
		for (TransformerBeanDefinition transformerBeanDefinition : transformers) {
			transformersIds.add(transformerBeanDefinition.registerBeanDefinition(beanContext.getBeanFactory()));
			getLog().debug("transformer registered.");
		}
		getLog().debug("building beans...");
		Request request = beanContext.getBean(requestName, Request.class);
		getLog().debug("request built.");
		Proxy proxy = null;
		if (proxyName != null) {
			proxy = beanContext.getBean(proxyName, Proxy.class);
			getLog().debug("proxy built.");
		}
		for (String transformerId : transformersIds) {
			beanContext.getBean(transformerId, Transformer.class);
			getLog().debug("transformer built.");
		}
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			// client
			HttpResponse response = HttpRequestUtils.query(httpclient, request, proxy, getLog());
			long transformTime = System.currentTimeMillis();
			Object current = response;
			for (String transformerId : transformersIds) {
				Transformer transformer = beanContext.getBean(transformerId, Transformer.class);
				current = transformer.transform(current, getLog());
			}
			getLog().info("Response processed in " + (System.currentTimeMillis() - transformTime) + "ms");
			httpclient.getConnectionManager().shutdown();
		} catch (ClassCastException cce) {
			if (failSafe) {
				getLog().warn("invalid transformation flow", cce);
			} else {
				throw new MojoExecutionException("invalid transformation flow", cce);
			}
		} catch (ClientProtocolException cpe) {
			if (failSafe) {
				getLog().warn("invalid protocol", cpe);
			} else {
				throw new MojoExecutionException("invalid protocol", cpe);
			}
		} catch (IOException ioe) {
			if (failSafe) {
				getLog().warn("network failure", ioe);
			} else {
				throw new MojoExecutionException("network failure", ioe);
			}
		} catch (MojoExecutionException mee) {
			if (failSafe) {
				getLog().warn("mojo execution failure", mee);
			} else {
				throw mee;
			}
		}
	}
	
	/**
	 * project getter.
	 * 
	 * @return the project
	 */
	public MavenProject getProject() {
		return project;
	}
	
	/**
	 * project setter.
	 * 
	 * @param project
	 *            the project to set
	 */
	public void setProject(MavenProject project) {
		this.project = project;
	}
	
	/**
	 * @return the transformers
	 */
	public TransformerBeanDefinition[] getTransformers() {
		return transformers;
	}
	
	/**
	 * @param transformers
	 *            the transformers to set
	 */
	public void setTransformers(TransformerBeanDefinition transformers[]) {
		this.transformers = transformers;
	}
	
	/**
	 * failSafe getter.
	 * 
	 * @return the failSafe
	 */
	public boolean isFailSafe() {
		return failSafe;
	}
	
	/**
	 * failSafe setter.
	 * 
	 * @param failSafe
	 *            the failSafe to set
	 */
	public void setFailSafe(boolean failSafe) {
		this.failSafe = failSafe;
	}
	
	/**
	 * request getter.
	 * 
	 * @return the request
	 */
	public RequestBeanDefinition getRequest() {
		return request;
	}
	
	/**
	 * request setter.
	 * 
	 * @param request
	 *            the request to set
	 */
	public void setRequest(RequestBeanDefinition request) {
		this.request = request;
	}
	
	/**
	 * proxy getter.
	 * 
	 * @return the proxy
	 */
	public ProxyBeanDefinition getProxy() {
		return proxy;
	}
	
	/**
	 * proxy setter.
	 * 
	 * @param proxy
	 *            the proxy to set
	 */
	public void setProxy(ProxyBeanDefinition proxy) {
		this.proxy = proxy;
	}
	
	/**
	 * @return the parser
	 */
	public BeanContext getParser() {
		return beanContext;
	}
	
}
