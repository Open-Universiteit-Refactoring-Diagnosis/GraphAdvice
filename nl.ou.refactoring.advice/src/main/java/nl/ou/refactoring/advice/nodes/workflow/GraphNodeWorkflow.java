package nl.ou.refactoring.advice.nodes.workflow;

import java.util.Optional;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgePrecedes;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.validation.workflow.RefactoringMustContainStartNodeException;

/**
 * Represents a node in a Refactoring Advice Graph that describes a component of
 * a refactoring.
 */
public abstract class GraphNodeWorkflow extends GraphNodeBase {
	/**
	 * Initialises a new instance of {@link GraphNodeWorkflow}.
	 * 
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeWorkflow(Graph graph) throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Finalises the refactoring.
	 * 
	 * @return An edge {@link GraphEdgeFinalises} that finalises the refactoring.
	 */
	public GraphEdgePrecedes finalises() throws RefactoringMustContainStartNodeException {
		final var endNode = this.graph.getEnd().orElse(new GraphNodeRefactoringEnd(this.getGraph()));
		return this.graph.computeEdge(
				this,
				endNode,
				(source, destination) -> new GraphEdgePrecedes(source, destination),
				GraphEdgePrecedes.class);
	}

	/**
	 * Gets the directly preceding workflow step.
	 * 
	 * @return The directly preceding workflow action wrapped in an {@link Optional}
	 *         or if not present, an empty {@link Optional}.
	 */
	public Optional<GraphNodeWorkflow> getPreceding() {
		return this.getEdgesIncoming(GraphEdgePrecedes.class).stream().map((edge) -> edge.getSourceNode()).findAny();
	}

	/**
	 * Gets the length of a chain of workflow actions in preceding direction.
	 * 
	 * @param workflowAction The preceding workflow action.
	 * @return The length of the chain of workflow actions, if there is any. If
	 *         there is no chain, -1 is returned.
	 */
	public int getPrecedingLength(GraphNodeWorkflow workflowAction) {
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
	 * Gets the directly succeeding workflow step.
	 * 
	 * @return The directly succeeding workflow step wrapped in an {@link Optional}
	 *         or if not present, an empty {@link Optional}.
	 */
	public Optional<GraphNodeWorkflow> getSucceeding() {
		return this.getEdges(GraphEdgePrecedes.class).stream().map((edge) -> edge.getDestinationNode()).findAny();
	}

	/**
	 * Gets the length of a chain of workflow steps in succeeding direction.
	 * 
	 * @param workflowStep The succeeding workflow step.
	 * @return The length of the chain of workflow steps, if there is any. If there
	 *         is no chain, -1 is returned.
	 */
	public int getSucceedingLength(GraphNodeWorkflow workflowStep) {
		return this.getSucceeding().map((succeedingNode) -> {
			if (succeedingNode.equals(workflowStep)) {
				return 1;
			}

			final var nextLength = succeedingNode.getSucceedingLength(workflowStep);
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
		return this.getSucceeding().map((node) -> node instanceof GraphNodeRefactoringEnd).orElse(false);
	}
}