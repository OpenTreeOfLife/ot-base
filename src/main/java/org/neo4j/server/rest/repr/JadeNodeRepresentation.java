package org.neo4j.server.rest.repr;

import jade.tree.JadeNode;

import java.net.URI;
import java.util.Map;

import org.opentree.IterableArray;
import org.opentree.properties.JadeNodeProperty;
import org.opentree.properties.OTProperty;

public class JadeNodeRepresentation extends MappingRepresentation {

    public JadeNodeRepresentation(RepresentationType type) {
        super(type);
    }

    public JadeNodeRepresentation(String type) {
        super(type);
    }

    @Override
    String serialize(RepresentationFormat format, URI baseUri, ExtensionInjector extensions) {
        MappingWriter writer = format.serializeMapping(type);
        Serializer.injectExtensions(writer, this, baseUri, extensions);
        serialize(new MappingSerializer(writer, baseUri, extensions));
        writer.done();
        return format.complete(writer);
    }

    @Override
    void putTo(MappingSerializer serializer, String key) {
        serializer.putMapping(key, this);
    }

    @Override
    protected void serialize(MappingSerializer serializer) {

    }

    // TODO: put properties into the JadeNodeProperty enum
    
    public static MappingRepresentation getJadeNodeRepresentation(final JadeNode jadeNode) {

        return new MappingRepresentation(RepresentationType.MAP.toString()) {
            @Override
            protected void serialize(final MappingSerializer serializer) {

            	if (jadeNode.getName() != null) {
						serializer.putString("name", jadeNode.getName());
				} else {
					serializer.putString("name", "");
				}
				
				OTProperty[] displayProperties = null;
				if (jadeNode.hasAssocObject(JadeNodeProperty.DISPLAY_PROPERTIES.propertyName())) {
					displayProperties = (OTProperty[]) jadeNode.getObject(JadeNodeProperty.DISPLAY_PROPERTIES.propertyName());
				} 
				
				// add properties suitable for the JSON
				if (displayProperties != null) {
					for (OTProperty property : displayProperties) {
						if (jadeNode.getObject(property.propertyName()) != null) {
							
							if (property.type() == int.class || property.type() == Integer.class) {
								serializer.putNumber(property.propertyName(), (Integer) jadeNode.getObject(property.propertyName()));
	
							} else if (property.type() == double.class || property.type() == Double.class) {
								serializer.putNumber(property.propertyName(), (Double) jadeNode.getObject(property.propertyName()));
	
							} else if (property.type() == Boolean.class || property.type() == boolean.class) {
								serializer.putBoolean(property.propertyName(), (Boolean) jadeNode.getObject(property.propertyName()));
								
							} else if (property.type() == String.class) {
								serializer.putString(property.propertyName(), (String) jadeNode.getObject(property.propertyName()));
	
							} else if (property.type().isArray()) {
								serializer.putList(property.propertyName(), OTRepresentationConverter.getListRepresentation(new IterableArray(jadeNode.getObject(property.propertyName()))));

							// TODO: not clear how to infer map type...
								
							} else {
								serializer.putString(property.propertyName(), jadeNode.getObject(property.propertyName()).toString());
							}
						}
					}
				}
				
				serializer.putList("children", OTRepresentationConverter.getListRepresentation(jadeNode.getChildren()));

				Object hc = jadeNode.getObject("haschild");
				if (hc != null) {
					int nc = (Integer)jadeNode.getObject("numchild");
					if(nc > jadeNode.getChildCount()){
						serializer.putBoolean("notcomplete", true);
					}
				}
			}
        };
    }
}
