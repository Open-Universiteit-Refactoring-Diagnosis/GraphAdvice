package nl.ou.refactoring.advice.nlp.grammar.verbs;

import java.util.function.BiFunction;

/**
 * A function that produces the conjugation of a {@link Verb} in a {@link VerbConjugationLookupTree}.
 */
@FunctionalInterface
public interface VerbConjugationProducer extends BiFunction<String, VerbConjugationKey, String> {
}