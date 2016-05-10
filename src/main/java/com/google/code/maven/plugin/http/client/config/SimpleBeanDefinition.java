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
package com.google.code.maven.plugin.http.client.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.Assert;

/**
 * Transformer Configuration : maven configuration pojo for defining transformer properties.
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 */
public class SimpleBeanDefinition<T> implements BeanDefinition<T> {
	
	/**
	 * @parameter
	 */
	private String id;
	
	/**
	 * @parameter
	 * @required
	 */
	private String className;
	
	/**
	 * wrapped transformer properties
	 * 
	 * @parameter
	 */
	private BeanProperty properties[];
	
	/**
	 * default constructor
	 */
	public SimpleBeanDefinition() {
		super();
	}
	
	/**
	 * class name constructor
	 * 
	 * @param className
	 */
	public SimpleBeanDefinition(String className) {
		setClassName(className);
	}
	
	public final String registerBeanDefinition(DefaultListableBeanFactory factory) {
		String beanId = id != null ? id : UniqueBeanIdentifier.getNextId(className);
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(className);
		if (properties != null) {
			for (BeanProperty property : properties) {
				property.validate();
				if (property.getValue() != null) {
					builder.addPropertyValue(property.getName(), property.getValue());
				} else if (property.getRef() != null) {
					builder.addPropertyReference(property.getName(), property.getRef());
				} else if (property.getArray() != null) {
					builder.addPropertyValue(property.getName(), property.getArray());
				} else if (property.getList() != null) {
					builder.addPropertyValue(property.getName(), property.getList());
				} else if (property.getSet() != null) {
					builder.addPropertyValue(property.getName(), property.getSet());
				} else if (property.getMap() != null) {
					builder.addPropertyValue(property.getName(), property.getMap());
				} else if (property.getProperties() != null) {
					builder.addPropertyValue(property.getName(), property.getProperties());
				} else if (property.getBean() != null) {
					builder.addPropertyValue(property.getName(), factory.getBean(property.getBean().registerBeanDefinition(factory)));
				} else if (property.getBeans() != null) {
					List<Object> beans = new ArrayList<Object>();
					for (SimpleBeanDefinition<?> bean : property.getBeans()) {
						beans.add(factory.getBean(bean.registerBeanDefinition(factory)));
					}
					builder.addPropertyValue(property.getName(), beans);
				}
			}
		}
		factory.registerBeanDefinition(beanId, builder.getBeanDefinition());
		return beanId;
	}
	
	@SuppressWarnings("unchecked")
	public final T create(DefaultListableBeanFactory factory) throws MojoExecutionException {
		try {
			String beanId = registerBeanDefinition(factory);
			T result = (T) factory.getBean(beanId);
			return result;
		} catch (BeansException be) {
			throw new MojoExecutionException("failed to set up transformer", be);
		}
	}
	
	/**
	 * @return the className
	 */
	public String getClassName() {
		Assert.isTrue(className == null, "class name already set!");
		return className;
	}
	
	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * @return the properties
	 */
	public BeanProperty[] getProperties() {
		return properties;
	}
	
	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(BeanProperty[] properties) {
		this.properties = properties;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
}
