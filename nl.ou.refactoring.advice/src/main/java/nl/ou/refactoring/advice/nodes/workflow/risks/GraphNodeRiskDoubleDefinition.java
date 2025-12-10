package nl.ou.refactoring.advice.nodes.workflow.risks;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;

/**
 * Represents a "Double Definition" risk in a Refactoring Advice Graph.
 * This risk may arise if a Class, Method or Field with the same definition is already defined in the same context.
 */
public final class GraphNodeRiskDoubleDefinition extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskDoubleDefinition}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskDoubleDefinition(Graph graph) throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Double Definition" risk affects an Attribute.
	 * @param attributeNode The affected Attribute.
	 * @return The edge that connects the "Double Definition" risk and the affected Attribute.
	 * @throws ArgumentNullException Thrown if attributeNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeAttribute attributeNode)
			throws ArgumentNullException {
		// TODO assert that this node is only connected to Attributes with Affects.
		return this.graph.getOrAddEdge(
				this,
				attributeNode,
				(source, destination) -> new GraphEdgeAffects(source, destination),
				GraphEdgeAffects.class);
	}
	
	/**
	 * Indicates that the "Double Definition" risk affects a Class.
	 * @param classNode The affected Class.
	 * @return The edge that connects the "Double Definition" risk and the affected Class.
	 * @throws ArgumentNullException Thrown if attributeNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeClass classNode)
			throws ArgumentNullException {
		// TODO assert that this node is only connected to Classes with Affects.
		return this.graph.getOrAddEdge(
				this,
				classNode,
				(source, destination) -> new GraphEdgeAffects(source, destination),
				GraphEdgeAffects.class);
	}
	
	/**
	 * Indicates that the "Double Definition" risk affects an Operation.
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Double Definition" risk and the affected Operation.
	 * @throws ArgumentNullException Thrown if attributeNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeOperation operationNode)
			throws ArgumentNullException {
		// TODO assert that this node is only connected to Operations with Affects.
		return this.graph.getOrAddEdge(
				this,
				operationNode,
				(source, destination) -> new GraphEdgeAffects(source, destination),
				GraphEdgeAffects.class);
	}
}
