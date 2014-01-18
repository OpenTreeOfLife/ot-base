package org.opentree.utils;

import java.util.*;

public class GeneralUtils {

    public static final int SHORT_NAME_LENGTH = 9;
    public static final int MEDIUM_NAME_LENGTH = 14;
    public static final int LONG_NAME_LENGTH = 19;

    public static final String offendingChars = "[\\Q\"_~`:;/[]{}|<>,.!@#$%^&*()?+=`\\\\\\E\\s]+";
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
    
    public static String[] convertToStringArray(List<String> list) {
    	String[] arr = new String[list.size()];
    	int i = 0;
    	for (String s : list) {
    		arr[i] = s;
    		i++;
    	}
    	return arr;
    }

    public static long[] convertToLongArray(List<Long> list) {
    	long[] arr = new long[list.size()];
    	int i = 0;
    	for (long l : list) {
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
}
