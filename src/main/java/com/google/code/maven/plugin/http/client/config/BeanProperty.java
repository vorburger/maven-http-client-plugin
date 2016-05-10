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

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.util.Assert;

/**
 * <h1>Bean property definition</h1>
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 */
public class BeanProperty implements Serializable {
	
	/**
	 * unique serial version identifier.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the property name.
	 * 
	 * @parameter
	 */
	private String name;
	
	/**
	 * the property value in case of a possible string conversion.
	 * 
	 * @parameter
	 */
	private String value;
	
	/**
	 * the reference value.
	 * 
	 * @parameter
	 */
	private String ref;
	
	/**
	 * the array value.
	 * 
	 * @parameter
	 */
	private String[] array;
	
	/**
	 * the list value.
	 * 
	 * @parameter
	 */
	private List<String> list;
	
	/**
	 * the {@link Set} value.
	 * 
	 * @parameter
	 */
	private Set<String> set;
	
	/**
	 * the {@link Map} value.
	 * 
	 * @parameter
	 */
	private Map<String, String> map;
	
	/**
	 * the {@link Properties} value.
	 * 
	 * @parameter
	 */
	private Properties properties;
	
	/**
	 * the beans array value
	 * 
	 * @parameter
	 */
	private SimpleBeanDefinition<?> bean;
	
	/**
	 * the beans array value
	 * 
	 * @parameter
	 */
	private SimpleBeanDefinition<?> beans[];
	
	/**
	 * default constructor
	 */
	public BeanProperty() {
		super();
	}
	
	/**
	 * default constructor
	 */
	public BeanProperty(String name, String value) {
		super();
		setName(name);
		setValue(value);
	}
	
	/**
	 * beans value constructor
	 */
	public BeanProperty(String name, SimpleBeanDefinition<?> beans[]) {
		super();
		setName(name);
		setBeans(beans);
	}
	
	/**
	 * validates that the property name is set and only one type of value is set.
	 */
	public void validate() {
		Assert.notNull(name, "a bean property name can not be null");
		int defined = 0;
		defined += value != null ? 1 : 0;
		defined += ref != null ? 1 : 0;
		defined += array != null ? 1 : 0;
		defined += list != null ? 1 : 0;
		defined += set != null ? 1 : 0;
		defined += map != null ? 1 : 0;
		defined += properties != null ? 1 : 0;
		defined += bean != null ? 1 : 0;
		defined += beans != null ? 1 : 0;
		Assert.isTrue(defined < 2, "multiple bean property values");
	}
	
	/**
	 * name getter.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * name setter.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * value getter.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * value setter.
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * reference getter.
	 * 
	 * @return the ref
	 */
	public String getRef() {
		return ref;
	}
	
	/**
	 * reference setter.
	 * 
	 * @param ref
	 *            the ref to set
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}
	
	/**
	 * array getter.
	 * 
	 * @return the array
	 */
	public String[] getArray() {
		return array;
	}
	
	/**
	 * array setter.
	 * 
	 * @param array
	 *            the array to set
	 */
	public void setArray(String[] array) {
		this.array = array;
	}
	
	/**
	 * list getter.
	 * 
	 * @return the list
	 */
	public List<String> getList() {
		return list;
	}
	
	/**
	 * list setter.
	 * 
	 * @param list
	 *            the list to set
	 */
	public void setList(List<String> list) {
		this.list = list;
	}
	
	/**
	 * set getter.
	 * 
	 * @return the set
	 */
	public Set<String> getSet() {
		return set;
	}
	
	/**
	 * set setter.
	 * 
	 * @param set
	 *            the set to set
	 */
	public void setSet(Set<String> set) {
		this.set = set;
	}
	
	/**
	 * map getter.
	 * 
	 * @return the map
	 */
	public Map<String, String> getMap() {
		return map;
	}
	
	/**
	 * map setter.
	 * 
	 * @param map
	 *            the map to set
	 */
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
	/**
	 * properties getter.
	 * 
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * properties setter.
	 * 
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	/**
	 * @return the bean
	 */
	public SimpleBeanDefinition<?> getBean() {
		return bean;
	}
	
	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(SimpleBeanDefinition<?> bean) {
		this.bean = bean;
	}
	
	/**
	 * @return the beans
	 */
	public SimpleBeanDefinition<?>[] getBeans() {
		return beans;
	}
	
	/**
	 * @param beans
	 *            the beans to set
	 */
	public void setBeans(SimpleBeanDefinition<?>[] beans) {
		this.beans = beans;
	}
	
}
