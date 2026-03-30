package nl.ou.refactoring.advice.nlp.processors;

import java.util.HashMap;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.NLPException;
import nl.ou.refactoring.advice.nlp.NLPProcessor;
import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.Sentence;
import nl.ou.refactoring.advice.nlp.grammar.nouns.CommonNoun;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounPhrase;
import nl.ou.refactoring.advice.nlp.grammar.nouns.ReferenceNoun;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.Preposition;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.PrepositionalPhrase;
import nl.ou.refactoring.advice.nlp.grammar.verbs.LexicalVerb;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbPhrase;
import nl.ou.refactoring.advice.nlp.languages.NLPLanguage;
import nl.ou.refactoring.advice.nlp.tokens.NLPTokenNotFoundException;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.workflow.GraphWorkflowExplorer;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMustContainStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepOperationMissingException;

/**
 * A Natural Language Processing provider that arrange text into natural language grammar
 * based on nodes visited in a Refactoring Advice Graph.
 */
public class NLPGrammarProcessor extends NLPProcessor {
	private final NLPLanguage language;
	
	/**
	 * Initialises a new instance of {@link NLPGrammarProcessor}.
	 * @param language The language that visits the grammar and follows its rules to generate {@link NLPResult}.
	 * @throws ArgumentNullException Thrown if language is null.
	 */
	public NLPGrammarProcessor(NLPLanguage language)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(language, "language");
		this.language = language;
	}

	@Override
	public NLPResult process(Graph graph)
			throws
				ArgumentNullException,
				GraphValidationException,
				NLPException {
		ArgumentGuard.requireNotNull(graph, "graph");
		
		final var references = new HashMap<String, GraphNode>();
		
		final var startNode =
			graph
				.getStart()
				.orElseThrow(() -> new RefactoringMustContainStartNodeException());
		
		var result = new NLPResult("", references);
		
		final var dangerNodes = GraphWorkflowExplorer.getDangers(graph);
		for (final var dangerNode : dangerNodes) {
			// Dangers
			final var dangerPaths = startNode.findPaths(dangerNode, 100);
			if (dangerPaths.isEmpty()) {
				continue;
			}
			final var dangerPath = dangerPaths.get(0);
			
			for (final var dangerPathSegment : dangerPath.getSegments()) {
				final var node = dangerPathSegment.getNode();
				switch (node) {
					case GraphNodeMicrostepAddMethod microstepAddMethodNode: {
						final var microstepSentence = new Sentence();
						final var operationNode =
							microstepAddMethodNode
								.getOperationNode()
								.orElseThrow(() -> new GraphNodeMicrostepOperationMissingException(microstepAddMethodNode));
						
						final var methodNoun =
							CommonNoun
								.fromToken(Tokens.Nouns.Common.METHOD)
								.orElseThrow(() -> new NLPTokenNotFoundException(Tokens.Nouns.Common.METHOD, Tokens.Nouns.Common.class));
						final var microstepNounPhrase = new NounPhrase(methodNoun);
						microstepSentence.setNounPhrase(microstepNounPhrase);
						final var addVerb =
							LexicalVerb
								.fromToken(Tokens.Verbs.Lexical.ADD)
								.orElseThrow(() -> new NLPTokenNotFoundException(Tokens.Verbs.Lexical.ADD, Tokens.Verbs.Lexical.class));
						final var microstepVerbPhrase = new VerbPhrase(addVerb);
						microstepSentence.setVerbPhrase(microstepVerbPhrase);
						final var toPreposition =
							Preposition
								.fromToken(Tokens.Prepositions.TO_TARGET_RECIPIENT)
								.orElseThrow(() -> new NLPTokenNotFoundException(Tokens.Prepositions.TO_TARGET_RECIPIENT, Tokens.Prepositions.class));
						
						// TODO add PP to VP
						
						final var microstepPrepositionalNounPhrase =
							operationNode
								.getClassNode()
								.map(
									cn -> {
										final var microstepClassReferenceNoun = new ReferenceNoun<GraphNodeClass>(cn, n -> n.getId().toString());
										return new NounPhrase(microstepClassReferenceNoun);
									}
								)
								.orElse(new NounPhrase(CommonNoun.fromToken(Tokens.Nouns.Common.CLASS_OO_PROGRAMMING).get()));
						final var prepositionalPhrase = new PrepositionalPhrase(toPreposition, microstepPrepositionalNounPhrase);
						microstepVerbPhrase.setPrepositionalPhrase(prepositionalPhrase);
						
						// TODO construct sentence
						result = result.merge(this.language.visit(microstepSentence));
						break;
					}
					default:
						break;
				}
			}
			
			// Remedies
			// TODO remedy paths
		}
		
		return result;
	}
	
	/**
	 * Gets the {@link NLPLanguage} that visits the grammar and follows its rules to generate {@link NLPResult}.
	 * @return The {@link NLPLanguage} that visits the grammar and follows its rules to generate {@link NLPResult}.
	 */
	public NLPLanguage getLanguage() {
		return this.language;
	}
	
	@Override
	public String toString() {
		return String.format("Grammar_%s", this.language.toString());
	}
}