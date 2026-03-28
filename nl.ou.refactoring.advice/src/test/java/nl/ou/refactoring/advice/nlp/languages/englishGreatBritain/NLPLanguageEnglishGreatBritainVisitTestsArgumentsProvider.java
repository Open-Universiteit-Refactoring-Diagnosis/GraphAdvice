package nl.ou.refactoring.advice.nlp.languages.englishGreatBritain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.Sentence;
import nl.ou.refactoring.advice.nlp.grammar.nouns.CommonNoun;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounPhrase;
import nl.ou.refactoring.advice.nlp.grammar.nouns.ReferenceNoun;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.Preposition;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.PrepositionalPhrase;
import nl.ou.refactoring.advice.nlp.grammar.verbs.AuxiliaryVerb;
import nl.ou.refactoring.advice.nlp.grammar.verbs.LexicalVerb;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbPhrase;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;

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
		final var methodIsAddedToClassWithReference = createSentenceMethodIsAddedToClassWithReference();
		argumentsList.add(Arguments.of(methodIsAddedToClassWithReference.getValue0(), methodIsAddedToClassWithReference.getValue1()));
		return argumentsList.stream();
	}
	
	public static Pair<Sentence, NLPResult> createSentenceMethodIsAddedToClass() {
		final var sentence = new Sentence();
		// TODO articles
		final var methodNoun =
			CommonNoun
				.fromToken(Tokens.Nouns.Common.METHOD)
				.get();
		final var methodNounPhrase = new NounPhrase(methodNoun);
		sentence.setNounPhrase(methodNounPhrase);
		final var toBeVerb =
			AuxiliaryVerb
				.fromToken(Tokens.Verbs.Auxiliary.BE)
				.get();
		final var addVerb =
			LexicalVerb
				.fromToken(Tokens.Verbs.Lexical.ADD)
				.get();
		final var toPreposition =
			Preposition
				.fromToken(Tokens.Prepositions.TO_DIRECTIONAL)
				.get();
		final var classNoun =
			CommonNoun
				.fromToken(Tokens.Nouns.Common.CLASS_OO_PROGRAMMING)
				.get();
		final var classNounPhrase = new NounPhrase(classNoun);
		final var prepositionalPhrase = new PrepositionalPhrase(toPreposition, classNounPhrase);
		final var verbPhrase = new VerbPhrase(addVerb);
		verbPhrase.setAuxiliaryVerb(toBeVerb);
		verbPhrase.setPrepositionalPhrase(prepositionalPhrase);
		sentence.setVerbPhrase(verbPhrase);
		
		final var nlpResult = new NLPResult("method is added to class", new HashMap<String, GraphNode>());
		return Pair.with(sentence, nlpResult);
	}
	
	public static Pair<Sentence, NLPResult> createSentenceMethodIsAddedToClassWithReference() {
		// Graph
		final var graph = new Graph("Sentence Graph");
		final var myPackageIdentifier = new GraphNodeIdentifier(graph, "myPackage");
		final var packageNode = new GraphNodePackage(graph, myPackageIdentifier);
		final var myClassIdentifier = new GraphNodeIdentifier(graph, "MyClass");
		final var classNode = new GraphNodeClass(graph, myClassIdentifier);
		packageNode.has(classNode);
	
		// Sentence
		final var sentence = new Sentence();
		// TODO articles
		final var methodNoun =
			CommonNoun
				.fromToken(Tokens.Nouns.Common.METHOD)
				.get();
		final var methodNounPhrase = new NounPhrase(methodNoun);
		sentence.setNounPhrase(methodNounPhrase);
		final var toBeVerb =
			AuxiliaryVerb
				.fromToken(Tokens.Verbs.Auxiliary.BE)
				.get();
		final var addVerb =
			LexicalVerb
				.fromToken(Tokens.Verbs.Lexical.ADD)
				.get();
		final var toPreposition =
			Preposition
				.fromToken(Tokens.Prepositions.TO_DIRECTIONAL)
				.get();
		final var classReferenceNoun = new ReferenceNoun<GraphNodeClass>(classNode, node -> node.getId().toString());
		final var classNounPhrase = new NounPhrase(classReferenceNoun);
		final var prepositionalPhrase = new PrepositionalPhrase(toPreposition, classNounPhrase);
		final var verbPhrase = new VerbPhrase(addVerb);
		verbPhrase.setAuxiliaryVerb(toBeVerb);
		verbPhrase.setPrepositionalPhrase(prepositionalPhrase);
		sentence.setVerbPhrase(verbPhrase);
		
		final var references = new HashMap<String, GraphNode>();
		final var classNodeIdString = String.format("{%s}", classNode.getId().toString());
		references.putIfAbsent(classNodeIdString, classNode);
		final var nlpResult = new NLPResult(String.format("method is added to %s", classNodeIdString), references);
		return Pair.with(sentence, nlpResult);
	}
}