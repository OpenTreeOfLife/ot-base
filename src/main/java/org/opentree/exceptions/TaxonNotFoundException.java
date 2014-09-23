package org.opentree.exceptions;

import java.util.LinkedList;
import java.util.List;

public class TaxonNotFoundException extends StoredEntityNotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// single name constructor
    public TaxonNotFoundException(Long ottId) {
        super(String.valueOf(ottId), "taxon", "taxa");
    }

    public TaxonNotFoundException(String nameOfTaxon) {
        super(nameOfTaxon, "taxon", "taxa");
    }

    // list of names constructor
    public TaxonNotFoundException(List<String> namesOfTaxa){
        super(namesOfTaxa, "taxon", "taxa");
    }
}
