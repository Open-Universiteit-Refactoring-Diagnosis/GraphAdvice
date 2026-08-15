package nl.ou.refactoring.advice.nodes.code.tokens;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.contracts.ArgumentPatternException;

final class GraphNodeIdentifierComputeIdentifierInvalidTestsProvider implements ArgumentsProvider {
	@Override
	public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context)
			throws Exception {
		return Stream.of(
				Arguments.of(null, ArgumentNullException.class),
				Arguments.of("", ArgumentEmptyException.class),
				Arguments.of("1", ArgumentPatternException.class),
				Arguments.of("@", ArgumentPatternException.class),
				Arguments.of("abc def", ArgumentPatternException.class));
	}
}