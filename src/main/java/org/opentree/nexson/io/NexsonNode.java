package org.opentree.nexson.io;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jade.tree.NodeOrder;
import jade.tree.TreeNode;

import org.json.simple.JSONObject;
import org.opentree.properties.OTVocabularyPredicate;

public class NexsonNode extends NexsonElement implements TreeNode {

	// TODO incomplete new methods
	
	@Override
	public String getNewick(boolean showBranchLengths) {
		// TODO: should code this up
		throw new UnsupportedOperationException();
	}

	// end incomplete new methods

	private List<NexsonNode> children = new ArrayList<NexsonNode>();
	private boolean isIngroupRoot = false;
	private boolean isTreeRoot = false;
	private boolean isLeaf = false;
	private Double branchLength = null;
	private NexsonTree parentTree = null;
	private NexsonNode parent = null;
	private NexsonOTU otu = null;
	
//	public static final String NEXSON_NODE_JADE_OBJECT_KEY = "nexson_node";

	/**
	 * Create a NexsonNode object using the provided NexSON. Requires a NexsonTree element to be supplied, which will be used to attempt
	 * to assign node properties such as the root node of the tree and the root of the ingroup, as well as OTUs for tips. For OTU assignment
	 * to function, the supplied NexsonTree must have been assigned a parent NexsonStudy object containing the OTUs (and of course, the OTU
	 * list must contain an OTU with the id specified by the incoming NexSON).
	 * 
	 * @param nexson
	 * @param parentTree
	 */
	public NexsonNode(JSONObject nexson, NexsonTree parentTree) throws NexsonParseException {
		setParentTree(parentTree);
		parseNexson(nexson);
	}
	
	/**
	 * Create an empty NexsonNode object.
	 */
	public NexsonNode() { }
	
	@Override
	public boolean isExternal() {
		return isLeaf;
	}

	@Override
	public boolean isInternal() {
		return ! isLeaf;
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public TreeNode getChild(int i) {
		return children.get(i);
	}

	@Override
	public List<TreeNode> getChildren() {
		return (List<TreeNode>) ((List<?>) this.children);
	}

	@Override
	public boolean addChild(TreeNode child) {
		return this.children.add((NexsonNode) child);
	}

	@Override
	public boolean removeChild(TreeNode child) {
		return this.children.remove(child);
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}
	
	public void setParent(NexsonNode parent) {
		this.parent = parent;
	}

	// ### getters
	
	public boolean isTreeRoot() {
		return isTreeRoot;
	}
		
	public Double getParentBranchLength() {
		return branchLength;
	}
	
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
	
	
	// ### setters
	
	public void setIsIngroupRoot(boolean isIngroupRoot) {
		this.isIngroupRoot = isIngroupRoot;
	}

	public void setIsTreeRoot(boolean isTreeRoot) {
		this.isTreeRoot = isTreeRoot;
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

    // ===== node iterators
	
    public Iterable<TreeNode> getDescendantLeaves(NodeOrder order) {
        LinkedList<TreeNode> leaves = new LinkedList<TreeNode>();
    	for (TreeNode descendant : getDescendants(order)) {
    		if (descendant.isExternal()) {
    			leaves.add(descendant);
    		}
    	}
    	return leaves;
    }
    
    public Iterable<TreeNode> getDescendantLeaves() {
    	return getDescendantLeaves(NodeOrder.PREORDER);
    }
    
    public Iterable<TreeNode> getDescendants() {
    	return getDescendants(NodeOrder.PREORDER);
    }
    
    public Iterable<TreeNode> getDescendants(NodeOrder order) {
        
        LinkedList<TreeNode> nodes = new LinkedList<TreeNode>();
        addDescendants((TreeNode) this, nodes, order);
        return nodes;
    }
    
    private void addDescendants(TreeNode n, List<TreeNode> children, NodeOrder order) {

        if (order == NodeOrder.PREORDER) {
            for (TreeNode c : n.getChildren()){
                addDescendants(c, children, order);
            }
            children.add(n);

        } else if (order == NodeOrder.POSTORDER) {
            children.add(n);

            for (TreeNode c : n.getChildren()){
                addDescendants(c, children, order);
            }
        }
    }
    
	// ### other stuff
	
	protected void parseNexson(JSONObject nexson) throws NexsonParseException {
		
		processMetadata(nexson);
		
		// if this is ingroup root then record that
		if (getId().equals((String) parentTree.getProperty(OTVocabularyPredicate.OT_INGROUP_CLADE.propertyName()))) {
			setIsIngroupRoot(true);
		}

		// identify the root if it has been specified
		if (getId().equals((String) parentTree.getProperty(OTVocabularyPredicate.OT_SPECIFIED_ROOT.propertyName()))) {
			setIsTreeRoot(true);
		}
		
		if (hasProperty(OTVocabularyPredicate.OT_IS_LEAF.propertyName())) {
			isLeaf = true;
		}

		// attempt to assign OTUs based on the OTU list from the associated parent study.
		// NOTE: IF THE PARENT STUDY HAS NOT BEEN SET, no attempt will be made to set otus nor to
		// record any information that would allow them to be assigned later.
		if (parentTree != null && parentTree.getParentStudy() != null) {

			// Some nodes have associated OTUs, others may not
			String otuId = (String)nexson.get("@otu");
			if (otuId != null) {				
				assignOTU(parentTree.getParentStudy().getOTUById(otuId)); // will assign null if the parent study has no OTUs

			} else if (isLeaf) { // fail case
                NexsonParseException e = new NexsonParseException("The node " + getId() + " is identified as a leaf but has not been assigned an OTU.");
                if (false)
                    throw e;
                else {
                    // See https://github.com/OpenTreeOfLife/peyotl/issues/126
                    // and https://github.com/OpenTreeOfLife/oti/issues/34  -JAR
                    System.err.println(e);
                    assignOTU(null);
                }
			}
		}
	}
}
