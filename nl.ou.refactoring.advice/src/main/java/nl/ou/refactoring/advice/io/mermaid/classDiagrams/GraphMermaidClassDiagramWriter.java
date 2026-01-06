package nl.ou.refactoring.advice.io.mermaid.classDiagrams;

import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.io.mermaid.GraphMermaidWriter;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClassMember;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;

public final class GraphMermaidClassDiagramWriter extends GraphMermaidWriter {
	/**
	 * Initialises a new instance of {@link GraphMermaidClassDiagramWriter}.
	 * @param stringWriter Writes text output. Cannot be null.
	 * @param settings The settings for the Mermaid Class Diagram writer.
	 * @throws ArgumentNullException Thrown if stringWriter is null.
	 */
	public GraphMermaidClassDiagramWriter(
			StringWriter stringWriter,
			GraphMermaidClassDiagramWriterSettings settings)
					throws ArgumentNullException {
		super(stringWriter);
	}
	
	/**
	 * Initialises a new instance of {@link GraphMermaidClassDiagramWriter}.
	 * @param stringWriter Writes text output. Cannot be null.
	 * @throws ArgumentNullException Thrown if stringWriter is null.
	 */
	public GraphMermaidClassDiagramWriter(StringWriter stringWriter) {
		this(stringWriter, null);
	}

	@Override
	public void write(Graph graph)
			throws ArgumentNullException, GraphPathSegmentInvalidException {
		ArgumentGuard.requireNotNull(graph, "graph");
		this.printLine("classDiagram");
		this.indentIndex++;
		
		final var packageNodes = graph.getNodes(GraphNodePackage.class);
		for (final var packageNode : packageNodes) {
			this.writePackage(packageNode);
		}
		// do we forgive and include classes that do not have a package?
		
		this.writeNotes(graph);
	}
	
	private void writePackage(GraphNodePackage packageNode) {
		this.printLine(MessageFormat.format("namespace {0} '{'", packageNode.getCaption()));
		this.indentIndex++;
		
		final var classNodes = packageNode.getClassNodes();
		for (final var classNode : classNodes) {
			this.writeClass(classNode);
		}
		
		this.indentIndex--;
		this.printLine("}");
	}
	
	private void writeClass(GraphNodeClass classNode) {
		// Class
		this.printLine(String.format("class %s {", classNode.getClassName()));
		this.indentIndex++;
		
		// Attributes
		final var attributeNodes = classNode.getAttributeNodes();
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
		final var operationNodes = classNode.getOperationNodes();
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
		final var dangerNodes =
				graph
					.getNodes(GraphNodeRisk.class)
					.stream()
					.filter(node -> node.getNeutralisers().size() == 0)
					.collect(Collectors.toSet());
		
		// Analyse risks
		for (final var dangerNode : dangerNodes) {
			final var codeNodesAffected =
					dangerNode
						.getEdges(GraphEdgeAffects.class)
						.stream()
						.map(edge -> edge.getDestinationNode())
						.filter(node -> GraphNodeCode.class.isAssignableFrom(node.getClass()))
						.map(GraphNodeCode.class::cast)
						.collect(Collectors.toUnmodifiableSet());
			for (final var codeNodeAffected : codeNodesAffected) {
				final var codeNodeSubject = findSubject(codeNodeAffected);
				for (final var codeNode2 : codeNodesAffected) {
					if (codeNodeAffected.equals(codeNode2) || codeNodeAffected.hashCode() < codeNode2.hashCode()) {
						continue;
					}
					final var codeNodeSubject2 = findSubject(codeNode2);
					this.printLine(
							MessageFormat.format(
									"{0} <..> {1} : {2}",
									codeNodeSubject.getCaption(),
									codeNodeSubject2.getCaption(),
									getDangerLabel(codeNodeAffected, codeNode2, dangerNode)));
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
	
	private final String getDangerLabel(GraphNodeCode one, GraphNodeCode other, GraphNodeRisk danger) {
		return
				MessageFormat.format(
						"<strong>{0}</strong><br />on {1} and {2}",
						danger.getCaption(),
						one.getCaption(),
						other.getCaption());
	}
}
