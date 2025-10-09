package nl.ou.refactoring.advice.nodes.workflow;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Represents an intermediate refactoring that is required to perform an encompassing refactoring.
 */
public final class GraphNodeIntermediateRefactoring extends GraphNodeWorkflowAction {
	private final String refactoringName;
	private final Graph intermediateRefactoringGraph;
	
	/**
	 * Initialises a new instance of {@link GraphNodeIntermediateRefactoring}.
	 * @param encompassingGraph The encompassing graph that contains this node.
	 * @param intermediateRefactoringGraph The intermediate refactoring graph.
	 * @throws ArgumentNullException Thrown if encompassingGraph, refactoringName or intermediateRefactoringGraph is null.
	 * @throws ArgumentEmptyException Thrown if refactoringName is empty or contains only white spaces.
	 */
	public GraphNodeIntermediateRefactoring(
			Graph encompassingGraph,
			String refactoringName,
			Graph intermediateRefactoringGraph)
					throws ArgumentNullException, ArgumentEmptyException {
		super(encompassingGraph);
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(refactoringName, "refactoringName");
		ArgumentGuard.requireNotNull(intermediateRefactoringGraph, "intermediateRefactoringGraph");
		this.refactoringName = refactoringName;
		this.intermediateRefactoringGraph = intermediateRefactoringGraph;
	}
	
	/**
	 * Gets the name of the intermediate refactoring.
	 * @return The name of the intermediate refactoring.
	 */
	public String getRefactoringName() {
		return this.refactoringName;
	}

	/**
	 * Gets the graph of an intermediate refactoring inside an encompassing refactoring.
	 * @return The graph of an intermediate refactoring inside an encompassing refactoring.
	 */
	public Graph getIntermediateRefactoringGraph() {
		return this.intermediateRefactoringGraph;
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
