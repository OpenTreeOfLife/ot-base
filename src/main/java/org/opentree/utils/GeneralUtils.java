package org.opentree.utils;

import java.io.Reader;
import java.util.*;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.opentree.exceptions.DataFormatException;

public class GeneralUtils {

    public static final int SHORT_NAME_LENGTH = 9;
    public static final int MEDIUM_NAME_LENGTH = 14;
    public static final int LONG_NAME_LENGTH = 19;

    public static final String NEWICK_ILLEGAL_CHARS = ".*[\\Q:;[](),\\E]+.*";
    public static final String NEXUS_ILLEGAL_CHARS = ".*[\\Q:;[](),/\\=*'\"`+-<>~\\E]+.*";
    public static final String NOT_ALPHANUMERIC_DASH_UNDERSCORE_CHARS = "[^A-Za-z0-9_\\-]+";
    public static final String offendingChars = "[\\Q\"~`:;/[]{}|<>,.!@#$%^&*()?+=`\\\\\\E\\s]+";
    public static final char QUOTE = '"';
    public static final char[] JSON_ILLEGAL_CHARS = {QUOTE};
    
    public static int sum_ints(List<Integer> list){
		if(list==null || list.size()<1)
			return 0; // shouldn't this throw a null pointer exception? otherwise how do we differentiate null from a list of zeroes?

		int sum = 0;
		for(Integer i: list)
			sum = sum+i;

		return sum;
	}
    
	public static Object convertToStringArray(Set<String> strings) {
		return convertToStringArray(strings, strings.size());
	}

	public static Object convertToStringArray(List<String> strings) {
		return convertToStringArray(strings, strings.size());
	}
	
    private static String[] convertToStringArray(Iterable<String> strings, int size) {
    	String[] arr = new String[size];
    	int i = 0;
    	for (String s : strings) {
    		arr[i] = s;
    		i++;
    	}
    	return arr;
    }

    public static long[] convertToLongArray(Set<Long> longs) {
    	return convertToLongArray(longs, longs.size());
    }

    public static long[] convertToLongArray(List<Long> longs) {
    	return convertToLongArray(longs, longs.size());
    }
    
    private static long[] convertToLongArray(Iterable<Long> longs, int size) {
    	long[] arr = new long[size];
    	int i = 0;
    	for (long l : longs) {
    		arr[i] = l;
    		i++;
    	}
    	return arr;
    }

	/**
	 * A function for converting string arrays to HashSets of strings, provided as a convenience for preparing string arrays for TNRS.
	 * @param input strings
	 * @return a set of Strings containing the input
	 */
	public static HashSet<String> stringArrayToHashset(String[] strings) {
	    HashSet<String> stringSet = new HashSet<String>();
	    for (int i = 0; i < strings.length; i++) {
	        stringSet.add(strings[i]);
	    }
	    return stringSet;
	}
	
    /**
     * Uses default replacement char (a space)
     * @param dirtyString
     * @return
     */
    public static String stripQuotes(String dirtyString) {
    	return stripQuotes(dirtyString, ' ');
    }
    
    /**
     * Replace quotes in a string with any single char
     * @param dirtyString
     * @return
     */
    public static String stripQuotes(String dirtyString, char replace) {
    	return dirtyString.replace(QUOTE, replace);
    }
   
	/**
	 * Replaces non-alphanumeric characters (excluding "_" and "-") in `dirtyName` with "_" and returns the cleaned name.
	 * 
	 * @param dirtyName
	 * @return cleaned name
	 */
	public static String scrubName(String dirtyName) {
	    String cleanName = dirtyName.replaceAll(offendingChars, "_");	    
	    return cleanName;
	}
	
	/**
	 * Make sure name conforms to valid newick usage (http://evolution.genetics.washington.edu/phylip/newick_doc.html).
	 * 
	 * Quote string if any illegal characters (NEWICK_ILLEGAL_CHARS) are present.
	 * Replaces single quotes in `origName` with "''" and puts a pair of single quotes around the entire string.
	 * 
	 * @param origName
	 * @return newickName
	 */
	public static String newickName (String origName) {
		Boolean needQuotes = false;
		String newickName = origName;
		
		// replace ':' with '_'. a hack for working with older versions of dendroscope e.g. 2.7.4
		//newickName = newickName.replaceAll(":", "_");
		
		// newick standard way of dealing with single quotes in taxon names
		if (newickName.contains("'")) {
			newickName = newickName.replaceAll("'", "''");
			needQuotes = true;
		} else if (newickName.matches(NEWICK_ILLEGAL_CHARS)) {
			// if offending characters are present, quotes are needed
			needQuotes = true;
		}
		if (needQuotes) {
			newickName = "'" + newickName + "'";
		} else {
			// replace all spaces with underscore
			newickName = newickName.replaceAll(" ", "_");
		}
		
		return newickName;
	}
	
	/**
	 * Make sure name conforms to valid Nexus usage (http://sysbio.oxfordjournals.org/content/46/4/590.full.pdf).
	 * 
	 * Quote string if any illegal characters (NEXUS_ILLEGAL_CHARS) are present.
	 * Replaces single quotes in `origName` with "''" and puts a pair of single quotes around the entire string.
	 * Puts quotes around name if any illegal characters are present.
	 * 
	 * @param origName
	 * @return nexusName
	 */
	public static String nexusName (String origName) {
		Boolean needQuotes = false;
		String nexusName = origName;
		
		// Nexus standard way of dealing with single quotes in taxon names
		if (nexusName.contains("'")) {
			nexusName = nexusName.replaceAll("'", "''");
			needQuotes = true;
		} else if (nexusName.matches(NEXUS_ILLEGAL_CHARS)) {
			// if offending characters are present, quotes are needed
			needQuotes = true;
		}
		if (needQuotes) {
			nexusName = "'" + nexusName + "'";
		} else {
			// replace all spaces with underscore
			nexusName = nexusName.replaceAll(" ", "_");
		}
		
		return nexusName;
	}
	
	/**
	 Peek at tree flavour, report back, reset reader for subsequent processing
	 @param r a tree file reader
	 @return treeFormat a string indicating recognized tree format
	 */
	public static String divineTreeFormat (Reader r) throws java.io.IOException, DataFormatException {
		String treeFormat = "";
		r.mark(1);
		char c = (char)r.read();
		r.reset();
		if (c == '(') {
			treeFormat = "newick";
		} else if (c == '{') {
			treeFormat = "nexson";
		} else if (c == '#') {
			throw new DataFormatException("Appears to be a nexus tree file, which is not currently supported.");
		} else {
			throw new DataFormatException("We don't know what format this tree is in.");
		}
		return treeFormat;
	}
	
	/** 
	 * Convert from string:
	 * PREFIX_STUDYID_TREEID_GITSHA
	 * to:
	 * {"study" : "NNN", "tree" : "MMM", "sha":"NANA"}
	 */
	public static HashMap<String, Object> reformatSourceID (String source) {
		
		HashMap<String, Object> results = new HashMap<String, Object>();
		
		String tempSource = source;
		if (source.endsWith(".tre")) {
			tempSource = source.substring(0, source.lastIndexOf('.'));
		}
		
		// format will be: pg_420_522_a2c48df995ddc9fd208986c3d4225112550c8452
		String[] res = tempSource.split("_");
		String studyId = "";
		String treeId  = "";
		String gitSha  = "";
		
		if (res.length == 4) {
			studyId = res[0] + "_" + res[1];
			treeId  = res[2];
			gitSha  = res[3];
			
		} else if (res.length == 3) { // ids lacking a git SHA
			studyId = res[0] + "_" + res[1];
			treeId  = res[2];
		} else if (res.length == 2) { // older DBs with no prefix or git SHA
			studyId = res[0];
			treeId  = res[1];
		} else { // taxonomy has only one element
			studyId = res[0];
		}
		results.put("study_id", studyId);
		results.put("tree_id", treeId);
		results.put("git_sha", gitSha);
		
		return (results);
	}
	
	// For constant formatting across packages
	public static String getTimestamp () {
		String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
		return timestamp;
	}

	public static void print(Object ... stuff) {
		StringBuilder junk = new StringBuilder();
		boolean first = true;
		for (Object s : stuff) {
			if (first) { first = false; } else { junk.append(" "); }
			junk.append(s);
		}
		System.out.println(junk.toString());
	}
	
	/**
	 * Return an iterable containing all the relationships of the indicated type(s) whose start node is n and end node is m.
	 * @param m
	 * @param n
	 * @param relTypes
	 * @return
	 */
	public static Iterable<Relationship> getRelationshipsFromTo(Node m, Node n, RelationshipType ... relTypes) {
		List<Relationship> rels = new ArrayList<Relationship>();
		for (Relationship r : m.getRelationships(Direction.OUTGOING, relTypes)) { // TODO: this is not finding any rels!?
			if (r.getEndNode().equals(n)) { rels.add(r); }
		}
		return rels;
	}
}
