package nl.ou.refactoring.advice.nodes.workflow;

import java.util.Optional;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgePrecedes;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.resources.ResourceProvider;
import nl.ou.refactoring.advice.validation.workflow.RefactoringMayContainOnlyOneStartNodeException;

/**
 * Represents the start node of a refactoring in a Refactoring Advice Graph.
 */
public final class GraphNodeRefactoringStart extends GraphNodeWorkflow {
	/**
	 * Initialises a new instance of {@link GraphNodeRefactoringStart}.
	 *
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException                          Thrown if graph or
	 *                                                        refactoringName is
	 *                                                        null.
	 * @throws RefactoringMayContainOnlyOneStartNodeException Thrown if the graph
	 *                                                        already contains a
	 *                                                        start node.
	 */
	public GraphNodeRefactoringStart(Graph graph)
			throws ArgumentNullException, RefactoringMayContainOnlyOneStartNodeException {
		super(graph);
		if (graph.getNodes(GraphNodeRefactoringStart.class).size() > 1) {
			throw new RefactoringMayContainOnlyOneStartNodeException();
		}
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeRefactoringStart(graph);
	}

	@Override
	public boolean equals(GraphNode other) {
		return other instanceof GraphNodeRefactoringStart
				&& this.getRefactoringName().equals(((GraphNodeRefactoringStart) other).getRefactoringName());
	}

	@Override
	public String getCaption() {
		return this.getRefactoringName();
	}

	@Override
	public String getLabel() {
		return ResourceProvider.GraphNodeLabels.getLabel(GraphNodeRefactoringStart.class);
	}

	/**
	 * Gets the workflow node that is the first step of the refactoring.
	 *
	 * @return The workflow node that is initiated by the start of the refactoring,
	 *         if present, wrapped in an {@link Optional}. If not present, an empty
	 *         {@link Optional}.
	 */
	public Optional<GraphNodeWorkflow> getFirstStep() {
		return this.getEdges(GraphEdgePrecedes.class).stream().map((edge) -> edge.getDestinationNode()).findAny();
	}

	/**
	 * Gets the name of the refactoring that is started.
	 *
	 * @return The name of the refactoring that is started.
	 */
	public String getRefactoringName() {
		return this.graph.getRefactoringName();
	}

	/**
	 * Initiates a refactoring with a particular type of workflow step. If the
	 * relationship already exists, no edges will be added to the graph and the
	 * existing edge will be returned.
	 *
	 * @param workflowStep The workflow step to initiate from the refactoring.
	 * @return The initial microstep.
	 * @throws ArgumentNullException Thrown if microstep is null.
	 */
	public GraphEdgePrecedes initiates(GraphNodeWorkflow workflowStep) throws ArgumentNullException {
		return this.graph.computeEdge(
				this,
				workflowStep,
				(source, destination) -> new GraphEdgePrecedes(source, destination),
				GraphEdgePrecedes.class);
	}
}