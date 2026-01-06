package nl.ou.refactoring.advice.nodes.code;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class GraphNodeClassStereotypeTests {
	private static final Logger LOGGER =
			LogManager.getLogger(GraphNodeClassStereotypeTests.class);
	private static final Locale[] SUPPORTED_LOCALES = {
		Locale.of("en", "GB"),
		Locale.of("nl", "NL")
	};

	@Test
	@DisplayName("toString should return a localised display name of the class stereotype")
	public void toStringTest() {
		final var stereotypeValues = GraphNodeClassStereotype.values();
		for (final var stereotypeValue : stereotypeValues) {
			for (final var locale : SUPPORTED_LOCALES) {
				Locale.setDefault(locale);
				final var displayName = stereotypeValue.toString();
				assertNotNull(displayName);
				LOGGER.info(
						"toString (stereotype: {}, locale: {}): {}",
						stereotypeValue.name(),
						locale.toLanguageTag(),
						displayName
				);
			}
		}
	}

}
