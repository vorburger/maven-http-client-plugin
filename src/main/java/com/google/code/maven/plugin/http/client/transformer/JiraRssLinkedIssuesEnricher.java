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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.maven.plugin.MojoExecutionException;
import org.springframework.beans.BeansException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.Assert;

import com.google.code.maven.plugin.http.client.Proxy;
import com.google.code.maven.plugin.http.client.Request;
import com.google.code.maven.plugin.http.client.Parameter;
import com.google.code.maven.plugin.http.client.config.ProxyBeanDefinition;
import com.google.code.maven.plugin.http.client.config.RequestBeanDefinition;
import com.google.code.maven.plugin.http.client.utils.HttpRequestUtils;

/**
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 */
public class JiraRssLinkedIssuesEnricher extends AbstractTransformer<Resource, Resource> {
	
	/**
	 * {@value}
	 */
	private static final String START_OPEN_TAG = "<";
	
	/**
	 * {@value}
	 */
	private static final String END_TAG = ">";
	
	/**
	 * {@value}
	 */
	private static final String START_CLOSE_TAG = "</";
	
	/**
	 * {@value}
	 */
	private static final String CHANNEL_LABEL = "channel";
	
	/**
	 * {@value}
	 */
	private static final String CHANNEL_END_TAG = START_CLOSE_TAG + CHANNEL_LABEL + END_TAG;
	
	/**
	 * {@value}
	 */
	private static final String ITEM_LABEL = "item";
	
	/**
	 * {@value}
	 */
	private static final String ITEM_START_TAG = START_OPEN_TAG + ITEM_LABEL + END_TAG;
	
	/**
	 * {@value}
	 */
	private static final String ITEM_END_TAG = START_CLOSE_TAG + ITEM_LABEL + END_TAG;
	
	/**
	 * {@value}
	 */
	private static final String KEY_LABEL = "key";
	
	/**
	 * {@value}
	 */
	private static final String KEY_START_TAG = START_OPEN_TAG + KEY_LABEL + " ";
	
	/**
	 * {@value}
	 */
	private static final String KEY_END_TAG = START_CLOSE_TAG + KEY_LABEL + END_TAG;
	
	/**
	 * @parameter
	 */
	private String linkType = "parent";
	
	/**
	 * @parameter depth
	 */
	private int depth = 1;
	
	/**
	 * @parameter console
	 */
	private Resource target = new FileSystemResource(new File("target/jira-enriched-rss.xml"));
	
	/**
	 * delete source indicator
	 * 
	 * @parameter console default-value="true"
	 */
	private final boolean deleteSource = true;
	
	/**
	 * delete source indicator
	 * 
	 * @parameter concurrent requests default-value="20"
	 */
	private int concurrentRequests = 20;
	
	/**
	 * source charset
	 * 
	 * @parameter Resource charset default-value="UTF-8"
	 */
	private final String charset = "UTF-8";
	
	private final HashSet<String> rssIssues = new HashSet<String>();
	
	private final HashSet<String> linkedIssues = new HashSet<String>();
	
	private final ResponseToString httpEntityContentToString = new ResponseToString();
	
	private final SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("jira-request-exec");
	
	private final AtomicInteger remaining = new AtomicInteger();
	
	public JiraRssLinkedIssuesEnricher() {
		super();
	}
	
	private String queryForJira(String jira) throws MojoExecutionException, ClientProtocolException, IOException {
		RequestBeanDefinition requestConfiguration = beanFactory.getBean(RequestBeanDefinition.class);
		Request request = requestConfiguration.create(beanFactory);
		for (Parameter parameter : request.getParameters()) {
			if ("jqlQuery".equals(parameter.getName())) {
				parameter.setValue("id=" + jira);
				break;
			}
		}
		Proxy proxy = null;
		try {
			ProxyBeanDefinition proxyConfiguration = beanFactory.getBean(ProxyBeanDefinition.class);
			proxy = proxyConfiguration.create(beanFactory);
		} catch (BeansException be) {
			proxy = null;
		}
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = HttpRequestUtils.query(httpclient, request, proxy, getLog());
		String content = httpEntityContentToString.transform(response, log);
		httpclient.getConnectionManager().shutdown();
		
		int start = content.indexOf(ITEM_START_TAG);
		if (start > 0) {
			int end = content.indexOf(ITEM_END_TAG);
			if (end > 0) {
				return content.substring(start, end + ITEM_END_TAG.length());
			}
		}
		return null;
	}
	
	protected HashSet<String> inspectItem(String issueContent) {
		HashSet<String> linkedIssues = new HashSet<String>();
		int start = issueContent.indexOf(KEY_START_TAG);
		start = issueContent.indexOf(END_TAG, start) + 1;
		int end = issueContent.indexOf(KEY_END_TAG, start);
		String issueId = issueContent.substring(start, end);
		rssIssues.add(issueId);
		log.info("rss issue found :" + issueId);
		int index = 0;
		String linkFieldOpenTag = START_OPEN_TAG + linkType + " ";
		String linkFieldEndTag = START_CLOSE_TAG + linkType + END_TAG;
		while ((index = issueContent.indexOf(linkFieldOpenTag, index)) > 0) {
			start = issueContent.indexOf(END_TAG, index) + 1;
			end = issueContent.indexOf(linkFieldEndTag, start);
			issueId = issueContent.substring(start, end);
			log.info("linked issue found :" + issueId);
			linkedIssues.add(issueId);
			index = end;
		}
		return linkedIssues;
	}
	
	@Override
	protected Resource doTransform(Resource input) throws Exception {
		Assert.notNull(input, "source can not be null");
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(input.getFile()), charset));
		if (target.getFile().getParentFile() != null && !target.getFile().getParentFile().exists()) {
			Assert.isTrue(target.getFile().getParentFile().mkdirs(), "failed to make directory tree for [" + target.getFile().getParentFile() + "]");
		}
		String line;
		StringBuilder item = new StringBuilder();
		BufferedWriter writer = new BufferedWriter(new FileWriter(target.getFile()));
		// read rss and find jira links
		while ((line = reader.readLine()) != null) {
			if (line.contains(CHANNEL_END_TAG)) {
				break;
			}
			if (line.contains(ITEM_END_TAG)) {
				item.append(line.substring(0, line.indexOf(ITEM_END_TAG)));
				linkedIssues.addAll(inspectItem(item.toString()));
			}
			if (line.contains(ITEM_START_TAG)) {
				item = new StringBuilder();
				item.append(line.substring(line.indexOf(ITEM_START_TAG)));
			} else {
				item.append(line).append("\n");
			}
			writer.write(line);
			writer.newLine();
			writer.flush();
		}
		enrich(writer, linkedIssues, depth - 1);
		while (remaining.get() > 0) {
			Thread.sleep(100);
		}
		// write end of rss
		do {
			writer.write(line);
		} while ((line = reader.readLine()) != null);
		reader.close();
		writer.close();
		if (deleteSource) {
			Assert.isTrue(input.getFile().delete(), "failed to delete source");
		}
		return target;
	}
	
	private void enrich(final BufferedWriter writer, final HashSet<String> linkedIssues, final int depth) throws ClientProtocolException, IOException {
		// removes duplicates
		for (String jira : rssIssues) {
			linkedIssues.remove(jira);
		}
		// request for jira missing in the rss
		for (final String issue : linkedIssues) {
			remaining.incrementAndGet();
			asyncTaskExecutor.execute(new Runnable() {
				public void run() {
					try {
						rssIssues.add(issue);
						String content = queryForJira(issue);
						if (content != null) {
							synchronized (writer) {
								writer.write(content);
								writer.newLine();
								writer.flush();
							}
							log.warn("issue [" + issue + "] added");
							if (depth > 0) {
								enrich(writer, inspectItem(content), depth - 1);
							}
						} else {
							log.warn("issue [" + issue + "] not found");
						}
					} catch (MojoExecutionException e) {
						log.warn("issue [" + issue + "] not found");
					} catch (ClientProtocolException e) {
						log.warn("issue [" + issue + "] not found");
					} catch (IOException e) {
						log.warn("issue [" + issue + "] not found");
					}
					remaining.decrementAndGet();
				}
			});
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(target, "output can not be null");
		Assert.notNull(linkType, "enriched link type can not be null");
		Assert.isTrue(concurrentRequests > 0, "concurrent requests can not be less than 1");
		Assert.isTrue(depth > 0, "depth can not be less than 1");
		Charset.forName(charset);
		asyncTaskExecutor.setConcurrencyLimit(concurrentRequests);
	}
	
	/**
	 * target getter
	 * 
	 * @return the target
	 */
	public Resource getTarget() {
		return target;
	}
	
	/**
	 * target setter
	 * 
	 * @param target
	 *            the target to set
	 */
	public void setTarget(Resource output) {
		this.target = output;
	}
	
	/**
	 * depth getter
	 * 
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}
	
	/**
	 * depth setter
	 * 
	 * @param depth
	 *            the depth to set
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	/**
	 * @return the linkType
	 */
	public String getLinkType() {
		return linkType;
	}
	
	/**
	 * @param linkType
	 *            the linkType to set
	 */
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	
	/**
	 * @return the deleteSource
	 */
	public boolean isDeleteSource() {
		return deleteSource;
	}
	
	/**
	 * @return the concurrentRequests
	 */
	public int getConcurrentRequests() {
		return concurrentRequests;
	}
	
	/**
	 * @param concurrentRequests
	 *            the concurrentRequests to set
	 */
	public void setConcurrentRequests(int concurrentRequests) {
		this.concurrentRequests = concurrentRequests;
	}
	
	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}
	
}
