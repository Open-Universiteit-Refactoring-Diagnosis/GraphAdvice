package ou.graphAdvice.edges.refactoring;

import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.edges.GraphEdge;
import ou.graphAdvice.nodes.code.GraphNodeAttribute;
import ou.graphAdvice.nodes.code.GraphNodeClass;
import ou.graphAdvice.nodes.code.GraphNodeOperation;
import ou.graphAdvice.nodes.refactoring.remedies.GraphNodeRemedyChooseDifferentName;
import ou.graphAdvice.nodes.refactoring.remedies.GraphNodeRemedyRenameConflictingSymbol;
import ou.graphAdvice.nodes.refactoring.risks.GraphNodeRiskBrokenLocalReferences;
import ou.graphAdvice.nodes.refactoring.risks.GraphNodeRiskBrokenSubTyping;
import ou.graphAdvice.nodes.refactoring.risks.GraphNodeRiskCorrespondingSubclassSpecification;
import ou.graphAdvice.nodes.refactoring.risks.GraphNodeRiskDoubleDefinition;
import ou.graphAdvice.nodes.refactoring.risks.GraphNodeRiskLostSpecification;
import ou.graphAdvice.nodes.refactoring.risks.GraphNodeRiskMissingAbstractImplementation;
import ou.graphAdvice.nodes.refactoring.risks.GraphNodeRiskMissingDefinition;
import ou.graphAdvice.nodes.refactoring.risks.GraphNodeRiskMissingSuperImplementation;
import ou.graphAdvice.nodes.refactoring.risks.GraphNodeRiskOverloadParameterConversion;
import ou.graphAdvice.nodes.refactoring.risks.GraphNodeRiskRemoveConcreteOverride;

/**
 * An edge that indicates that a refactoring node affects a code symbol.
 * @implNote This is mainly used to connect refactoring subgraphs with code symbol subgraphs.
 */
public final class GraphEdgeAffects extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param chooseDifferentName Choose a different name to mitigate a Double Definition risk.
	 * @param attributeNode The Attribute that will receive a different name.
	 * @throws ArgumentNullException Thrown if chooseDifferentName or attributeNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRemedyChooseDifferentName chooseDifferentName,
			GraphNodeAttribute attributeNode)
					throws ArgumentNullException {
		super(chooseDifferentName, attributeNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param chooseDifferentName Choose a different name to mitigate a Double Definition risk.
	 * @param classNode The Class that will receive a different name.
	 * @throws ArgumentNullException Thrown if chooseDifferentName or classNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRemedyChooseDifferentName chooseDifferentName,
			GraphNodeClass classNode)
					throws ArgumentNullException {
		super(chooseDifferentName, classNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param chooseDifferentName Choose a different name to mitigate a Double Definition risk.
	 * @param operationNode The Operation that will receive a different name.
	 * @throws ArgumentNullException Thrown if chooseDifferentName or operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRemedyChooseDifferentName chooseDifferentName,
			GraphNodeOperation operationNode)
					throws ArgumentNullException {
		super(chooseDifferentName, operationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param renameConflictingSymbol Rename the conflicting code symbol to mitigate a Double Definition risk.
	 * @param attributeNode The Attribute that will be renamed or the Attribute that is introduced.
	 * @throws ArgumentNullException Thrown if renameConflictingSymbol or attributeNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRemedyRenameConflictingSymbol renameConflictingSymbol,
			GraphNodeAttribute attributeNode)
					throws ArgumentNullException {
		super(renameConflictingSymbol, attributeNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param renameConflictingSymbol Rename the conflicting code symbol to mitigate a Double Definition risk.
	 * @param classNode The Class that will be renamed or the Class that is introduced.
	 * @throws ArgumentNullException Thrown if renameConflictingSymbol or classNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRemedyRenameConflictingSymbol renameConflictingSymbol,
			GraphNodeClass classNode)
					throws ArgumentNullException {
		super(renameConflictingSymbol, classNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param renameConflictingSymbol Rename the conflicting code symbol to mitigate a Double Definition risk.
	 * @param operationNode The Operation that will be renamed or the Operation that is introduced.
	 * @throws ArgumentNullException Thrown if renameConflictingSymbol or operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRemedyRenameConflictingSymbol renameConflictingSymbol,
			GraphNodeOperation operationNode)
					throws ArgumentNullException {
		super(renameConflictingSymbol, operationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param brokenLocalReferences A Broken Local References risk.
	 * @param operationNode The Operation that may contain a body with local references.
	 * @throws ArgumentNullException Thrown if brokenLocalReferences or operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRiskBrokenLocalReferences brokenLocalReferences,
			GraphNodeOperation operationNode)
					throws ArgumentNullException {
		super(brokenLocalReferences, operationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param brokenSubTyping A Broken Sub-typing Risk.
	 * @param operationNode The Operation that may inadvertently override a method in a generalised class or the operation that is being overridden.
	 * @throws ArgumentNullException Thrown if brokenSubTyping or operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRiskBrokenSubTyping brokenSubTyping,
			GraphNodeOperation operationNode)
					throws ArgumentNullException {
		super(brokenSubTyping, operationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param correspondingSubclassSpecification A Corresponding Subclass Specification risk.
	 * @param operationNode The Operation that may be defined in a subclass or the operation that is added.
	 * @throws ArgumentNullException Thrown if correspondingSubclassSpecification or operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRiskCorrespondingSubclassSpecification correspondingSubclassSpecification,
			GraphNodeOperation operationNode)
					throws ArgumentNullException {
		super(correspondingSubclassSpecification, operationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param doubleDefinition A Double Definition risk.
	 * @param attributeNode The Attribute that may have the same definition as another Attribute in the same context.
	 * @throws ArgumentNullException Thrown if doubleDefinition or attributeNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRiskDoubleDefinition doubleDefinition,
			GraphNodeAttribute attributeNode)
					throws ArgumentNullException {
		super(doubleDefinition, attributeNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param doubleDefinition A Double Definition risk.
	 * @param classNode The Class that may have the same definition as another Class in the same context.
	 * @throws ArgumentNullException Thrown if doubleDefinition or classNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRiskDoubleDefinition doubleDefinition,
			GraphNodeClass classNode)
					throws ArgumentNullException {
		super(doubleDefinition, classNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param doubleDefinition A Double Definition risk.
	 * @param operationNode The Operation that may have the same definition as another Operation in the same context.
	 * @throws ArgumentNullException Thrown if doubleDefinition or operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRiskDoubleDefinition doubleDefinition,
			GraphNodeOperation operationNode)
					throws ArgumentNullException {
		super(doubleDefinition, operationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param lostSpecification A Lost Specification risk.
	 * @param operationNode The Operation that may define the specification of a root of a tree of overriding methods, or any of the overriding methods.
	 * @throws ArgumentNullException Thrown if lostSpecification or operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRiskLostSpecification lostSpecification,
			GraphNodeOperation operationNode)
					throws ArgumentNullException {
		super(lostSpecification, operationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param missingAbstractImplementation A Missing Abstract Implementation risk.
	 * @param operationNode The Operation that is removed or the abstract Operation.
	 * @throws ArgumentNullException Thrown if missingAbstractImplementation or operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRiskMissingAbstractImplementation missingAbstractImplementation,
			GraphNodeOperation operationNode)
					throws ArgumentNullException {
		super(missingAbstractImplementation, operationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param missingDefinition A Missing Definition risk.
	 * @param operationNode The Operation that may become missing.
	 * @throws ArgumentNullException Thrown if missingDefinition or operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRiskMissingDefinition missingDefinition,
			GraphNodeOperation operationNode) 
					throws ArgumentNullException {
		super(missingDefinition, operationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param missingSuperImplementation A Missing Super Implementation risk.
	 * @param operationNode The Operation that may contain an overriding implementation.
	 * @throws ArgumentNullException Thrown if missingSuperImplementation or operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRiskMissingSuperImplementation missingSuperImplementation,
			GraphNodeOperation operationNode)
					throws ArgumentNullException {
		super(missingSuperImplementation, operationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param overloadParameterConversion An Overload Parameter Conversion risk.
	 * @param operationNode The Operation that may be overloaded or the overloading Operation.
	 * @throws ArgumentNullException Thrown if overloadedParameterConversion or operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRiskOverloadParameterConversion overloadParameterConversion,
			GraphNodeOperation operationNode)
					throws ArgumentNullException {
		super(overloadParameterConversion, operationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAffects}.
	 * @param removeConcreteOverride A Remove Concrete Override risk.
	 * @param operationNode The Operation that is being removed and overrides another Operation, or the Operation that a call will fall back to.
	 * @throws ArgumentNullException Thrown if removeConcreteOverride or operationNode is null.
	 */
	public GraphEdgeAffects(
			GraphNodeRiskRemoveConcreteOverride removeConcreteOverride,
			GraphNodeOperation operationNode)
					throws ArgumentNullException {
		super(removeConcreteOverride, operationNode);
	}
}
