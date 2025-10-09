package nl.ou.refactoring.advice.nodes.workflow;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgePrecedes;

/**
 * Represents an actionable node in a Refactoring Advice Graph workflow.
 */
public abstract class GraphNodeWorkflowAction extends GraphNodeWorkflow {
	/**
	 * Initialises a new instance of {@link GraphNodeWorkflowAction}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeWorkflowAction(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}

	/**
	 * Gets the directly preceding microstep.
	 * @return The directly preceding microstep or if it's not there, null.
	 */
	public GraphNodeWorkflowAction getPreceding() {
		final var edge =
				this
					.graph
					.getEdgesTo(this, GraphEdgePrecedes.class)
					.stream()
					.findAny()
					.orElse(null);
		return edge == null ? null : edge.getSourceNode();
	}
	
	/**
	 * Gets the length of a chain of workflow actions.
	 * @param workflowAction The preceding workflow action.
	 * @return The length of the chain of workflow actions, if there is any. If there is no chain, -1 is returned.
	 */
	public int getPrecedingLength(GraphNodeWorkflowAction workflowAction) {
		final var directlyPrecedingWorkflowStep = this.getPreceding();
		if (directlyPrecedingWorkflowStep == null) {
			return -1;
		}
		if (directlyPrecedingWorkflowStep.equals(workflowAction)) {
			return 1;
		}
		final var nextLength = directlyPrecedingWorkflowStep.getPrecedingLength(workflowAction);
		if (nextLength < 0) {
			return nextLength;
		}
		return nextLength + 1;
	}
	
	/**
	 * Indicates whether this workflow action is preceded by the specified workflow action.
	 * @param workflowAction The workflow action that may or may not precede the current workflow action.
	 * @return True if the specified workflow action precedes the current workflow action, false if not.
	 */
	public boolean isPrecededBy(GraphNodeWorkflowAction workflowAction) {
		return this.getPrecedingLength(workflowAction) > 0;
	}
	
	/**
	 * Chains a workflow step to this workflow step.
	 * If the relationship already exists, no edges will be added to the graph and the existing edge will be returned.
	 * @param next The next workflow step in the chain.
	 * @return An edge {@link GraphEdgePrecedes} that is connected to this node and the next node.
	 * @throws ArgumentNullException Thrown if next is null.
	 */
	public GraphEdgePrecedes precedes(GraphNodeWorkflowAction next)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				next,
				(sourceNode, destinationNode) -> new GraphEdgePrecedes(sourceNode, destinationNode),
				GraphEdgePrecedes.class);
	}
}
