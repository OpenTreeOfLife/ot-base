package org.opentree.graphdb;

/**
 * An interface implemented by enums which define node indexes. Could be generalized to
 * include relationship indexes, but not sure what the advantages/disadvantages of that would be.
 */
public interface NodeIndexDescription {

	public String indexName();
	public String[] parameters();
	
}
