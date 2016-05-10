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
import org.springframework.util.Assert;
import org.springframework.util.MethodInvoker;

/**
 * <h1>Third party transformer container</h1>
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
public class GenericTransformer<Source, Target> extends AbstractTransformer<Source, Target> {
	
	/**
	 * the target class
	 * 
	 * @required
	 */
	private Class<?> targetClass;
	
	/**
	 * the target method
	 * 
	 * @required
	 */
	private String targetMethod;
	
	@SuppressWarnings("unchecked")
	@Override
	protected Target doTransform(Source source) throws Exception {
		Assert.notNull(targetClass, "target class can not be null");
		Assert.notNull(targetMethod, "target method can not be null");
		MethodInvoker invoker = new MethodInvoker();
		invoker.setArguments(new Object[] { source });
		invoker.setTargetClass(targetClass);
		invoker.setTargetMethod(targetMethod);
		try {
			invoker.prepare();
		} catch (Exception e) {
			throw new MojoExecutionException("failed to find method [" + targetMethod + "] on class [" + targetClass + "]", e);
		}
		return (Target) invoker.invoke();
	}
	
	/**
	 * @return the targetClass
	 */
	public Class<?> getTargetClass() {
		return targetClass;
	}
	
	/**
	 * @param targetClass
	 *            the targetClass to set
	 */
	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}
	
	/**
	 * @return the targetMethod
	 */
	public String getTargetMethod() {
		return targetMethod;
	}
	
	/**
	 * @param targetMethod
	 *            the targetMethod to set
	 */
	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}
	
}
