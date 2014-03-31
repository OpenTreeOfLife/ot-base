package org.opentree.properties;

/**
 * OT namespace vocabulary. For more information, see:
 * 
 * http://opentree.wikispaces.com/NexSON
 *  
 */
public enum OTVocabularyPredicate implements OTPropertyPredicate {
	
	/**
	 * Node property
	 */
	OT_AGE ("ot:age", Double.class),
	
	/**
	 * Node property
	 */
	OT_AGE_MIN ("ot:ageMin", Double.class),
	
	/**
	 * Node property
	 */
	OT_AGE_MAX ("ot:ageMax", Double.class),
	
	/**
	 * Study property
	 */
	OT_AUTHOR_CONTRIBUTED ("ot:authorContributed", OTVocabularyObject.class),
	
	/**
	 * Tree property
	 */
	OT_BRANCH_LENGTH_DESCRIPTION ("ot:branchLengthDescription", String.class),
	
	/**
	 * Tree property
	 */
	OT_BRANCH_LENGTH_TIME_UNITS ("ot:branchLengthTimeUnits", String.class),
	
	/**
	 * Tree property. The type of branch lengths for this tree.
	 */
	OT_BRANCH_LENGTH_MODE("ot:branchLengthMode", String.class),
	
	/**
	 * Node property
	 */
	OT_NODE_LABEL ("ot:nodeLabel", String.class),
	
	/**
	 * Tree property. Describes the type of information stored in node labels.
	 */
	OT_NODE_LABEL_DESCRIPTION ("ot:nodeLabelDescription", String.class),
	
	/**
	 * Tree property.
	 */
	OT_NODE_LABEL_MODE ("ot:nodeLabelMode", OTVocabularyObject.class),

	/**
	 * General property
	 */
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
	 * Study property. An ott id identifying the broadest taxon to which this study applies. Deprecated in favor of ot:focalCladeOTTId.
	 */
	@Deprecated
	OT_FOCAL_CLADE ("ot:focalClade", Long.class),
	
	/**
	 * Study property. The ott id identifying the broadest taxon to which this study applies.
	 */
	OT_FOCAL_CLADE_OTT_TAXON_ID ("ot:focalCladeOTTId", String.class),
	
	/**
	 * Study property. The OTT taxon name for the clade that this study is about. Deprecated in favor of ot:focalCladeTaxonName (more general, not necessarily an OTT-defined name).
	 */
	@Deprecated
	OT_FOCAL_CLADE_OTT_TAXON_NAME ("ot:focalCladeOTTTaxonName", String.class),
	
	/**
	 * Study property. The name of the focal clade that this study is about. May not be an ott-recognized name!
	 */
	OT_FOCAL_CLADE_TAXON_NAME ("ot:focalCladeTaxonName", String.class),
	
	/**
	 * Tree property. Describes the way the tree was inferred.
	 */
	OT_INFERENCE_METHOD ("ot:inferenceMethod", String.class),
	
	/**
	 * Some unknown identifier node that is the designated ingroup for this tree. A property of tree root nodes.
	 * 
	 * TODO: This should be the id of the node in the nexson. Not entirely clear that this is the case.
	 */
	OT_INGROUP_CLADE("ot:inGroupClade", String.class),
	
	/**
	 * Node property. Is this node the ingroup for this tree?
	 */
	OT_IS_INGROUP ("ot:isIngroup", Boolean.class), // TODO: switch over to this term for the otu labels currently using the OTUNodeProperty term

	/**
	 * Node property. Is this node a leaf in the tree?
	 */
	OT_IS_LEAF ("ot:isLeaf", Boolean.class), // TODO: switch over to this term for the otu labels currently using the OTUNodeProperty term
	
	/**
	 * Node (otu) property. The original label assigned to this node.
	 */
	OT_ORIGINAL_LABEL ("ot:originalLabel", String.class),	

	/**
	 * Node (otu) property. The ott id associated with the taxon assigned to this node. Should not be set if the node has not been mapped.
	 */
	OT_OTT_ID ("ot:ottId", Long.class),
	
	/**
	 * Node (otu) property. The ott taxon name associated with this taxon.
	 */
	OT_OTT_TAXON_NAME ("ot:ottTaxonName", String.class),

	/**
	 * Node property.
	 */
	OT_PARENT ("ot:parent", String.class),
	
	/**
	 * The citation string for published studies. A property of source meta nodes.
	 */
    OT_PUBLICATION_REFERENCE ("ot:studyPublicationReference", String.class),

    /**
     * A tree property. The nexson id of the node that is the specified root node for the tree. Should always be the deepest node in the tree (by convention/nexson spec).
     */
    OT_SPECIFIED_ROOT ("ot:specifiedRoot", String.class),
    
    /**
     * The phylografter study id. A property of source meta nodes.
     */
    OT_STUDY_ID ("ot:studyId", String.class),
    
    /**
     * Study property.
     */
    OT_STUDY_LABEL ("ot:studyLabel", String.class),
    
    /**
     * Study property.
     */
    OT_STUDY_LAST_EDITOR ("ot:studyLastEditor", String.class),
    
    /**
     * Study property. A datetime string. Format?
     */
    OT_STUDY_MODIFIED ("ot:studyModified", String.class), // a datetime string
    
    /**
     * A URI (or other identifier) for published studies. A property of source meta nodes.
     */
    OT_STUDY_PUBLICATION("ot:studyPublication", String.class),

    /**
     * Study property. A datetime string. Format?
     */
    OT_STUDY_UPLOADED ("ot:studyUploaded", String.class), // a datetime string

    /**
     * The tag field exported from phylografter. A property of source meta nodes.
     */
    OT_TAG ("ot:tag", String.class),
    
    /**
     * Tree property.
     */
    OT_TREEBASE_TREE_ID ("ot:treebaseTreeId", String.class),
    
    /**
     * Node (otu) property.
     */
    OT_TREEBASE_OTU_ID ("ot:treebaseOTUId", String.class),
    
    /**
     * Tree property. A datetime string.
     */
    OT_TREE_LAST_EDITED ("ot:treeLastEdited", String.class), // a datetime string
    
    /**
     * Tree property. A datetime string.
     */
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
