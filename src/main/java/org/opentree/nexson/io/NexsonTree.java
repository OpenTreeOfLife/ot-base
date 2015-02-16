package org.opentree.nexson.io;

import jade.tree.JadeNode;
import jade.tree.JadeTree;
import jade.tree.NodeOrder;
import jade.tree.Tree;
import jade.tree.TreeBipartition;
import jade.tree.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opentree.properties.OTVocabularyPredicate;

/**
 *
 * NexsonTree wraps the JadeTree class and provides access to the appropriate JadeTree methods as well as providing access to
 * Nexson-specific methods.
 * 
 * I am developing NexsonTree to contain a JadeTree structure with each node mirrored by a NexsonNode element. Each NexsonNode
 * has a jadeNode() method to access the underlying JadeNode, and each JadeNode has its associated NexsonNode stored as an
 * object available from the JadeNode.getObject() method under the key "nexson_node".
 * 
 * Each NexsonNode contains NexSON information and links to things like NexsonOTU, etc. as well as the basic NexsonElement
 * methods. Tree operations can be performed on the set of JadeNodes, with minimal interaction with the NexsonNodes (they have no
 * attached edges so shouldn't need much messing with; during rerooting however we will have to take node addition/deletion into
 * account--new NexsonNode elements might need to be created, designated as root, etc.)
 * 
 * @author cody
 *
 */
public class NexsonTree extends NexsonElement implements Tree {
	
	// TODO: new methods
	
	public TreeBipartition getBipartition(TreeNode node) {
		return null;
	}
	
	@Override
	public Iterable<TreeNode> internalNodes(NodeOrder order) {
		return null;
	}

	
	
	public int internalNodeCount() {
		return 0;
	}
	
	// end TODO
	
	private JadeTree tree = null;
	private NexsonSource parentStudy = null;
	private NexsonNode ingroupNode = null;
	
	/**
	 * Create a NexsonTree object using the provided NexSON. Requires a study element to be specified containing any OTUs
	 * to be associated with the tips of the NexsonTree.
	 * 
	 * @param nexson
	 * @param parentStudy
	 */
	public NexsonTree(JSONObject nexson, NexsonSource parentStudy)  {
		setParentStudy(parentStudy);
		parseNexson(nexson);
	}
	
	/**
	 * Create a NexsonTree object using the provided JadeTree.
	 * @param nexson
	 * 
	 * 
	 * FOR NOW THIS IS JUST A KLUDGE SO NexsonReader will work... This is a case that may not be supported in the future.
	 * 
	 * 
	 */
	public NexsonTree(JadeTree jt)  {
		tree = jt;
	}
	
	/**
	 * Create an empty NexsonTree object.
	 */
	public NexsonTree()  { }
	
	// ### getters
	
	/**
	 * Return the parent study of this tree. This should be set to null if there is none.
	 * @return
	 */
	public NexsonSource getParentStudy() {
		return parentStudy;
	}
	
	public NexsonNode getSpecifiedIngroupNode() {
		return ingroupNode;
	}
	
	/**
	 * Return the root node for this tree. If this NexsonTree is empty, throw a NoSuchElementException.
	 * @return
	 */
	public JadeNode getRoot() {
		if (tree != null) {
			return tree.getRoot();
		} else {
			throw new java.util.NoSuchElementException("Attempt to get the root of an empty NexsonTree object.");
		}
	}
	
	/**
	 * Get an iterable over the tip nodes of the NexsonTree element
	 * @return
	 */
	@Override
	public Iterable<TreeNode> externalNodes() {
		return tree.externalNodes(); // should use nexsontree instead of jadetree
	}
	
	
	// ### setters
	
	/**
	 * Assign the OTU list from the specified NexsonStudy to this nexson tree. This is required in order to assign OTUs to nodes in the tree.
	 * @param otus
	 */
	public void setParentStudy(NexsonSource study) {
		this.parentStudy = study;
	}
	
	/**
	 * Designate the provided node as the ingroup of the tree. This node must be a child of the root node (or the root node itself) of this
	 * tree or an IllegalArgumentException will be thrown.
	 * 
	 * @param ingroupNode
	 */
	public void specifyIngroup(NexsonNode ingroupNode) {
		
		
		// TODO: making this consistent with Tree and TreeNode, and switching away from jadenode-connected implementation.
		// PICKUP HERE.
		
		
		NexsonNode observedRoot = ingroupNode; //.getJadeNode();

		while (observedRoot.getParent() != null) {
			observedRoot = (NexsonNode) observedRoot.getParent();
		}
		if (observedRoot.equals(getRoot())) {
			this.ingroupNode = ingroupNode;
		} else {
			throw new IllegalArgumentException("Attempt to specify an ingroup node in a tree that does not contain the designated node.");
		}
	}
	
	@Override
	protected void parseNexson(JSONObject nexson) {
		
		/* trees can have their own specific metadata e.g.
		[
			{"@property":"ot:branchLengthMode","@xsi:type":"nex:LiteralMeta","$":"ot:substitutionCount"},
			{"@property":"ot:inGroupClade","$":"node208482","xsi:type":"nex:LiteralMeta"}
		] */
		processMetadata(nexson);
		
		// arbitraryNode is used a starting point for finding the root later on (if not specified), see below
		NexsonNode arbitraryNode = null;
//		JadeNode arbitraryJadeNode = null;
		
		// if we have a specified root node, record it, we will use this to validate the nexson structure
		NexsonNode specifiedRoot = null;
		
		// nodes and edges will be used to build the tree
		JSONArray nexsonNodeArr = (JSONArray) nexson.get("node");
		JSONArray edgeList = (JSONArray) nexson.get("edge");
		Map<String, NexsonNode> nodeForId = new HashMap<String, NexsonNode>();
		
		// For each node as specified in the Nexson file
		// e.g. {"@otu": "otu221", "@id": "node692"}
		for (Object node : nexsonNodeArr) {

			// create a nexson node from the incoming JSON source and squirrel it away its associated JadeNode
			NexsonNode nexsonNode = new NexsonNode((JSONObject)node, this);
			nodeForId.put(nexsonNode.getId(), nexsonNode);
			
			arbitraryNode = nexsonNode; //.getJadeNode();
			
			if (nexsonNode.isTheRoot()) {
				specifiedRoot = nexsonNode;
			}
		}
		
		// Currently, we do not remember NexSON edges. We record them as links between NexsonNodes
		
		// For each specified edge, hook up the two corresponding nodes
		for (Object edge : edgeList) {
			JSONObject j = (JSONObject)edge;
			// {"@source": "node830", "@target": "node834", "@length": 0.000241603, "@id": "edge834"}

			// source is parent, target is child
			NexsonNode parent = nodeForId.get(j.get("@source"));
			if (parent == null) {
				throw new NexsonParseException("Edge with source property " + (String)j.get("@source") + " not corresponding to any known nodes");
			}
			
			NexsonNode child = nodeForId.get(j.get("@target"));
			if (child == null) {
				throw new NexsonParseException("Edge with target property " + (String)j.get("@target") + " not corresponding to any known nodes");
			}
			
			Number length = (Number) j.get("@length");
			if (length != null) {
				child.setParentBranchLength(length.doubleValue());
			}
			
			parent.addChild(child);
		}
		
		// Find the observed root (the node without a parent) so we can assess whether
		// it matches the specifiedRoot. If the input file is malicious this might loop forever.
		NexsonNode observedRoot = null;
		for (NexsonNode jn = arbitraryNode; jn != null; jn = (NexsonNode) jn.getParent()) {
			observedRoot = jn;
		}

		// Validation, assumes nexson edge polarity matches tree edge polarity (it should).
		if (specifiedRoot != null && specifiedRoot != observedRoot) {
			throw new NexsonParseException("The specified root node " + specifiedRoot.getId() +
					" is different from the observed root of the tree in the NexSON object hierarchy. This is nonsensical.");
		}
		
		// GraphImporter looks for root as node with no parents, so we set this here. This seems unnecessary...
		observedRoot.setParent(null);
		
		// TODO: Think we just need to reroot the tree here...
//		tree = new NexsonTree(observedRoot.getJadeNode());		
	}

	@Override
	public int externalNodeCount() {
		// TODO Auto-generated method stub
		return 0;
	}
}