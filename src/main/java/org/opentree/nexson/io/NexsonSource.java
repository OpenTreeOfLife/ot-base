package org.opentree.nexson.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.opentree.properties.OTVocabularyPredicate;

/**
 * <p>A class for interfacing with NexSON documents. NexSON is a JSON format derived from badgerfish-processed NexML.
 * NexSON files are intended to provide compact, standardized storage for phylogenetic trees and associated information.
 * NexSON is the pseudo-official (i.e. only currently supported) format for the Open Tree of Life project's datastore.</p>
 * <br/>
 * <p>For information about NexML, see:<br/>
 * http://www.nexml.org/manual.</p>
 * <br/>
 * <p>For information about the ot:* namespace vocabulary terms and other NexSON information, see:<br/>
 * http://opentree.wikispaces.com/NexSON</p>
 * <br/>
 * <p>For detailed information about NexSON annotations, see:<br/>
 * http://github.com/OpenTreeOfLife/api.opentreeoflife.org/wiki/Annotations-in-NexSON</p>
 * 
 * @author cody
 *
 */
public class NexsonSource extends NexsonElement {

	private List<NexsonTree> trees = new ArrayList<NexsonTree>();
	private List<NexsonOTU> otus = new ArrayList<NexsonOTU>();
	private Map <String, NexsonOTU> otusById = new HashMap<String, NexsonOTU>();
	
	/**
	 * Create a NexsonStudy object populated from a JSONObject containing the NexSON to be parsed.
	 * @param nexson
	 */
	public NexsonSource(JSONObject nexson) {
		parseNexson(nexson);
	}
	
	/**
	 * Create a NexsonStudy object from a Reader object providing access to the NexSON to be parsed.
	 */
	public NexsonSource (Reader reader) {
		parseNexson((JSONObject) JSONValue.parse(reader));
	}
	
	/**
	 * Create an empty NexsonStudy object.
	 */
	public NexsonSource() { }

	// ## trees
	
	public List<NexsonTree> getTrees() {
		return trees;
	}
	
	public NexsonTree getTree(int i) {
		return trees.get(i);
	}
	
	public void addTree(NexsonTree tree) {
		if (tree != null) {
			trees.add(tree);
		} else {
			throw new java.lang.IllegalArgumentException("attempt to add a null tree");
		}
	}	

	public void addTrees(Collection<NexsonTree> treesToAdd) {
		for (NexsonTree tree : treesToAdd) {
			addTree(tree);
		}
	}

	public int getTreeCount() {
		return trees.size();
	}
	
	public int getOTUCount() {
		return otus.size();
	}
	
	public List<NexsonOTU> getOTUList() {
		return otus;
	}
	
	public NexsonOTU getOTUById(String otuId) {
		return otusById.get(otuId);
	}
	
	/**
	 * Add an OTU to the list of OTUs for this study. Performs basic error checking... Might need to be better.
	 * @param otu
	 */
	public void addOTU(NexsonOTU otu) {
		if (otu.getId() == null) {
			throw new NullPointerException("Cannot add an OTU with a null id");

		} else if (getOTUById(otu.getId()) != null) {
			throw new IllegalArgumentException("Cannot add OTU with id '" + otu.getId() + "' as an OTU with this id is already present.");
		
		} else {
			otus.add(otu);
			otusById.put(otu.getId(), otu);
		}
	}
	
	/**
	 * Should fill out the properties of a NexsonTree based on the properties of the NexsonOTU elements associated with its tips.
	 * 
	 * May not be useful... Should consider implications.
	 * 
	 * @param tree
	 */
	public void propagateOTUPropertiesToTree(NexsonTree tree) {

		/*
		JSONObject otu = (JSONObject) otuMap.get(otuId);
		if (otu == null) {
			throw new NexsonParseException("OTU id '" + otuId + "', specified for node with id " + nodeId + ", is not recognized");
		} */

		//		jadeNode.setName(label);

	}
		
	/**
	 * <p>Parse incoming study NexSON. Farms out the processing of sub-element (e.g. trees, etc.) NexSON to the appropriate sub-element classes (e.g. NexsonTree).</p>
	 * <br/>
	 * <p>The format of the file, roughly speaking (some noise omitted):<br/>
	 * <br/>
	 * {"nexml": {<br/>
	 * "@xmlns": ...,<br/>
	 * "@nexmljson": "http:\/\/www.somewhere.org",<br/>
	 * "meta": [...],<br/>
	 * "otus": { "otu": [...] },<br/>
	 * "trees": { "tree": [...] }}}
	 * </p>
	 * <br/>
	 * 
	 */
	@Override
	protected void parseNexson(JSONObject nexson) {
		
		// The XML root element
		JSONObject studyRootElement = (JSONObject)nexson.get("nexml");	
		
		// ===== study metadata
		
		processMetadata(studyRootElement);
		
		if (!hasProperty(OTVocabularyPredicate.OT_STUDY_ID.propertyName())) {
			throw new NexsonParseException("Attempt to parse NexSON study without ot:studyId property. " +
					"NexSON begins with: \n\n" + studyRootElement.toJSONString().substring(0, 256) + "...");
		}

		// ===== otus
		
		JSONArray otuList = (JSONArray) ((JSONObject)studyRootElement.get("otus")).get("otu");

		for (Object otuJSON : otuList) {
//			NexsonOTU otu = new NexsonOTU((JSONObject) otuJSON);
			addOTU(new NexsonOTU((JSONObject) otuJSON));
//			otusById.put(otu.getId(), otu);
		}

		// ===== trees
		
		JSONArray treeList = (JSONArray)((JSONObject) studyRootElement.get("trees")).get("tree");

		// Process each tree in turn, yielding a JadeTree
		for (Object treeObj : treeList) {
			JSONObject treeJSON = (JSONObject)treeObj;
			
			NexsonTree tree = new NexsonTree(treeJSON, this);
			addTree(tree);
			
		}		
	}
	
	/*
	 * Update the internal OTU map (with keys in the form of OTU id strings).
	 *
	private void updateOTUMap() {
		otusById = new HashMap<String, NexsonOTU>();
		for (NexsonOTU otu : getOTUList()) {
			otusById.put(otu.getId(), otu);
		}
	} */
	
	/**
	 * For testing
	 * @param args
	 */
	public static void main(String[] args) {
		Reader r = null;
		try {
			r = new FileReader(args[0]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		NexsonSource test = new NexsonSource(r);
	}
}
