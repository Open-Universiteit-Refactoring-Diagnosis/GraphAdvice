package nl.ou.refactoring.advice.nodes.workflow;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Represents an intermediate refactoring that is required to perform an encompassing refactoring.
 */
public final class GraphNodeIntermediateRefactoring extends GraphNodeWorkflowAction {
	private final Graph intermediateRefactoringGraph;
	
	/**
	 * Initialises a new instance of {@link GraphNodeIntermediateRefactoring}.
	 * @param encompassingGraph The encompassing graph that contains this node.
	 * @param intermediateRefactoringGraph The intermediate refactoring graph.
	 * @throws ArgumentNullException Thrown if encompassingGraph or intermediateRefactoringGraph is null.
	 */
	public GraphNodeIntermediateRefactoring(Graph encompassingGraph, Graph intermediateRefactoringGraph)
			throws ArgumentNullException {
		super(encompassingGraph);
		ArgumentGuard.requireNotNull(intermediateRefactoringGraph, "intermediateRefactoringGraph");
		this.intermediateRefactoringGraph = intermediateRefactoringGraph;
	}

	/**
	 * Gets the graph of an intermediate refactoring inside an encompassing refactoring.
	 * @return The graph of an intermediate refactoring inside an encompassing refactoring.
	 */
	public Graph getIntermediateRefactoringGraph() {
		return this.intermediateRefactoringGraph;
	}
}
