package nl.ou.refactoring.advice.io.plantuml.classDiagrams;

import java.awt.Color;
import java.io.StringWriter;
import java.util.Stack;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.SortOrder;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.operations.expressions.GraphEdgeInvokes;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.io.GraphWriterException;
import nl.ou.refactoring.advice.io.GraphWriterMemberOwningClassMissingException;
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
import nl.ou.refactoring.advice.resources.ResourceProvider;

import static nl.ou.refactoring.advice.io.ColorExtensions.toHexadecimal;

/**
 * Writes PlantUML Class Diagrams from Refactoring Advice Graphs.
 */
public final class GraphPlantUmlClassDiagramWriter extends GraphPlantUmlWriter {
	private static final String SPRITE_NAME_DANGER = "$danger";
	private static final String SPRITE_NAME_NEW = "$new";
	private static final String SPRITE_NAME_REMOVED = "$removed";

	/**
	 * Initialises a new instance of {@link GraphPlantUmlClassDiagramWriter}.
	 * 
	 * @param stringWriter A {@link StringWriter} that is responsible for text
	 *                     output.
	 */
	public GraphPlantUmlClassDiagramWriter(StringWriter stringWriter) {
		super(stringWriter);
	}

	@Override
	public void write(Graph graph) throws ArgumentNullException, GraphValidationException, GraphWriterException {
		ArgumentGuard.requireNotNull(graph, "graph");
		this.writeStartUml(graph.getRefactoringName());
		this.writeSetSeparator("none");
		this.writeSpriteBug(SPRITE_NAME_DANGER);
		this.writeSpriteMinus(SPRITE_NAME_REMOVED);
		this.writeSpritePlus(SPRITE_NAME_NEW);

		// Domain model
		final var packageNodes = graph.getNodes(GraphNodePackage.class).stream()
				.filter(packageNode -> packageNode.getParent().isEmpty()).collect(Collectors.toUnmodifiableSet());
		for (final var packageNode : packageNodes) {
			this.writePackage(packageNode);
		}

		// Notes
		this.writeNotes(graph);

		// Invocations
		this.writeInvocations(graph);

		this.writeEndUml();
	}

	private void writePackage(GraphNodePackage packageNode) throws GraphNodeClassHasMultipleGeneralisationsException {
		final var packageNodesPathStack = new Stack<GraphNodePackagePath>();
		packageNodesPathStack.push(new GraphNodePackagePath(packageNode, packageNode.getPackageName())); // root node

		while (!packageNodesPathStack.isEmpty()) {
			final var packageNodePathCurrent = packageNodesPathStack.pop();
			final var packageNodeCurrent = packageNodePathCurrent.getPackageNode();
			final var packagePathCurrent = packageNodePathCurrent.getPath();

			final var classNodes = packageNodeCurrent.getClassNodes();
			final var hasContents = !classNodes.isEmpty();

			if (hasContents) {
				this.printLine(String.format("namespace %s {", packagePathCurrent));
				this.indentIndex++;

				for (final var classNode : classNodes) {
					this.writeClass(classNode);
				}

				pushPackageChildren(packageNodeCurrent, packageNodesPathStack, packagePathCurrent);

				this.indentIndex--;
				this.printLine("}");
			} else {
				pushPackageChildren(packageNodeCurrent, packageNodesPathStack, packagePathCurrent);
			}
		}
	}

	private static void pushPackageChildren(GraphNodePackage packageNode,
			Stack<GraphNodePackagePath> packageNodesPathStack, String packageNodePathCurrent) {
		for (final var packageNodeChild : packageNode.getPackageNodes()) {
			final var packageNodeChildPath = packageNodePathCurrent + "." + packageNodeChild.getPackageName();
			packageNodesPathStack.push(new GraphNodePackagePath(packageNodeChild, packageNodeChildPath));
		}
	}

	private void writeClass(GraphNodeClass classNode) throws GraphNodeClassHasMultipleGeneralisationsException {
		// Class
		final var stereotype = classNode.getStereotype();
		this.printLine(String.format("class %s%s {", getSanitizedName(classNode),
				stereotype == null ? "" : " <<" + stereotype + ">> " + getStereotypeInlineStyle(stereotype)));
		this.indentIndex++;

		// Attributes
		final var attributeNodes = classNode.getAttributeNodes(SortOrder.ASCENDING);
		for (final var attributeNode : attributeNodes) {
			final var attributeTypeNode = attributeNode.getType();
			final var stringBuilder = new StringBuilder();

			final var attributeIsNew = attributeNode.getAddedBy().isPresent();
			if (attributeIsNew) {
				stringBuilder.append(String.format("<%s> ", SPRITE_NAME_NEW));
			}

			final var attributeIsRemoved = attributeNode.getRemovedBy().isPresent();
			if (attributeIsRemoved) {
				stringBuilder.append(String.format("<%s> ", SPRITE_NAME_REMOVED));
			}

			final var attributeHasDangers = !attributeNode.getDangers().isEmpty();
			if (attributeHasDangers) {
				stringBuilder
						.append(String.format("<color:%s><%s></color> ", toHexadecimal(Color.red), SPRITE_NAME_DANGER));
			}

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

			final var operationIsNew = operationNode.getAddedBy().isPresent();
			if (operationIsNew) {
				stringBuilder.append(String.format("<%s> ", SPRITE_NAME_NEW));
			}

			final var operationIsRemoved = operationNode.getRemovedBy().isPresent();
			if (operationIsRemoved) {
				stringBuilder.append(String.format("<%s> ", SPRITE_NAME_REMOVED));
			}

			final var operationHasDangers = !operationNode.getDangers().isEmpty();
			if (operationHasDangers) {
				stringBuilder
						.append(String.format("<color:%s><%s></color> ", toHexadecimal(Color.red), SPRITE_NAME_DANGER));
			}

			stringBuilder.append(operationNode.getCaption() + "(");
			final var parameters = operationNode.getOperationParameters();
			stringBuilder.append(String.join(", ", parameters.stream().map(param -> {
				final var paramName = param.getParameterName();
				final var paramType = param.getParameterType();
				return paramType == null ? paramName : String.format("%s : %s", paramName, paramType.getCaption());
			}).collect(Collectors.toList())));
			stringBuilder.append(")");
			if (returnType.isPresent()) {
				stringBuilder.append(" " + returnType.get().getCaption());
			}
			this.printLine(stringBuilder.toString());
		}

		this.indentIndex--;
		this.printLine("}");

		// Inner Classes
		final var innerClassNodes = classNode.getInnerClassNodes();
		for (final var innerClassNode : innerClassNodes) {
			this.writeClass(innerClassNode);
			this.printLine(String.format("%s +-- %s", getSanitizedName(classNode), getSanitizedName(innerClassNode)));
		}

		// Specialisations
		final var generalisation = classNode.getGeneralisationClassNode();
		if (generalisation != null) {
			this.printLine(String.format("%s <|-- %s", getSanitizedName(generalisation), getSanitizedName(classNode)));
		}
	}

	private void writeNotes(Graph graph) {
		var noteCounter = 0;

		// Analyse risks
		final var dangerNodes = graph.getNodes(GraphNodeRisk.class).stream()
				.filter(node -> node.getNeutralisers().size() == 0).collect(Collectors.toSet());
		for (final var dangerNode : dangerNodes) {
			noteCounter++;
			final var affects = dangerNode.getEdges(GraphEdgeAffects.class).stream()
					.map(edge -> edge.getDestinationNode()).filter(node -> node instanceof GraphNodeCode)
					.map(GraphNodeCode.class::cast).collect(Collectors.toUnmodifiableList());

			final var noteIdentifier = String.format("N%d", noteCounter);
			this.printLine(String.format("note as %s", noteIdentifier));
			this.indentIndex++;

			// Note text
			this.printLine(String.format("<b>%s</b> on %s", // TODO use Preposition for 'on' from NLP name space, put
															// the rest in a Sentence as well
					dangerNode.getCaption(), String.join(", ", affects.stream().map(node -> {
						return switch (node) {
						case GraphNodeAttribute attributeNode -> attributeNode.getCaption();
						case GraphNodeClass classNode -> classNode.getCaption();
						case GraphNodeOperation operationNode -> operationNode.getCaption();
						default -> findSubject(node).getCaption();
						};
					}).toList())));

			this.indentIndex--;
			this.printLine("end note");

			// Note relationships
			for (final var codeNode : affects) {
				this.writeNoteRelationship(codeNode, noteIdentifier);
			}
		}
	}

	private void writeNoteRelationship(GraphNodeCode codeNode, String noteIdentifier) {
		switch (codeNode) {
		case GraphNodeAttribute codeNodeAttribute: {
			final var codeNodeClass = findSubject(codeNodeAttribute);
			this.printLine(String.format("%s::%s .. %s", getSanitizedName(codeNodeClass),
					getSanitizedName(codeNodeAttribute), getSanitizedName(noteIdentifier)));
			break;
		}
		case GraphNodeClass codeNodeClass: {
			this.printLine(
					String.format("%s .. %s", getSanitizedName(codeNodeClass), getSanitizedName(noteIdentifier)));
			break;
		}
		case GraphNodeOperation codeNodeOperation: {
			final var codeNodeClass = findSubject(codeNodeOperation);
			this.printLine(String.format("%s::%s .. %s", getSanitizedName(codeNodeClass),
					getSanitizedName(codeNodeOperation), getSanitizedName(noteIdentifier)));
			break;
		}
		default: {
			final var codeNodeSubject = findSubject(codeNode);
			this.writeNoteRelationship(codeNodeSubject, noteIdentifier);
			break;
		}
		}
	}

	private void writeInvocations(Graph graph) {
		final var methodInvocationExpressions = graph.getNodes(GraphNodeMethodInvocationExpression.class);
		for (final var methodInvocationExpression : methodInvocationExpressions) {
			final var invokingMethod = methodInvocationExpression.getOperationNode();
			final var invokingMethodClass = findSubject(invokingMethod);
			final var invokedMethod = methodInvocationExpression.getInvokedOperationNode();
			final var invokedMethodClass = findSubject(invokedMethod);
			this.printLine(String.format("%s::%s -[dotted]-> %s::%s : %s", getSanitizedName(invokingMethodClass),
					getSanitizedName(invokingMethod), getSanitizedName(invokedMethodClass),
					getSanitizedName(invokedMethod),
					ResourceProvider.GraphEdgeLabels.getLabel(GraphEdgeInvokes.class)));
		}
	}

	private GraphNodeCode findSubject(GraphNodeCode codeNode) {
		return switch (codeNode) {
		case GraphNodePackage pkg -> pkg;
		case GraphNodeClass cls -> cls;
		case GraphNodeClassMember member ->
			member.getClassNode().orElseThrow(() -> new GraphWriterMemberOwningClassMissingException(member));
		case GraphNodeMethodInvocationExpression methodInvocationExpression ->
			methodInvocationExpression.getOperationNode();
		default -> codeNode;
		};
	}

	private static String getSanitizedName(GraphNodeCode codeNode) {
		final var name = switch (codeNode) {
		case GraphNodeClass classNode -> {
			final var className = classNode.getClassName();
			yield className;
		}
		default -> codeNode.getCaption();
		};

		return getSanitizedName(name);
	}

	private static String getSanitizedName(String name) {
		return name.replace('*', '_');
	}

	private static String getStereotypeInlineStyle(GraphNodeClassStereotype stereotype) {
		return switch (stereotype) {
		case GraphNodeClassStereotype.BEFORE -> "#DarkGray";
		case GraphNodeClassStereotype.AFTER -> "#LightGray";
		case GraphNodeClassStereotype.MITIGATED -> "#LightGreen";
		default -> "";
		};
	}
}
