package com.google.code.maven.plugin.http.client.config;

import com.google.code.maven.plugin.http.client.Proxy;
import com.google.code.maven.plugin.http.client.Request;
import com.google.code.maven.plugin.http.client.transformer.Transformer;

/**
 * maven {@link Request} bean definition.
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 * @see Proxy
 * @see SimpleBeanDefinition
 */
public final class TransformerBeanDefinition extends SimpleBeanDefinition<Transformer<?, ?>> {
	
	/**
	 * default constructor
	 */
	public TransformerBeanDefinition() {
		super();
	}
	
	/**
	 * class name constructor
	 */
	public TransformerBeanDefinition(String className) {
		super(className);
	}
}
