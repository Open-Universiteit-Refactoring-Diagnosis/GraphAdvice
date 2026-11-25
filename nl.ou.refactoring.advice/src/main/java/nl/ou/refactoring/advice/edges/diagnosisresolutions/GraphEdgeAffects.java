package nl.ou.refactoring.advice.edges.diagnosisresolutions;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeMethodInvocationExpression;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerBrokenLocalReferences;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerBrokenSubTyping;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerChangedNestedRelationship;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerCorrespondingSubclassSpecification;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerDoubleDefinition;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerForcedOverride;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerImposedSpecification;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerMissingAbstractImplementation;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerMissingConcreteImplementation;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerMissingDefinition;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerMissingSpecification;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerMissingSuperImplementation;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerPrecedingOverload;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerScopeShadowing;

/**
 * An edge that indicates that a refactoring Node affects a code symbol. This is
 * mainly used to connect refactoring workflow subgraphs with code symbol
 * subgraphs.
 */
public final class GraphEdgeAffects extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param brokenLocalReferences A Broken Local References risk.
	 * @param operationNode         The Operation that may contain a body with local
	 *                              references.
	 * @throws ArgumentNullException Thrown if brokenLocalReferences or
	 *                               operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeDangerBrokenLocalReferences brokenLocalReferences,
			GraphNodeOperation operationNode) throws ArgumentNullException {
		super(brokenLocalReferences, operationNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param brokenSubTyping A Broken Sub-typing Risk.
	 * @param operationNode   The Operation that may inadvertently override a method
	 *                        in a generalised class or the operation that is being
	 *                        overridden.
	 * @throws ArgumentNullException Thrown if brokenSubTyping or operationNode is
	 *                               null.
	 */
	public GraphEdgeAffects(GraphNodeDangerBrokenSubTyping brokenSubTyping, GraphNodeOperation operationNode)
			throws ArgumentNullException {
		super(brokenSubTyping, operationNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param changedNestedRelationship      A Changed Nested Relationship Risk.
	 * @param methodInvocationExpressionNode The Method Invocation Expression that
	 *                                       calls an Operation that is also
	 *                                       affected by the Changed Nested
	 *                                       Relationshop risk.
	 * @throws ArgumentNullException Thrown if changedNestedRelationship or
	 *                               methodInvocationExpressionNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeDangerChangedNestedRelationship changedNestedRelationship,
			GraphNodeMethodInvocationExpression methodInvocationExpressionNode) throws ArgumentNullException {
		super(changedNestedRelationship, methodInvocationExpressionNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param changedNestedRelationship A Changed Nested Relationship risk.
	 * @param operationNode             The Operation that may either be in an outer
	 *                                  class or an inner class that is involved in
	 *                                  the Changed Nested Relationship risk.
	 * @throws ArgumentNullException Thrown if changedNestedRelationship or
	 *                               operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeDangerChangedNestedRelationship changedNestedRelationship,
			GraphNodeOperation operationNode) throws ArgumentNullException {
		super(changedNestedRelationship, operationNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param correspondingSubclassSpecification A Corresponding Subclass
	 *                                           Specification risk.
	 * @param operationNode                      The Operation that may be defined
	 *                                           in a subclass or the operation that
	 *                                           is added.
	 * @throws ArgumentNullException Thrown if correspondingSubclassSpecification or
	 *                               operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeDangerCorrespondingSubclassSpecification correspondingSubclassSpecification,
			GraphNodeOperation operationNode) throws ArgumentNullException {
		super(correspondingSubclassSpecification, operationNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param doubleDefinition A Double Definition risk.
	 * @param attributeNode    The Attribute that may have the same definition as
	 *                         another Attribute in the same context.
	 * @throws ArgumentNullException Thrown if doubleDefinition or attributeNode is
	 *                               null.
	 */
	public GraphEdgeAffects(GraphNodeDangerDoubleDefinition doubleDefinition, GraphNodeAttribute attributeNode)
			throws ArgumentNullException {
		super(doubleDefinition, attributeNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param doubleDefinition A Double Definition risk.
	 * @param classNode        The Class that may have the same definition as
	 *                         another Class in the same context.
	 * @throws ArgumentNullException Thrown if doubleDefinition or classNode is
	 *                               null.
	 */
	public GraphEdgeAffects(GraphNodeDangerDoubleDefinition doubleDefinition, GraphNodeClass classNode)
			throws ArgumentNullException {
		super(doubleDefinition, classNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param doubleDefinition A Double Definition risk.
	 * @param operationNode    The Operation that may have the same definition as
	 *                         another Operation in the same context.
	 * @throws ArgumentNullException Thrown if doubleDefinition or operationNode is
	 *                               null.
	 */
	public GraphEdgeAffects(GraphNodeDangerDoubleDefinition doubleDefinition, GraphNodeOperation operationNode)
			throws ArgumentNullException {
		super(doubleDefinition, operationNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param forcedOverride A Forced Override risk.
	 * @param operationNode  The Operation that may override an Operation with the
	 *                       same signature in a super Class.
	 * @throws ArgumentNullException Thrown if forcedOverride or operationNode is
	 *                               null.
	 */
	public GraphEdgeAffects(GraphNodeDangerForcedOverride forcedOverride, GraphNodeOperation operationNode)
			throws ArgumentNullException {
		super(forcedOverride, operationNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param imposedSpecification An Imposed Specification risk.
	 * @param operationNode        The Operation that impose a new specification for
	 *                             subclass(es).
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphEdgeAffects(GraphNodeDangerImposedSpecification imposedSpecification, GraphNodeOperation operationNode)
			throws ArgumentNullException {
		super(imposedSpecification, operationNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param lostSpecification A Lost Specification risk.
	 * @param operationNode     The Operation that may define the specification of a
	 *                          root of a tree of overriding methods, or any of the
	 *                          overriding methods.
	 * @throws ArgumentNullException Thrown if lostSpecification or operationNode is
	 *                               null.
	 */
	public GraphEdgeAffects(GraphNodeDangerMissingSpecification lostSpecification, GraphNodeOperation operationNode)
			throws ArgumentNullException {
		super(lostSpecification, operationNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param missingAbstractImplementation A Missing Abstract Implementation risk.
	 * @param operationNode                 The Operation that is removed or the
	 *                                      abstract Operation.
	 * @throws ArgumentNullException Thrown if missingAbstractImplementation or
	 *                               operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeDangerMissingAbstractImplementation missingAbstractImplementation,
			GraphNodeOperation operationNode) throws ArgumentNullException {
		super(missingAbstractImplementation, operationNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param missingConcreteImplementation A Missing Concrete Implementation risk.
	 * @param operationNode                 The Operation that is removed or the
	 *                                      abstract Operation.
	 * @throws ArgumentNullException Thrown if missingConcreteImplementation or
	 *                               operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeDangerMissingConcreteImplementation missingConcreteImplementation,
			GraphNodeOperation operationNode) throws ArgumentNullException {
		super(missingConcreteImplementation, operationNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param missingDefinition A Missing Definition risk.
	 * @param attributeNode     The Attribute that may become missing.
	 * @throws ArgumentNullException Thrown if missingDefinition or attributeNode is
	 *                               null.
	 */
	public GraphEdgeAffects(GraphNodeDangerMissingDefinition missingDefinition, GraphNodeAttribute attributeNode)
			throws ArgumentNullException {
		super(missingDefinition, attributeNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param missingDefinition A Missing Definition risk.
	 * @param operationNode     The Operation that may become missing.
	 * @throws ArgumentNullException Thrown if missingDefinition or operationNode is
	 *                               null.
	 */
	public GraphEdgeAffects(GraphNodeDangerMissingDefinition missingDefinition, GraphNodeOperation operationNode)
			throws ArgumentNullException {
		super(missingDefinition, operationNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param missingDefinition A Missing Definition risk.
	 * @param classNode         The Class that may become missing.
	 * @throws ArgumentNullException Thrown if missingDefinition of classNode is
	 *                               null.
	 */
	public GraphEdgeAffects(GraphNodeDangerMissingDefinition missingDefinition, GraphNodeClass classNode)
			throws ArgumentNullException {
		super(missingDefinition, classNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param missingSuperImplementation A Missing Super Implementation risk.
	 * @param operationNode              The Operation that may contain an
	 *                                   overriding implementation.
	 * @throws ArgumentNullException Thrown if missingSuperImplementation or
	 *                               operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeDangerMissingSuperImplementation missingSuperImplementation,
			GraphNodeOperation operationNode) throws ArgumentNullException {
		super(missingSuperImplementation, operationNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param precedingOverload A Preceding Overload risk.
	 * @param operationNode     The Operation that may add an overload that precedes
	 *                          other overload(s).
	 * @throws ArgumentNullException Thrown if precedingOverload or operationNode is
	 *                               null.
	 */
	public GraphEdgeAffects(GraphNodeDangerPrecedingOverload precedingOverload, GraphNodeOperation operationNode)
			throws ArgumentNullException {
		super(precedingOverload, operationNode);
	}

	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * 
	 * @param scopeShadowing A Scope Shadowing risk.
	 * @param attributeNode  The Attribute that may shadow another Attribute.
	 * @throws ArgumentNullException Thrown if scopeShadowing or attributeNode is
	 *                               null.
	 */
	public GraphEdgeAffects(GraphNodeDangerScopeShadowing scopeShadowing, GraphNodeAttribute attributeNode)
			throws ArgumentNullException {
		super(scopeShadowing, attributeNode);
	}
}
