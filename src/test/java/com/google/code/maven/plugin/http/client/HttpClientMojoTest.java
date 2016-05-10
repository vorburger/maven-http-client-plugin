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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.google.code.maven.plugin.http.client.config.BeanProperty;
import com.google.code.maven.plugin.http.client.config.ProxyBeanDefinition;
import com.google.code.maven.plugin.http.client.config.RequestBeanDefinition;
import com.google.code.maven.plugin.http.client.config.TransformerBeanDefinition;
import com.google.code.maven.plugin.http.client.transformer.HttpStatusCodeValidator;
import com.google.code.server.http.AuthenticationHttpHandler;
import com.google.code.server.http.DownloadResourceHandler;
import com.google.code.server.http.FileCredentialsAuthenticator;
import com.google.code.server.http.SimpleHttpServer;
import com.google.code.server.http.StaticTextContentResponseHandler;
import com.google.code.server.proxy.SimpleProxyServer;

/**
 * {@link HttpClientMojo} Test case
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 */
public class HttpClientMojoTest {
	
	SimpleHttpServer httpServer;
	SimpleProxyServer proxyServer;
	
	@Before
	public void before() throws IOException, InterruptedException {
		httpServer = new SimpleHttpServer();
		httpServer.withHttpHandler("/helloWorld.html", new StaticTextContentResponseHandler("<html>\n\t<body>Hello World!</body>\n</html>"));
		httpServer.withHttpHandler("/hello-world.txt", new DownloadResourceHandler(new ClassPathResource("hello-world.txt", SimpleHttpServer.class)));
		httpServer.withHttpHandler("/helloWorldProxy.html", new StaticTextContentResponseHandler("<html>\n\t<body>Hello World throw proxy!</body>\n</html>"));
		httpServer.withHttpHandler("/helloWorldAuthentication.html", new AuthenticationHttpHandler(new FileCredentialsAuthenticator("Basic",
				new ClassPathResource("credentials.properties", SimpleHttpServer.class).getFile()), new StaticTextContentResponseHandler(
				"<html>\n\t<body>Hello World Authenticated!</body>\n</html>")));
		httpServer.start();
		proxyServer = new SimpleProxyServer();
		proxyServer.start();
	}
	
	@After
	public void after() throws Exception {
		httpServer.shutdown();
		proxyServer.shutdown();
	}
	
	@Test
	public void testExecute() throws Exception {
		testPlainTextResponse();
		testDownloadFile();
		test404Response();
		proxyTest();
		// httpAuthenticationTest();
		
	}
	
	public void testPlainTextResponse() throws Exception {
		HttpClientMojo mojo = new HttpClientMojo();
		mojo.setFailSafe(false);
		mojo.setProject(new MavenProject());
		RequestBeanDefinition request = new RequestBeanDefinition();
		request.setProperties(new BeanProperty[] { new BeanProperty("url", "http://localhost/helloWorld.html") });
		mojo.setRequest(request);
		mojo.execute();
	}
	
	public void testDownloadFile() throws Exception {
		HttpClientMojo mojo = new HttpClientMojo();
		mojo.setFailSafe(false);
		mojo.setProject(new MavenProject());
		RequestBeanDefinition request = new RequestBeanDefinition();
		request.setProperties(new BeanProperty[] { new BeanProperty("url", "http://localhost/hello-world.txt") });
		mojo.setRequest(request);
		mojo.execute();
	}
	
	public void test404Response() throws Exception {
		MavenProject project = new MavenProject();
		HttpClientMojo mojo = new HttpClientMojo();
		mojo.setFailSafe(false);
		mojo.setProject(project);
		RequestBeanDefinition request = new RequestBeanDefinition();
		request.setProperties(new BeanProperty[] { new BeanProperty("url", "http://localhost/something") });
		mojo.setRequest(request);
		mojo.setTransformers(new TransformerBeanDefinition[] {//
				new TransformerBeanDefinition(HttpStatusCodeValidator.class.getName()) });
		try {
			mojo.execute();
			Assert.fail("unexpected");
		} catch (MojoExecutionException m) {
			Assert.assertEquals("invalid response status code 404", m.getMessage());
		}
	}
	
	public void proxyTest() throws Exception {
		HttpClientMojo mojo = new HttpClientMojo();
		mojo.setFailSafe(false);
		mojo.setProject(new MavenProject());
		RequestBeanDefinition request = new RequestBeanDefinition();
		request.setProperties(new BeanProperty[] { new BeanProperty("url", "http://localhost/helloWorldProxy.html") });
		mojo.setRequest(request);
		mojo.setProxy(new ProxyBeanDefinition("localhost", "8080"));
		mojo.execute();
	}
	
	// public void httpAuthenticationTest() throws Exception {
	// HttpClientMojo mojo = new HttpClientMojo();
	// mojo.setFailSafe(false);
	// mojo.setProject(new MavenProject());
	// RequestBeanDefinition request = new RequestBeanDefinition();
	// request.setUrl("http://localhost/helloWorldAuthentication.html");
	// request.setCredentials(new CredentialsDefinition());
	// request.getCredentials().setLogin("guest");
	// request.getCredentials().setPassword("guest");
	// mojo.setRequest(request);
	// mojo.execute();
	// }
	//	
	// public void formAuthentication() throws Exception {
	// MavenProject project = new MavenProject();
	// HttpClientMojo mojo = new HttpClientMojo();
	// mojo.setProject(project);
	// RequestBeanDefinition request = new RequestBeanDefinition();
	// FormCredentialsDefinition formCredentials = new FormCredentialsDefinition();
	// formCredentials.setFormUrl("http://jira-eqd.bfi.echonet/jira/login.jsp");
	// formCredentials.setPassword("guest");
	// formCredentials.setPasswordParameterName("os_password");
	// formCredentials.setLogin("guest");
	// formCredentials.setLoginParameterName("os_username");
	// ParameterBeanDefinition cookie = new ParameterBeanDefinition();
	// cookie.setName("os_cookie");
	// cookie.setValue("true");
	// formCredentials.setParameters(new ParameterBeanDefinition[] { cookie });
	// request.setFormCredentials(formCredentials);
	// request.setUrl("http://jira-eqd.bfi.echonet/jira/secure/Dashboard.jspa");
	// mojo.setRequest(request);
	// mojo.execute();
	// }
}
