package nl.ou.refactoring.advice.nlp.grammar.nouns;

/**
 * The categories of a noun in a Natural Language Grammar.
 * @param lexicalCategory The lexical category of the Noun.
 * @param semanticClassification The semantic classification of the Noun.
 * @param countability The countability of the Noun.
 */
public final record NounCategories
(
	LexicalCategory lexicalCategory,
	SemanticClassification semanticClassification,
	Countability countability
) { }