package org.opentree.utils;

import java.util.*;

public class GeneralUtils {

    public static final int SHORT_NAME_LENGTH = 9;
    public static final int MEDIUM_NAME_LENGTH = 14;
    public static final int LONG_NAME_LENGTH = 19;

    public static final String offendingChars = "[\\Q\"_~`:;/[]{}|<>,.!@#$%^&*()?+=`\\\\\\E\\s]+";
    public static final String newickIllegal = ".*[\\Q:;/[]{}(),\\E]+.*";
    public static final char QUOTE = '"';
    public static final char[] OFFENDING_JSON_CHARS = {QUOTE};
    
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
	public static String cleanName(String dirtyName) {
	    String cleanName = dirtyName.replaceAll(offendingChars, "_");	    
	    return cleanName;
	}
	
	/**
	 * Make sure name conforms to valid newick usage (http://evolution.genetics.washington.edu/phylip/newick_doc.html).
	 * 
	 * Replaces single quotes in `origName` with "''" and puts a pair of single quotes around the entire string.
	 * Puts quotes around name if any illegal characters are present.
	 * 
	 * @param origName
	 * @return newickName
	 */
	public static String newickName (String origName) {
		Boolean needQuotes = false;
		String newickName = origName;
		
		// replace all spaces with underscore
		newickName = newickName.replaceAll(" ", "_");
		
		// replace ':' with '_'. a hack for working with older versions of dendroscope e.g. 2.7.4
		newickName = newickName.replaceAll(":", "_");
		
		// newick standard way of dealing with single quotes in taxon names
		if (newickName.contains("'")) {
			newickName = newickName.replaceAll("'", "''");
			needQuotes = true;
		}
		// if offending characters are present, quotes are needed
		if (newickName.matches(newickIllegal)) {
			needQuotes = true;
		}
		if (needQuotes) {
			newickName = "'" + newickName + "'";
		}
		
		return newickName;
	}

}
