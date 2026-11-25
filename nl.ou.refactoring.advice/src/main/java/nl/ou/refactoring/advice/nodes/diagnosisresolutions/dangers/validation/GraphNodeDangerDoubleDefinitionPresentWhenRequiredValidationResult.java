package nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.validation;

import java.util.Collections;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.mechanics.GraphEdgeAdds;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDangerDoubleDefinition;
import nl.ou.refactoring.advice.nodes.mechanics.microsteps.GraphNodeMicrostep;
import nl.ou.refactoring.advice.nodes.mechanics.microsteps.GraphNodeMicrostepAddClass;
import nl.ou.refactoring.advice.nodes.mechanics.microsteps.GraphNodeMicrostepAddField;
import nl.ou.refactoring.advice.nodes.mechanics.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.validation.GraphValidationFixableResult;
import nl.ou.refactoring.advice.validation.GraphValidationResultFixFailedException;

/**
 * A validation result that indicates whether a Double Definition risk node is
 * missing when it is expected.
 */
public final class GraphNodeDangerDoubleDefinitionPresentWhenRequiredValidationResult
		extends GraphValidationFixableResult {
	private static final Logger LOGGER =
			LogManager.getLogger(GraphNodeDangerDoubleDefinitionPresentWhenRequiredValidationResult.class);
	private final Set<GraphNodeCode> nodesExisting;
	private final GraphNodeCode nodeAdded;

	/**
	 * Initialises a new instance of
	 * {@link GraphNodeDangerDoubleDefinitionPresentWhenRequiredValidationResult}.
	 * 
	 * @param graph         The Refactoring Advice Graph (RAG) {@link Graph} that is
	 *                      being validated.
	 * @param nodesExisting The existing nodes that have an identical signature.
	 * @param nodeAdded     The code node that is added and causes a double
	 *                      definition.
	 * @param isValid       A value that indicates whether the {@link Graph} is
	 *                      valid.
	 * @throws ArgumentNullException Thrown if graph, nodesExisting or nodeAdded is
	 *                               null.
	 */
	public GraphNodeDangerDoubleDefinitionPresentWhenRequiredValidationResult(
			Graph graph,
			Set<GraphNodeCode> nodesExisting,
			GraphNodeCode nodeAdded,
			boolean isValid) throws ArgumentNullException {
		super(graph, isValid);
		ArgumentGuard.requireNotNull(nodesExisting, "nodesExisting");
		ArgumentGuard.requireNotNull(nodeAdded, "nodeAdded");
		this.nodesExisting = nodesExisting;
		this.nodeAdded = nodeAdded;
	}

	/**
	 * Gets the existing nodes that have an identical signature.
	 * 
	 * @return An unmodifiable set of existing nodes that have an identical
	 *         signature.
	 */
	public Set<GraphNodeCode> getNodesConflicting() {
		return Collections.unmodifiableSet(this.nodesExisting);
	}

	/**
	 * Gets the code node that is added and causes a double definition.
	 * 
	 * @return The code node that is added and causes a double definition.
	 */
	public GraphNodeCode getNodeAdded() {
		return this.nodeAdded;
	}

	@Override
	public Graph fix(boolean cloneGraph) throws GraphValidationResultFixFailedException {
		var graph = this.getGraph();
		if (cloneGraph) {
			LOGGER.info("Cloning Graph before provisioning the missing Double Definition node");
			graph = graph.clone(graph.getRefactoringName());
		}

		final var dangerNode = new GraphNodeDangerDoubleDefinition(graph);

		switch (this.nodeAdded) {
			case GraphNodeAttribute attributeNode: {
				LOGGER.debug("The double node is an attribute '{}'", attributeNode.getAttributeName());
				final var microstep = getMicrostep(attributeNode, GraphNodeMicrostepAddField.class);
				microstep.causes(dangerNode);
				break;
			}
			case GraphNodeOperation operationNode: {
				LOGGER.debug("The double node is an operation '{}'", operationNode.getOperationName());
				final var microstep = getMicrostep(operationNode, GraphNodeMicrostepAddMethod.class);
				microstep.causes(dangerNode);
				break;
			}
			case GraphNodeClass classNode: {
				LOGGER.debug("The double node is a class '{}'", classNode.getClassName());
				final var microstep = getMicrostep(classNode, GraphNodeMicrostepAddClass.class);
				microstep.causes(dangerNode);
				break;
			}
			default: {
				throw new GraphNodeCodeDangerFixNotSupportedException(this.nodeAdded);
			}
		}
		
		final var nodesConflicting = this.getNodesConflicting();
		for (final var nodeConflicting : nodesConflicting) {
			switch (nodeConflicting) {
				case GraphNodeAttribute attributeNode: {
					dangerNode.affects(attributeNode);
					break;
				}
				case GraphNodeOperation operationNode: {
					dangerNode.affects(operationNode);
					break;
				}
				case GraphNodeClass classNode: {
					dangerNode.affects(classNode);
					break;
				}
				default: {
					break;
				}
			}
		}

		return graph;
	}

	private static <TMicrostep extends GraphNodeMicrostep> TMicrostep getMicrostep(
			GraphNodeCode codeNode,
			Class<TMicrostep> nodeClassType) throws GraphNodeCodeMicrostepNotFoundException {
		return codeNode.getEdgesIncoming(GraphEdgeAdds.class)
				.stream()
				.map((edge) -> edge.getSourceNode())
				.filter(nodeClassType::isInstance)
				.map(nodeClassType::cast)
				.findAny()
				.orElseThrow(() -> new GraphNodeCodeMicrostepNotFoundException(codeNode));
	}
}