package nl.ou.refactoring.advice.nodes.workflow.risks;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeObsolesces;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeWorkflow;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeWorkflowAction;

/**
 * Represents a risk in a Refactoring Advice Graph.
 */
public abstract class GraphNodeRisk extends GraphNodeWorkflow {
	/**
	 * Initialises a new instance of {@link GraphNodeRisk}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRisk(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Gets the nodes that are affected by the risk.
	 * @return The nodes that are affected by the risk.
	 */
	public Set<GraphNode> getAffected() {
		return
				this
					.graph
					.getEdgesFrom(this, GraphEdgeAffects.class)
					.stream()
					.map(edge -> edge.getDestinationNode())
					.collect(Collectors.toSet());
	}
	
	/**
	 * Gets the nodes that neutralise the risk.
	 * @return The nodes that neutralise the risk.
	 */
	public Set<GraphNodeWorkflowAction> getNeutralisers() {
		final var neutralisers =
				this
					.graph
					.getEdgesTo(this, GraphEdgeObsolesces.class)
					.stream()
					.map(edge -> edge.getSourceNode())
					.collect(Collectors.toSet());
		if (neutralisers.size() <= 1) {
			return Collections.unmodifiableSet(neutralisers);
		}
		
		// Ensure a chain of workflow steps in the correct order.
		final var neutraliserWorkflowSteps =
				neutralisers
					.stream()
					.sorted((one, other) -> one.getPrecedingLength(other))
					.collect(Collectors.toList());
		if (neutraliserWorkflowSteps.size() <= 1) {
			return Collections.unmodifiableSet(neutralisers);
		}
		for (var i = neutraliserWorkflowSteps.size() - 1; i > 0; i--) {
			final var current = neutraliserWorkflowSteps.get(i);
			final var previous = neutraliserWorkflowSteps.get(i - 1);
			if (!current.isPrecededBy(previous)) {
				// return empty set if neutralisers do not form a chain
				return Collections.unmodifiableSet(Set.of());
			}
		}
		return Collections.unmodifiableSet(neutralisers);
	}
	
	@Override
	public String getLabel() {
		return this.getNeutralisers().size() > 0 ? "Risk" : "Danger";
	}
}
