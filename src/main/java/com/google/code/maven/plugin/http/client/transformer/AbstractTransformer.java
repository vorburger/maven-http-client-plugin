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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.Assert;

/**
 * <h1>abstract utility class for {@link Transformer} implementation</h1>
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 * @param <Source>
 *            the transformation source
 * @param <Target>
 *            the transformation target
 */
public abstract class AbstractTransformer<Source, Target> implements Transformer<Source, Target>, InitializingBean, BeanFactoryAware {
	
	/**
	 * the logger
	 */
	protected Log log = new SystemStreamLog();
	
	protected DefaultListableBeanFactory beanFactory;
	
	public final Target transform(Source source, Log log) throws MojoExecutionException {
		try {
			Assert.notNull(log, "the logger can not be null");
			Assert.notNull(source, "the source can not be null");
			this.log = log;
			return doTransform(source);
		} catch (Throwable t) {
			if (t instanceof MojoExecutionException) {
				throw (MojoExecutionException) t;
			} else {
				throw new MojoExecutionException("failed to transform", t);
			}
		}
	}
	
	/**
	 * transform
	 * 
	 * @param source
	 *            the source to transform
	 * @return the target object resulting from the source transformation
	 * @throws Exception
	 */
	protected abstract Target doTransform(Source source) throws Exception;
	
	/**
	 * @return the log
	 */
	public Log getLog() {
		return log;
	}
	
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}
	
	public void afterPropertiesSet() throws Exception {
	}
}
