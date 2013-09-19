package org.opentree.properties;

/**
 * OT namespace vocabulary. For more information, see:
 * 
 * http://opentree.wikispaces.com/NexSON
 *  
 */
public enum OTVocabularyPredicate implements OTPropertyPredicate {
	
	OT_AGE ("ot:age", Double.class),
	
	OT_AGE_MIN ("ot:ageMin", Double.class),
	
	OT_AGE_MAX ("ot:ageMax", Double.class),
	
	OT_AUTHOR_CONTRIBUTED ("ot:authorContributed", OTVocabularyObject.class),
	
	OT_BRANCH_LENGTH_DESCRIPTION ("ot:branchLengthDescription", String.class),
	
	OT_BRANCH_LENGTH_TIME_UNITS ("ot:branchLengthTimeUnits", String.class),
	
	/**
	 * The type of branch lengths for this tree. A property of tree root nodes.
	 */
	OT_BRANCH_LENGTH_MODE("ot:branchLengthMode", String.class),
	
	OT_NODE_LABEL ("ot:nodeLabel", String.class),
	
	OT_NODE_LABEL_DESCRIPTION ("ot:nodeLabelDescription", String.class),
	
	OT_NODE_LABEL_MODE ("ot:nodeLabelMode", OTVocabularyObject.class),
	
	OT_COMMENT ("ot:comment", String.class),
	
	/**
	 * The name of curator who uploaded the source. Used for study sources.
	 */
	OT_CURATOR_NAME("ot:curatorName", String.class),

	/**
	 * A URI (or other identifier) for the published data. Used for study sources.
	 */
	OT_DATA_DEPOSIT("ot:dataDeposit", String.class),

	/**
	 * The ottolid of root of clade specified as focal in the study
	 */
	OT_FOCAL_CLADE ("ot:focalClade", Long.class),
	
	OT_INFERENCE_METHOD ("ot:inferenceMethod", String.class),
	
	/**
	 * Some unknown identifier node that is the designated ingroup for this tree. A property of tree root nodes.
	 * 
	 * TODO: This should be the id of the node in the nexson. Not entirely clear that this is the case.
	 */
	OT_INGROUP_CLADE("ot:inGroupClade", String.class),
	
	OT_IS_INGROUP ("ot:isIngroup", Boolean.class), // TODO: switch over to this term for the otu labels currently using the OTUNodeProperty term
	
	OT_IS_OTU ("ot:isOTU", Boolean.class), // TODO: switch over to this term for the otu labels currently using the OTUNodeProperty term
	
	/**
	 * The original label assigned to this node.
	 */
	OT_ORIGINAL_LABEL ("ot:originalLabel", String.class),	

	/**
	 * The ott id associated with the node. A property of tip nodes. Should not be set if the node has not been mapped.
	 */
	OT_OTT_ID ("ot:ottolid", Long.class), // TODO: will change to ot:ottId, change here when necessary
	
	/**
	 * The ottol name associated with this taxon
	 */
	OT_OTT_TAXON_NAME ("ot:ottTaxonName", String.class),
	
	OT_PARENT ("ot:parent", String.class),
	
	/**
	 * The citation string for published studies. A property of source meta nodes.
	 */
    OT_PUBLICATION_REFERENCE ("ot:studyPublicationReference", String.class),

    OT_SPECIFIED_ROOT ("ot:specifiedRoot", String.class),
    
    /**
     * The phylografter study id. A property of source meta nodes.
     */
    OT_STUDY_ID ("ot:studyId", String.class),
    
    OT_STUDY_LABEL ("ot:studyLabel", String.class),
    
    OT_STUDY_LAST_EDITOR ("ot:studyLastEditor", String.class),
    
    OT_STUDY_MODIFIED ("ot:studyModified", String.class), // a datetime string
    
    OT_STUDY_UPLOADED ("ot:studyUploaded", String.class), // a datetime string
    
    /**
     * A URI (or other identifier) for published studies. A property of source meta nodes.
     */
    OT_STUDY_PUBLICATION("ot:studyPublication", String.class),
    
    /**
     * The tag field exported from phylografter. A property of source meta nodes.
     */
    OT_TAG ("ot:tag", String.class),
    
    OT_TREEBASE_TREE_ID ("ot:treebaseTreeId", String.class),
    
    OT_TREEBASE_OTU_ID ("ot:treebaseOTUIid", String.class),
    
    OT_TREE_LAST_EDITED ("ot:treeLastEdited", String.class), // a datetime string
    
    OT_TREE_MODIFIED ("ot:treeModified", String.class), // a datetime string
    
    /**
     * The year the study was published. A property of source meta nodes.
     */
    OT_YEAR ("ot:studyYear", Integer.class);
	
	private final String propertyName;
	private final Class<?> type;
    
	OTVocabularyPredicate(String name, Class<?> type) {
    	this.propertyName = name;
        this.type = type;
    }

	public String propertyName() {
		return propertyName;
	}

	public Class<?> type() {
		return type;
	}
	
}
