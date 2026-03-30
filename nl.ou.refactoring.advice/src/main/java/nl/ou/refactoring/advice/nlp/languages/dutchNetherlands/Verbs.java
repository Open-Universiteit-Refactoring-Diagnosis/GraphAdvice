package nl.ou.refactoring.advice.nlp.languages.dutchNetherlands;

import java.util.Set;

import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbAspect;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationLookupTreeNode;

class Verbs {
	private Verbs() { }

	static VerbConjugationLookupTreeNode<Void, VerbAspect> conjugationDefaultTree() {
		final var rootNode = new VerbConjugationLookupTreeNode<Void, VerbAspect>(Set.of(), null);
		
		
		
		return rootNode;
	}
}
