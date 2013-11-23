package org.opentree.properties;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines preset choices available for specific properties. This is referenced by elements of the UI that
 * present the possible choices to users.
 * @author cody
 */
public enum OTPropertyChoices implements OTPropertyPredicate {

	OT_BRANCH_LENGTH_MODE (OTVocabularyPredicate.OT_BRANCH_LENGTH_MODE, new OTVocabularyObject[] {
			OTVocabularyObject.OT_BOOTSTRAP_VALUES,
			OTVocabularyObject.OT_CHANGES_COUNT,
			OTVocabularyObject.OT_OTHER,
			OTVocabularyObject.OT_POSTERIOR_SUPPORT,
			OTVocabularyObject.OT_SUBSTITUTION_COUNT,
			OTVocabularyObject.OT_TIME,
			OTVocabularyObject.OT_UNDEFINED}),
	
	OT_NODE_LABEL_MODE (OTVocabularyPredicate.OT_NODE_LABEL_MODE, new OTVocabularyObject[] {
			OTVocabularyObject.OT_BOOTSTRAP_VALUES,
			OTVocabularyObject.OT_OTHER,
			OTVocabularyObject.OT_POSTERIOR_SUPPORT,
			OTVocabularyObject.OT_TAXON_NAMES,
			OTVocabularyObject.OT_UNDEFINED});

	// TODO: others?
	
	private OTVocabularyPredicate predicate;
	private OTVocabularyObject[] choicesObjects;
	private List<String> choices;
	
	OTPropertyChoices (OTVocabularyPredicate predicate, OTVocabularyObject[] choicesArr) {
		this.predicate = predicate;
		this.choicesObjects = choicesArr;
		choices = new ArrayList<String>();
		for (OTPropertyObject p : choicesObjects) {
			choices.add(p.propertyName());
		}
	}
	
	@Override
	public String propertyName() {
		return predicate.propertyName();
	}

	@Override
	public Class<?> type() {
		return predicate.type();
	}
	
	public List<String> choices() {
		return choices;
	}
}
