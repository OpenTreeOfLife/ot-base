package org.opentree.nexson.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class AnnotationAuthor {

	private String name	= null; // Name of software that produced the annotation
	private String url = null; // URL of service or page that describes the tool
	private String description	= null; // human-readable description of the tool
	private String version	= null; // version number string of the authoring tool
	private AuthorInvocation invocation = new AuthorInvocation(); // object that contains the relevant info need to rerun. Fields described in the next rows
	Set<String> checksPerformed	= new HashSet<String>(); // list of Message Codes that the service claims to check for
	Map<String, Object> properties = new HashMap<String,Object>();

	/**
	 * Create an AnnotationAuthor object from the provided NexSON.
	 * @param nexson
	 */
	public AnnotationAuthor(JSONObject nexson) {
		parseNexson(nexson);
	}
	
	/**
	 * Create an empty AnnotationAuthor object.
	 */
	public AnnotationAuthor() { }
	
	/**
	 * A class containing information about the invocation of the annotating program or service (if it is one).
	 * @author cody
	 *
	 */
	public class AuthorInvocation {
		private List<String> commandLineArgs = new ArrayList<String>(); // (optional) args
		private Object env = null; // relevant key to string value pairs
		private String method = null; // GET, PUT... for web services
		private Object params = null; // parameters required in for the web-services call
		Set<String> checksPerformed	= new HashSet<String>(); // list of Message Codes that the service claims to check for

		/**
		 * Create an AuthorInvocation object from the provided NexSON.
		 */
		public AuthorInvocation(JSONObject nexson) {
			parseNexson(nexson);
		}

		/**
		 * Create an empty AuthorInvocation object.
		 */
		public AuthorInvocation() { }
		
		// ### getters
		
		public List<String> getCommandLineArgs() {
			return commandLineArgs;
		}
		
		public Object getEnv() { // TODO: wtf is this?
			return env;
		}

		public String getMethod() {
			return method;
		}
		
		public Object getParams() {
			return params;
		}

		public Set<String> getChecksPerformed() {
			return checksPerformed;
		}
		
		// ### setters
		
		public void addCommandLineArg(String arg) {
			commandLineArgs.add(arg);
		}
		
		public void setEnv(Object env) { // TODO: wtf is this?
			this.env = env;
		}

		public void setMethod(String method) {
			this.method = method;
		}
		
		public void setParams(Object params) {
			this.params = params;
		}
		
		public void addCheckPerformed(String checkPerformed) {
			this.checksPerformed.add(checkPerformed);
		}
		
		/**
		 * Parse incoming nexson to set object properties.
		 * @param nexson
		 */
		protected void parseNexson(JSONObject nexson) {

			JSONArray commandLineArgs = (JSONArray) nexson.get("commandLine");
			if (commandLineArgs != null) {
				for (Object argObj : commandLineArgs) {
					addCommandLineArg((String) argObj);
				}
			}

			setEnv ((Object) nexson.get("env"));
			setMethod ((String) nexson.get("method"));
			setParams((Object) nexson.get("params"));
			
			JSONArray checksPerformedArr = (JSONArray) nexson.get("checksPerformed");
			if (checksPerformedArr != null) {
				for (Object checkObj : (JSONArray) nexson.get("checksPerformed")) {
					addCheckPerformed((String) checkObj);
				}
			}
		}
	}

	// ### getters
	
	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getVersion() {
		return version;
	}
	
	public AuthorInvocation getInvocation() {
		return invocation;
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public Object getProperty(String name) {
		return properties.get(name);
	}

	public boolean hasProperty(String name) {
		return properties.containsKey(name);
	}

	// ### setters
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public void setInvocation(AuthorInvocation invocation) {
		this.invocation = invocation;
	}

	public void addProperty(String name, Object value) {
		properties.put(name, value);
	}
	
	protected void parseNexson(JSONObject nexson) {

		setName((String) nexson.get("name"));
		setUrl((String) nexson.get("url"));
		setDescription((String) nexson.get("description"));
		setVersion((String) nexson.get("version"));
		setInvocation(new AuthorInvocation((JSONObject) nexson.get("invocation")));
		
		// TODO: set optional properties, would be better if these were stored in a meta tag so we could just iterate over them
	}
}
