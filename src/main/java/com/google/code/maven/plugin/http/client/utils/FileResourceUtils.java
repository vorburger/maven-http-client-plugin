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
package com.google.code.maven.plugin.http.client.utils;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * 
 * @author Nadim Benabdenbi
 * 
 */
public abstract class FileResourceUtils {
	
	/**
	 * 
	 * @param resource
	 * @param overwrite
	 * @return
	 * @throws IOException
	 */
	public static final File create(Resource resource, boolean overwrite) throws IOException {
		File file = resource.getFile();
		if (file.exists()) {
			Assert.isTrue(overwrite, "file already exists: enable overwriting");
		} else {
			File parent = file.getParentFile();
			if (parent != null && !parent.exists()) {
				Assert.isTrue(parent.mkdirs(), "failed to create file directory tree");
			}
		}
		
		return file;
	}
}
