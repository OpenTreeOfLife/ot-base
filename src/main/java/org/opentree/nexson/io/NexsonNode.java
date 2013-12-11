package org.opentree.nexson.io;

import jade.tree.JadeNode;

import org.json.simple.JSONObject;
import org.opentree.properties.OTVocabularyPredicate;

public class NexsonNode extends NexsonElement {

	private boolean isIngroupRoot = false;
	private boolean isTreeRoot = false;
	private Double branchLength = null;
	private JadeNode jadeNode = new JadeNode();
	private NexsonTree parentTree = null;
	private NexsonOTU otu = null;
	
	public static final String NEXSON_NODE_JADE_OBJECT_KEY = "nexson_node";

	/**
	 * Create a NexsonNode object using the provided NexSON. Requires a NexsonTree element to be supplied, which will be used to attempt
	 * to assign node properties such as the root node of the tree and the root of the ingroup, as well as OTUs for tips. For OTU assignment
	 * to function, the supplied NexsonTree must have been assigned a parent NexsonStudy object containing the OTUs (and of course, the OTU
	 * list must contain an OTU with the id specified by the incoming NexSON).
	 * 
	 * @param nexson
	 * @param parentTree
	 */
	public NexsonNode(JSONObject nexson, NexsonTree parentTree) {
		initialize();
		setParentTree(parentTree);
		parseNexson(nexson);
	}
	
	/**
	 * Create an empty NexsonNode object.
	 */
	public NexsonNode() { 
		initialize();
	}
	
	// ### getters
	
	public boolean isIngroupRoot() {
		return isIngroupRoot;
	}

	public boolean isTreeRoot() {
		return isTreeRoot;
	}
	
	public JadeNode getJadeNode() {
		return jadeNode;
	}
	
	public Double getParentBranchLength() {
		return branchLength;
	}
	
	public NexsonOTU getOTU() {
		return otu;
	}
	
	// ### setters
	
	public void setIsIngroupRoot(boolean isIngroupRoot) {
		this.isIngroupRoot = isIngroupRoot;
	}

	public void setIsTreeRoot(boolean isTreeRoot) {
		this.isTreeRoot = isTreeRoot;
	}

	public void assignJadeNode(JadeNode jadeNode) {
		this.jadeNode = jadeNode;
	}
	
	public void setParentTree(NexsonTree parentTree) {
		this.parentTree = parentTree;
	}

	public void setParentBranchLength(Double branchLength) {
		this.branchLength = branchLength;
	}
	
	public void assignOTU(NexsonOTU otu) {
		this.otu = otu;
	}

	// ### other methods
	
	protected void parseNexson(JSONObject nexson) {
		
		processMetadata(nexson);
		
		// if this is ingroup root then record that
		if (getId().equals((String) parentTree.getProperty(OTVocabularyPredicate.OT_INGROUP_CLADE.propertyName()))) {
			setIsIngroupRoot(true);
		}

		// identify the root if it has been specified
		if (getId().equals((String) parentTree.getProperty(OTVocabularyPredicate.OT_SPECIFIED_ROOT.propertyName()))) {
			setIsTreeRoot(true);
		}

		// attempt to assign OTUs based on the OTU list from the associated parent study.
		// NOTE: IF THE PARENT STUDY HAS NOT BEEN SET, no attempt will be made to set otus nor to
		// record any information that would allow them to be assigned later.
		if (parentTree != null && parentTree.getParentStudy() != null) {

			// Some nodes have associated OTUs, others may not
			String otuId = (String)nexson.get("@otu");
			if (otuId != null) {
				assignOTU(parentTree.getParentStudy().getOTUById(otuId)); // will assign null if the parent study has no OTUs
			}
		}
	}
	
	/**
	 * Logic called during construction
	 */
	private void initialize() {
		jadeNode.assocObject(NEXSON_NODE_JADE_OBJECT_KEY, this);
	}
}
