package nl.ou.refactoring.advice.edges.workflow;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.workflow.remedies.GraphNodeRemedyChooseDifferentName;
import nl.ou.refactoring.advice.nodes.workflow.remedies.GraphNodeRemedyRenameConflictingSymbol;
import nl.ou.refactoring.advice.nodes.workflow.remedies.GraphNodeRemedyUpdateReferences;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskMissingDefinition;

/**
 * An edge that indicates that a Remedy mitigates a Risk.
 */
public final class GraphEdgeMitigates extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeMitigates}.
	 * @param chooseDifferentName Choose a different name to mitigate a Double Definition.
	 * @param doubleDefinition The remedy mitigates a Double Definition.
	 * @throws ArgumentNullException Thrown if chooseDifferentName or doubleDefinition is null.
	 */
	public GraphEdgeMitigates(
			GraphNodeRemedyChooseDifferentName chooseDifferentName,
			GraphNodeRiskDoubleDefinition doubleDefinition)
					throws ArgumentNullException {
		super(chooseDifferentName, doubleDefinition);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeMitigates}.
	 * @param renameConflictingSymbol Rename the conflicting code symbol to mitigate a Double Definition.
	 * @param doubleDefinition The remedy mitigates a Double Definition.
	 * @throws ArgumentNullException Thrown if renameConflictingSymbol or doubleDefinition is null.
	 */
	public GraphEdgeMitigates(
			GraphNodeRemedyRenameConflictingSymbol renameConflictingSymbol,
			GraphNodeRiskDoubleDefinition doubleDefinition)
					throws ArgumentNullException {
		super(renameConflictingSymbol, doubleDefinition);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeMitigates}.
	 * @param updateReferences Update references to a moved or renamed code symbol to mitigate Missing Definition.
	 * @param missingDefinition The remedy mitigates a Missing Definition.
	 * @throws ArgumentNullException Thrown if updateReferences or missingDefinition is null.
	 */
	public GraphEdgeMitigates(
			GraphNodeRemedyUpdateReferences updateReferences,
			GraphNodeRiskMissingDefinition missingDefinition)
					throws ArgumentNullException {
		super(updateReferences, missingDefinition);
	}
}
