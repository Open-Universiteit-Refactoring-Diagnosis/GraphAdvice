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
import nl.ou.refactoring.advice.nlp.grammar.prepositions.PrepositionalPhrase;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbPhrase;
import nl.ou.refactoring.advice.nlp.languages.NLPLanguage;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.workflow.GraphWorkflowExplorer;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMustContainStartNodeException;

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
			
			final var dangerSentence = new Sentence();
			final var refactoringNoun =
				CommonNoun
					.fromToken(Tokens.Nouns.Common.REFACTORING, this.language.getNounGenderSuppliers())
					.get();
			final var dangerNounPhrase = new NounPhrase(refactoringNoun);
			for (final var dangerPathSegment : dangerPath.getSegments()) {
				// TODO verb phrases for microsteps
				final var node = dangerPathSegment.getNode();
			}
			// TODO construct sentence
			result = result.merge(this.language.visit(dangerSentence));
			
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
}