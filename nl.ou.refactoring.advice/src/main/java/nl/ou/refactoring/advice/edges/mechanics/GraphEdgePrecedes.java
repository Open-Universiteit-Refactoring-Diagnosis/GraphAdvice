package nl.ou.refactoring.advice.edges.mechanics;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.mechanics.GraphNodeMechanic;

/**
 * An Edge that indicates that a Workflow Step precedes another Workflow Step in a refactoring.
 */
public final class GraphEdgePrecedes extends GraphEdgeMechanic {
	/**
	 * Initialises a new instance of {@link GraphEdgePrecedes}.
	 * @param currentWorkflowAction The current workflow action.
	 * @param nextWorkflowAction The next current workflow action.
	 * @throws ArgumentNullException Thrown if currentWorkflowStep or nextWorkflowStep is null.
	 */
	public GraphEdgePrecedes(GraphNodeMechanic currentWorkflowAction, GraphNodeMechanic nextWorkflowAction)
			throws ArgumentNullException {
		super(currentWorkflowAction, nextWorkflowAction);
	}
	
	@Override
	public GraphNodeMechanic getSourceNode() {
		return (GraphNodeMechanic)super.getSourceNode();
	}
	
	@Override
	public GraphNodeMechanic getDestinationNode() {
		return (GraphNodeMechanic)super.getDestinationNode();
	}
}
