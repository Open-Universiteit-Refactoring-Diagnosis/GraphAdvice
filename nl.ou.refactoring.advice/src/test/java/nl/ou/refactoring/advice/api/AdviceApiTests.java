package nl.ou.refactoring.advice.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

final class AdviceApiTests {
	@Test
	@DisplayName("Should create a Refactoring Advice Graph (RAG) from a refactoring template")
	final void createRefactoringGraphByTemplateTest() {
		final var templateIdentifiers = RefactoringTemplateIdentifier.values();
		for (final var templateIdentifier : templateIdentifiers) {
			final var graph = AdviceApi.createRefactoringGraphByTemplate(templateIdentifier);
			assertNotNull(graph);
		}
	}

	@Test
	@DisplayName("Should throw if a new Refactoring Advice Graph (RAG)'s specified name is null, empty or contains only white spaces.")
	final void createRefactoringGraphShouldThrowTest() {
		assertThrows(ArgumentNullException.class, () -> AdviceApi.createRefactoringGraph(null));
		assertThrows(ArgumentEmptyException.class, () -> AdviceApi.createRefactoringGraph(""));
		assertThrows(ArgumentEmptyException.class, () -> AdviceApi.createRefactoringGraph("  "));
	}

	@Test
	@DisplayName("Should create a new Refactoring Advice Graph (RAG)")
	final void createRefactoringGraphTest() {
		final var graph = AdviceApi.createRefactoringGraph("Test");
		assertNotNull(graph);
		assertEquals("Test", graph.getRefactoringName());
	}
}