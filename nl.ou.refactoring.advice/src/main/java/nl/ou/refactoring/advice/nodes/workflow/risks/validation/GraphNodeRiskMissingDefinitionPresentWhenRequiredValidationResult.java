package nl.ou.refactoring.advice.nodes.workflow.risks.validation;

import java.util.Collections;
import java.util.Set;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeRemoves;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveClass;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskMissingDefinition;
import nl.ou.refactoring.advice.validation.GraphValidationFixableResult;
import nl.ou.refactoring.advice.validation.GraphValidationResultFixFailedException;

/**
 * A validation result that indicates whether a Missing Definition risk node is missing when it is expected.
 */
public final class GraphNodeRiskMissingDefinitionPresentWhenRequiredValidationResult extends GraphValidationFixableResult {
	private final Set<GraphNodeCode> referringNodes;
	private final GraphNodeCode referredNode;
	
	/**
	 * Initialises a new instance of {@link GraphNodeRiskMissingDefinitionPresentWhenRequiredValidationResult}.
	 * @param graph The Refactoring Advice Graph (RAG) {@link Graph} that is being validated.
	 * @param referringNodes
	 * @param referredNode
	 * @param isValid
	 * @throws ArgumentNullException
	 */
	public GraphNodeRiskMissingDefinitionPresentWhenRequiredValidationResult(
		Graph graph,
		Set<GraphNodeCode> referringNodes,
		GraphNodeCode referredNode,
		boolean isValid
	) throws ArgumentNullException {
		super(graph, isValid);
		ArgumentGuard.requireNotNull(referringNodes, "referringNode");
		ArgumentGuard.requireNotNull(referredNode, "referredNode");
		this.referringNodes = Collections.unmodifiableSet(referringNodes);
		this.referredNode = referredNode;
	}
	
	/**
	 * Gets the referring nodes that are referring to a missing definition.
	 * @return The referring nodes that are referring to a missing a definition.
	 */
	public Set<GraphNodeCode> getReferringNodes() {
		return this.referringNodes;
	}
	
	/**
	 * Gets the referred node that is missing.
	 * @return The referred node that is missing.
	 */
	public GraphNodeCode getReferredNode() {
		return this.referredNode;
	}

	@Override
	public Graph fix(boolean cloneGraph) throws GraphValidationResultFixFailedException {
		var graph = this.getGraph();
		if (cloneGraph) {
			graph = graph.clone(graph.getRefactoringName());
		}
		
		final var riskNode = new GraphNodeRiskMissingDefinition(graph);
		
		switch (this.referredNode) {
			case GraphNodeAttribute attributeNode: {
				final var microstep = getMicrostep(attributeNode, GraphNodeMicrostepRemoveField.class);
				microstep.causes(riskNode);
				riskNode.affects(attributeNode);
				break;
			}
			case GraphNodeOperation operationNode: {
				final var microstep = getMicrostep(operationNode, GraphNodeMicrostepRemoveMethod.class);
				microstep.causes(riskNode);
				riskNode.affects(operationNode);
				break;
			}
			case GraphNodeClass classNode: {
				final var microstep = getMicrostep(classNode, GraphNodeMicrostepRemoveClass.class);
				microstep.causes(riskNode);
				riskNode.affects(classNode);
				break;
			}
			default: {
				throw new GraphNodeCodeRiskFixNotSupportedException(this.referredNode);
			}
		}
		
		return graph;
	}

	private static <TMicrostep extends GraphNodeMicrostep> TMicrostep getMicrostep(
		GraphNodeCode codeNode,
		Class<TMicrostep> nodeClassType
	) throws GraphNodeCodeMicrostepNotFoundException {
		return
			codeNode
				.getEdgesIncoming(GraphEdgeRemoves.class)
				.stream()
				.map((edge) -> edge.getSourceNode())
				.filter(nodeClassType::isInstance)
				.map(nodeClassType::cast)
				.findAny()
				.orElseThrow(() -> new GraphNodeCodeMicrostepNotFoundException(codeNode));
	}
}
