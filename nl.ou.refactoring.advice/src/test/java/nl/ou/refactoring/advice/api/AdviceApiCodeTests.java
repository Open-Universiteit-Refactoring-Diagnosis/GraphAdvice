package nl.ou.refactoring.advice.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

public final class AdviceApiCodeTests {
	@ParameterizedTest
	@ArgumentsSource(AdviceApiCodeComputeClassSpecificationTestArgumentsProvider.class)
	@DisplayName("Should compute a Class Specification in a Refactoring Advice Graph (RAG)")
	public void computeClassSpecificationTests(Runnable testExecutor) {
		testExecutor.run();
	}
	
	@ParameterizedTest
	@ArgumentsSource(AdviceApiCodeComputeFieldSpecificationTestArgumentsProvider.class)
	@DisplayName("Should compute a Field Specification in a Refactoring Advice Graph (RAG)")
	public void computeFieldSpecificationTests(Runnable testExecutor) {
		testExecutor.run();
	}
	
	@ParameterizedTest
	@ArgumentsSource(AdviceApiCodeComputeIdentifierTestArgumentsProvider.class)
	@DisplayName("Should compute an identifier node in a Refactoring Advice Graph (RAG)")
	public void computeIdentifierTests(Runnable testExecutor) {
		testExecutor.run();
	}
	
	@ParameterizedTest
	@ArgumentsSource(AdviceApiCodeComputeMethodSpecificationTestArgumentsProvider.class)
	@DisplayName("Should compute a Method Specification in a Refactoring Advice Graph (RAG)")
	public void computeMethodSpecificationTests(Runnable testExecutor) {
		testExecutor.run();
	}
}