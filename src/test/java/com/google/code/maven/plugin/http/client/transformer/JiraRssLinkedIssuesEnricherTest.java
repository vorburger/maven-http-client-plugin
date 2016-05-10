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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.project.MavenProject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.code.maven.plugin.http.client.HttpClientMojo;
import com.google.code.maven.plugin.http.client.config.BeanProperty;
import com.google.code.maven.plugin.http.client.config.ParameterBeanDefinition;
import com.google.code.maven.plugin.http.client.config.RequestBeanDefinition;
import com.google.code.maven.plugin.http.client.config.SimpleBeanDefinition;
import com.google.code.maven.plugin.http.client.config.TransformerBeanDefinition;
import com.google.code.server.http.SimpleHttpServer;
import com.google.code.server.http.StaticJiraXmlSearchHandler;

/**
 * {@link JiraRssLinkedIssuesEnricherTest} Test case
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 */
public class JiraRssLinkedIssuesEnricherTest {
	
	SimpleHttpServer httpServer;
	
	@Before
	public void before() throws IOException, InterruptedException {
		httpServer = new SimpleHttpServer();
		Map<String, Resource> search = new HashMap<String, Resource>();
		search.put("0", new ClassPathResource("0.xml", SimpleHttpServer.class));
		search.put("1", new ClassPathResource("1.xml", SimpleHttpServer.class));
		search.put("1.1", new ClassPathResource("1.1.xml", SimpleHttpServer.class));
		search.put("1.2", new ClassPathResource("1.2.xml", SimpleHttpServer.class));
		search.put("2.1", new ClassPathResource("2.1.xml", SimpleHttpServer.class));
		search.put("2.2", new ClassPathResource("2.2.xml", SimpleHttpServer.class));
		search.put("search-query", new ClassPathResource("search-query.xml", SimpleHttpServer.class));
		httpServer.withHttpHandler("/jira/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml", new StaticJiraXmlSearchHandler(search));
		httpServer.start();
	}
	
	@After
	public void after() throws Exception {
		httpServer.shutdown();
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Test
	public void jiraParent() throws Exception {
		HttpClientMojo mojo = new HttpClientMojo();
		mojo.setProject(new MavenProject());
		RequestBeanDefinition request = new RequestBeanDefinition();
		request.setProperties(new BeanProperty[] {//				
				new BeanProperty("url", "http://localhost/jira/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml"),//
						new BeanProperty("parameters", new SimpleBeanDefinition<?>[] { new ParameterBeanDefinition("jqlQuery", "search-query") }) });
		mojo.setRequest(request);
		mojo.setTransformers(new TransformerBeanDefinition[] {//
				new TransformerBeanDefinition(ResponseToFileResource.class.getName()),//
						new TransformerBeanDefinition(JiraRssLinkedIssuesEnricher.class.getName()) });
		mojo.execute();
	}
}
