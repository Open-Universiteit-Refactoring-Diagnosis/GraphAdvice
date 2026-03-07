package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeRemoves;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;

/**
 * Represents a Microstep in a Refactoring Advice Graph that removes a Class.
 */
public final class GraphNodeMicrostepRemoveClass extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepRemoveClass}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepRemoveClass(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Remove Class" microstep removes the class represented by classNode.
	 * @param classNode The class that is removed by this microstep.
	 * @return The edge that indicates that the "Remove Class" microstep removes the operation represented by classNode.
	 * @throws ArgumentNullException Thrown if classNode is null.
	 */
	public GraphEdgeRemoves removes(GraphNodeClass classNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(classNode, "classNode");
		return
			this.graph.computeEdge(
				this,
				classNode,
				(source, destination) -> new GraphEdgeRemoves(source, destination),
				GraphEdgeRemoves.class
			);
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeMicrostepRemoveClass(graph);
	}
}