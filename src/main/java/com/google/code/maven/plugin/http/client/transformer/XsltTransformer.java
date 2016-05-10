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
import java.io.IOException;
import java.util.Map;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.google.code.maven.plugin.http.client.utils.FileResourceUtils;

/**
 * <h1>transforms the {@link File} source file to the output {@link File} using the provided xsl style sheet</h1>
 * <p>
 * Note: a Transformer implementation should be provided (e.g. saxon, xalan as plugin dependencies)
 * </p>
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 * @see AbstractTransformer
 * @see File
 */
public class XsltTransformer extends AbstractTransformer<Resource, Resource> {
	
	/**
	 * the xslt output result
	 * 
	 * @parameter
	 */
	private Resource target;
	
	/**
	 * the xsl resource
	 * 
	 * @parameter
	 */
	private Resource xsl;
	
	/**
	 * the xsl transformer parameters
	 * 
	 * @parameter
	 */
	private Map<String, String> parameters;
	
	/**
	 * overwrites target indicator default-value="true"
	 */
	private final boolean overwrite = true;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(target, "output file could not be null");
		Assert.notNull(xsl, "xsl style sheet file could not be null");
		Assert.isTrue(xsl.exists(), "xsl style sheet not found [" + xsl.getURL() + "]");
	}
	
	@Override
	protected Resource doTransform(Resource xml) throws Exception {
		Assert.notNull(xml, "xml file could not be null");
		Assert.isTrue(xml.exists(), "xml file not found [" + xml + "]");
		File result = FileResourceUtils.create(target, overwrite);
		log.info("tranforming [" + xml + "] with [" + xsl + "] to [" + target + "]");
		TransformerFactory factory = TransformerFactory.newInstance();
		StreamSource xslSource = new StreamSource(xsl.getFile());
		StreamSource xmlSource = new StreamSource(xml.getFile());
		StreamResult outputTarget = new StreamResult(result);
		javax.xml.transform.Transformer transformer = factory.newTransformer(xslSource);
		if (parameters != null) {
			for (String key : parameters.keySet()) {
				transformer.setParameter(key, parameters.get(key));
			}
		}
		transformer.transform(xmlSource, outputTarget);
		log.info("xslt tranformer processed.");
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
	 * @return the xsl
	 */
	public Resource getXsl() {
		return xsl;
	}
	
	/**
	 * @param xsl
	 *            the xsl to set
	 * @throws IOException
	 */
	public void setXsl(Resource xsl) throws IOException {
		Assert.notNull(xsl, "xsl style sheet file could not be null");
		Assert.isTrue(xsl.exists(), "xsl style sheet not found [" + xsl.getURL() + "]");
		this.xsl = xsl;
	}
	
	/**
	 * @return the parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * @return the overwrite
	 */
	public boolean isOverwrite() {
		return overwrite;
	}
	
}
