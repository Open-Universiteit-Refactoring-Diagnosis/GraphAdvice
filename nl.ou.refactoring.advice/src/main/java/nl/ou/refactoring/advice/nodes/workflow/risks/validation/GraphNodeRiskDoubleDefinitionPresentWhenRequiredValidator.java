package nl.ou.refactoring.advice.nodes.workflow.risks.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.GraphNodeSignature;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddClass;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;
import nl.ou.refactoring.advice.validation.GraphValidationResult;
import nl.ou.refactoring.advice.validation.GraphValidator;

/**
 * Validates whether Double Definition risk nodes are present where they are
 * expected.
 */
public final class GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidator implements GraphValidator {
	/**
	 * The singleton instance of
	 * {@link GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidator}.
	 */
	public static final GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidator INSTANCE =
			new GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidator();

	/**
	 * Initialises a new instance of
	 * {@link GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidator}.
	 */
	private GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidator() {
	}

	@Override
	public List<GraphValidationResult> validate(final Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");

		final var results = new ArrayList<GraphValidationResult>();

		validateClasses(graph, results);
		validateOperations(graph, results);
		validateAttributes(graph, results);

		return results;
	}

	private static void validateClasses(final Graph graph, final List<GraphValidationResult> results) {
		final var addClassNodes = graph.getNodes(GraphNodeMicrostepAddClass.class);
		for (final var addClassNode : addClassNodes) {
			final var classNodeAdded = addClassNode.getClassNode();
			final var packageNodeOptional = classNodeAdded.getPackageNode();
			if (packageNodeOptional.isEmpty()) {
				results.add(
						new GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidationResult(
								graph,
								new HashSet<GraphNodeCode>(),
								classNodeAdded,
								true // Depends on whether we allow classes without packages.
						));
			} else {
				final var packageNode = packageNodeOptional.get();
				final var classNodes =
						packageNode.getClassNodes()
								.stream()
								.filter(
										(node) -> node != classNodeAdded
												&& node.getClassName().equals(classNodeAdded.getClassName()))
								.map(GraphNodeCode.class::cast)
								.collect(Collectors.toUnmodifiableSet());
				if (classNodes.isEmpty()) {
					results.add(
							new GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidationResult(
									graph,
									classNodes,
									classNodeAdded,
									true));
				}

				results.add(
						new GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidationResult(
								graph,
								classNodes,
								classNodeAdded,
								// Double Definition detected, validation now only succeeds if the risk is
								// associated.
								classNodeAdded.getRisks()
										.stream()
										.anyMatch(GraphNodeRiskDoubleDefinition.class::isInstance)));
			}
		}
	}

	private static void validateOperations(final Graph graph, final ArrayList<GraphValidationResult> results) {
		final var addMethodNodes = graph.getNodes(GraphNodeMicrostepAddMethod.class);
		for (final var addMethodNode : addMethodNodes) {
			final var methodNodeAdded = addMethodNode.getOperationNode().get();
			final var classNodeOptional = methodNodeAdded.getClassNode();
			if (classNodeOptional.isEmpty()) {
				results.add(
						new GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidationResult(
								graph,
								new HashSet<GraphNodeCode>(),
								methodNodeAdded,
								false // We can't allow free floating operations;
										// this is a different type of
										// validation though.
						));
			} else {
				final var classNode = classNodeOptional.get();
				final var memberNodes =
						classNode.getEdges(GraphEdgeHas.class)
								.stream()
								.map((edge) -> edge.getDestinationNode())
								.filter(
										(node) -> node != methodNodeAdded && node instanceof GraphNodeCode
												&& GraphNodeSignature.equals((GraphNodeCode) node, methodNodeAdded))
								.map(GraphNodeCode.class::cast)
								.collect(Collectors.toUnmodifiableSet());
				results.add(
						new GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidationResult(
								graph,
								memberNodes,
								methodNodeAdded,
								// No members with identical identifier, so no Double Definition detected.
								memberNodes.isEmpty() ||
								// Double Definition detected, validation now only succeeds if the risk is
								// associated.
										methodNodeAdded.getRisks()
												.stream()
												.anyMatch(GraphNodeRiskDoubleDefinition.class::isInstance)));
			}
		}
	}

	private static void validateAttributes(final Graph graph, final ArrayList<GraphValidationResult> results) {
		final var addFieldNodes = graph.getNodes(GraphNodeMicrostepAddField.class);
		for (final var addFieldNode : addFieldNodes) {
			final var attributeNodeAdded = addFieldNode.getAttributeNode().get();
			final var classNodeOptional = attributeNodeAdded.getClassNode();
			if (classNodeOptional.isEmpty()) {
				results.add(
						new GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidationResult(
								graph,
								new HashSet<GraphNodeCode>(),
								attributeNodeAdded,
								false // We can't allow free floating
										// attributes; this is a different type
										// of validation, though.
						));
			} else {
				final var classNode = classNodeOptional.get();
				final var memberNodes =
						classNode.getEdges(GraphEdgeHas.class)
								.stream()
								.map((edge) -> (GraphNodeCode) edge.getDestinationNode())
								.filter(
										(node) -> node != attributeNodeAdded
												&& GraphNodeSignature.equals(node, attributeNodeAdded))
								.map(GraphNodeCode.class::cast)
								.collect(Collectors.toUnmodifiableSet());
				results.add(
						new GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidationResult(
								graph,
								memberNodes,
								attributeNodeAdded,
								// No members with identical identifier, so no Double Definition detected.
								memberNodes.isEmpty() ||
								// Double Definition detected, validation now only succeeds if the risk is
								// associated.
										attributeNodeAdded.getRisks()
												.stream()
												.anyMatch(GraphNodeRiskDoubleDefinition.class::isInstance)));
			}
		}
	}
}
