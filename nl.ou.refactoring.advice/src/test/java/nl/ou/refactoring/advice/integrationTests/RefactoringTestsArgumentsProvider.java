package nl.ou.refactoring.advice.integrationTests;

import java.io.StringReader;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphReaderException;
import nl.ou.refactoring.advice.io.json.GraphJsonReader;
import nl.ou.refactoring.advice.io.json.JsonSamplesLoader;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;

public final class RefactoringTestsArgumentsProvider implements ArgumentsProvider {

	@Override
	public Stream<? extends Arguments> provideArguments
	(
		ParameterDeclarations parameters,
		ExtensionContext context
	) throws RefactoringMayContainOnlyOneStartNodeException, ArgumentNullException, GraphReaderException {
		final var refactoringMoveMethodJson = JsonSamplesLoader.loadJson("/refactoringMoveMethod.json");
		final var refactoringMoveMethod = new GraphJsonReader(new StringReader(refactoringMoveMethodJson)).read();
		return
			Stream.of(
				refactoringMoveMethod
			)
			.map(Arguments::of);
	}

}
