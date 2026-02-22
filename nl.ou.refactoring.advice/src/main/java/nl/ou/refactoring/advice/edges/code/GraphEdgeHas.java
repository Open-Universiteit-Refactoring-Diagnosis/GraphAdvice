package nl.ou.refactoring.advice.edges.code;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodeInterface;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeBlock;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeStatementExpression;
import nl.ou.refactoring.advice.nodes.code.operations.statements.GraphNodeExpressionStatement;
import nl.ou.refactoring.advice.nodes.code.operations.statements.GraphNodeStatement;

/**
 * An edge that represents ownership of a code symbol.
 * Code edges follow Object Constraint Language (OCL) standards.
 */
public final class GraphEdgeHas extends GraphEdge {
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
	 * @param outerClassNode A Class node that represents an outer class.
	 * @param innerClassNode A class node that represents an inner class.
	 * @throws ArgumentNullException Thrown if outerClassNode or innerClassNode is null.
	 */
	public GraphEdgeHas(GraphNodeClass outerClassNode, GraphNodeClass innerClassNode)
			throws ArgumentNullException {
		super(outerClassNode, innerClassNode);
	}
	
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
	 * @param operationNode The Operation node that contains the Block node.
	 * @param blockNode A Block node that is the body of the operation represented by operationNode.
	 * @throws ArgumentNullException Thrown if operationNode or blockNode is null.
	 */
	public GraphEdgeHas(GraphNodeOperation operationNode, GraphNodeBlock blockNode)
			throws ArgumentNullException {
		super(operationNode, blockNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param blockNode A Block node that contains the Statement node.
	 * @param statementNode A Statement node that is contained by the Block node.
	 * @throws ArgumentNullException Thrown if blockNode or statementNode is null.
	 */
	public GraphEdgeHas(GraphNodeBlock blockNode, GraphNodeStatement statementNode)
			throws ArgumentNullException {
		super(blockNode, statementNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param expressionStatement A node that represents a statement that contains an expression.
	 * @param statementExpression A node that represents an expression in a statement.
	 * @throws ArgumentNullException Thrown if expressionStatement or statementExpression is null.
	 */
	public GraphEdgeHas(GraphNodeExpressionStatement expressionStatement, GraphNodeStatementExpression statementExpression)
			throws ArgumentNullException {
		super(expressionStatement, statementExpression);
	}
}
