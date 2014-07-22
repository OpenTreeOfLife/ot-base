package org.opentree.graphdb;

import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.opentree.properties.OTPropertyPredicate;

/**
 * An abstraction of the Neo4J database that provides identical modes of access to both embedded and served databases,
 * and which defines some convenience methods for accessing certain database features (e.g. common types of index
 * queries).
 * 
 * @author cody hinchliff and stephen smith
 * 
 */
public class GraphDatabaseAgent {

	private EmbeddedGraphDatabase embeddedGraphDb;
	private GraphDatabaseService graphDbService;
	private boolean embedded;

	public GraphDatabaseAgent(GraphDatabaseService gdbs) {
		graphDbService = gdbs;
		embedded = false;
	}

	public GraphDatabaseAgent(EmbeddedGraphDatabase egdb) {
		embeddedGraphDb = egdb;
		embedded = true;
	}

	public GraphDatabaseAgent(String graphDbName) {
		embeddedGraphDb = new EmbeddedGraphDatabase(graphDbName);
		embedded = true;
	}

	public Index<Node> getNodeIndex(String indexName, String... parameters) {
		Index<Node> index;

		// Example of optional parameters that can be passed
		// Map<String,String> indexPars = MapUtil.stringMap( "type", "exact", "to_lower_case", "true" );
		// Map<String,String> indexPars = MapUtil.stringMap(IndexManager.PROVIDER, "lucene", "type", "fulltext");

		Map<String, String> indexPars = MapUtil.stringMap(parameters);
		if (embedded)
			index = embeddedGraphDb.index().forNodes(indexName, indexPars);
		else
			index = graphDbService.index().forNodes(indexName, indexPars);

		return index;
	}

	public Index<Relationship> getRelationshipIndex(String indexName, String... parameters) {
		Index<Relationship> index;

		// Example of optional parameters that can be passed
		// Map<String,String> indexPars = MapUtil.stringMap( "type", "exact", "to_lower_case", "true" );
		// Map<String,String> indexPars = MapUtil.stringMap(IndexManager.PROVIDER, "lucene", "type", "fulltext");

		Map<String, String> indexPars = MapUtil.stringMap(parameters);
		if (embedded)
			index = embeddedGraphDb.index().forRelationships(indexName, indexPars);
		else
			index = graphDbService.index().forRelationships(indexName, indexPars);

		return index;
	}

	public Node createNode() {
		if (embedded) {
			return embeddedGraphDb.createNode();
		} else {
			return graphDbService.createNode();
		}
	}

	public Transaction beginTx() {
		if (embedded) {
			return embeddedGraphDb.beginTx();
		} else {
			return graphDbService.beginTx();
		}
	}

	public Node getNodeById(Long arg0) {
		if (embedded) {
			return embeddedGraphDb.getNodeById(arg0);
		} else {
			return graphDbService.getNodeById(arg0);
		}
	}

	public Relationship getRelationshipById(Long arg0) {
		if (embedded) {
			return embeddedGraphDb.getRelationshipById(arg0);
		} else {
			return graphDbService.getRelationshipById(arg0);
		}
	}

	public void shutdownDb() {
		registerShutdownHook();
	}

	// TODO: add a hasGraphProperty method

	/**
	 * These assume that the graph properties will be stored at the root node as properties. The root node should always
	 * be node 0
	 * 
	 * @param propname
	 * @return
	 */
	public Object getGraphProperty(String propname) {
		if (embedded) {
			if (embeddedGraphDb.getNodeById(0).hasProperty(propname)) {
				return embeddedGraphDb.getNodeById(0).getProperty(propname);
			} else {
				return null;
			}
		} else {
			if (graphDbService.getNodeById(0).hasProperty(propname)) {
				return graphDbService.getNodeById(0).getProperty(propname);
			} else {
				return null;
			}
		}
	}

	/**
	 * These assume that the graph properties will be stored at the root node as properties. The root node should always
	 * be node 0
	 * 
	 * @param propname
	 * @return
	 */
	public Object getGraphProperty(OTPropertyPredicate gp) {
		return getGraphProperty(gp.propertyName());
	}

	/**
	 * These assume that the graph properties will be stored at the root node as properties. The root node should always
	 * be node 0
	 * 
	 * @param propname
	 * @return
	 */
	public void removeGraphProperty(OTPropertyPredicate gp) {
		removeGraphProperty(gp.propertyName());
	}

	/**
	 * These assume that the graph properties will be stored at the root node as properties. The root node should always
	 * be node 0
	 * 
	 * @param propname
	 * @return
	 */
	public void removeGraphProperty(String propname) {
		if (embedded) {
			Transaction tx = embeddedGraphDb.beginTx();
			try {
				embeddedGraphDb.getNodeById(0).removeProperty(propname);
				tx.success();
			} finally {
				tx.finish();
			}
		} else {
			Transaction tx = graphDbService.beginTx();
			try {
				graphDbService.getNodeById(0).removeProperty(propname);
				tx.success();
			} finally {
				tx.finish();
			}
		}
	}

	/**
	 * These assume that the graph properties will be stored at the actual graph db root node as properties. The root
	 * node of the graph db itself should always be node 0, although this is not necessarily of the tree alignment graph
	 * stored within the graphdb.
	 * 
	 * @param propname
	 *            , prop
	 * @return
	 */
	public void setGraphProperty(String propname, Object prop) {
		if (embedded) {
			Transaction tx = embeddedGraphDb.beginTx();
			try {
				embeddedGraphDb.getNodeById(0).setProperty(propname, prop);
				tx.success();
			} finally {
				tx.finish();
			}
		} else {
			Transaction tx = graphDbService.beginTx();
			try {
				graphDbService.getNodeById(0).setProperty(propname, prop);
				tx.success();
			} finally {
				tx.finish();
			}
		}
	}

	protected void registerShutdownHook() {

		if (embedded) {
			embeddedGraphDb.shutdown();

		} else {
			// Apparently the use of a a new thread instance is required when the graph is running as a service (at
			// least on localhost).
			// If we just call the shutdown() method directly from within a plugin (via GraphExplorer.shutdownDB() for
			// instance), the server fails.
			Runtime.getRuntime().addShutdownHook(new Thread() {

				@Override
				public void run() {
					graphDbService.shutdown();
				}
			});
		}
	}
}
