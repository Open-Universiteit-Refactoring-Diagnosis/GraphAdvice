package nl.ou.refactoring.advice.io.json;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue.ValueType;
import jakarta.json.spi.JsonProvider;
import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.io.GraphReader;
import nl.ou.refactoring.advice.io.GraphReaderException;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart;

/**
 * Reads Refactoring Advice Graphs from JSON.
 */
public class GraphJsonReader implements GraphReader {
	private final Reader reader;
	private final Map<Class<?>, Function<Graph, GraphNode>> nodeConstructors = new HashMap<>();

	/**
	 * Initialises a new instance of {@link GraphJsonReader}.
	 * @param reader Reads JSON.
	 * @throws ArgumentNullException Thrown if reader is null.
	 */
	public GraphJsonReader(Reader reader)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(reader, "reader");
		this.reader = reader;
		this.nodeConstructors.put(
				GraphNodeRefactoringStart.class,
				g -> {
		            try {
		                var constructor = GraphNodeRefactoringStart.class.getConstructors()[0];
		                return (GraphNode) constructor.newInstance(g, g.getRefactoringName());
		            } catch (Exception e) {
		                throw new RuntimeException(e);
		            }
				});
	}

	@Override
	public Graph read()
			throws GraphReaderException {
		try {
			this.reader.reset();
		}
		catch (IOException exception) {
			exception.printStackTrace();
			throw new GraphJsonReaderException(exception);
		}
		
		final var jsonProvider = JsonProvider.provider();
		final var jsonReader = jsonProvider.createReader(this.reader);
		final var refactoringObject = jsonReader.readObject();
		var refactoringName = readRefactoringName(refactoringObject);
		final var graph = new Graph(refactoringName);
		
		final var nodeJsonArray = refactoringObject.getJsonArray("nodes");
		if (nodeJsonArray != null) {
			List<GraphNode> nodes = new ArrayList<>();
			for (var i = 0; i < nodeJsonArray.size(); i++) {
				final var nodeJsonObject = nodeJsonArray.getJsonObject(i);
				final var nodeType = nodeJsonObject.getString("type");
				final Class<? extends GraphNode> nodeClass;
				try {
					nodeClass = (Class<? extends GraphNode>)Class.forName(nodeType);
				} catch (ClassNotFoundException exception) {
					throw new GraphJsonReaderNodeClassNotFoundException(nodeType);
				}

				nodes.add(this.constructNode(graph, nodeClass, nodeJsonObject));
			}
			for (var i = 0; i < nodeJsonArray.size(); i++) {
				final var node = nodes.get(i);
				final var nodeJsonObject = nodeJsonArray.getJsonObject(i);
				final var edgesJsonArray = nodeJsonObject.getJsonArray("edges");
				if (edgesJsonArray == null) {
					continue;
				}
				for (var j = 0; j < edgesJsonArray.size(); j++) {
					final var edgeJsonObject = edgesJsonArray.getJsonObject(j);
					final var edgeType = edgeJsonObject.getString("type");
					final var edgeTo = edgeJsonObject.getInt("to");
					final var nodeTo = nodes.get(edgeTo);
					final Class<GraphEdge> edgeClass;
					try {
						edgeClass = (Class<GraphEdge>)Class.forName(edgeType);
					} catch (ClassNotFoundException exception) {
						throw new RuntimeException(exception);
					}
					
					graph.getOrAddEdge(
							node,
							nodeTo,
							(_, _) -> this.constructEdge(edgeClass, node, nodeTo),
							edgeClass);
				}
			}
		}
		
		return graph;
	}
	
	private <TEdge extends GraphEdge> TEdge constructEdge(
			Class<TEdge> nodeEdge,
			GraphNode left,
			GraphNode right) {
		try {
			final var edgeConstructors = List.of(nodeEdge.getConstructors());
			final var edgeConstructor =
				edgeConstructors
					.stream()
					.filter(
						constructor -> {
							final var constructorParameterTypes = constructor.getParameterTypes();
							if (constructorParameterTypes.length != 2) {
								return false;
							}
							
							return
								constructorParameterTypes[0].isAssignableFrom(left.getClass()) &&
								constructorParameterTypes[1].isAssignableFrom(right.getClass());
						}
					)
					.findAny()
					.orElse(null);
			if (edgeConstructor == null) {
				throw new RuntimeException("No matching constructor"); // TODO specific exception
			}
			return (TEdge)edgeConstructor.newInstance(left, right);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}
	
	private GraphNode constructNode(Graph graph, Class<?> nodeClass, JsonObject nodeJsonObject) {
		try {
			final var nodeClassConstructorDefault = nodeClass.getConstructors()[0];
			final var nodeClassConstructorArguments = new ArrayList<Object>();
			nodeClassConstructorArguments.add(graph);
			switch (nodeClass.getName()) {
				case "nl.ou.refactoring.advice.nodes.code.GraphNodePackage": {
					final var packageName = nodeJsonObject.getString("packageName");
					nodeClassConstructorArguments.add(packageName);
					break;
				}
				case "nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass": {
					final var className = nodeJsonObject.getString("className");
					nodeClassConstructorArguments.add(className);
					nodeClassConstructorArguments.add(null); // class stereotype
					break;
				}
				case "nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation": {
					final var operationName = nodeJsonObject.getString("operationName");
					nodeClassConstructorArguments.add(operationName);
					nodeClassConstructorArguments.add(null); // operation parameters
					break;
				}
				case "nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart": {
					final var refactoringName = this.readRefactoringName(nodeJsonObject);
					nodeClassConstructorArguments.add(refactoringName);
					break;
				}
				default: {
					break;
				}
			}
			return (GraphNode)nodeClassConstructorDefault.newInstance(nodeClassConstructorArguments.toArray());
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	private String readRefactoringName(final JsonObject refactoringObject)
			throws GraphJsonReaderRefactoringNameNotFoundException {
		final var refactoringNameValue = refactoringObject.get("refactoringName");
		if (refactoringNameValue == null ||
				refactoringNameValue.getValueType() != ValueType.STRING ||
				refactoringNameValue.toString().trim().length() == 0) {
			throw new GraphJsonReaderRefactoringNameNotFoundException();
		}
		
		var refactoringName = refactoringNameValue.toString();
		refactoringName = refactoringName.substring(1, refactoringName.length() - 1);
		return refactoringName;
	}
}
