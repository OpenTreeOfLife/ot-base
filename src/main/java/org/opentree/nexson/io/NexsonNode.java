package org.opentree.nexson.io;

import java.util.ArrayList;
import java.util.List;

import jade.tree.JadeNode;
import jade.tree.TreeNode;

import org.json.simple.JSONObject;
import org.opentree.properties.OTVocabularyPredicate;

public class NexsonNode extends NexsonElement implements TreeNode {

	// TODO new methods
	
	@Override
	public boolean isExternal() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInternal() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<TreeNode> getDescendantLeaves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getChildCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TreeNode getChild(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TreeNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNewick(boolean showBranchLengths) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void addChild(NexsonNode child) {
		this.children.add(child);
	}
	
	@Override
	public TreeNode getParent() {
		return parent;
	}
	
	public void setParent(NexsonNode parent) {
		this.parent = parent;
	}


	private List<NexsonNode> children = new ArrayList<NexsonNode>();
	
	// end TODO
	
	
	private boolean isIngroupRoot = false;
	private boolean isTreeRoot = false;
	private Double branchLength = null;
//	private JadeNode jadeNode = new JadeNode();
	private NexsonTree parentTree = null;
	private NexsonNode parent = null;
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
		setParentTree(parentTree);
		parseNexson(nexson);
	}
	
	/**
	 * Create an empty NexsonNode object.
	 */
	public NexsonNode() { }
	
	// ### getters
	
/*	
	public boolean isTreeRoot() {
		return isTreeRoot;
	} */
		
/*	public Double getParentBranchLength() {
		return branchLength;
	} */
	
	@Override
	public boolean isTheRoot() {
		return isTreeRoot;
	}

	@Override
	public double getBL() {
		return branchLength;
	}
	
	@Override
	public Object getLabel() {
		Object label = null;
		if (otu != null) {
			label = otu.getProperty(OTVocabularyPredicate.OT_OTT_ID.propertyName());
		}
		return label;
	}

	public NexsonOTU getOTU() {
		return otu;
	}

	public boolean isIngroupRoot() {
		return isIngroupRoot;
	}
	
	/*
	public JadeNode getJadeNode() {
		return jadeNode;
	} */

	
	// ### setters
	
	public void setIsIngroupRoot(boolean isIngroupRoot) {
		this.isIngroupRoot = isIngroupRoot;
	}

	public void setIsTreeRoot(boolean isTreeRoot) {
		this.isTreeRoot = isTreeRoot;
	}

	/*
	public void assignJadeNode(JadeNode jadeNode) {
		this.jadeNode = jadeNode;
	} */
	
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

			} else { // fail case
				if (hasProperty(OTVocabularyPredicate.OT_IS_LEAF.propertyName())) {
					throw new NexsonParseException("The node " + getId() + " is identified as a leaf but has not been assigned an OTU.");
				}
			}
		}
	}

	@Override
	public boolean addChild(TreeNode child) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeChild(TreeNode child) {
		// TODO Auto-generated method stub
		return false;
	}
}
