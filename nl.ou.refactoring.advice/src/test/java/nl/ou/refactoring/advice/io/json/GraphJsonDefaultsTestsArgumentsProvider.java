package nl.ou.refactoring.advice.io.json;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphReaderException;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;

public final class GraphJsonDefaultsTestsArgumentsProvider implements ArgumentsProvider {
	@Override
	public Stream<? extends Arguments> provideArguments
	(
		ParameterDeclarations parameters,
		ExtensionContext context
	) throws RefactoringMayContainOnlyOneStartNodeException, ArgumentNullException, GraphReaderException {
		return
			Stream.of(
				GraphJsonDefaults.moveMethod(),
				GraphJsonDefaults.renameField(),
				GraphJsonDefaults.renameMethod()
			)
			.map(Arguments::of);
	}
}
