package nl.ou.refactoring.advice.nlp.grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.ou.refactoring.advice.GraphPath;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAdds;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeRemoves;
import nl.ou.refactoring.advice.nlp.NLPGraphDiagnostics;
import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.nouns.CommonNoun;
import nl.ou.refactoring.advice.nlp.grammar.nouns.Countability;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionKey;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounPhrase;
import nl.ou.refactoring.advice.nlp.grammar.nouns.ReferenceNoun;
import nl.ou.refactoring.advice.nlp.grammar.nouns.SemanticClassification;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.Preposition;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.PrepositionalPhrase;
import nl.ou.refactoring.advice.nlp.grammar.verbs.LexicalVerb;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbAspect;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationKey;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbModality;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbPhrase;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbTense;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbVoice;
import nl.ou.refactoring.advice.nlp.tokens.NLPTokenNotFoundException;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemove;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskMissingDefinition;

/**
 * Builds sentences from Refactoring Advice Graph (RAG) paths.
 */
public final class NLPGrammarSentenceBuilder {
	/**
	 * Initialises a new instance of {@link NLPGrammarSentenceBuilder}. Has a
	 * private scope because the Class is intended to be static.
	 */
	private NLPGrammarSentenceBuilder() {
	}

	/**
	 * Builds sentences for a danger description.
	 * 
	 * @param graphPath  The path through a Refactoring Advice Graph (RAG) that will
	 *                   result in a danger.
	 * @param references References to nodes in the Refactoring Advice Graph (RAG)
	 *                   and their textual representation.
	 * @return A list of {@link Sentence} with the generated danger description.
	 */
	public static List<Sentence> buildDangerDescription(GraphPath graphPath, Map<String, GraphNode> references) {
		final var dangerPathSegments = graphPath.getSegments();
		final var dangerPathSegmentLast = dangerPathSegments.getLast();
		final var dangerNode = dangerPathSegmentLast.getNode();
		return switch (dangerNode) {
			case GraphNodeRiskDoubleDefinition doubleDefinitionNode ->
				buildDoubleDefinitionDescription(doubleDefinitionNode, graphPath, references);
			case GraphNodeRiskMissingDefinition missingDefinitionNode ->
				buildMissingDefinitionDescription(missingDefinitionNode, graphPath, references);
			default -> new ArrayList<Sentence>();
		};
	}

	private static List<Sentence> buildDoubleDefinitionDescription(
			GraphNodeRiskDoubleDefinition doubleDefinitionNode,
			GraphPath graphPath,
			Map<String, GraphNode> references) {
		final var sentences = new ArrayList<Sentence>();

		/*
		 * Get the affected nodes, distinguishing the new node and the conflicting
		 * node(s).
		 */
		final var affected = doubleDefinitionNode.getAffected();
		final var cause =
				doubleDefinitionNode.getCause()
						.orElseThrow(() -> new RuntimeException("Could not determine the cause of the risk"));

		// Node added
		final var nodeAdded =
				cause.getEdges(GraphEdgeAdds.class)
						.stream()
						.map(GraphEdge::getDestinationNode)
						.findAny()
						.orElseThrow(() -> new RuntimeException("Could not determine which node is added"));
		final var nodeAddedReferenceString = NLPResult.extractReferenceString(nodeAdded);
		references.putIfAbsent(nodeAddedReferenceString, nodeAdded);
		final var nodeAddedReferenceNoun = new ReferenceNoun<GraphNode>(nodeAdded, _ -> nodeAddedReferenceString);

		// Node removed
		final var nodeMicrostepRemove =
				cause.getRelated()
						.stream()
						.filter(GraphNodeMicrostepRemove.class::isInstance)
						.map(GraphNodeMicrostepRemove.class::cast)
						.findAny()
						.orElseThrow(
								() -> new RuntimeException(
										"Could not find the microstep for removing the element before the rename"));
		final var nodeRemoved =
				nodeMicrostepRemove.getEdges(GraphEdgeRemoves.class)
						.stream()
						.map(GraphEdgeRemoves::getDestinationNode)
						.findAny()
						.orElseThrow(() -> new RuntimeException("Could not determine which node is removed"));
		final var nodeRemovedReferenceString = NLPResult.extractReferenceString(nodeRemoved);
		references.putIfAbsent(nodeRemovedReferenceString, nodeRemoved);
		final var nodeRemovedReferenceNoun = new ReferenceNoun<GraphNode>(nodeRemoved, _ -> nodeRemovedReferenceString);

		// Nodes conflicting
		final var nodeConflicting =
				affected.stream()
						.filter((node) -> node != nodeAdded)
						.findAny()
						.orElseThrow(() -> new RuntimeException("Could not determine which node is conflicting"));
		final var nodeConflictingReferenceString = NLPResult.extractReferenceString(nodeConflicting);
		references.putIfAbsent(nodeConflictingReferenceString, nodeConflicting);
		final var conflictingNoun = new ReferenceNoun<GraphNode>(nodeConflicting, _ -> nodeConflictingReferenceString);

		// Determine what kind of transformation we are dealing with.
		final var isRename = NLPGraphDiagnostics.isRenameRefactoring(graphPath);
		if (isRename) {
			/*
			 * Build the rename sentence:
			 * "Renaming of [GraphNode removed] to [GraphNode added] causes a conflict with [GraphNode conflicting]"
			 */
			final var renameSentence = new Sentence();
			final var renamingNoun =
					new CommonNoun(
							Tokens.Nouns.Verbal.RENAMING,
							SemanticClassification.ABSTRACT,
							Countability.COUNTABLE);
			final var renamingNounPhrase = new NounPhrase(renamingNoun);
			renamingNounPhrase
					.setDeclension(new NounDeclensionKey(GrammaticalNumber.SINGULAR, GrammaticalRegister.PLAIN));
			final var ofPreposition = new Preposition(Tokens.Prepositions.OF_AGENCY_SUBJECTIVE_GENITIVE);
			final var nodeRemovedNounPhrase = new NounPhrase(nodeRemovedReferenceNoun);
			final var renamingPrepositionalPhrase = new PrepositionalPhrase(ofPreposition, nodeRemovedNounPhrase);
			renamingNounPhrase.setPrepositionalPhrase(renamingPrepositionalPhrase);

			final var toPreposition = new Preposition(Tokens.Prepositions.TO_TARGET_RECIPIENT);
			final var nodeAddedNounPhrase = new NounPhrase(nodeAddedReferenceNoun);
			final var toPrepositionalPhrase = new PrepositionalPhrase(toPreposition, nodeAddedNounPhrase);
			nodeRemovedNounPhrase.setPrepositionalPhrase(toPrepositionalPhrase);

			renameSentence.setNounPhrase(renamingNounPhrase);

			final var causeVerb =
					LexicalVerb.fromToken(Tokens.Verbs.Lexical.CAUSE)
							.orElseThrow(
									() -> new NLPTokenNotFoundException(
											Tokens.Verbs.Lexical.CAUSE,
											Tokens.Verbs.Lexical.class));
			final var causeVerbPhrase = new VerbPhrase(causeVerb);
			causeVerbPhrase.setConjugation(
					new VerbConjugationKey(
							GrammaticalPerson.THIRD,
							GrammaticalNumber.SINGULAR,
							VerbAspect.IMPERFECTIVE,
							VerbModality.INDICATIVE,
							VerbTense.PRESENT,
							VerbVoice.ACTIVE,
							GrammaticalRegister.PLAIN));
			final var conflictNoun =
					CommonNoun.fromToken(Tokens.Nouns.Common.CONFLICT_INCOMPATIBILITY)
							.orElseThrow(
									() -> new NLPTokenNotFoundException(
											Tokens.Nouns.Common.CONFLICT_INCOMPATIBILITY,
											Tokens.Nouns.Common.class));
			final var conflictNounPhrase = new NounPhrase(conflictNoun);
			final var conflictReferenceNounPhrase = new NounPhrase(conflictingNoun);
			final var conflictWithPreposition = new Preposition(Tokens.Prepositions.WITH_AGAINST);
			final var conflictWithPrepositionalPhrase = new PrepositionalPhrase(conflictWithPreposition, conflictReferenceNounPhrase);
			conflictNounPhrase.setPrepositionalPhrase(conflictWithPrepositionalPhrase);
			causeVerbPhrase.setNounPhrase(conflictNounPhrase);
			
			renameSentence.setVerbPhrase(causeVerbPhrase);

			sentences.add(renameSentence);
		} else {
			// Build the conflict sentence.
			final var conflictSentence = new Sentence();
			final var nodeAddedNounPhrase = new NounPhrase(nodeAddedReferenceNoun);
			final var conflictVerb =
					LexicalVerb.fromToken(Tokens.Verbs.Lexical.CONFLICT)
							.orElseThrow(
									() -> new NLPTokenNotFoundException(
											Tokens.Verbs.Lexical.CONFLICT,
											Tokens.Verbs.Lexical.class));
			final var conflictVerbPhrase = new VerbPhrase(conflictVerb);
			conflictVerbPhrase.setConjugation(
					new VerbConjugationKey(
							GrammaticalPerson.THIRD,
							GrammaticalNumber.SINGULAR,
							VerbAspect.IMPERFECTIVE,
							VerbModality.INDICATIVE,
							VerbTense.PRESENT,
							VerbVoice.ACTIVE,
							GrammaticalRegister.PLAIN));
			final var conflictingWithPreposition = new Preposition(Tokens.Prepositions.WITH_AGAINST);
			final var conflictingNounPhrase = new NounPhrase(conflictingNoun);
			final var conflictPrepositionalPhrase =
					new PrepositionalPhrase(conflictingWithPreposition, conflictingNounPhrase);
			conflictVerbPhrase.setPrepositionalPhrase(conflictPrepositionalPhrase);
			conflictSentence.setNounPhrase(nodeAddedNounPhrase);
			conflictSentence.setVerbPhrase(conflictVerbPhrase);
			sentences.add(conflictSentence);
		}

		return sentences;
	}

	private static List<Sentence> buildMissingDefinitionDescription(
			GraphNodeRiskMissingDefinition missingDefinitionNode,
			GraphPath graphPath,
			Map<String, GraphNode> references) {
		final var sentences = new ArrayList<Sentence>();

		return sentences;
	}
}
