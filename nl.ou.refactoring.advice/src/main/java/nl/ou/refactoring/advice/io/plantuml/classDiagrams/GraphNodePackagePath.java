package nl.ou.refactoring.advice.io.plantuml.classDiagrams;

import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;

/**
 * A path in a package tree.
 */
final class GraphNodePackagePath {
	/**
	 * The node that represents a package node in a package tree.
	 */
	private final GraphNodePackage packageNode;
	
	/**
	 * The path associated with the current package node.
	 */
	private final String path;
	
	/**
	 * Initialises a new instance of {@link GraphNodePackagePath}.
	 * @param packageNode The node that represents a package node in a package tree.
	 * @param path The path associated with the current package node.
	 */
	GraphNodePackagePath(GraphNodePackage packageNode, String path) {
		this.packageNode = packageNode;
		this.path = path;
	}
	
	/**
	 * Gets the node that represents a package node in a package tree.
	 * @return The node that represents a package node in a package tree.
	 */
	GraphNodePackage getPackageNode() {
		return this.packageNode;
	}
	
	/**
	 * Gets the path associated with the current package node.
	 * @return The path associated with the current package node.
	 */
	String getPath() {
		return this.path;
	}
}
