package org.opentree.properties;

/**
 * OT namespace vocabulary
 */
public enum OTVocabulary implements OTProperty {
	
	/**
	 * The type of branch lengths for this tree. A property of tree root nodes.
	 */
	OT_BRANCH_LENGTH_MODE("ot:branchLengthMode", String.class),

	/**
	 * The name of curator who uploaded the source. Used for study sources.
	 */
	OT_CURATOR_NAME("ot:curatorName", String.class),

	/**
	 * A URI (or other identifier) for the published data. Used for study sources.
	 */
	OT_DATA_DEPOSIT("ot:dataDeposit", String.class),

	/**
	 * Some unknown identifier node that is the designated ingroup for this tree. A property of tree root nodes.
	 * 
	 * TODO: This should be the id of the node in the nexson. Not entirely clear that this is the case.
	 */
	OT_INGROUP_CLADE("ot:inGroupClade", String.class),
	
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
	
	/**
	 * The citation string for published studies. A property of source meta nodes.
	 */
    OT_PUBLICATION_REFERENCE ("ot:studyPublicationReference", String.class),

    /**
     * The phylografter study id. A property of source meta nodes.
     */
    OT_STUDY_ID ("ot:studyId", String.class),
    
    /**
     * A URI (or other identifier) for published studies. A property of source meta nodes.
     */
    OT_STUDY_PUBLICATION("ot:studyPublication", String.class),
    
    /**
     * The tag field exported from phylografter. A property of source meta nodes.
     */
    OT_TAG ("ot:tag", String.class),
    
    /**
     * The year the study was published. A property of source meta nodes.
     */
    OT_YEAR ("ot:studyYear", int.class);
	
	public final String propertyName;
	public final Class<?> type;
    
	OTVocabulary(String name, Class<?> type) {
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
