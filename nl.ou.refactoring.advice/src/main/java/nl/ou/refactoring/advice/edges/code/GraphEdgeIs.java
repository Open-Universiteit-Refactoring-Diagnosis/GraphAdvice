package nl.ou.refactoring.advice.edges.code;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.GraphNodeInterface;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperationParameter;
import nl.ou.refactoring.advice.nodes.code.GraphNodeType;

/**
 * An edge that represents the type of a code symbol.
 * @implNote Code edges follow Object Constraint Language (OCL) standards.
 */
public final class GraphEdgeIs extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeIs}.
	 * @param attributeNode The Attribute that has a particular data type.
	 * @param typeNode The data Type of the Attribute.
	 * @throws ArgumentNullException Thrown if attributeNode or typeNode is null.
	 */
	public GraphEdgeIs(GraphNodeAttribute attributeNode, GraphNodeType typeNode)
			throws ArgumentNullException {
		super(attributeNode, typeNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeIs}.
	 * @param specialisedClassNode The Class that is a specialisation of the generalised Class.
	 * @param generalisedClassNode The Class that is a generalisation of the specialised Class.
	 * @throws ArgumentNullException Thrown if specialisedClassNode or generalisedClassNode is null.
	 */
	public GraphEdgeIs(GraphNodeClass specialisedClassNode, GraphNodeClass generalisedClassNode)
			throws ArgumentNullException {
		super(specialisedClassNode, generalisedClassNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeIs}.
	 * @param classNode The Class that implements the Interface.
	 * @param interfaceNode The Interface that is implemented by the Class.
	 * @throws ArgumentNullException Thrown if classNode or interfaceNode is null.
	 */
	public GraphEdgeIs(GraphNodeClass classNode, GraphNodeInterface interfaceNode)
			throws ArgumentNullException {
		super(classNode, interfaceNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeIs}.
	 * @param derivedInterfaceNode The Interface that derives from the base Interface.
	 * @param baseInterfaceNode The base Interface.
	 * @throws ArgumentNullException Thrown if derivedInterfaceNode or baseInterfaceNode is null.
	 */
	public GraphEdgeIs(GraphNodeInterface derivedInterfaceNode, GraphNodeInterface baseInterfaceNode)
			throws ArgumentNullException {
		super(derivedInterfaceNode, baseInterfaceNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeIs}.
	 * @param operationNode The Operation that has a particular return Type.
	 * @param typeNode The return Type of the Operation.
	 * @throws ArgumentNullException Thrown if operationNode or typeNode is null.
	 */
	public GraphEdgeIs(GraphNodeOperation operationNode, GraphNodeType typeNode)
			throws ArgumentNullException {
		super(operationNode, typeNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeIs}.
	 * @param operationParameterNode The Operation Parameter that has a particular data type.
	 * @param typeNode The data Type of the Operation Parameter.
	 * @throws ArgumentNullException Thrown if operationParameterNode or typeNode is null.
	 */
	public GraphEdgeIs(GraphNodeOperationParameter operationParameterNode, GraphNodeType typeNode)
			throws ArgumentNullException {
		super(operationParameterNode, typeNode);
	}
}
