package nl.ou.refactoring.advice.nlp.grammar;

import java.util.HashMap;
import java.util.Map;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.NLPException;
import nl.ou.refactoring.advice.nlp.NLPProcessor;
import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.nouns.CommonNoun;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounPhrase;
import nl.ou.refactoring.advice.nlp.grammar.nouns.ReferenceNoun;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.Preposition;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.PrepositionalPhrase;
import nl.ou.refactoring.advice.nlp.grammar.verbs.LexicalVerb;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbAspect;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationKey;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbModality;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbPhrase;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbTense;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbVoice;
import nl.ou.refactoring.advice.nlp.languages.NLPLanguage;
import nl.ou.refactoring.advice.nlp.tokens.NLPTokenNotFoundException;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;
import nl.ou.refactoring.advice.nlp.tokens.Tokens.Verbs;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.workflow.GraphWorkflowExplorer;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMustContainStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAttributeMissingException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepOperationMissingException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;

/**
 * A Natural Language Processing provider that arrange text into natural
 * language grammar based on nodes visited in a Refactoring Advice Graph.
 */
public class NLPGrammarProcessor extends NLPProcessor {
	private final NLPLanguage language;

	/**
	 * Initialises a new instance of {@link NLPGrammarProcessor}.
	 * 
	 * @param language The language that visits the grammar and follows its rules to
	 *                 generate {@link NLPResult}.
	 * @throws ArgumentNullException Thrown if language is null.
	 */
	public NLPGrammarProcessor(NLPLanguage language) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(language, "language");
		this.language = language;
	}

	@Override
	public NLPResult process(Graph graph) throws ArgumentNullException, GraphValidationException, NLPException {
		ArgumentGuard.requireNotNull(graph, "graph");

		final var references = new HashMap<String, GraphNode>();

		final var startNode = graph.getStart().orElseThrow(() -> new RefactoringMustContainStartNodeException());

		var result = new NLPResult("", references);

		final var dangerNodes = GraphWorkflowExplorer.getDangers(graph);
		for (final var dangerNode : dangerNodes) {
			// Dangers
			final var dangerPaths = startNode.findPaths(dangerNode, 100);
			if (dangerPaths.isEmpty()) {
				continue;
			}
			final var dangerPath = dangerPaths.get(0);
			final var dangerDescription = NLPGrammarSentenceBuilder.buildDangerDescription(dangerPath, references);
			result = dangerDescription.stream().map(this.language::visit).reduce(result, NLPResult::merge);

			// Remedies
			// TODO remedy paths
		}

		return result;
	}

	/**
	 * Gets the {@link NLPLanguage} that visits the grammar and follows its rules to
	 * generate {@link NLPResult}.
	 * 
	 * @return The {@link NLPLanguage} that visits the grammar and follows its rules
	 *         to generate {@link NLPResult}.
	 */
	public NLPLanguage getLanguage() {
		return this.language;
	}

	@Override
	public String toString() {
		return String.format("Grammar_%s", this.language.toString());
	}

	private static Sentence createMicrostepAddFieldSentence(
			GraphNodeMicrostepAddField microstepAddFieldNode,
			Map<String, GraphNode> references) {
		final var microstepSentence = new Sentence();
		final var fieldNode =
				microstepAddFieldNode.getAttributeNode()
						.orElseThrow(() -> new GraphNodeMicrostepAttributeMissingException(microstepAddFieldNode));

		final var fieldNoun =
				new ReferenceNoun<GraphNodeAttribute>(fieldNode, node -> String.format("{%s}", node.getId()));
		final var microstepNounPhrase = new NounPhrase(fieldNoun);
		microstepSentence.setNounPhrase(microstepNounPhrase);
		final var addVerb =
				LexicalVerb.fromToken(Tokens.Verbs.Lexical.ADD)
						.orElseThrow(
								() -> new NLPTokenNotFoundException(
										Tokens.Verbs.Lexical.ADD,
										Tokens.Verbs.Lexical.class));
		final var microstepVerbPhrase = new VerbPhrase(addVerb);
		microstepVerbPhrase.setConjugation(
				new VerbConjugationKey(
						GrammaticalPerson.THIRD,
						GrammaticalNumber.SINGULAR,
						VerbAspect.IMPERFECTIVE,
						VerbModality.INDICATIVE,
						VerbTense.PRESENT,
						VerbVoice.PASSIVE,
						GrammaticalRegister.PLAIN));
		microstepSentence.setVerbPhrase(microstepVerbPhrase);
		final var toPreposition =
				Preposition.fromToken(Tokens.Prepositions.TO_TARGET_RECIPIENT)
						.orElseThrow(
								() -> new NLPTokenNotFoundException(
										Tokens.Prepositions.TO_TARGET_RECIPIENT,
										Tokens.Prepositions.class));

		final var microstepPrepositionalNounPhrase = fieldNode.getClassNode().map(cn -> {
			final var microstepClassReferenceNoun = new ReferenceNoun<GraphNodeClass>(cn, n -> n.getId().toString());
			return new NounPhrase(microstepClassReferenceNoun);
		}).orElse(new NounPhrase(CommonNoun.fromToken(Tokens.Nouns.Common.CLASS_OO_PROGRAMMING).get()));
		final var prepositionalPhrase = new PrepositionalPhrase(toPreposition, microstepPrepositionalNounPhrase);
		microstepVerbPhrase.setPrepositionalPhrase(prepositionalPhrase);

		return microstepSentence;
	}

	private static Sentence createMicrostepAddMethodSentence(
			GraphNodeMicrostepAddMethod microstepAddMethodNode,
			Map<String, GraphNode> references) {
		final var microstepSentence = new Sentence();
		final var operationNode =
				microstepAddMethodNode.getOperationNode()
						.orElseThrow(() -> new GraphNodeMicrostepOperationMissingException(microstepAddMethodNode));
		final var operationNodeReferenceString = String.format("{%s}", operationNode.getId());
		references.putIfAbsent(operationNodeReferenceString, operationNode);

		final var methodNoun = new ReferenceNoun<GraphNodeOperation>(operationNode, _ -> operationNodeReferenceString);
		final var microstepNounPhrase = new NounPhrase(methodNoun);
		microstepSentence.setNounPhrase(microstepNounPhrase);
		final var addVerb =
				LexicalVerb.fromToken(Tokens.Verbs.Lexical.ADD)
						.orElseThrow(
								() -> new NLPTokenNotFoundException(
										Tokens.Verbs.Lexical.ADD,
										Tokens.Verbs.Lexical.class));
		final var microstepVerbPhrase = new VerbPhrase(addVerb);
		microstepVerbPhrase.setConjugation(
				new VerbConjugationKey(
						GrammaticalPerson.THIRD,
						GrammaticalNumber.SINGULAR,
						VerbAspect.IMPERFECTIVE,
						VerbModality.INDICATIVE,
						VerbTense.PRESENT,
						VerbVoice.PASSIVE,
						GrammaticalRegister.PLAIN));
		microstepSentence.setVerbPhrase(microstepVerbPhrase);
		final var toPreposition =
				Preposition.fromToken(Tokens.Prepositions.TO_TARGET_RECIPIENT)
						.orElseThrow(
								() -> new NLPTokenNotFoundException(
										Tokens.Prepositions.TO_TARGET_RECIPIENT,
										Tokens.Prepositions.class));

		final var microstepPrepositionalNounPhrase = operationNode.getClassNode().map(cn -> {
			final var microstepClassReferenceNoun = new ReferenceNoun<GraphNodeClass>(cn, n -> n.getId().toString());
			final var classReferenceString = String.format("{%s}", cn.getId());
			references.putIfAbsent(classReferenceString, cn);
			return new NounPhrase(microstepClassReferenceNoun);
		}).orElse(new NounPhrase(CommonNoun.fromToken(Tokens.Nouns.Common.CLASS_OO_PROGRAMMING).get()));
		final var prepositionalPhrase = new PrepositionalPhrase(toPreposition, microstepPrepositionalNounPhrase);
		microstepVerbPhrase.setPrepositionalPhrase(prepositionalPhrase);

		return microstepSentence;
	}

	private static Sentence createMicrostepRemoveFieldSentence(
			GraphNodeMicrostepRemoveField microstepRemoveFieldNode,
			Map<String, GraphNode> references) {
		final var microstepSentence = new Sentence();
		final var fieldNode =
				microstepRemoveFieldNode.getAttributeNode()
						.orElseThrow(() -> new GraphNodeMicrostepAttributeMissingException(microstepRemoveFieldNode));

		final var fieldNoun =
				new ReferenceNoun<GraphNodeAttribute>(fieldNode, node -> String.format("{%s}", node.getId()));
		final var microstepNounPhrase = new NounPhrase(fieldNoun);
		microstepSentence.setNounPhrase(microstepNounPhrase);
		final var removeVerb =
				LexicalVerb.fromToken(Tokens.Verbs.Lexical.REMOVE)
						.orElseThrow(
								() -> new NLPTokenNotFoundException(
										Tokens.Verbs.Lexical.REMOVE,
										Tokens.Verbs.Lexical.class));
		final var microstepVerbPhrase = new VerbPhrase(removeVerb);
		microstepVerbPhrase.setConjugation(
				new VerbConjugationKey(
						GrammaticalPerson.THIRD,
						GrammaticalNumber.SINGULAR,
						VerbAspect.IMPERFECTIVE,
						VerbModality.INDICATIVE,
						VerbTense.PRESENT,
						VerbVoice.PASSIVE,
						GrammaticalRegister.PLAIN));
		microstepSentence.setVerbPhrase(microstepVerbPhrase);
		final var fromPreposition =
				Preposition.fromToken(Tokens.Prepositions.FROM_REMOVAL_SEPARATION)
						.orElseThrow(
								() -> new NLPTokenNotFoundException(
										Tokens.Prepositions.FROM_REMOVAL_SEPARATION,
										Tokens.Prepositions.class));

		final var microstepPrepositionalNounPhrase = fieldNode.getClassNode().map(cn -> {
			final var microstepClassReferenceNoun = new ReferenceNoun<GraphNodeClass>(cn, n -> n.getId().toString());
			return new NounPhrase(microstepClassReferenceNoun);
		}).orElse(new NounPhrase(CommonNoun.fromToken(Tokens.Nouns.Common.CLASS_OO_PROGRAMMING).get()));
		final var prepositionalPhrase = new PrepositionalPhrase(fromPreposition, microstepPrepositionalNounPhrase);
		microstepVerbPhrase.setPrepositionalPhrase(prepositionalPhrase);

		return microstepSentence;
	}

	private static Sentence createMicrostepRemoveMethodSentence(
			GraphNodeMicrostepRemoveMethod microstepRemoveMethodNode,
			Map<String, GraphNode> references) {
		final var microstepSentence = new Sentence();
		final var operationNode = microstepRemoveMethodNode.getOperationNode();
		final var operationNodeReferenceString = String.format("{%s}", operationNode.getId());
		references.putIfAbsent(operationNodeReferenceString, operationNode);

		final var methodNoun = new ReferenceNoun<GraphNodeOperation>(operationNode, _ -> operationNodeReferenceString);
		final var microstepNounPhrase = new NounPhrase(methodNoun);
		microstepSentence.setNounPhrase(microstepNounPhrase);
		final var removeVerb =
				LexicalVerb.fromToken(Tokens.Verbs.Lexical.REMOVE)
						.orElseThrow(
								() -> new NLPTokenNotFoundException(
										Tokens.Verbs.Lexical.REMOVE,
										Tokens.Verbs.Lexical.class));
		final var microstepVerbPhrase = new VerbPhrase(removeVerb);
		microstepVerbPhrase.setConjugation(
				new VerbConjugationKey(
						GrammaticalPerson.THIRD,
						GrammaticalNumber.SINGULAR,
						VerbAspect.IMPERFECTIVE,
						VerbModality.INDICATIVE,
						VerbTense.PRESENT,
						VerbVoice.PASSIVE,
						GrammaticalRegister.PLAIN));
		microstepSentence.setVerbPhrase(microstepVerbPhrase);
		final var fromPreposition =
				Preposition.fromToken(Tokens.Prepositions.FROM_REMOVAL_SEPARATION)
						.orElseThrow(
								() -> new NLPTokenNotFoundException(
										Tokens.Prepositions.FROM_REMOVAL_SEPARATION,
										Tokens.Prepositions.class));

		final var microstepPrepositionalNounPhrase = operationNode.getClassNode().map(cn -> {
			final var microstepClassReferenceNoun = new ReferenceNoun<GraphNodeClass>(cn, n -> n.getId().toString());
			final var classReferenceString = String.format("{%s}", cn.getId());
			references.putIfAbsent(classReferenceString, cn);
			return new NounPhrase(microstepClassReferenceNoun);
		}).orElse(new NounPhrase(CommonNoun.fromToken(Tokens.Nouns.Common.CLASS_OO_PROGRAMMING).get()));
		final var prepositionalPhrase = new PrepositionalPhrase(fromPreposition, microstepPrepositionalNounPhrase);
		microstepVerbPhrase.setPrepositionalPhrase(prepositionalPhrase);

		return microstepSentence;
	}

	private Sentence createRiskDoubleDefinitionSentence(
			GraphNodeRiskDoubleDefinition riskDoubleDefinitionNode,
			HashMap<String, GraphNode> references) {
		final var dangerSentence = new Sentence();

		final var affectedNodes = riskDoubleDefinitionNode.getAffected();

		final var hasVerb =
				LexicalVerb.fromToken(Verbs.Lexical.HAVE)
						.orElseThrow(
								() -> new NLPTokenNotFoundException(Verbs.Lexical.HAVE, Tokens.Verbs.Lexical.class));
		final var dangerVerbPhrase = new VerbPhrase(hasVerb);
		dangerVerbPhrase.setConjugation(
				new VerbConjugationKey(
						GrammaticalPerson.THIRD,
						GrammaticalNumber.PLURAL,
						VerbAspect.IMPERFECTIVE,
						VerbModality.INDICATIVE,
						VerbTense.PRESENT,
						VerbVoice.ACTIVE,
						GrammaticalRegister.PLAIN));

		return dangerSentence;
	}
}