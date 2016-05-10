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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.google.code.maven.plugin.http.client.utils.FileResourceUtils;

/**
 * Writes an {@link HttpResponse} entity content to a file {@link Resource}
 * 
 * @author Nadim Benabdenbi
 * 
 */
public class ResponseToFileResource extends AbstractResponseTransformer<Resource> {
	
	/**
	 * the target resource
	 * 
	 * @parameter
	 */
	private Resource target = new FileSystemResource(new File("/target/generated-html/response-" + System.currentTimeMillis() + ".tmp"));
	
	/**
	 * overwrites target indicator default-value="true"
	 */
	private boolean overwrite = true;
	
	/**
	 * defaul constructor
	 */
	public ResponseToFileResource() {
		super();
	}
	
	/**
	 * defaul constructor
	 */
	public ResponseToFileResource(Resource target) {
		super();
		setTarget(target);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(target, "target can not be null");
	}
	
	@Override
	protected Resource transform(HttpResponse response) throws Exception {
		InputStream in = getContentInputStream(response.getEntity());
		File file = FileResourceUtils.create(target, overwrite);
		FileOutputStream out = new FileOutputStream(file);
		byte buffer[] = new byte[256];
		int size;
		while ((size = in.read(buffer)) > 0) {
			out.write(buffer, 0, size);
			out.flush();
		}
		out.close();
		in.close();
		return target;
	}
	
	/**
	 * @return the target
	 */
	public Resource getTarget() {
		return target;
	}
	
	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(Resource target) {
		this.target = target;
	}
	
	/**
	 * @return the overwrite
	 */
	public boolean isOverwrite() {
		return overwrite;
	}
	
	/**
	 * @param overwrite
	 *            the overwrite to set
	 */
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
	
}
