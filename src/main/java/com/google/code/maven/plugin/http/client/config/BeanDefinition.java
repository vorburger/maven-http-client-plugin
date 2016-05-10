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

import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * Bean contract
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 */
public interface BeanDefinition<T> {
	
	/**
	 * builds the target bean with the provided factory
	 * 
	 * @param factory
	 *            the factory bean
	 * @return T the
	 * @throws Exception
	 *             when the target bean can not be built
	 */
	public T create(DefaultListableBeanFactory factory) throws Exception;
}
