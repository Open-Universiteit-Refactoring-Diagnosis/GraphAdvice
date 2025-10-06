package nl.ou.refactoring.advice.edges.code;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.GraphNodeInterface;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;

/**
 * An edge that represents ownership of a code symbol.
 * @implNote Code edges follow Object Constraint Language (OCL) standards.
 */
public final class GraphEdgeHas extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param classNode A Class node that owns the Attribute node.
	 * @param attributeNode An Attribute node that is owned by the Class node.
	 * @throws ArgumentNullException Thrown if classNode or attributeNode is null.
	 */
	public GraphEdgeHas(GraphNodeClass classNode, GraphNodeAttribute attributeNode)
			throws ArgumentNullException {
		super(classNode, attributeNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param classNode A Class node that owns the Operation node.
	 * @param operationNode An Operation node that is owned by the Class node.
	 * @throws ArgumentNullException Thrown if classNode or operationNode is null.
	 */
	public GraphEdgeHas(GraphNodeClass classNode, GraphNodeOperation operationNode)
			throws ArgumentNullException {
		super(classNode, operationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param interfaceNode An Interface node that owns the Attribute node.
	 * @param attributeNode An Attribute node that is owned by the Interface node.
	 * @throws ArgumentNullException Thrown if interfaceNode or attributeNode is null.
	 */
	public GraphEdgeHas(GraphNodeInterface interfaceNode, GraphNodeAttribute attributeNode)
			throws ArgumentNullException {
		super(interfaceNode, attributeNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param interfaceNode An Interface node that owns the Operation node.
	 * @param operationNode An Operation node that is owned by the Interface node.
	 * @throws ArgumentNullException Thrown if interfaceNode or operationNode is null.
	 */
	public GraphEdgeHas(GraphNodeInterface interfaceNode, GraphNodeOperation operationNode)
			throws ArgumentNullException {
		super(interfaceNode, operationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param packageNode A Package node that owns the Class node.
	 * @param classNode A Class node that is owned by the Package node.
	 * @throws ArgumentNullException Thrown if packageNode or classNode is null.
	 */
	public GraphEdgeHas(GraphNodePackage packageNode, GraphNodeClass classNode)
			throws ArgumentNullException {
		super(packageNode, classNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param packageNode A Package node that owns the Interface node.
	 * @param interfaceNode An Interface node that is owned by the Package node.
	 * @throws ArgumentNullException Thrown if packageNode or interfaceNode is null.
	 */
	public GraphEdgeHas(GraphNodePackage packageNode, GraphNodeInterface interfaceNode)
			throws ArgumentNullException {
		super(packageNode, interfaceNode);
	}
}
