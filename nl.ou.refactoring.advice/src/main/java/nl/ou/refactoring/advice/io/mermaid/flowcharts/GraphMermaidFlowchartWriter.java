package nl.ou.refactoring.advice.io.mermaid.flowcharts;

import java.io.StringWriter;
import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgePrecedes;
import nl.ou.refactoring.advice.io.mermaid.GraphMermaidWriter;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.GraphNodeType;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperationParameter;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;
import nl.ou.refactoring.advice.nodes.workflow.remedies.GraphNodeRemedy;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * Writes a graph to a <a href="https://www.mermaidchart.com/">Mermaid</a> diagram.
 */
public final class GraphMermaidFlowchartWriter extends GraphMermaidWriter {
	private final GraphMermaidFlowchartDirection direction;
	
	/**
	 * Initialises a new instance of {@link GraphMermaidFlowchartWriter}.
	 * @param stringWriter Writes the flowchart output as a {@link String}.
	 * @param direction The direction of the flowchart.
	 */
	public GraphMermaidFlowchartWriter(
			StringWriter stringWriter,
			GraphMermaidFlowchartDirection direction) {
		super(stringWriter);
		this.direction = direction;
	}
	
	/**
	 * Gets the flowchart direction.
	 * @return The flowchart direction.
	 */
	public GraphMermaidFlowchartDirection getDirection() {
		return this.direction;
	}

	@Override
	public void write(Graph graph)
			throws ArgumentNullException, GraphPathSegmentInvalidException {
		this.printLine("flowchart " + getDirectionString(this.direction));
		this.indentIndex++;
		final var nodes = graph.getNodes();
		for (final var node : nodes) {
			this.writeNode(node);
		}
	}
	
	private void writeEdge(GraphEdge edge) {
		final var sourceNode = edge.getSourceNode();
		final var destinationNode = edge.getDestinationNode();
		this.printLine(
				String.format(
						"%s %s %s",
						sourceNode.getId(),
						switch (edge) {
							case GraphEdgeAffects _ -> String.format("-. %s .->", edge.getLabel());
							default -> String.format("-- %s -->", edge.getLabel());
						},
						destinationNode.getId()
				)
		);
	}
	
	private void writeNode(GraphNode node) {
		this.printLine(getNodeDeclarationString(node));
		for (var edge : node.getEdges()) {
			this.writeEdge(edge);
		}
		if (node instanceof GraphNodeOperation) {
			this.writeNodeOperation((GraphNodeOperation)node);
		}
		this.printLine(
				String.format(
						"style %s fill:%s,color:%s",
						node.getId().toString(),
						getFill(node),
						getColor(node)));
	}
	
	private void writeNodeOperation(GraphNodeOperation operationNode) {
		final var operationParameterNodes = operationNode.getOperationParameters();
		final var format = "%s -. %s .-> %s";
		if (operationParameterNodes.size() > 0) {
			this.printLine(
				String.format(
					format,
					operationNode.getId(),
					ResourceProvider.GraphEdgeLabels.getLabel(GraphEdgeHas.class),
					operationParameterNodes.get(0).getId())
				);
		}
		if (operationParameterNodes.size() == 1) {
			return;
		}
		for (var i = 1; i < operationParameterNodes.size(); i++) {
			this.printLine(
				String.format(
					format,
					operationNode.getId(),
					ResourceProvider.GraphEdgeLabels.getLabel(GraphEdgePrecedes.class),
					operationParameterNodes.get(i).getId()
				)
			);
		}
	}
	
	private static String getDirectionString(GraphMermaidFlowchartDirection direction) {
		return switch (direction) {
			case LeftToRight -> "LR";
			case RightToLeft -> "RL";
			case TopToBottom -> "TB";
			case BottomToTop -> "BT";
		};
	}
	
	private static String getColor(GraphNode node) {
		return switch(node) {
			case GraphNodeAttribute _ -> "#FFFFFF";
			case GraphNodeClass _ -> "#FFFFFF";
			case GraphNodeMicrostep _ -> "#000000";
			case GraphNodeOperation _ -> "#FFFFFF";
			case GraphNodeOperationParameter _ -> "#FFFFFF";
			case GraphNodePackage _ -> "#FFFFFF";
			case GraphNodeRefactoringStart _ -> "#000000";
			case GraphNodeRemedy _ -> "#000000";
			case GraphNodeRisk _ -> "#000000";
			case GraphNodeType _ -> "#FFFFFF";
			default -> "#000000";
		};
	}
	
	private static String getFill(GraphNode node) {
		return switch(node) {
			case GraphNodeAttribute _ -> "#60A917";
			case GraphNodeClass _ -> "#0050EF";
			case GraphNodeMicrostep _ -> "#DAE8FC";
			case GraphNodeOperation _ -> "#6A00FF";
			case GraphNodeOperationParameter _ -> "#A90F9E";
			case GraphNodePackage _ -> "#D80073";
			case GraphNodeRefactoringStart _ -> "#F5F5F5";
			case GraphNodeRemedy _ -> "#D5E8D4";
			case GraphNodeRisk risk ->
				risk.getNeutralisers().size() > 0
					? "#FFE6CC"
					: "#F8CECC";
			case GraphNodeType _ -> "#CC6600";
			default -> "#FFFFFF";
		};
	}
	
	private static String getNodeDeclarationString(GraphNode node) {
		final var nodeIdentifier = node.getId().toString();
		final var nodeLabel = node.getLabel();
		final var nodeCaption = node.getCaption();
		final var nodeString = String.format("%s([<b>%s</b><br>%s])", nodeIdentifier, nodeLabel, nodeCaption);
		return nodeString;
	}
}
