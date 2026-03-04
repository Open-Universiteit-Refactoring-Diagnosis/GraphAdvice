package nl.ou.refactoring.advice.io.text.concatenation;

import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphWriterException;
import nl.ou.refactoring.advice.io.text.GraphTextWriter;
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
 * Generates refactoring advice text from a Refactoring Advice Graph by concatenating the text.
 */
public final class GraphTextConcatenationWriter extends GraphTextWriter {
	/**
	 * Initialises a new instance of {@link GraphTextConcatenationWriter}.
	 * @param stringWriter The {@link StringWriter} that is responsible for text output.
	 */
	public GraphTextConcatenationWriter(StringWriter stringWriter) {
		super(stringWriter);
	}

	@Override
	public void write(Graph graph)
			throws
				ArgumentNullException,
				GraphValidationException,
				GraphWriterException {
		final var startNode = graph.getStart();
		if (startNode == null) {
			throw new RefactoringMustContainStartNodeException();
		}
		
		final var dangerNodes = GraphWorkflowExplorer.getDangers(graph);
		for (final var dangerNode : dangerNodes) {
			final var dangerPaths = startNode.findPaths(dangerNode, 100);
			if (dangerPaths.size() == 0) {
				continue;
			}
			
			final var dangerPath = dangerPaths.get(0);
			for (final var dangerPathSegment : dangerPath.getSegments()) {
				switch (dangerPathSegment.getNode()) {
					case GraphNodeRefactoringStart start -> this.writeNodeRefactoringStart(start);
					case GraphNodeMicrostepAddClass addClass -> this.writeNodeMicrostepAddClass(addClass);
					case GraphNodeMicrostepAddField addField -> this.writeNodeMicrostepAddField(addField);
					case GraphNodeMicrostepAddMethod addMethod -> this.writeNodeMicrostepAddMethod(addMethod);
					case GraphNodeRiskDoubleDefinition doubleDefinition -> this.writeNodeRiskDoubleDefinition(doubleDefinition);
					case GraphNodeRiskForcedOverride forcedOverride -> this.writeNodeRiskForcedOverride(forcedOverride);
					case GraphNodeRisk risk -> this.writeNodeRisk(risk);
					default -> { }
				}
			}
		}
	}
	
	private void writeNodeRefactoringStart(GraphNodeRefactoringStart startNode) {
		this.printLine(startNode.getRefactoringName());
	}
	
	private void writeNodeMicrostepAddClass(GraphNodeMicrostepAddClass addClass) {
		final var classNode = addClass.getClassNode();
		if (classNode == null) {
			this.print("Adding class");
		} else {
			this.print(String.format("Adding class '%s'", classNode.getCaption()));
			final var packageNode = classNode.getPackageNode();
			if (packageNode != null) {
				this.print(String.format(" to package '%s'", packageNode.getPackageName()));
			}
		}
	}
	
	private void writeNodeMicrostepAddField(GraphNodeMicrostepAddField addField) {
		final var attributeNode = addField.getAttributeNode();
		if (attributeNode == null) {
			this.print("Adding field");
		} else {
			this.print(String.format("Adding field '%s'", attributeNode.getCaption()));
			final var classNode = attributeNode.getClassNode();
			if (classNode != null) {
				this.print(String.format(" to class '%s'", classNode.getCaption()));
			}
		}
	}
	
	private void writeNodeMicrostepAddMethod(GraphNodeMicrostepAddMethod addMethod) {
		final var operationNode = addMethod.getOperationNode();
		if (operationNode == null) {
			this.print("Adding method");
		} else {
			this.print(String.format("Adding method '%s'", operationNode.getCaption()));
			final var classNode = operationNode.getClassNode();
			if (classNode != null) {
				this.print(String.format(" to class '%s'", classNode.getCaption()));
			}
		}
	}
	
	private void writeNodeRisk(GraphNodeRisk risk) {
		this.print(String.format(" will cause a %s", risk.getCaption()));
		final var affectedNodes = risk.getAffected();
		final var affectedNames =
			affectedNodes
				.stream()
				.map(node -> node.getCaption())
				.collect(Collectors.toUnmodifiableList());
		this.print(" on " + getEnumeration(affectedNames));
	}
	
	private void writeNodeRiskDoubleDefinition(GraphNodeRiskDoubleDefinition doubleDefinition) {
		this.print(" will introduce code symbols with identical signatures");
		final var affectedNodes = doubleDefinition.getAffected();
		final var affectedNames =
			affectedNodes
				.stream()
				.map(node -> node.getCaption())
				.collect(Collectors.toUnmodifiableList());
		this.print(" on " + getEnumeration(affectedNames));
	}
	
	private void writeNodeRiskForcedOverride(GraphNodeRiskForcedOverride forcedOverride) {
		this.print(" will forcefully cause an override of ");
		final var affectedNodes = forcedOverride.getAffected();
		final var affectedNames =
			affectedNodes
				.stream()
				.map(node -> node.getCaption())
				.collect(Collectors.toUnmodifiableList());
		this.print(getEnumeration(affectedNames));
	}
	
	private static String getEnumeration(List<String> items) {
		if (items.size() == 0) {
			return "";
		}
		if (items.size() == 1) {
			return items.get(0);
		}
		if (items.size() == 2) {
			return String.format("'%s' and '%s'", items.get(0), items.get(1));
		}
		final var stringBuilder = new StringBuilder();
		stringBuilder.append(String.format("'%s'", items.get(0)));
		for (var i = 1; i < items.size() - 1; i++) {
			stringBuilder.append(String.format(", '%s'", items.get(i)));
		}
		stringBuilder.append(String.format(" and '%s'", items.get(items.size() - 1)));
		return stringBuilder.toString();
	}
}
