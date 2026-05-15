package nl.ou.refactoring.advice.nodes.code;

import java.util.Optional;

import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;

/**
 * Handles signatures of code nodes in a Refactoring Advice Graph (RAG).
 */
public final class GraphNodeSignature {
	/**
	 * Initialises a new instance of {@link GraphNodeSignature}.
	 */
	private GraphNodeSignature() { }
	
	/**
	 * Determines whether the signatures of the specified code nodes are equal.
	 * @param codeNodeLeft The left-hand side code node of which to compare its signature.
	 * @param codeNodeRight The right-hand side code node of which to compare its signature.
	 * @return A value that indicates whether the signatures of the specified code nodes are equal.
	 */
	public static boolean equals(GraphNodeCode codeNodeLeft, GraphNodeCode codeNodeRight) {
		if (codeNodeLeft == null || codeNodeRight == null) {
			return false;
		}
		
		if (codeNodeLeft instanceof GraphNodeOperation && codeNodeRight instanceof GraphNodeOperation) {
			return equals((GraphNodeOperation)codeNodeLeft, (GraphNodeOperation)codeNodeRight);
		}
		
		final var identifierNodeLeftOptional = getIdentifierNode(codeNodeLeft);
		final var identifierNodeRightOptional = getIdentifierNode(codeNodeRight);
		if (identifierNodeLeftOptional.isEmpty() || identifierNodeRightOptional.isEmpty()) {
			return false;
		}
		
		return
			identifierNodeLeftOptional
				.get()
				.getIdentifier()
				.equals(
					identifierNodeRightOptional
						.get()
						.getIdentifier()
				);
	}
	
	private static Optional<GraphNodeIdentifier> getIdentifierNode(GraphNodeCode codeNode) {
		return
			codeNode
				.getEdges(GraphEdgeHas.class)
				.stream()
				.map((edge) -> edge.getDestinationNode())
				.filter(GraphNodeIdentifier.class::isInstance)
				.map(GraphNodeIdentifier.class::cast)
				.findAny();
	}
	
	/**
	 * Determines whether the signatures of the two specified operations are equal.
	 * @param operationNodeLeft The left-hand side operation of which to compare its signature.
	 * @param operationNodeRight The right-hand side operation of which to compare its signature.
	 * @return A value that indicates whether the signatures of the two operations are equal.
	 */
	public static boolean equals(GraphNodeOperation operationNodeLeft, GraphNodeOperation operationNodeRight) {
		if (operationNodeLeft == null || operationNodeRight == null) {
			return false;
		}
		
		if (!operationNodeLeft.getOperationName().equals(operationNodeRight.getOperationName())) {
			return false;
		}
		
		final var operationNodeLeftParameters = operationNodeLeft.getOperationParameters();
		final var operationNodeRightParameters = operationNodeRight.getOperationParameters();
		return operationNodeLeftParameters.equals(operationNodeRightParameters);
	}
}