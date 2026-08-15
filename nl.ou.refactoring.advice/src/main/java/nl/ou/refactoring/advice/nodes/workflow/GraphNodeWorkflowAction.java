package nl.ou.refactoring.advice.nodes.workflow;

import java.util.Optional;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeFinalises;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgePrecedes;

/**
 * Represents an actionable node in a Refactoring Advice Graph workflow.
 */
public abstract class GraphNodeWorkflowAction extends GraphNodeWorkflow {
	/**
	 * Initialises a new instance of {@link GraphNodeWorkflowAction}.
	 * 
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeWorkflowAction(Graph graph) throws ArgumentNullException {
		super(graph);
	}

	/**
	 * Gets the directly preceding workflow action.
	 * 
	 * @return The directly preceding workflow action wrapped in an {@link Optional}
	 *         or if not present, an empty {@link Optional}.
	 */
	public Optional<GraphNodeWorkflowAction> getPreceding() {
		return this.getEdgesIncoming(GraphEdgePrecedes.class).stream().map((edge) -> edge.getSourceNode()).findAny();
	}

	/**
	 * Gets the length of a chain of workflow actions in preceding direction.
	 * 
	 * @param workflowAction The preceding workflow action.
	 * @return The length of the chain of workflow actions, if there is any. If
	 *         there is no chain, -1 is returned.
	 */
	public int getPrecedingLength(GraphNodeWorkflowAction workflowAction) {
		return this.getPreceding().map((precedingNode) -> {
			if (precedingNode.equals(workflowAction)) {
				return 1;
			}

			final var nextLength = precedingNode.getPrecedingLength(workflowAction);
			if (nextLength < 0) {
				return nextLength;
			}
			return nextLength + 1;
		}).orElse(-1);
	}

	/**
	 * Gets the directly succeeding workflow action.
	 * 
	 * @return The directly succeeding workflow action wrapped in an
	 *         {@link Optional} or if not present, an empty {@link Optional}.
	 */
	public Optional<GraphNodeWorkflowAction> getSucceeding() {
		return this.getEdges(GraphEdgePrecedes.class).stream().map((edge) -> edge.getDestinationNode()).findAny();
	}

	/**
	 * Gets the length of a chain of workflow actions in succeeding direction.
	 * 
	 * @param workflowAction The succeeding workflow action.
	 * @return The length of the chain of workflow actions, if there is any. If
	 *         there is no chain, -1 is returned.
	 */
	public int getSucceedingLength(GraphNodeWorkflowAction workflowAction) {
		return this.getSucceeding().map((succeedingNode) -> {
			if (succeedingNode.equals(workflowAction)) {
				return 1;
			}

			final var nextLength = succeedingNode.getSucceedingLength(workflowAction);
			if (nextLength < 0) {
				return nextLength;
			}
			return nextLength + 1;
		}).orElse(-1);
	}

	/**
	 * Indicates whether the workflow action finalises the refactoring.
	 * 
	 * @return True if the workflow action finalises the refactoring, false if not.
	 */
	public boolean isFinalising() {
		return this.getEdges(GraphEdgeFinalises.class).size() == 1;
	}

	/**
	 * Indicates whether this workflow action is preceded by the specified workflow
	 * action.
	 * 
	 * @param workflowAction The workflow action that may or may not precede the
	 *                       current workflow action.
	 * @return True if the specified workflow action precedes the current workflow
	 *         action, false if not.
	 */
	public boolean isPrecededBy(GraphNodeWorkflowAction workflowAction) {
		return this.getPrecedingLength(workflowAction) > 0;
	}

	/**
	 * Indicates whether this workflow action is succeeded by the specified workflow
	 * action.
	 * 
	 * @param workflowAction The workflow action that may or may not succeed the
	 *                       current workflow action.
	 * @return True if the specified workflow action succeeds the current workflow
	 *         action, false if not.
	 */
	public boolean isSucceededBy(GraphNodeWorkflowAction workflowAction) {
		return this.getSucceedingLength(workflowAction) > 0;
	}

	/**
	 * Finalises the refactoring.
	 * 
	 * @return An edge {@link GraphEdgeFinalises} that finalises the refactoring.
	 */
	public GraphEdgeFinalises finalises() throws RefactoringMustContainStartNodeException {
		final var startNode = this.graph.getStart().orElseThrow(() -> new RefactoringMustContainStartNodeException());
		return this.graph.computeEdge(
				this,
				startNode,
				(source, destination) -> new GraphEdgeFinalises(source, destination),
				GraphEdgeFinalises.class);
	}

	/**
	 * Chains a workflow step to this workflow step. If the relationship already
	 * exists, no edges will be added to the graph and the existing edge will be
	 * returned.
	 * 
	 * @param next The next workflow step in the chain.
	 * @return An edge {@link GraphEdgePrecedes} that is connected to this node and
	 *         the next node.
	 * @throws ArgumentNullException Thrown if next is null.
	 */
	public GraphEdgePrecedes precedes(GraphNodeWorkflowAction next) throws ArgumentNullException {
		return this.graph.computeEdge(
				this,
				next,
				(sourceNode, destinationNode) -> new GraphEdgePrecedes(sourceNode, destinationNode),
				GraphEdgePrecedes.class);
	}
}
