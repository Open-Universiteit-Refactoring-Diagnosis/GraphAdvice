package nl.ou.refactoring.advice.nlp.languages.englishGreatBritain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.Sentence;
import nl.ou.refactoring.advice.nodes.GraphNode;

public final class NLPLanguageEnglishGreatBritainVisitTestsArgumentsProvider implements ArgumentsProvider {
	@Override
	public Stream<? extends Arguments> provideArguments
	(
		ParameterDeclarations parameters,
		ExtensionContext context
	) throws Exception {
		final var argumentsList = new ArrayList<Arguments>();
		final var methodIsAddedToClass = createSentenceMethodIsAddedToClass();
		argumentsList.add(Arguments.of(methodIsAddedToClass.getValue0(), methodIsAddedToClass.getValue1()));
		return argumentsList.stream();
	}
	
	public static Pair<Sentence, NLPResult> createSentenceMethodIsAddedToClass() {
		final var sentence = new Sentence();
		final var nlpResult = new NLPResult("", new HashMap<String, GraphNode>());
		return Pair.with(sentence, nlpResult);
	}
}