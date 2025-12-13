package nl.ou.refactoring.advice.io.plantuml.classDiagrams;

import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.stream.Collectors;

import javax.swing.SortOrder;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.io.plantuml.GraphPlantUmlWriter;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClassMember;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;

public final class GraphPlantUmlClassDiagramWriter extends GraphPlantUmlWriter {

	public GraphPlantUmlClassDiagramWriter(StringWriter stringWriter) {
		super(stringWriter);
	}
	
	@Override
	public void write(Graph graph)
			throws ArgumentNullException, GraphPathSegmentInvalidException {
		ArgumentGuard.requireNotNull(graph, "graph");
		this.writeStartUml(graph.getRefactoringName());
		
		// Domain model
		final var packageNodes = graph.getNodes(GraphNodePackage.class);
		for (final var packageNode : packageNodes) {
			this.writePackage(packageNode);
		}
		
		// Notes
		this.writeNotes(graph);
		
		this.writeEndUml();
	}

	private void writePackage(GraphNodePackage packageNode) {
		this.printLine(String.format("namespace %s {", packageNode.getCaption()));
		this.indentIndex++;
		
		final var classNodes = packageNode.getClassNodes(SortOrder.ASCENDING);
		for (final var classNode : classNodes) {
			this.writeClass(classNode);
		}
		
		this.indentIndex--;
		this.printLine("}");
	}
	
	private void writeClass(GraphNodeClass classNode) {
		// Class
		final var className = classNode.getClassName();
		this.printLine(
				String.format(
						"class \"%s\" as %s {",
						className,
						sanitize(className)
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
			stringBuilder.append(operationNode.getCaption() + "(");
			final var parameters = operationNode.getOperationParameters();
			stringBuilder.append(
					String.join(
							", ",
							parameters
								.stream()
								.map(param -> param.getParameterName())
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
	}
	
	private void writeNotes(Graph graph) {
		var nodeCounter = 0;
		final var dangerNodes =
				graph
					.getNodes(GraphNodeRisk.class)
					.stream()
					.filter(node -> node.getNeutralisers().size() == 0)
					.collect(Collectors.toSet());
		
		// Analyse risks
		for (final var dangerNode : dangerNodes) {
			final var affects =
					dangerNode
						.getEdges(GraphEdgeAffects.class)
						.stream()
						.map(edge -> edge.getDestinationNode())
						.filter(node -> GraphNodeCode.class.isAssignableFrom(node.getClass()))
						.map(GraphNodeCode.class::cast)
						.collect(Collectors.toUnmodifiableSet());
			for (final var codeNode : affects) {
				nodeCounter++;
				final var codeNodeSubject = findSubject(codeNode);
				for (final var codeNode2 : affects) {
					if (codeNode.equals(codeNode2) || codeNode.hashCode() < codeNode2.hashCode()) {
						continue;
					}
					
					final var nodeIdentifier = String.format("N%d", nodeCounter);
					final var codeNodeSubject2 = findSubject(codeNode2);
					
					this.printLine(String.format("note as %s", nodeIdentifier));
					this.indentIndex++;
					this.printLine(getDangerLabel(codeNode, codeNode2, dangerNode));
					this.indentIndex--;
					this.printLine("end note");
					this.printLine(
							String.format(
									"%s::%s .. %s",
									sanitize(codeNodeSubject.getCaption()),
									sanitize(codeNode.getCaption()),
									sanitize(nodeIdentifier)
							)
					);
					this.printLine(
							String.format(
									"%s .. %s::%s",
									sanitize(nodeIdentifier),
									sanitize(codeNodeSubject2.getCaption()),
									sanitize(codeNode2.getCaption())
							)
					);
				}
			}
		}
	}
	
	private final GraphNodeCode findSubject(GraphNodeCode codeNode) {
		return switch(codeNode) {
			case GraphNodePackage pkg -> pkg;
			case GraphNodeClass cls -> cls;
			case GraphNodeClassMember member -> member.getClassNode();
			default -> codeNode;
		};
	}
	
	private final String sanitize(String name) {
		return name.replace('*', '_');
	}
	
	private final String getDangerLabel(GraphNodeCode one, GraphNodeCode other, GraphNodeRisk danger) {
		return
				MessageFormat.format(
						"<b>{0}</b> on {1} and {2}",
						danger.getCaption(),
						one.getCaption(),
						other.getCaption());
	}
}
