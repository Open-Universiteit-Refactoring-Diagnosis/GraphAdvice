package nl.ou.refactoring.advice.io.json;

import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.classgraph.ClassGraph;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonGenerator;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeWorkflow;

/**
 * Writes the JSON Schema for validating JSON representations of Refactoring Advice Graphs.
 */
public final class GraphJsonSchemaWriter {
	private final Writer writer;

	/**
	 * Initialises a new instance of {@link GraphJsonSchemaWriter}.
	 * @param writer Writes the JSON Schema.
	 * @throws ArgumentNullException Thrown if writer is null.
	 */
	public GraphJsonSchemaWriter(Writer writer)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(writer, "writer");
		this.writer = writer;
	}

	/**
	 * Writes the JSON Schema based on the current Refactoring Advice Graph model.
	 */
	public void write() {
		Map<String, Boolean> configuration = new HashMap<>();
		configuration.put(JsonGenerator.PRETTY_PRINTING, true);
		final var jsonProvider = JsonProvider.provider();
		final var jsonWriterFactory = jsonProvider.createWriterFactory(configuration);
		final var jsonWriter = jsonWriterFactory.createWriter(this.writer);
		final var jsonSchemaObjectBuilder = Json.createObjectBuilder();
		jsonSchemaObjectBuilder.add("$schema", "https://json-schema.org/draft-07/schema");
		jsonSchemaObjectBuilder.add("$id", "http://ou.nl/schemas/RefactoringAdviceGraph.schema.json");
		
		List<Class<?>> nodeClasses = new ArrayList<Class<?>>();
		
		// Definitions
		final var jsonSchemaDefsObjectBuilder = Json.createObjectBuilder();
		try (final var scanResult =
				new ClassGraph()
					.enableAllInfo()
					.acceptPackages("nl.ou.refactoring.advice.edges")
					.acceptPackages("nl.ou.refactoring.advice.nodes")
					.scan()) {
			final var classes = scanResult.getSubclasses(GraphNodeCode.class);
			classes.addAll(scanResult.getSubclasses(GraphNodeWorkflow.class));
			nodeClasses =
				classes
					.loadClasses()
					.stream()
					.filter(cls -> !Modifier.isAbstract(cls.getModifiers()))
					.sorted((left, right) -> left.getName().compareTo(right.getName()))
					.collect(Collectors.toUnmodifiableList());
			for (final var classType : nodeClasses) {
				final var graphNodeSchemaBuilder = Json.createObjectBuilder();
				graphNodeSchemaBuilder.add("type", "object");
				final var graphNodeRequiredSchemaArrayBuilder = Json.createArrayBuilder();
				final var graphNodePropertiesObjectBuilder = Json.createObjectBuilder();
				graphNodePropertiesObjectBuilder.add("type", getTypeSchema(classType));
				graphNodePropertiesObjectBuilder.add("edges", getEdgesSchema(classType));
				graphNodeRequiredSchemaArrayBuilder.add(Json.createValue("type"));
				graphNodeRequiredSchemaArrayBuilder.add(Json.createValue("edges"));
				final var constructorParameters =
					List.of(
						List.of(classType.getConstructors())
							.stream()
							.sorted((c1, c2) -> c1.getParameterCount() - c2.getParameterCount())
							.findFirst()
							.get()
							.getParameters()
						)
						.stream()
						.skip(1) // skip the usual "graph" parameter, this parameter is provided implicitly.
						.collect(Collectors.toUnmodifiableList());
				for (final var constructorParameter : constructorParameters) {
					final var graphNodeAdditionalPropertySchemaObjectBuilder = Json.createObjectBuilder();
					graphNodeAdditionalPropertySchemaObjectBuilder.add("type", getClassTypeSchemaJsonValue(constructorParameter.getType()));
					graphNodePropertiesObjectBuilder.add(constructorParameter.getName(), graphNodeAdditionalPropertySchemaObjectBuilder.build());
					if (
						classType.getConstructors().length == 1 ||
						List.of(classType.getConstructors())
							.stream()
							.allMatch(c -> constructorHasParameter(c, constructorParameter))
					) {
						// If there is only one constructor or there are more constructors and all of them contain the current parameter, it is a required parameter.
						graphNodeRequiredSchemaArrayBuilder.add(Json.createValue(constructorParameter.getName()));
					}
				}
				graphNodeSchemaBuilder.add("properties", graphNodePropertiesObjectBuilder.build());
				graphNodeSchemaBuilder.add("required", graphNodeRequiredSchemaArrayBuilder);
				final var graphNodeJsonObjectName = getJsonObjectName(classType);
				jsonSchemaDefsObjectBuilder.add(graphNodeJsonObjectName, graphNodeSchemaBuilder.build());
			}
		}
		jsonSchemaObjectBuilder.add("$defs", jsonSchemaDefsObjectBuilder.build());
		
		jsonSchemaObjectBuilder.add("title", "Refactoring Advice Graph");
		jsonSchemaObjectBuilder.add("description", "Describes a Refactoring Advice Graph that may lead to a human-readable advice for a refactoring in software code.");
		jsonSchemaObjectBuilder.add("type", "object");
		
		// Properties
		final var jsonSchemaPropertiesObjectBuilder = Json.createObjectBuilder();
		final var jsonSchemaPropertiesRefactoringNameObjectBuilder = Json.createObjectBuilder();
		jsonSchemaPropertiesRefactoringNameObjectBuilder.add("title", "Refactoring Name");
		jsonSchemaPropertiesRefactoringNameObjectBuilder.add("description", "The name of the Refactoring that is described by the Refactoring Advice Graph");
		jsonSchemaPropertiesRefactoringNameObjectBuilder.add("type", "string");
		jsonSchemaPropertiesObjectBuilder.add("refactoringName", jsonSchemaPropertiesRefactoringNameObjectBuilder.build());
		final var jsonSchemaPropertiesNodesObjectBuilder = Json.createObjectBuilder();
		jsonSchemaPropertiesNodesObjectBuilder.add("title", "Nodes");
		jsonSchemaPropertiesNodesObjectBuilder.add("description", "The nodes in the Refactoring Advice Graph");
		jsonSchemaPropertiesNodesObjectBuilder.add("type", "array");
		jsonSchemaPropertiesNodesObjectBuilder.add("items", getNodesItemsSchema(nodeClasses));
		jsonSchemaPropertiesObjectBuilder.add("nodes", jsonSchemaPropertiesNodesObjectBuilder.build());
		jsonSchemaObjectBuilder.add("properties", jsonSchemaPropertiesObjectBuilder.build());
		
		// Required
		final var jsonSchemaRequiredArrayBuilder = Json.createArrayBuilder();
		jsonSchemaRequiredArrayBuilder.add("refactoringName");
		jsonSchemaRequiredArrayBuilder.add("nodes");
		jsonSchemaObjectBuilder.add("required", jsonSchemaRequiredArrayBuilder);
		
		final var jsonSchemaObject = jsonSchemaObjectBuilder.build();
		jsonWriter.write(jsonSchemaObject);
	}
	
	private static Boolean constructorHasParameter(Constructor<?> constructor, Parameter parameter) {
		return
			List.of(constructor.getParameters())
				.stream()
				.anyMatch(p -> p.getName().equals(parameter.getName()) && p.getType().equals(parameter.getType()));
	}
	
	private static String getJsonObjectName(Class<?> classType) {
		return classType.getSimpleName();
	}
	
	private static JsonObject getTypeSchema(Class<?> nodeClass) {
		final var schemaJsonObjectBuilder = Json.createObjectBuilder();
		schemaJsonObjectBuilder.add("title", "Type");
		schemaJsonObjectBuilder.add("description", "The Class type of the Refactoring Advice Graph node");
		schemaJsonObjectBuilder.add("const", nodeClass.getName());
		return schemaJsonObjectBuilder.build();
	}
	
	private static Set<Class<?>> getEdgeClasses(Class<?> nodeClass) {
		return
			List.of(nodeClass.getMethods())
				.stream()
				.filter(method -> GraphEdge.class.isAssignableFrom(method.getReturnType()))
				.map(method -> method.getReturnType())
				.sorted((left, right) -> left.getName().compareTo(right.getName()))
				.collect(Collectors.toUnmodifiableSet());
	}
	
	private static JsonObject getEdgesSchema(Class<?> nodeClass) {
		final var schemaJsonObjectBuilder = Json.createObjectBuilder();
		schemaJsonObjectBuilder.add("title", "Edges");
		schemaJsonObjectBuilder.add("description", "The outgoing edges of the node.");
		schemaJsonObjectBuilder.add("type", "array");
		final var itemsSchemaJsonObjectBuilder = Json.createObjectBuilder();
		final var itemsAnyOfSchemaJsonArrayBuilder = Json.createArrayBuilder();
		for (final var edgeClass : getEdgeClasses(nodeClass)) {
			itemsAnyOfSchemaJsonArrayBuilder.add(getEdgeSchema(edgeClass));
		}
		itemsSchemaJsonObjectBuilder.add("anyOf", itemsAnyOfSchemaJsonArrayBuilder.build());
		schemaJsonObjectBuilder.add("items", itemsSchemaJsonObjectBuilder.build());
		return schemaJsonObjectBuilder.build();
	}
	
	private static JsonObject getEdgeSchema(Class<?> edgeClass) {
		final var schemaJsonObjectBuilder = Json.createObjectBuilder();
		schemaJsonObjectBuilder.add("title", edgeClass.getSimpleName());
		schemaJsonObjectBuilder.add("description", "An edge in the Refactoring Advice Graph.");
		schemaJsonObjectBuilder.add("type", "object");
		final var propertiesSchemaJsonObjectBuilder = Json.createObjectBuilder();
		final var propertiesTypeSchemaJsonObjectBuilder = Json.createObjectBuilder();
		propertiesTypeSchemaJsonObjectBuilder.add("title", edgeClass.getSimpleName());
		propertiesTypeSchemaJsonObjectBuilder.add("const", edgeClass.getName());
		propertiesSchemaJsonObjectBuilder.add("type", propertiesTypeSchemaJsonObjectBuilder.build());
		final var propertiesToSchemaJsonObjectBuilder = Json.createObjectBuilder();
		propertiesToSchemaJsonObjectBuilder.add("title", "Relates to");
		propertiesToSchemaJsonObjectBuilder.add("description", "The index of the node in the nodes list to which the edge refers.");
		propertiesToSchemaJsonObjectBuilder.add("type", "number");
		propertiesSchemaJsonObjectBuilder.add("to", propertiesToSchemaJsonObjectBuilder.build());
		schemaJsonObjectBuilder.add("properties", propertiesSchemaJsonObjectBuilder.build());
		final var requiredArrayBuilder = Json.createArrayBuilder();
		requiredArrayBuilder.add("type");
		requiredArrayBuilder.add("to");
		schemaJsonObjectBuilder.add("required", requiredArrayBuilder);
		return schemaJsonObjectBuilder.build();
	}
	
	private static JsonObject getNodesItemsSchema(List<Class<?>> nodeClasses) {
		final var schemaJsonObjectBuilder = Json.createObjectBuilder();
		final var schemaAnyOfJsonArrayBuilder = Json.createArrayBuilder();
		for (final var nodeClass : nodeClasses) {
			final var schemaNodeReferenceObjectBuilder = Json.createObjectBuilder();
			schemaNodeReferenceObjectBuilder.add("$ref", "#/$defs/" + getJsonObjectName(nodeClass));
			schemaAnyOfJsonArrayBuilder.add(schemaNodeReferenceObjectBuilder.build());
		}
		schemaJsonObjectBuilder.add("anyOf", schemaAnyOfJsonArrayBuilder.build());
		return schemaJsonObjectBuilder.build();
	}
	
	private static JsonValue getClassTypeSchemaJsonValue(Class<?> classType) {
		final var classTypeName = classType.getName();
		return switch (classTypeName) {
			case "java.lang.Boolean" -> Json.createValue("boolean");
			case "java.lang.Byte" -> Json.createValue("number");
			case "java.lang.Double" -> Json.createValue("number");
			case "java.lang.Float" -> Json.createValue("number");
			case "java.lang.Integer" -> Json.createValue("number");
			case "java.lang.Long" -> Json.createValue("number");
			case "java.lang.Number" -> Json.createValue("number");
			case "java.lang.Short" -> Json.createValue("number");
			case "java.lang.String" -> Json.createValue("string");
			default -> Json.createValue("object");
		};
	}
}
