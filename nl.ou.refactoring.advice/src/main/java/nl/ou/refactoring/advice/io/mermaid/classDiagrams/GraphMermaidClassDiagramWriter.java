package nl.ou.refactoring.advice.io.mermaid.classDiagrams;

import java.io.StringWriter;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.mermaid.GraphMermaidWriter;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;

public final class GraphMermaidClassDiagramWriter extends GraphMermaidWriter {
	/**
	 * Initialises a new instance of {@link GraphMermaidClassDiagramWriter}.
	 * @param stringWriter Writes text output. Cannot be null.
	 * @throws ArgumentNullException Thrown if stringWriter is null.
	 */
	public GraphMermaidClassDiagramWriter(StringWriter stringWriter)
			throws ArgumentNullException {
		super(stringWriter);
	}

	@Override
	public void write(Graph graph)
			throws ArgumentNullException, GraphPathSegmentInvalidException {
		this.printLine("classDiagram");
		this.indentIndex++;
		final var packageNodes = graph.getNodes(GraphNodePackage.class);
		for (final var packageNode : packageNodes) {
			this.writePackage(packageNode);
		}
		// do we forgive and include classes that do not have a package?
	}
	
	private void writePackage(GraphNodePackage packageNode) {
		final var classNodes =
				packageNode
					.getEdges()
					.stream()
					.map(edge -> edge.getDestinationNode())
					.filter(
							node ->
							GraphNodeClass.class.isAssignableFrom(node.getClass())
					)
					.map(node -> (GraphNodeClass)node)
					.collect(Collectors.toSet());
		for (final var classNode : classNodes) {
			this.writeClass(classNode);
		}
	}
	
	private void writeClass(GraphNodeClass classNode) {
		// Class
		this.printLine(String.format("class %s {", classNode.getClassName()));
		this.indentIndex++;
		
		final var codeNodes =
				classNode
					.getEdges()
					.stream()
					.map(edge -> edge.getDestinationNode())
					.filter(
							node ->
							GraphNodeCode.class.isAssignableFrom(node.getClass())
					)
					.map(node -> (GraphNodeCode)node)
					.collect(Collectors.toSet());
		
		// Attributes
		final var attributeNodes =
				codeNodes
					.stream()
					.filter(
							node ->
							GraphNodeAttribute.class.isAssignableFrom(node.getClass())
					)
					.map(node -> (GraphNodeAttribute)node)
					.sorted((left, right) -> left.getCaption().compareTo(right.getCaption()))
					.collect(Collectors.toSet());
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
		final var operationNodes =
				codeNodes
					.stream()
					.filter(
							node ->
							GraphNodeOperation.class.isAssignableFrom(node.getClass())
					)
					.map(node -> (GraphNodeOperation)node)
					.sorted((left, right) -> left.getCaption().compareTo(right.getCaption()))
					.collect(Collectors.toSet());
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
}
