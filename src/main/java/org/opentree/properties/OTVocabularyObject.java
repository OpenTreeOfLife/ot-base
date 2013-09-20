package org.opentree.properties;

public enum OTVocabularyObject implements OTPropertyObject {

	/**
	 * Indicates that values are bootstraps.
	 */
	OT_BOOTSTRAP_VALUES ("ot:bootstrapValues"),

	/**
	 * Indicates that values are number of changes.
	 */
	OT_CHANGE_COUNT ("ot:changeCount"),

	/**
	 * Indicates that values are defined but are not within the known set of value types. Refer to ot:*Description property for more information.
	 */
	OT_OTHER("ot:other"),
	
	/**
	 * Indicates that values are posterior support.
	 */
    OT_POSTERIOR_SUPPORT ("ot:posteriorSupport"),
    
    /**
     * Indicates that values are number of substitutions per site.
     */
    OT_SUBSTITUTION_COUNT ("ot:substitutionCount"),

    /**
     * Indicates that the values are taxon names.
     */
    OT_TAXON_NAMES ("ot:taxonNames"),
    
    /**
     * Indicates that the values are of an unknown type. Used when there is no other information available and/or the type of the value is ambiguous.
     */
    OT_UNDEFINED ("ot:undefined"),
    
    /**
     * Indicates that values are units of time. The default expected time unit is millions of years, but this is not specified.
     */
	OT_TIME ("ot:time");
	
	private String propertyName;
	
	OTVocabularyObject(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public String propertyName() {
		return propertyName;
	}
}
