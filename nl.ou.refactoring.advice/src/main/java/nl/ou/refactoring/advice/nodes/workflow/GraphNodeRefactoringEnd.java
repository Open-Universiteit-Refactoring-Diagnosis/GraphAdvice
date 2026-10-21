package nl.ou.refactoring.advice.nodes.workflow;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.resources.ResourceProvider;
import nl.ou.refactoring.advice.validation.workflow.RefactoringMayContainOnlyOneEndNodeException;

/**
 * The end node of a refactoring workflow.
 */
public final class GraphNodeRefactoringEnd extends GraphNodeWorkflow {
	/**
	 * Initialises a new instance of {@link GraphNodeRefactoringEnd}.
	 * 
	 * @param graph The Refactoring Advice Graph (RAG).
	 * @throws ArgumentNullException                        Thrown if {@code graph}
	 *                                                      is null.
	 * @throws RefactoringMayContainOnlyOneEndNodeException Thrown if the graph
	 *                                                      already contains an end
	 *                                                      node.
	 */
	public GraphNodeRefactoringEnd(Graph graph)
			throws ArgumentNullException, RefactoringMayContainOnlyOneEndNodeException {
		super(graph);
		if (graph.getNodes(GraphNodeRefactoringEnd.class).size() > 1) {
			throw new RefactoringMayContainOnlyOneEndNodeException();
		}
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeRefactoringEnd(graph);
	}

	@Override
	public boolean equals(GraphNode other) {
		return other instanceof GraphNodeRefactoringEnd;
	}
	
	@Override
	public String getCaption() {
		return this.getGraph().getRefactoringName();
	}

	@Override
	public String getLabel() {
		return ResourceProvider.GraphNodeLabels.getLabel(GraphNodeRefactoringEnd.class);
	}
}