package org.opentree.constants;

/**
 * Specifies several basic types that are used for looking up object types on property import.
 */
public enum BasicType {

	BOOLEAN (Boolean.class),
	INTEGER (Integer.class),
	DECIMAL (Double.class),
	STRING (String.class);
	
	public final Class<?> type;
	
	BasicType(Class<?> type) {
		this.type = type;
	}
	
	public Object convertToValue(Object value) {
		if (this.type == Double.class) {
			return Double.valueOf((String) value);

		} else if (this.type == Integer.class) {
			return Integer.valueOf((String) value);
		
		} else if (this.type == String.class) {
			return String.valueOf(value);

		} else if (this.type == Boolean.class) {
			return Boolean.valueOf((String) value);

		} else {
			throw new IllegalStateException ("cannot convert to unknown type");
		}
	}
}
