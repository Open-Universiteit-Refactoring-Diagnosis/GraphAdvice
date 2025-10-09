package nl.ou.refactoring.advice.nodes.workflow;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeInitiates;

/**
 * Represents the start node of a refactoring in a Refactoring Advice Graph.
 */
public final class GraphNodeRefactoringStart extends GraphNodeWorkflow {
	private final String refactoringName;
	
	/**
	 * Initialises a new instance of {@link GraphNodeRefactoringStart}.
	 * @param graph The graph that contains the node.
	 * @param refactoringName The name of the refactoring that is started.
	 * @throws ArgumentNullException Thrown if graph or refactoringName is null.
	 * @throws RefactoringMayContainOnlyOneStartNodeException Thrown if the graph already contains a start node.
	 * @throws ArgumentEmptyException Thrown if refactoringName is empty or contains only white spaces.
	 */
	public GraphNodeRefactoringStart(Graph graph, String refactoringName)
			throws ArgumentNullException, RefactoringMayContainOnlyOneStartNodeException {
		super(graph);
		if (graph.getNodes(GraphNodeRefactoringStart.class).size() > 1) {
			throw new RefactoringMayContainOnlyOneStartNodeException();
		}
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(refactoringName, "refactoringName");
		this.refactoringName = refactoringName;
	}
	
	/**
	 * Initiates a refactoring with a particular type of workflow step.
	 * If the relationship already exists, no edges will be added to the graph and the existing edge will be returned.
	 * @param workflowStep The workflow step to initiate from the refactoring.
	 * @return The initial microstep.
	 * @throws ArgumentNullException Thrown if microstep is null.
	 */
	public GraphEdgeInitiates initiates(GraphNodeWorkflow workflowStep)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				workflowStep,
				(source, destination) -> new GraphEdgeInitiates(source, destination),
				GraphEdgeInitiates.class);
	}
	
	/**
	 * Gets the name of the refactoring that is started.
	 * @return The name of the refactoring that is started.
	 */
	public String getRefactoringName() {
		return this.refactoringName;
	}

	@Override
	public String getLabel() {
		return "Refactoring";
	}

	@Override
	public String getCaption() {
		return this.refactoringName;
	}
}
