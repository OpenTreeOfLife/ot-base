package org.opentree.properties;

public enum JadeNodeProperty implements OTPropertyPredicate {

	/**
	 * A list of OTProperty objects specifying the properties to be displayed for this JadeNode (if they exist).
	 * This list is used by JadeNodeRepresentation when transcribing a JadeNode tree structure to JSON.
	 */
	DISPLAY_PROPERTIES ("displayProperties", OTPropertyPredicate[].class);

	private String propertyName;
	private Class<?> type;
	
	JadeNodeProperty(String propertyName, Class<?> type) {
		this.propertyName = propertyName;
		this.type = type;
	}
	
	@Override
	public String propertyName() {
		return propertyName;
	}

	@Override
	public Class<?> type() {
		return type;
	}
}
