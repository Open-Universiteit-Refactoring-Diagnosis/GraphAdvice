package nl.ou.refactoring.advice.edges.code;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.GraphNodeInterface;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.GraphNodeProgramLocation;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeBlock;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperationParameter;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeAssignmentExpression;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeFieldAccess;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeAssignment;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeStatementExpression;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeLeftHandSide;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodePrimaryExpression;
import nl.ou.refactoring.advice.nodes.code.operations.statements.GraphNodeExpressionStatement;
import nl.ou.refactoring.advice.nodes.code.operations.statements.GraphNodeStatement;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;

/**
 * An edge that represents ownership of a code symbol.
 * Code edges follow Object Constraint Language (OCL) standards.
 */
public final class GraphEdgeHas extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param codeNode The node that represents a code symbol.
	 * @param programLocationNode The node that represents the location in a software program's source code where the code symbol can be found.
	 * @throws ArgumentNullException Thrown if codeNode or programLocationNode is null.
	 */
	public GraphEdgeHas(GraphNodeCode codeNode, GraphNodeProgramLocation programLocationNode)
			throws ArgumentNullException {
		super(codeNode, programLocationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param packageNode The node that represents a package.
	 * @param identifierNode The identifier of the package.
	 * @throws ArgumentNullException Thrown if packageNode or identifierNode is null.
	 */
	public GraphEdgeHas(GraphNodePackage packageNode, GraphNodeIdentifier identifierNode)
			throws ArgumentNullException {
		super(packageNode, identifierNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param packageNodeParent The node that represents the parent package.
	 * @param packageNodeChild The node that represents the child package.
	 * @throws ArgumentNullException Thrown if packageNodeParent or packageNodeChild is null.
	 */
	public GraphEdgeHas(GraphNodePackage packageNodeParent, GraphNodePackage packageNodeChild)
			throws ArgumentNullException {
		super(packageNodeParent, packageNodeChild);
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
	 * @param classNode A node that represents a Class.
	 * @param classNameIdentifierNode A node that represents the identifier that serves as the name of a Class.
	 * @throws ArgumentNullException Thrown if classNode or classNameIdentifierNode is null.
	 */
	public GraphEdgeHas(GraphNodeClass classNode, GraphNodeIdentifier classNameIdentifierNode)
			throws ArgumentNullException {
		super(classNode, classNameIdentifierNode);
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
	 * @param operationNode The node that represents the operation.
	 * @param operationName The node that represents the operation name.
	 * @throws ArgumentNullException Thrown if operationNode or operationName is null.
	 */
	public GraphEdgeHas(GraphNodeOperation operationNode, GraphNodeIdentifier operationName)
			throws ArgumentNullException {
		super(operationNode, operationName);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param operationNode The node that represents the operation.
	 * @param operationParameter The node that represents the first operation parameter.
	 * @throws ArgumentNullException Thrown if operationNode or operationParameter is null.
	 */
	public GraphEdgeHas(GraphNodeOperation operationNode, GraphNodeOperationParameter operationParameter)
			throws ArgumentNullException {
		super(operationNode, operationParameter);
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
	
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param assignment A node that represents a value assignment.
	 * @param leftHandSide The left hand side of the assignment.
	 * @throws ArgumentNullException Thrown if assignment or leftHandSide is null.
	 */
	public GraphEdgeHas(GraphNodeAssignment assignment, GraphNodeLeftHandSide leftHandSide)
			throws ArgumentNullException {
		super(assignment, leftHandSide);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param assignment A node that represents a value assignment.
	 * @param rightHandSide The right hand side of the assignment.
	 * @throws ArgumentNullException Thrown if assignment or rightHandSide is null.
	 */
	public GraphEdgeHas(GraphNodeAssignment assignment, GraphNodeAssignmentExpression rightHandSide)
			throws ArgumentNullException {
		super(assignment, rightHandSide);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param fieldAccess A node that represents a field access expression.
	 * @param primaryExpression A node that represents a primary expression.
	 * @throws ArgumentNullException Thrown if fieldAccess or primaryExpression is null.
	 */
	public GraphEdgeHas(GraphNodeFieldAccess fieldAccess, GraphNodePrimaryExpression primaryExpression)
			throws ArgumentNullException {
		super(fieldAccess, primaryExpression);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeHas}.
	 * @param fieldAccess A node that represents a field access expression.
	 * @param identifier A node that represents an identifier.
	 * @throws ArgumentNullException Thrown if fieldAccess or identifier is null.
	 */
	public GraphEdgeHas(GraphNodeFieldAccess fieldAccess, GraphNodeIdentifier identifier)
			throws ArgumentNullException {
		super(fieldAccess, identifier);
	}
}
