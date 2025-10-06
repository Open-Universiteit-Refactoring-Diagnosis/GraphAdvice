package nl.ou.refactoring.advice.nodes.workflow;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeInitiates;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;

/**
 * Represents the start node of a refactoring in a Refactoring Advice Graph.
 */
public final class GraphNodeRefactoringStart extends GraphNodeRefactoring {
	/**
	 * Initialises a new instance of {@link GraphNodeRefactoringStart}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 * @throws RefactoringMayContainOnlyOneStartNodeException Thrown if the graph already contains a start node.
	 */
	public GraphNodeRefactoringStart(Graph graph)
			throws ArgumentNullException, RefactoringMayContainOnlyOneStartNodeException {
		super(graph);
		if (graph.getNodes(GraphNodeRefactoringStart.class).size() > 1) {
			throw new RefactoringMayContainOnlyOneStartNodeException();
		}
	}
	
	/**
	 * Initiates a refactoring with a particular type of microstep.
	 * If the relationship already exists, no edges will be added to the graph and the existing edge will be returned.
	 * @param microstep The microstep to initiate from the refactoring.
	 * @return The initial microstep.
	 * @throws ArgumentNullException Thrown if microstep is null.
	 */
	public GraphEdgeInitiates initiates(GraphNodeMicrostep microstep)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				microstep,
				(source, destination) -> new GraphEdgeInitiates(source, destination),
				GraphEdgeInitiates.class);
	}
}
