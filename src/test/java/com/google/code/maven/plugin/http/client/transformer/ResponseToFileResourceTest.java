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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;

/**
 * {@link BufferedReaderResourceWriter} Test case
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 */
public class ResponseToFileResourceTest {
	
	@Test
	public void transformTest() throws Exception {
		String charset = "UTF-8";
		HttpResponse response = createMock(HttpResponse.class);// 
		HttpEntity entity = createMock(HttpEntity.class);// createMockBuilder(HttpEntity.class).createMock();
		ResponseToFileResource transformer = new ResponseToFileResource();
		transformer.setTarget(new FileSystemResource(new File("target/test-files/" + getClass().getSimpleName())));
		ByteArrayInputStream in = new ByteArrayInputStream("Hello World".getBytes(charset));
		expect(response.getEntity()).andReturn(entity);
		expect(entity.getContent()).andReturn(in);
		replay(response, entity);
		transformer.transform(response);
		verify(response, entity);
	}
}
