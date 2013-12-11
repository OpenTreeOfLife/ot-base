package org.opentree.nexson.io;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

public class AnnotationMessage {

	private Severity severity = null; // = null;	// like logger message levels, see below
	private MessageCode	code = null; // one of the Message Codes (see below)
	private HumanMessageType humanMessageType = null; // one of the Human-readable Message Types (see below)
	private String humanMessage = null; // human-interpretable message (ie. no NexSON IDs). Optional if the Error Code indicates that a front end should be able generate a message from the code (see below).
	private String dataAnnotation = null; // More precise message for machine consumption
	private Object data	= null; // string or object	fields depend on the Message Code (see below)
	private String refersTo	= null; // path to the object that the message refers to (see path syntax below )
	private boolean preserve = false; // False by default. True serves as a flag to future invocations of the same tool indicating that the message should be retained (MTH: I can't remember the use case for this, but it was in my notes, so I included it here)
			
	Map<String, Object> properties = new HashMap<String,Object>();

	/**
	 * Enum constants identifying the available message types.
	 * @author cody
	 *
	 */
	public enum HumanMessageType {
		NONE,
		NOTE,
		COMMENT,
		REPLY,
		EXPLANATORYNOTE,
		QUESTION,
		ERRATUM
	}
	
	/**
	 * Enum constants identifying message levels.
	 * @author cody
	 *
	 */
	public enum Severity {
		WARNING,
		ERROR,
		INFO
	}
	
	/**
	 * Enum constants identifying message codes
	 * @author cody
	 *
	 */
	public enum MessageCode {
				
		/*


		TODO: this.
		
		Hopefully we will be able to require that data is a map. If so, it will be possible to predefine
		the object type (even complex ones) for every element in an enum...
		
		Might not be necessary to have a map... getData could just return an generic Object, the structure of which is defined below...
		
		
		REFERENCED_ID_NOT_FOUND	{key: string, value: string}	The NexSON attribute with the name key refers to an ID, but the ID is not in the NexSON. We have about 3000 cases of this with @otu in nodes or @source in edge objects not matching.
		TIP_WITHOUT_OTU	{}	refersTo object is a node that is a tip on the tree, but is not mapped to any OTU object. This is an NexSON error, not failure to map to OTT. We have about 3000 cases
		UNRECOGNIZED_PROPERTY_VALUE	{key: string, value: string}	the meta array associated key value pair in which the key is recognized, but the value is not valid. We have about 51 cases of this in which key is "ot:branchLengthMode" and value is "ot:years" (which is deprecated, I think).
		MISSING_OPTIONAL_KEY	string	the attribute is not found. Used to report lack of "ot:dataDeposit", "ot:focalClade", "ot:inGroupClade", "ot:ottolid", and "ot:studyPublication" fields. So we have about 32 thousand of these
		NO_ROOT_NODE	{}	tree that is refersTo has no node flagged as the root. We have 12 cases
		TIP_WITHOUT_OTT_ID	{}	refersTo is a node with and otu, but the otu has no OTT ID. (about 31 thousand cases)
		MULTIPLE_TIPS_MAPPED_TO_OTT_ID	{nodes:[list of IDs]}	refersTo is a tree the nodes listed are tips in the tree that map to the same OTT ID (about 31 thousand nodes)
		MULTIPLE_TREES	{}	trees element is refersTo and it has multiple trees with no indication of which one treemachine should prefer to use
		UNRECOGIZED_TAG	string	value of an ot:tag meta is not understood. This is not unexpected at all (and this sort of message will probably be suppressed), but the validator does emit it currently so we can see what tags are being used.
		UNVALIDATED_ANNOTATION	{key: string, value: string}	a object in the meta list was an unrecognized key. Not surprising (will be suppressed).
		CONFLICTING_PROPERTY_VALUES	list of key-value pairs that conflict	Flags with conflicting meanings, for example the "delete me" and the "choose me" tags
		NO_TREES	{}	file contains no trees that are not flagged for deletion
		NON_MONOPHYLETIC_TIPS_MAPPED_TO_OTT_ID	list of lists of IDs. each sublist is a set of nodes that are monophyletic on the tree and for which all the tips have the same OTT ID	This code is more serious than MULTIPLE_TIPS_MAPPED_TO_OTT_ID because it indicates cases in which different arbitrary prunings could lead to different phylogenetic statements
		Codes that can be emitted when we support POSTing of NexSON. These indicate serious problems with the NexSON (and we can probably be unfriendly about them in terms of UI, because they'll probably be encountered by developers):

		code name	data contents	explanation
		MISSING_MANDATORY_KEY	string - key name	refersTo object lacks a mandatory attribute.
		UNRECOGNIZED_KEY	string - key name	refersTo object has an attribute that is not allowed by the NeXML schema
		MISSING_LIST_EXPECTED	?	element (e.g. edge) that should be a list, was not
		DUPLICATING_SINGLETON_KEY	string	the attribute specified was encountered more than one time, though it should have been found only once (e.g. a doi)
		REPEATED_ID	string	ID found more than once
		MULTIPLE_ROOT_NODES	{}	tree has more than one node marked as root
		MULTIPLE_EDGES_FOR_NODES	{}	node has more than one edge to parent
		CYCLE_DETECTED	{node : id string}	tree has a cycle (including the referenced node)
		DISCONNECTED_GRAPH_DETECTED	{}	tree is not connected graph
		INCORRECT_ROOT_NODE_LABEL	{}	the node labelled as the root has a parent
		
		*/
	}
	
	/**
	 * Create an AnnotationMessage object from the provided NexSON.
	 * @param nexson
	 */
	public AnnotationMessage(JSONObject nexson) {
		parseNexson(nexson);
	}
	
	/**
	 * Create an empty AnnotationMessage object.
	 */
	public AnnotationMessage() { }
	
	// ### getters
	
	public Severity getSeverity() {
		return severity;
	}
	
	public MessageCode getCode() {
		return code;
	}
	
	public HumanMessageType getHumanMessageType() {
		return humanMessageType;
	}

	public String getHumanMessage() {
		return humanMessage;
	}

	public String getDataAnnotation() {
		return dataAnnotation;
	}
	
	public Object getData() { // TODO: wtf if this
		return data;
	}
	
	public String getRefersTo() {
		return refersTo;
	}
	
	public boolean getToBePreserved() {
		return preserve;
	}
	
	public Object getProperty(String name) {
		return properties.get(name);
	}
	
	public boolean hasProperty(String name) {
		return properties.containsKey(name);
	}

	// ### setters

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
	
	public void setCode(MessageCode code) {
		this.code = code;
	}
	
	public void setHumanMessageType(HumanMessageType type) {
		this.humanMessageType = type;
	}
	
	public void setHumanDescription(String message) {
		this.humanMessage = message;
	}
	
	public void setDataAnnotation(String dataAnnotation) {
		this.dataAnnotation = dataAnnotation;
	}
	
	public void setData(Object data) { // TODO: specify type
		this.data = data;
	}

	public void setRefersTo(String refersTo) {
		this.refersTo = refersTo;
	}
	
	public void setToBePreserved(boolean preserve) {
		this.preserve = preserve;
	}
	
	public void addProperty(String name, Object value) {
		properties.put(name, value);
	}
	
	public void parseNexson(JSONObject nexson) {
		
		setSeverity(Severity.valueOf((String) nexson.get("severity")));
//		setCode(MessageCode.valueOf((String) nexson.get("code")));
		System.out.println((String) nexson.get("code"));

//		setHumanMessageType(HumanMessageType.valueOf((String) nexson.get("humanReadableMessageType")));
		System.out.println((String) nexson.get("humanReadableMessageType"));

		if (getHumanMessageType() != HumanMessageType.NONE) {
			setHumanDescription((String) nexson.get("humanReadableMessage"));
		}
	
		setDataAnnotation((String) nexson.get("dataAnnotation"));
//		setData((JSONObject) nexson.get("data"));
		data = (Object) nexson.get("data");
		if (data != null) {
			System.out.println(data.toString());
		}
		
//		setRefersTo((String) nexson.get("refersTo"));
		System.out.println(((JSONObject) nexson.get("refersTo")).toJSONString());

		setToBePreserved((Boolean) nexson.get("preserve"));

		// TODO: set arbitrary properties. will work better if these are set in meta elements so they are straightforward to access
		
	}
}
