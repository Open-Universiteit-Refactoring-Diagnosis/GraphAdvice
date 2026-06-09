package nl.ou.refactoring.advice.nodes.code;

import java.util.Objects;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;

/**
 * Represents a data type in a Refactoring Advice Graph.
 */
public final class GraphNodeType extends GraphNodeCode {
	private final String typeName;
	
	/**
	 * Initialises a new instance of {@link GraphNodeType}.
	 * @param graph The graph that contains the node.
	 * @param typeName The name of the data type of code affected by a refactoring.
	 * @throws ArgumentNullException Thrown if graph or typeName is null.
	 * @throws ArgumentEmptyException Thrown if typeName is empty or contains only white spaces.
	 */
	public GraphNodeType(Graph graph, String typeName)
			throws ArgumentNullException, ArgumentEmptyException {
		super(graph);
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(typeName, "typeName");
		this.typeName = typeName;
	}
	
	/**
	 * Computes the type node, creating and returning a new type node if it is not yet present in the Refactoring Advice Graph (RAG), otherwise, the existing type node.
	 * @param graph The Refactoring Advice Graph (RAG).
	 * @param typeName The name of the type.
	 * @return The existing type node if already present in the graph, otherwise a newly created type node.
	 * @throws ArgumentNullException Thrown if graph or typeName is null.
	 * @throws ArgumentEmptyException Thrown if typeName is empty or contains only white spaces.
	 */
	public static GraphNodeType computeType(Graph graph, String typeName)
			throws ArgumentNullException, ArgumentEmptyException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(typeName, "typeName");
		final var existingType =
			graph
				.getNodes(GraphNodeType.class)
				.stream()
				.filter((typeNode) -> typeNode.getTypeName().equals(typeName))
				.findAny();
		return existingType.orElseGet(() -> new GraphNodeType(graph, typeName));
	}
	
	/**
	 * Gets the name of the data type of code affected by a refactoring.
	 * @return The name of the data type of code affected by a refactoring.
	 */
	public String getTypeName() {
		return this.typeName;
	}
	
	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeType(graph, this.typeName);
	}
	
	@Override
	public boolean equals(GraphNode other) {
		if (other == null || !(other instanceof GraphNodeType)) {
			return false;
		}
		final var typeNodeOther = (GraphNodeType)other;
		final var typeNameThis = this.getTypeName();
		final var typeNameOther = typeNodeOther.getTypeName();
		return
			((typeNameThis == null && typeNameOther == null) ||
			typeNameThis != null && typeNameThis.equals(typeNameOther));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.typeName);
	}

	@Override
	public String getLabel() {
		return "Type";
	}

	@Override
	public String getCaption() {
		return this.typeName;
	}
}