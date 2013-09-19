package org.opentree.properties;


/**
 * An enum providing access to the properties defined by the Nexson spec.
 * 
 * @author cody
 *
 */
public enum NexsonProperty implements OTPropertyPredicate {

	// TODO: fill this in and use these properties in things like NexsonReader and NexsonWriter
	
	;

	private String name;
	private Class<?> type;

	NexsonProperty (String propertyName, Class<?> type) {
		this.name = propertyName;
		this.type = type;
	}
	
	public String propertyName() {
		return name;
	}

	public Class<?> type() {
		return type;
	}
	
}
