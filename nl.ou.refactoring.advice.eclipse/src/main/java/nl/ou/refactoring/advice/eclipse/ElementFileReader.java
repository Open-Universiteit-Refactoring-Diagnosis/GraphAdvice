package nl.ou.refactoring.advice.eclipse;

import java.io.FileNotFoundException;
import java.io.FileReader;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Reads the file of a Java Element in Eclipse.
 */
public final class ElementFileReader extends FileReader {
	/**
	 * Initialises a new instance of {@link ElementFileReader}.
	 * @param element The Java element of which to read the file.
	 * @throws ElementResourceNotFoundException Thrown if the element's resource could not be found.
	 * @throws ElementResourceLocationNotFoundException Thrown if the element's resource location could not be found.
	 * @throws FileNotFoundException Thrown if the element's file could not be found.
	 * @throws ArgumentNullException Thrown if elementFile is null.
	 */
	public ElementFileReader(final ElementFile elementFile)
			throws
				ElementResourceNotFoundException,
				ElementResourceLocationNotFoundException,
				FileNotFoundException,
				ArgumentNullException {
		super(elementFile);
		ArgumentGuard.requireNotNull(elementFile, "elementFile");
	}
}