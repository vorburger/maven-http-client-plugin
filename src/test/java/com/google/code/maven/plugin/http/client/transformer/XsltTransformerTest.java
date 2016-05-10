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

import junit.framework.Assert;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * {@link XsltTransformer} Test case
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 */
public class XsltTransformerTest {
	
	@Test
	public void transformTest() throws Exception {
		XsltTransformer transformer = new XsltTransformer();
		Resource output = new FileSystemResource("target/test-files/" + getClass().getSimpleName() + ".html");
		if (output.exists()) {
			Assert.assertTrue(output.getFile().delete());
		}
		transformer.setTarget(output);
		transformer.setXsl(new ClassRelativeResourceLoader(getClass()).getResource(getClass().getSimpleName() + ".xsl"));
		Resource source = new ClassPathResource(getClass().getSimpleName() + ".xml", getClass());
		transformer.transform(source, new SystemStreamLog());
		Assert.assertTrue(output.exists());
	}
}
