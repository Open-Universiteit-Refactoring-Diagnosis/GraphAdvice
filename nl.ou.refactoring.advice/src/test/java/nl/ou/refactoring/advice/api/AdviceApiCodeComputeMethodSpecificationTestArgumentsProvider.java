package nl.ou.refactoring.advice.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperationParameter;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperationParameterSignature;

final class AdviceApiCodeComputeMethodSpecificationTestArgumentsProvider implements ArgumentsProvider {
	private static Arguments getMethodNodeDoesNotExistHappy() {
		final Runnable testExecutor = () -> {
			final var graph = new Graph("Test Graph");
			final var packageName = "nl.ou.refactoring.advice.tests";
			GraphNodePackage.parse(graph, packageName);
			final var packageNode = graph.getNode(packageName, GraphNodePackage.class).get();
			final var className = "TestClass";
			final var classNameIdentifier = AdviceApi.Code.computeIdentifier(graph, className);
			final var classNode = new GraphNodeClass(graph, classNameIdentifier);
			packageNode.has(classNode);
			final var operationsBefore = classNode.getOperationNodes();
			assertEquals(0, operationsBefore.size());

			final var classNameFullyQualified = packageName + "." + className;
			final var methodName = "testMethod";
			final List<GraphNodeOperationParameterSignature> methodParameters = List.of();
			final var operationNodeActual =
					AdviceApi.Code
							.computeMethodSpecification(graph, classNameFullyQualified, methodName, methodParameters);
			assertNotNull(operationNodeActual);
			final var operationsAfter = classNode.getOperationNodes();
			assertEquals(1, operationsAfter.size());
		};

		return Arguments.of(testExecutor);
	}

	private static Arguments getMethodExistsHappy() {
		final Runnable testExecutor = () -> {
			final var graph = new Graph("Test Graph");
			final var packageName = "nl.ou.refactoring.advice.tests";
			GraphNodePackage.parse(graph, packageName);
			final var packageNode = graph.getNode(packageName, GraphNodePackage.class).get();
			final var className = "TestClass";
			final var classNameIdentifier = AdviceApi.Code.computeIdentifier(graph, className);
			final var classNode = new GraphNodeClass(graph, classNameIdentifier);
			packageNode.has(classNode);

			final var methodName = "testMethod";
			final var methodNameIdentifier = AdviceApi.Code.computeIdentifier(graph, methodName);
			final var operationNode = new GraphNodeOperation(graph, methodNameIdentifier);
			classNode.has(operationNode);
			final var operationsBefore = classNode.getOperationNodes();
			assertEquals(1, operationsBefore.size());

			final var classNameFullyQualified = packageName + "." + className;
			final List<GraphNodeOperationParameterSignature> methodParameters = List.of();
			final var operationNodeActual =
					AdviceApi.Code
							.computeMethodSpecification(graph, classNameFullyQualified, methodName, methodParameters);
			assertNotNull(operationNodeActual);
			final var operationsAfter = classNode.getOperationNodes();
			assertEquals(1, operationsAfter.size());
			assertEquals(operationNode.getId(), operationNodeActual.getId());
		};

		return Arguments.of(testExecutor);
	}

	private static Arguments getMethodExistsWithMultipleParametersHappy() {
		final Runnable testExecutor = () -> {
			final var graph = new Graph("Test Graph");
			final var packageName = "nl.ou.refactoring.advice.tests";
			GraphNodePackage.parse(graph, packageName);
			final var packageNode = graph.getNode(packageName, GraphNodePackage.class).get();
			final var className = "TestClass";
			final var classNameIdentifier = AdviceApi.Code.computeIdentifier(graph, className);
			final var classNode = new GraphNodeClass(graph, classNameIdentifier);
			packageNode.has(classNode);

			final var methodName = "testMethod";
			final var methodNameIdentifier = AdviceApi.Code.computeIdentifier(graph, methodName);

			// No parameters
			final var operationWithNoParametersNode = new GraphNodeOperation(graph, methodNameIdentifier, List.of());
			classNode.has(operationWithNoParametersNode);

			// Two parameters
			final var operationParameterOneName = "one";
			final var operationParameterOneNameIdentifier =
					AdviceApi.Code.computeIdentifier(graph, operationParameterOneName);
			final var operationParameterOneNode =
					new GraphNodeOperationParameter(graph, operationParameterOneNameIdentifier);
			final var operationParameterTwoName = "two";
			final var operationParameterTwoNameIdentifier =
					AdviceApi.Code.computeIdentifier(graph, operationParameterTwoName);
			final var operationParameterTwoNode =
					new GraphNodeOperationParameter(graph, operationParameterTwoNameIdentifier);
			final var operationWithTwoParametersNode =
					new GraphNodeOperation(
							graph,
							methodNameIdentifier,
							List.of(operationParameterOneNode, operationParameterTwoNode));
			classNode.has(operationWithTwoParametersNode);

			final var operationsBefore = classNode.getOperationNodes();
			assertEquals(2, operationsBefore.size());

			final var classNameFullyQualified = packageName + "." + className;
			final List<GraphNodeOperationParameterSignature> methodParameters = List.of();
			final var operationNodeActual =
					AdviceApi.Code
							.computeMethodSpecification(graph, classNameFullyQualified, methodName, methodParameters);
			assertNotNull(operationNodeActual);
			final var operationsAfter = classNode.getOperationNodes();
			assertEquals(2, operationsAfter.size()); // should remain the same as before
			assertEquals(operationWithNoParametersNode.getId(), operationNodeActual.getId());
		};

		return Arguments.of(testExecutor);
	}

	@Override
	public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context)
			throws Exception {
		return Stream.of(
				getMethodNodeDoesNotExistHappy(),
				getMethodExistsHappy(),
				getMethodExistsWithMultipleParametersHappy());
	}
}
