package nl.ou.refactoring.advice.edges.refactoring;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.refactoring.remedies.GraphNodeRemedyChooseDifferentName;
import nl.ou.refactoring.advice.nodes.refactoring.remedies.GraphNodeRemedyRenameConflictingSymbol;
import nl.ou.refactoring.advice.nodes.refactoring.risks.GraphNodeRiskDoubleDefinition;

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
}
