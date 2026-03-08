package nl.ou.refactoring.advice.nlp.providers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.NLPException;
import nl.ou.refactoring.advice.nlp.NLPProvider;
import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart;
import nl.ou.refactoring.advice.nodes.workflow.GraphWorkflowExplorer;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMustContainStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddClass;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskForcedOverride;

/**
 * A Natural Language Processing provider that concatenates pieces of texts
 * based on nodes visited in a Refactoring Advice Graph.
 */
public final class NLPConcatenationProvider extends NLPProvider {
	/**
	 * Initialises a new instance of {@link NLPConcatenationProvider}.
	 */
	public NLPConcatenationProvider() { }

	@Override
	public NLPResult process(Graph graph) throws ArgumentNullException, NLPException {
		ArgumentGuard.requireNotNull(graph, "graph");
		
		final var references = new HashMap<String, GraphNode>();
		
		final var startNodeOptional = graph.getStart();
		if (startNodeOptional.isEmpty()) {
			throw new RefactoringMustContainStartNodeException();
		}
		final var startNode = startNodeOptional.get();
		
		final var dangerNodes = GraphWorkflowExplorer.getDangers(graph);
		for (final var dangerNode : dangerNodes) {
			final var dangerPaths = startNode.findPaths(dangerNode, 100);
			if (dangerPaths.size() == 0) {
				continue;
			}
			
			final var dangerPath = dangerPaths.get(0);
			for (final var dangerPathSegment : dangerPath.getSegments()) {
				switch (dangerPathSegment.getNode()) {
					case GraphNodeRefactoringStart start -> this.appendNodeRefactoringStart(start, references);
					case GraphNodeMicrostepAddClass addClass -> this.appendNodeMicrostepAddClass(addClass, references);
					case GraphNodeMicrostepAddField addField -> this.appendNodeMicrostepAddField(addField, references);
					case GraphNodeMicrostepAddMethod addMethod -> this.appendNodeMicrostepAddMethod(addMethod, references);
					case GraphNodeRiskDoubleDefinition doubleDefinition -> this.appendNodeRiskDoubleDefinition(doubleDefinition, references);
					case GraphNodeRiskForcedOverride forcedOverride -> this.appendNodeRiskForcedOverride(forcedOverride, references);
					case GraphNodeRisk risk -> this.appendNodeRisk(risk, references);
					default -> { }
				}
			}
		}
		
		return new NLPResult(this.stringBuilder.toString(), references);
	}
	
	private void append(String text) {
		this.stringBuilder.append(text);
	}
	
	private void appendFormat(String format, Object... args) {
		this.stringBuilder.append(String.format(format, args));
	}
	
	private void appendLine(String text) {
		this.append(text);
		this.stringBuilder.append(System.lineSeparator());
	}
	
	private void appendNodeRefactoringStart(GraphNodeRefactoringStart startNode, Map<String, GraphNode> references) {
		this.appendLine(startNode.getRefactoringName());
	}
	
	private void appendNodeMicrostepAddClass(GraphNodeMicrostepAddClass addClassNode, Map<String, GraphNode> references) {
		final var classNode = addClassNode.getClassNode();
		if (classNode == null) {
			this.append(", adding class");
		} else {
			final var classNodeId = getId(classNode);
			this.appendFormat(", adding class '%s'", classNodeId);
			references.computeIfAbsent(classNodeId, (_) -> classNode);
			final var packageNode = classNode.getPackageNode();
			if (packageNode.isPresent()) {
				final var packageNodeId = getId(packageNode.get());
				this.appendFormat(" to package '%s'", packageNodeId);
				references.computeIfAbsent(packageNodeId, (_) -> packageNode.get());
			}
		}
	}
	
	private void appendNodeMicrostepAddField(GraphNodeMicrostepAddField addFieldNode, Map<String, GraphNode> references) {
		final var attributeNodeOptional = addFieldNode.getAttributeNode();
		if (attributeNodeOptional.isEmpty()) {
			this.append(", adding field");
		} else {
			final var attributeNode = attributeNodeOptional.get();
			final var attributeNodeId = getId(attributeNode);
			this.appendFormat(", adding field '%s'", attributeNodeId);
			references.computeIfAbsent(attributeNodeId, (_) -> attributeNode);
			final var classNode = attributeNode.getClassNode();
			if (classNode.isPresent()) {
				final var classNodeId = getId(classNode.get());
				this.appendFormat(" to class '%s'", classNodeId);
				references.computeIfAbsent(classNodeId, (_) -> classNode.get());
			}
		}
	}
	
	private void appendNodeMicrostepAddMethod(GraphNodeMicrostepAddMethod addMethodNode, Map<String, GraphNode> references) {
		final var operationNode = addMethodNode.getOperationNode();
		if (operationNode.isEmpty()) {
			this.append(", adding method");
		} else {
			final var operationNodeId = getId(operationNode.get());
			this.appendFormat(", adding method '%s'", operationNodeId);
			references.computeIfAbsent(operationNodeId, (_) -> operationNode.get());
			final var classNode = operationNode.get().getClassNode();
			if (classNode.isPresent()) {
				final var classNodeId = getId(classNode.get());
				this.appendFormat(" to class '%s'", classNodeId);
				references.computeIfAbsent(classNodeId, (_) -> classNode.get());
			}
		}
	}
	
	private void appendNodeRisk(GraphNodeRisk riskNode, Map<String, GraphNode> references) {
		this.appendFormat(" will cause a %s", riskNode.getCaption());
		final var affectedNodes = riskNode.getAffected();
		final var affectedNodesList =
			affectedNodes
				.stream()
				.collect(Collectors.toUnmodifiableList());
		this.append(" on " + getEnumeration(affectedNodesList, references));
	}
	
	private void appendNodeRiskDoubleDefinition(GraphNodeRiskDoubleDefinition doubleDefinition, Map<String, GraphNode> references) {
		this.append(" will introduce code symbols with identical signatures");
		final var affectedNodes = doubleDefinition.getAffected();
		final var affectedNodesList =
			affectedNodes
				.stream()
				.collect(Collectors.toUnmodifiableList());
		this.append(" on " + getEnumeration(affectedNodesList, references));
	}
	
	private void appendNodeRiskForcedOverride(GraphNodeRiskForcedOverride forcedOverride, Map<String, GraphNode> references) {
		this.append(" will forcefully cause an override of ");
		final var affectedNodes = forcedOverride.getAffected();
		final var affectedNames =
			affectedNodes
				.stream()
				.collect(Collectors.toUnmodifiableList());
		this.append(getEnumeration(affectedNames, references));
	}
	
	private static String getEnumeration(List<GraphNode> items, Map<String, GraphNode> references) {
		if (items.size() == 0) {
			return "";
		}
		
		if (items.size() == 1) {
			final var itemSingle = items.get(0);
			final var itemSingleId = getId(itemSingle);
			references.computeIfAbsent(itemSingleId, (_) -> itemSingle);
			return itemSingleId;
		}

		final var stringBuilder = new StringBuilder();
		final var itemFirst = items.get(0);
		final var itemFirstId = getId(itemFirst);
		stringBuilder.append(String.format("'%s'", itemFirstId));
		references.computeIfAbsent(itemFirstId, (_) -> itemFirst);
		
		for (var i = 1; i < items.size() - 1; i++) {
			final var itemNext = items.get(i);
			final var itemNextId = getId(itemNext);
			stringBuilder.append(String.format(", '%s'", itemNextId));
			references.computeIfAbsent(itemNextId, (_) -> itemNext);
		}
		
		final var itemLast = items.get(items.size() - 1);
		final var itemLastId = getId(itemLast);
		stringBuilder.append(String.format(" and '%s'", itemLastId));
		references.computeIfAbsent(itemLastId, (_) -> itemLast);
		
		return stringBuilder.toString();
	}
	
	private static String getId(GraphNode node) {
		return String.format("{%s}", node.getId().toString());
	}
}