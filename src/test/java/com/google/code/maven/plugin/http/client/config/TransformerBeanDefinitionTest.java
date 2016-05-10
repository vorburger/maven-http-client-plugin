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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.Assert;
import org.junit.Test;

import com.google.code.maven.plugin.http.client.transformer.ResponseConsolePrinter;
import com.google.code.maven.plugin.http.client.transformer.Transformer;

/**
 * {@link SimpleBeanDefinition} Test case
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 */
public class TransformerBeanDefinitionTest {
	
	@Test
	public void testPropertySetting() throws MojoExecutionException {
		TransformerBeanDefinition configuration = new TransformerBeanDefinition();
		configuration.setClassName(ResponseConsolePrinter.class.getName());
		String expectedConsoleType = "err";
		BeanProperty property = new BeanProperty();
		property.setName("console");
		property.setValue(expectedConsoleType);
		configuration.setProperties(new BeanProperty[] { property });
		Transformer<?, ?> transformer = configuration.create(new BeanContext(new MavenProject()).getBeanFactory());
		Assert.assertTrue(transformer instanceof ResponseConsolePrinter);
		ResponseConsolePrinter consoleTransformer = (ResponseConsolePrinter) transformer;
		Assert.assertEquals(expectedConsoleType, consoleTransformer.getConsole());
	}
	
	@Test
	public void testExpressionSetting() throws MojoExecutionException {
		MavenProject project = new MavenProject();
		String expectedConsoleType = "err";
		project.setName(expectedConsoleType);
		BeanContext configurationContext = new BeanContext(project);
		TransformerBeanDefinition transformerConfiguration = new TransformerBeanDefinition();
		transformerConfiguration.setClassName(ResponseConsolePrinter.class.getName());
		BeanProperty property = new BeanProperty();
		property.setName("console");
		property.setValue(expectedConsoleType);
		transformerConfiguration.setProperties(new BeanProperty[] { property });
		Transformer<?, ?> transformer = transformerConfiguration.create(configurationContext.getBeanFactory());
		Assert.assertTrue(transformer instanceof ResponseConsolePrinter);
		Assert.assertEquals(expectedConsoleType, ((ResponseConsolePrinter) transformer).getConsole());
	}
	
}
