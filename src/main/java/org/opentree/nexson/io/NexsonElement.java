package org.opentree.nexson.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opentree.properties.OTVocabularyObject;
import org.opentree.properties.OTVocabularyPredicate;

public abstract class NexsonElement {

	protected String id = null;
	protected boolean deprecatedStatus = false;
	protected Map<String, Object> properties = new HashMap<String, Object>();
	protected List<NexsonAnnotation> annotations = new ArrayList<NexsonAnnotation>();
	
	/**
	 * Each NexSON element extending this class should be able to parse incoming NexSON to populate the element itself.
	 * @param nexson
	 */
	protected abstract void parseNexson(JSONObject nexson);
	
	public NexsonElement() { }
	
	// ## element id
	
	public String getId() {
		return id;
	}
	
	public void setId(String sourceId) {
		if (sourceId != null) {
			this.id = sourceId;
		} else {
			throw new java.lang.NullPointerException("attempt to add a null source Id");
		}
	}
	
	// ## meta annotations
	
	public List<NexsonAnnotation> getAnnotations() {
		return annotations;
	}
	
	public void addAnnotation(NexsonAnnotation annotation) {
		if (annotation != null) {
			annotations.add(annotation);
		} else {
			throw new java.lang.IllegalArgumentException("attempt to add a null annotation");
		}
	}
		
	// ## meta properties

	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public boolean hasProperty(String propertyName) {
		return properties.containsKey(propertyName);
	}
	
	/**
	 * Returns null if the property is not set
	 * @param propertyName
	 * @return
	 */
	public Object getProperty(String propertyName) {
		if (hasProperty(propertyName)) {
			return properties.get(propertyName);
		} else {
			return null;
		}
	}

	/**
	 * Returns the property if it is set and is not null, or throws a NullPointerException if either of those conditions is false.
	 * @param propertyName
	 * @return
	 */
	public Object getNonNullProperty(String propertyName) {
		Object value = getProperty(propertyName);
		if (value == null) {
			throw new NullPointerException(this.getClass() + " " + getId() + " has a value of null for property '" +
					propertyName + "', but this is not permitted.");
		}
		return value;
	}
	
	/*
	 * Returns false is the property is not set or is set but is null.
	 * @param propertyName
	 * @return
	 *
	public boolean propertyIsNotNull(String propertyName) {
		return (hasProperty(propertyName) && getProperty(propertyName) != null);
	} */

	/**
	 * Currently allows properties to be set with value==null. Not sure if this is what we want.
	 * @param propertyName
	 * @param value
	 */
	public void setProperty(String propertyName, Object value) {
		if (propertyName != null) {
			properties.put(propertyName, value);
		} else {
			throw new java.lang.IllegalArgumentException("Attempt to add a property with name==null");
		}
	}
	
	// ## deprecation
	
	public boolean isDeprecated() {
		return deprecatedStatus;
	}
	
	public void setDeprecatedStatus(boolean isDeprecated) {
		this.deprecatedStatus = isDeprecated;
	}


	/**
	 * Process the JSON in the meta property of the provided rootElement and store the resulting properties,
	 * annotations, and deprecated status in this NexsonElement.
	 * 
	 * @param rootElement
	 */
	protected void processMetadata(JSONObject rootElement) {
		
		List<Object> metadata = getMetadataElementsList(rootElement);

		try {
			setId((String)rootElement.get("@id"));
		} catch (NullPointerException ex) {
			throw new NexsonParseException("Attempt to add a null source id near: " + rootElement.toJSONString().substring(0, 200));
		}

		// Process and store study-level metadata and annotations
		if (metadata != null) {
			
			extractMetadata(metadata);
		}
			// If the study has been deprecated, flag it as such
//			deprecatedStatus = checkDeprecated(metadata);
	}
	
	/**
	 * Just extract the children of the meta element of this NexSON object and return them in a list. If the meta element exists but
	 * does not contain a JSON array, this function will return a list of length 1 containing those contents as a single item. If the
	 * object has no meta element, this function returns null.
	 * @param obj
	 * @return
	 * 
	 * 
	 * TODO: clean this up / combine it with checkDeprecated and extractMetadataInto
	 * 
	 */
	protected static List<Object> getMetadataElementsList(JSONObject obj) {

		Object metaObj = obj.get("meta");
		if (metaObj == null) {
			return null;
		}
		
		if (metaObj instanceof JSONObject) {
			List<Object> l = new ArrayList<Object>(1);
			l.add(metaObj);
			return l;

		} else {
			return (JSONArray) metaObj;
		}
	}
	
	/**
	 * Check through metadata information for ot:tag del*
	 * works for both study-wide and tree-specific metadata
	 * @param metadata
	 * @return
	 * 
	 * 
	 * TODO: clean this up / combine it with getMetadataElementsList and extractMetadataInto
	 * 
	 *
	private static Boolean checkDeprecated (List<Object> metadata) {
		
		// TODO: should combine this with extractMetadatInto method
		
//		Boolean deprecated = false;
//		for (Object meta : metadata) {
//			JSONObject j = (JSONObject)meta;
			if (((String)j.get("@property")).compareTo("ot:tag") == 0) {
				if ((j.get("$")) != null) {
					String currentTag = (String)j.get("$");
					if (currentTag.startsWith("del")) {
						return true;
					}
				} else {
					throw new NexsonParseException("missing property value for name: " + j);
				}
			}
		}
		return deprecated;
	} */
	
	/**
	 * <p>Parse the provided NexSON metadata list, storing metadata properties in propertiesContainer and annotations in annotationsContainer.</p>
	 * <br/>
	 * <p>See https://github.com/nexml/nexml/wiki/NeXML-Manual#wiki-Metadata_annotations_and_NeXML</p>
	 * <br/>
	 * <p>An example NexSON metadata entry could be:<br/>
	 * {"@property": "ot:curatorName", "@xsi:type": "nex:LiteralMeta", "$": "Rick Ree"},</p>
	 *
	 * 
	 * TODO: clean this up / combine it with checkDeprecated and getMetadataElementsList
	 * 
	 */
	private void extractMetadata(List<Object> metadata) {
		
		for (Object meta : metadata) {
			JSONObject j = (JSONObject)meta;
			String propertyName = (String)j.get("@property");
			
			// in case this is using nonstandard nexson (legacy?) 
			if (propertyName == null) {
				propertyName = (String)j.get("@rel");
			}
			
			if (propertyName == null) {
				throw new NexsonParseException("missing property name: " + j);

			} else { // there is a property name so we can continue

				if (propertyName.equals(OTVocabularyObject.OT_ANNOTATION.propertyName())) { // annotations

					addAnnotation(new NexsonAnnotation(j));
					
				} else { // simple properties
					
					// looking for either "$" or "@href" (former is more frequent)
					Object value = (j.get("$"));
					if (value == null) {
						value = j.get("@href");
					}

					// check tags to see if this element has been marked for deletion
					if (propertyName.equals(OTVocabularyPredicate.OT_TAG.propertyName()) && value != null) {
						if (String.valueOf(value).startsWith("del")) {
							deprecatedStatus = true;
						}
					}
					
					// record the property
					setProperty(propertyName, value);
				}
			}
		}
	}
}
