package nl.ou.refactoring.advice.io.javaParser;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

public final class GraphJavaReaderTestsArgumentsProvider implements ArgumentsProvider {
	@Override
	public Stream<? extends Arguments> provideArguments
	(
		ParameterDeclarations parameters,
		ExtensionContext context
	) {
		return
			Stream.of(
				javaHelloWorld()
			)
			.map(Arguments::of);
	}
	
	private static String javaHelloWorld() {
		return "package nl.ou.refactoring; public class HelloWorld { private final String hello; public String getHello(int index) { return this.hello; } }";
	}
}
