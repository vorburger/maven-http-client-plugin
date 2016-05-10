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
package com.google.code.maven.plugin.http.client.config;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.maven.project.MavenProject;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.util.Assert;

/**
 * Configuration Context
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 */
public class BeanContext {
	
	/**
	 * the unique id holder.
	 */
	protected static final AtomicInteger id = new AtomicInteger();
	
	/**
	 * the bean factory.
	 */
	private final DefaultListableBeanFactory beanFactory;
	
	/**
	 * default constructor.
	 * 
	 * @param project
	 */
	public BeanContext(MavenProject project) {
		super();
		Assert.notNull(project, "project can not be null");
		beanFactory = new DefaultListableBeanFactory();
		beanFactory.registerSingleton("project", project);
		beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver());
	}
	
	public <T> T getBean(String name, Class<T> requiredType) {
		return beanFactory.getBean(name, requiredType);
	}
	
	public void registerConfigFactory(BeanDefinition<?> bean) {
		beanFactory.registerSingleton(Integer.toString(id.incrementAndGet()), bean);
	}
	
	/**
	 * @return the beanFactory
	 */
	public DefaultListableBeanFactory getBeanFactory() {
		return beanFactory;
	}
	
}
