package org.opentree.exceptions;

import java.util.List;

public class TreeNotFoundException extends StoredEntityNotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// single name constructor
    public TreeNotFoundException(String nameOfTaxon) {
        super(nameOfTaxon, "tree", "trees");
    }

    // list of names constructor
    public TreeNotFoundException(List<String> namesOfTaxa){
        super(namesOfTaxa, "tree", "trees");
    }
}
