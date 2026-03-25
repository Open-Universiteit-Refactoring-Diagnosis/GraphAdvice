package nl.ou.refactoring.advice.nlp.grammar.determiners;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;

public class PronounPossessiveLookupTreeTestsArgumentsProvider implements ArgumentsProvider {
	@Override
	public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context)
			throws Exception {
		final var argumentsList = new ArrayList<Arguments>();
		argumentsList.add(
			Arguments.of(
				GrammaticalPerson.FIRST,
				GrammaticalGender.FEMININE,
				GrammaticalNumber.SINGULAR,
				GrammaticalGender.FEMININE,
				GrammaticalNumber.SINGULAR,
				GrammaticalRegister.CASUAL,
				"mijn"
			)
		);
		argumentsList.add(
			Arguments.of(
				GrammaticalPerson.FIRST,
				GrammaticalGender.MASCULINE,
				GrammaticalNumber.PLURAL,
				GrammaticalGender.NEUTER,
				GrammaticalNumber.SINGULAR,
				GrammaticalRegister.POLITE,
				"ons"
			)
		);
		argumentsList.add(
			Arguments.of(
				GrammaticalPerson.FIRST,
				GrammaticalGender.FEMININE,
				GrammaticalNumber.PLURAL,
				GrammaticalGender.NEUTER,
				GrammaticalNumber.PLURAL,
				GrammaticalRegister.PLAIN,
				"onze"
			)
		);
		argumentsList.add(
			Arguments.of(
				GrammaticalPerson.FIRST,
				GrammaticalGender.MASCULINE,
				GrammaticalNumber.PLURAL,
				GrammaticalGender.FEMININE,
				GrammaticalNumber.PLURAL,
				GrammaticalRegister.DEFERENTIAL,
				"onze"
			)
		);
		argumentsList.add(
			Arguments.of(
				GrammaticalPerson.SECOND,
				GrammaticalGender.MASCULINE,
				GrammaticalNumber.SINGULAR,
				GrammaticalGender.FEMININE,
				GrammaticalNumber.PLURAL,
				GrammaticalRegister.CASUAL,
				"jouw"
			)
		);
		return argumentsList.stream();
	}
}