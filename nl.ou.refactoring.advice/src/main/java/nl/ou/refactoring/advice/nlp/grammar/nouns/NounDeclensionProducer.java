package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.function.BiFunction;

/**
 * A function that produces the declension of a {@link Noun} in a {@link NounDeclensionLookupTree}.
 */
@FunctionalInterface
public interface NounDeclensionProducer extends BiFunction<String, NounDeclensionKey, String> {
}