package org.opentree.nexson.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Container type for nexson annotation information
 * @author cody
 *
 */
public class NexsonAnnotation {
	
	private String id = null;
	private String description = null;
	private AnnotationAuthor author = null;
	private String dateCreated = null;
	private String dateModified = null;
	private List<AnnotationMessage> messages = new ArrayList<AnnotationMessage>();
	private boolean elementPassesValidation = true;
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	/**
	 * Create a NexsonAnnotation containing information extracted from the provided NexSON.
	 * @param nexson
	 */
	public NexsonAnnotation(JSONObject nexson) throws NexsonParseException {
		parseNexson(nexson);
	}
	
	/**
	 * Create an empty NexsonAnnotation.
	 */
	public NexsonAnnotation() { }

	// ### getters
	
	public String getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public AnnotationAuthor getAuthor() {
		return author;
	}

	public String getDateCreated() {
		return dateCreated; // should have option to get this as a time object
	}
	
	public String getDateModified() {
		return dateModified; // should have option to get this as a time object
	}
	
	public List<AnnotationMessage> getMessages() {
		return messages;
	}
	
	public boolean elementPassesValidation() {
		return elementPassesValidation;
	}
	
	// ### setters
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setAuthor(AnnotationAuthor author) {
		this.author = author;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated; // need time validation here
	}
	
	public void setDateModified(String dateModified) {
		this.dateModified = dateModified; // need time validation here
	}
	
	public void addMessage(AnnotationMessage message) {
		messages.add(message);
	}
	
	public void setPassesValidation(boolean passesValidation) {
		this.elementPassesValidation = passesValidation;
	}
	
	// ### for arbitrary properties (not predefined by nexson spec)
	
	public boolean hasProperty(String key) {
		return properties.containsKey(key);
	}
	
	public Object getProperty(String key) {
		return properties.get(key);
	}
	
	public Map<String, Object> getUserProperties() {
		return properties;
	}
	
	public void setUserProperty(String key, Object value) {
		properties.put(key, value);
	}

	// ### other methods
	
	protected void parseNexson(JSONObject nexson) throws NexsonParseException {

		for (Object keyObj : nexson.keySet()) {
			
			String key = null;
			if (keyObj instanceof String) {
				key = (String) keyObj;
			} else {
				throw new NexsonParseException("unrecognized property key in annotation: " + String.valueOf(keyObj));
			}
			
			// TODO: should be using an enum for property names
			
			if (key.equals("id")) {
				setId ((String) nexson.get(key));
				
			} else if (key.equals("$")) {
				setDescription ((String) nexson.get(key));

			} else if (key.equals("author")) {
				setAuthor(new AnnotationAuthor((JSONObject) nexson.get(key)));

			} else if (key.equals("setDateCreated")) {
				setDateCreated((String) nexson.get(key));

			} else if (key.equals("dateModified")) {
				setDateModified((String) nexson.get(key));

			} else if (key.equals("isValid")) { // TODO: update to "parentElementPassesValidation" when this change is made
				setPassesValidation((Boolean) nexson.get(key));

			} else if (key.startsWith("@") == false) { // arbitrary properties not defined in NexSON annotation spec
				setUserProperty(key, nexson.get(key));
			}
		}
		
		JSONArray messagesArr = (JSONArray) nexson.get("messages");
		if (messagesArr != null) {
			for (Object messageObj : messagesArr) {
				addMessage(new AnnotationMessage((JSONObject) messageObj));
			}
		}
	}
}
