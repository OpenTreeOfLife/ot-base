package org.opentree.nexson.io;

import org.json.simple.JSONObject;
import org.opentree.properties.OTVocabularyPredicate;

public class NexsonOTU extends NexsonElement {

	String label = "";

	/**
	 * Create an OTU object containing information from the provided nexson.
	 * @param nexson
	 */
	public NexsonOTU(JSONObject nexson) {
		parseNexson(nexson);
	}
	
	/**
	 * Create an empty OTU object.
	 */
	public NexsonOTU() { }
	
	@Override
	protected void parseNexson(JSONObject nexson) {

		processMetadata(nexson);
		
		// Kludge! For important special case, need to convert incoming ottIds to longs if they are not
		if (hasProperty(OTVocabularyPredicate.OT_OTT_ID.propertyName())) {
			Object ottId = getNonNullProperty(OTVocabularyPredicate.OT_OTT_ID.propertyName());
			
			if (ottId instanceof String) {
				ottId = Long.parseLong((String)ottId);
				
			} else if (ottId instanceof Integer) {
				ottId = new Long((((Integer)ottId).intValue()));

			} else if (ottId instanceof Long) {
				; // do nothing, this is the desired case, necessary to avoid the catch-all fail case below
				
			} else {
				throw new NexsonParseException("Invalid value for " + OTVocabularyPredicate.OT_OTT_ID.propertyName() + ": " + ottId);
			}
		}

		// set the label
		if (hasProperty(OTVocabularyPredicate.OT_OTT_TAXON_NAME.propertyName())) {
			label = (String) getProperty(OTVocabularyPredicate.OT_OTT_ID.propertyName());
		} else {
			label = (String) getProperty(OTVocabularyPredicate.OT_ORIGINAL_LABEL.propertyName());
		}		
	}
}
