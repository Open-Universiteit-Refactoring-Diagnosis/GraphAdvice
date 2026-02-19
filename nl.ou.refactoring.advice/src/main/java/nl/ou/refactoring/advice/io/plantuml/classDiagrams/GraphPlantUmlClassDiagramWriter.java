package nl.ou.refactoring.advice.io.plantuml.classDiagrams;

import java.awt.Color;
import java.io.StringWriter;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.SortOrder;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.io.plantuml.GraphPlantUmlWriter;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClassHasMultipleGeneralisationsException;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClassMember;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClassStereotype;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeMethodInvocationExpression;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;

import static nl.ou.refactoring.advice.io.ColorExtensions.toHexadecimal;

/**
 * Writes PlantUML Class Diagrams from Refactoring Advice Graphs.
 */
public final class GraphPlantUmlClassDiagramWriter extends GraphPlantUmlWriter {
	private static final String SPRITE_NAME_DANGER = "$danger";
	private static final String SPRITE_NAME_NEW = "$new";
	
	/**
	 * Initialises a new instance of {@link GraphPlantUmlClassDiagramWriter}.
	 * @param stringWriter A {@link StringWriter} that is responsible for text output.
	 */
	public GraphPlantUmlClassDiagramWriter(StringWriter stringWriter) {
		super(stringWriter);
	}
	
	@Override
	public void write(Graph graph)
			throws
				ArgumentNullException,
				GraphPathSegmentInvalidException,
				GraphNodeClassHasMultipleGeneralisationsException
	{
		ArgumentGuard.requireNotNull(graph, "graph");
		this.writeStartUml(graph.getRefactoringName());
		this.writeSetSeparator("none");
		this.writeSpriteBug(SPRITE_NAME_DANGER);
		this.writeSpritePlus(SPRITE_NAME_NEW);
		
		// Domain model
		final var packageNodes = graph.getNodes(GraphNodePackage.class);
		for (final var packageNode : packageNodes) {
			this.writePackage(packageNode);
		}
		
		// Notes
		this.writeNotes(graph);
		
		this.writeEndUml();
	}

	private void writePackage(GraphNodePackage packageNode)
			throws GraphNodeClassHasMultipleGeneralisationsException {
		this.printLine(String.format("namespace %s {", packageNode.getCaption()));
		this.indentIndex++;
		
		final var classNodes = packageNode.getClassNodes(SortOrder.ASCENDING);
		for (final var classNode : classNodes) {
			this.writeClass(classNode);
		}
		
		this.indentIndex--;
		this.printLine("}");
	}
	
	private void writeClass(GraphNodeClass classNode)
			throws GraphNodeClassHasMultipleGeneralisationsException {
		// Class
		final var stereotype = classNode.getStereotype();
		this.printLine(
				String.format(
						"class %s%s {",
						getSanitizedName(classNode),
						stereotype == null
							? ""
							: " <<" + stereotype + ">> " + getStereotypeInlineStyle(stereotype)
				)
		);
		this.indentIndex++;
		
		// Attributes
		final var attributeNodes = classNode.getAttributeNodes(SortOrder.ASCENDING);
		for (final var attributeNode : attributeNodes) {
			final var attributeTypeNode = attributeNode.getType();
			final var stringBuilder = new StringBuilder();
			if (attributeTypeNode != null) {
				stringBuilder.append(attributeTypeNode.getCaption() + " ");
			}
			stringBuilder.append(attributeNode.getCaption());
			this.printLine(stringBuilder.toString());
		}
		
		// Operations
		final var operationNodes = classNode.getOperationNodes(SortOrder.ASCENDING);
		for (final var operationNode : operationNodes) {
			final var returnType = operationNode.getReturnType();
			final var stringBuilder = new StringBuilder();
			
			final var operationIsNew = operationNode.getAddedBy() != null;
			if (operationIsNew) {
				stringBuilder.append(
						String.format(
								"<%s> ",
								SPRITE_NAME_NEW
						)
				);
			}
			
			final var operationHasDangers = !operationNode.getDangers().isEmpty();
			if (operationHasDangers) {
				stringBuilder.append(
						String.format(
								"<color:%s><%s></color> ",
								toHexadecimal(Color.red),
								SPRITE_NAME_DANGER
						)
				);
			}
			
			stringBuilder.append(operationNode.getCaption() + "(");
			final var parameters = operationNode.getOperationParameters();
			stringBuilder.append(
					String.join(
							", ",
							parameters
								.stream()
								.map(param -> {
									final var paramName = param.getParameterName();
									final var paramType = param.getParameterType();
									return
											paramType == null
												? paramName
												: String.format("%s : %s", paramName, paramType.getCaption());
								})
								.collect(Collectors.toList()))
			);
			stringBuilder.append(")");
			if (returnType != null) {
				stringBuilder.append(" " + returnType.getCaption());
			}
			this.printLine(stringBuilder.toString());
		}
		
		this.indentIndex--;
		this.printLine("}");
		
		// Inner Classes
		final var innerClassNodes = classNode.getInnerClassNodes();
		for (final var innerClassNode : innerClassNodes) {
			this.writeClass(innerClassNode);
			this
				.printLine(
					String.format(
						"%s +-- %s",
						getSanitizedName(classNode),
						getSanitizedName(innerClassNode)
					)
				);
		}
		
		// Specialisations
		final var generalisation = classNode.getGeneralisationClassNode();
		if (generalisation != null) {
			this.printLine(
					String.format(
							"%s <|-- %s",
							getSanitizedName(generalisation),
							getSanitizedName(classNode)
					)
			);
		}
	}
	
	private void writeNotes(Graph graph) {
		var noteCounter = 0;
		final var dangerNodes =
			graph
				.getNodes(GraphNodeRisk.class)
				.stream()
				.filter(node -> node.getNeutralisers().size() == 0)
				.collect(Collectors.toSet());
		
		// Analyse risks
		for (final var dangerNode : dangerNodes) {
			noteCounter++;
			final var affects =
				dangerNode
					.getEdges(GraphEdgeAffects.class)
					.stream()
					.map(edge -> edge.getDestinationNode())
					.filter(node -> node instanceof GraphNodeCode)
					.map(GraphNodeCode.class::cast)
					.collect(Collectors.toUnmodifiableList());
			
			final var noteIdentifier = String.format("N%d", noteCounter);
			this.printLine(String.format("note as %s", noteIdentifier));
			this.indentIndex++;
			
			// Note text
			this.printLine(
				String.format(
					"<b>%s</b> on %s",
					dangerNode.getCaption(),
					String.join(
						", ",
						affects
							.stream()
							.map(node -> {
								return switch (node) {
									case GraphNodeAttribute attributeNode -> attributeNode.getCaption();
									case GraphNodeClass classNode -> classNode.getCaption();
									case GraphNodeOperation operationNode -> operationNode.getCaption();
									default -> findSubject(node).getCaption();
								};
							})
							.toList()
					)
				)
			);
			
			this.indentIndex--;
			this.printLine("end note");
			
			// Note relationships
			for (final var codeNode : affects) {
				this.writeNoteRelationship(codeNode, noteIdentifier);
			}
		}
	}
	
	private final void writeNoteRelationship(GraphNodeCode codeNode, String noteIdentifier) {
		switch (codeNode) {
			case GraphNodeAttribute codeNodeAttribute: {
				final var codeNodeClass = findSubject(codeNodeAttribute);
				this.printLine(
					String.format(
						"%s::%s .. %s",
						getSanitizedName(codeNodeClass),
						getSanitizedName(codeNodeAttribute),
						getSanitizedName(noteIdentifier)
					)
				);
				break;
			}
			case GraphNodeClass codeNodeClass: {
				this.printLine(
					String.format(
						"%s .. %s",
						getSanitizedName(codeNodeClass),
						getSanitizedName(noteIdentifier)
					)
				);
				break;
			}
			case GraphNodeOperation codeNodeOperation: {
				final var codeNodeClass = findSubject(codeNodeOperation);
				this.printLine(
					String.format(
						"%s::%s .. %s",
						getSanitizedName(codeNodeClass),
						getSanitizedName(codeNodeOperation),
						getSanitizedName(noteIdentifier)
					)
				);
				break;
			}
			default: {
				final var codeNodeSubject = findSubject(codeNode);
				this.writeNoteRelationship(codeNodeSubject, noteIdentifier);
				break;
			}
		}
	}
	
	private final GraphNodeCode findSubject(GraphNodeCode codeNode) {
		return switch(codeNode) {
			case GraphNodePackage pkg -> pkg;
			case GraphNodeClass cls -> cls;
			case GraphNodeClassMember member -> member.getClassNode();
			case GraphNodeMethodInvocationExpression methodInvocationExpression -> methodInvocationExpression.getOperationNode();
			default -> codeNode;
		};
	}
	
	private static final String getSanitizedName(GraphNodeCode codeNode) {
		final var name = switch (codeNode) {
			case GraphNodeClass classNode -> {
				final var className = classNode.getClassName();
				yield className;
			}
			default -> codeNode.getCaption();
		};

		return getSanitizedName(name);
	}
	
	private static final String getSanitizedName(String name) {
		return name.replace('*', '_');
	}
	
	private static final String getStereotypeInlineStyle(GraphNodeClassStereotype stereotype) {
		return switch(stereotype) {
			case GraphNodeClassStereotype.BEFORE -> "#DarkGray";
			case GraphNodeClassStereotype.AFTER -> "#LightGray";
			case GraphNodeClassStereotype.MITIGATED -> "#LightGreen";
			default -> "";
		};
	}
}
