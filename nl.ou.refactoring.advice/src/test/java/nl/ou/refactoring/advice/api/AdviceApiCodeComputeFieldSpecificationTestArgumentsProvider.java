package nl.ou.refactoring.advice.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;

final class AdviceApiCodeComputeFieldSpecificationTestArgumentsProvider implements ArgumentsProvider {
	private static Arguments getAttributeNodeDoesNotExistHappy() {
		final var graph = new Graph("Test Graph");
		final var packageName = "nl.ou.refactoring.advice.tests";
		GraphNodePackage.parse(graph, packageName);
		final var packageNode = graph.getNode(packageName, GraphNodePackage.class).get();
		final var className = "TestClass";
		final var classNameIdentifier = AdviceApi.Code.computeIdentifier(graph, className);
		final var classNode = new GraphNodeClass(graph, classNameIdentifier);
		packageNode.has(classNode);
		final Runnable testExecutor = () -> {
			final var classNameFullyQualified = packageName + "." + className;
			final var fieldName = "testField";
			final var fieldTypeName = "String";
			final var attributeNodeActual =
					AdviceApi.Code.computeFieldSpecification(graph, classNameFullyQualified, fieldName, fieldTypeName);

			final var classNodeActualOptional = attributeNodeActual.getClassNode();
			assertTrue(classNodeActualOptional.isPresent());

			final var fieldTypeNodeActualOptional = attributeNodeActual.getType();
			assertTrue(fieldTypeNodeActualOptional.isPresent());

			final var classNameActual = classNodeActualOptional.get().getClassName();
			final var fieldNameActual = attributeNodeActual.getAttributeName();
			final var fieldTypeNameActual = fieldTypeNodeActualOptional.get().getTypeName();
			assertEquals(className, classNameActual);
			assertEquals(fieldName, fieldNameActual);
			assertEquals(fieldTypeName, fieldTypeNameActual);
		};

		return Arguments.of(testExecutor);
	}

	private static Arguments getAttributeNodeExistsHappy() {
		final var graph = new Graph("Test Graph");
		final var packageName = "nl.ou.refactoring.advice.tests";
		GraphNodePackage.parse(graph, packageName);
		final var packageNode = graph.getNode(packageName, GraphNodePackage.class).get();
		final var className = "TestClass";
		final var classNameIdentifier = AdviceApi.Code.computeIdentifier(graph, className);
		final var classNode = new GraphNodeClass(graph, classNameIdentifier);
		packageNode.has(classNode);
		final var attributeName = "testField";
		final var attributeNameIdentifier = AdviceApi.Code.computeIdentifier(graph, attributeName);
		final var attributeNode = new GraphNodeAttribute(graph, attributeNameIdentifier);
		classNode.has(attributeNode);
		final Runnable testExecutor = () -> {
			final var classNameFullyQualified = packageName + "." + className;
			final var fieldName = "testField";
			final var fieldTypeName = "String";
			final var attributeNodeActual =
					AdviceApi.Code.computeFieldSpecification(graph, classNameFullyQualified, fieldName, fieldTypeName);

			final var classNodeActualOptional = attributeNodeActual.getClassNode();
			assertTrue(classNodeActualOptional.isPresent());

			final var fieldTypeNodeActualOptional = attributeNodeActual.getType();
			assertTrue(fieldTypeNodeActualOptional.isPresent());

			final var classNameActual = classNodeActualOptional.get().getClassName();
			final var fieldNameActual = attributeNodeActual.getAttributeName();
			final var fieldTypeNameActual = fieldTypeNodeActualOptional.get().getTypeName();
			assertEquals(className, classNameActual);
			assertEquals(fieldName, fieldNameActual);
			assertEquals(fieldTypeName, fieldTypeNameActual);
			assertEquals(attributeNode.getId(), attributeNodeActual.getId());
		};

		return Arguments.of(testExecutor);
	}

	private static Arguments getClassNodeDoesNotExistUnhappy() {
		final Runnable testExecutor = () -> {
			final var graph = new Graph("Test Graph");
			final var classNameFullyQualified = "nl.ou.refactoring.advice.tests.TestClass";
			final var fieldName = "testField";
			final var fieldTypeName = "String";
			assertThrows(AdviceApiCodeClassSpecificationNotFoundException.class, () -> {
				AdviceApi.Code.computeFieldSpecification(graph, classNameFullyQualified, fieldName, fieldTypeName);
			});
		};

		return Arguments.of(testExecutor);
	}

	@Override
	public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context)
			throws Exception {
		return Stream.of(
				getAttributeNodeDoesNotExistHappy(),
				getAttributeNodeExistsHappy(),
				getClassNodeDoesNotExistUnhappy());
	}
}