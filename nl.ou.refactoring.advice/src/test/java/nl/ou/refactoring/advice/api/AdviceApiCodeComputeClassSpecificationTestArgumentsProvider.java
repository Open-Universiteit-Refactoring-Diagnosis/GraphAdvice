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
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;

final class AdviceApiCodeComputeClassSpecificationTestArgumentsProvider implements ArgumentsProvider {
	private static Arguments getClassNodeDoesNotExistHappy() {
		final var graph = new Graph("Test Graph");
		final var packageName = "nl.ou.refactoring.advice.tests";
		GraphNodePackage.parse(graph, packageName);
		final var className = "NewTestClass";
		final Runnable testExecutor = () -> {
			final var classNodeActual = AdviceApi.Code.computeClassSpecification(graph, packageName, className);

			final var packageNodeActualOptional = classNodeActual.getPackageNode();
			assertTrue(packageNodeActualOptional.isPresent());

			final var packageNameActual = packageNodeActualOptional.get().getPackageNameFull();
			final var classNameActual = classNodeActual.getClassName();
			assertEquals(packageName, packageNameActual);
			assertEquals(className, classNameActual);
		};

		return Arguments.of(testExecutor);
	}

	private static Arguments getClassNodeExistsHappy() {
		final var graph = new Graph("Test Graph");
		final var packageName = "nl.ou.refactoring.advice.tests";
		GraphNodePackage.parse(graph, packageName);
		final var packageNode = graph.getNode(packageName, GraphNodePackage.class).get();
		final var className = "ExistingTestClass";
		final var classNameIdentifier = AdviceApi.Code.computeIdentifier(graph, className);
		final var classNode = new GraphNodeClass(graph, classNameIdentifier);
		packageNode.has(classNode);

		final Runnable testExecutor = () -> {
			final var classNodeActual = AdviceApi.Code.computeClassSpecification(graph, packageName, className);

			final var packageNodeActualOptional = classNodeActual.getPackageNode();
			assertTrue(packageNodeActualOptional.isPresent());

			final var packageNameActual = packageNodeActualOptional.get().getPackageNameFull();
			final var classNameActual = classNodeActual.getClassName();
			assertEquals(packageName, packageNameActual);
			assertEquals(className, classNameActual);

			assertEquals(classNode.getId(), classNodeActual.getId());
		};

		return Arguments.of(testExecutor);
	}

	private static Arguments getPackageNodeDoesNotExistUnhappy() {
		final var graph = new Graph("Test Graph");
		final var packageName = "nl.ou.refactoring.advice.tests";
		final var className = "NoPackageTestClass";

		final Runnable testExecutor = () -> {
			assertThrows(AdviceApiCodePackageSpecificationNotFoundException.class, () -> {
				AdviceApi.Code.computeClassSpecification(graph, packageName, className);
			});
		};

		return Arguments.of(testExecutor);
	}

	@Override
	public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context)
			throws Exception {
		return Stream
				.of(getClassNodeDoesNotExistHappy(), getClassNodeExistsHappy(), getPackageNodeDoesNotExistUnhappy());
	}
}
