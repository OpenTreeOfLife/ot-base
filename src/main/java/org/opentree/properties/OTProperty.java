package org.opentree.properties;

public interface OTProperty {

	/**
	 * The property name. Used to identify this property in indexes and on nodes.
	 * @return
	 */
	public String propertyName();

	/**
	 * Indicates the datatype of this property.
	 * @return
	 */
	public Class<?> type();
	
}
