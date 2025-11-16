package nl.ou.refactoring.advice.io.json;

import java.util.Scanner;

public final class JsonSamplesLoader {
	private JsonSamplesLoader() { }

	protected static String loadJson(String resourceName) {
		final var jsonResourceStream =
				JsonSamplesLoader
					.class
					.getResourceAsStream(resourceName);
		final var jsonExpectedScanner =
				new Scanner(jsonResourceStream)
					.useDelimiter("\\A");
		final var jsonExpected =
				jsonExpectedScanner.hasNext()
					? jsonExpectedScanner.next()
					: "";
		jsonExpectedScanner.close();
		return jsonExpected;
	}
}
