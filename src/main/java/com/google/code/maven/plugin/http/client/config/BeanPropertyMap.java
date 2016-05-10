package com.google.code.maven.plugin.http.client.config;

/**
 * <h1>Bean property map</h1>
 * 
 * @author Nadim Benabdenbi
 * @version 1.0
 * @since JDK1.6
 * 
 */
public class BeanPropertyMap {
	
	/**
	 * @parameter
	 */
	private BeanPropertyEntry entries;
	
	/**
	 * @return the entries
	 */
	public BeanPropertyEntry getEntries() {
		return entries;
	}
	
	/**
	 * @param entries
	 *            the entries to set
	 */
	public void setEntries(BeanPropertyEntry entries) {
		this.entries = entries;
	}
	
	/**
	 * Bean entry
	 * 
	 * @author Nadim Benabdenbi
	 * @version 1.0
	 * @since JDK1.6
	 * 
	 */
	public class BeanPropertyEntry {
		
		/**
		 * @parameter
		 */
		private String key;
		
		/**
		 * @parameter
		 */
		private SimpleBeanDefinition<?> bean;
		
		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}
		
		/**
		 * @param key
		 *            the key to set
		 */
		public void setKey(String key) {
			this.key = key;
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
		
	}
}
