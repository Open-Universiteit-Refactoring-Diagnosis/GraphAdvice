package nl.ou.refactoring.advice.eclipse.refactorings.methods;

import java.io.FileNotFoundException;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Composite;

import nl.ou.refactoring.advice.GraphTemplates;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.eclipse.ElementResourceLocationNotFoundException;
import nl.ou.refactoring.advice.eclipse.ElementResourceNotFoundException;
import nl.ou.refactoring.advice.eclipse.refactorings.Refactoring;
import nl.ou.refactoring.advice.eclipse.refactorings.RefactoringInputsComposite;
import nl.ou.refactoring.advice.io.GraphReaderException;

/**
 * Represents a Rename Method refactoring.
 */
public final class RenameMethodRefactoring extends Refactoring {
	/**
	 * Initialises a new instance of {@link RenameMethodRefactoring}.
	 * @param method The method to rename.
	 * @throws ArgumentNullException Thrown if method is null.
	 * @throws GraphReaderException Thrown if the method code file could not be read to the graph.
	 * @throws JavaModelException Thrown if the code file that contains the method code is invalid.
	 * @throws FileNotFoundException Thrown if the code file that contains the method code could not be found.
	 * @throws ElementResourceLocationNotFoundException Thrown if the method resource location could not be found.
	 * @throws ElementResourceNotFoundException Thrown if the method resource could not be found.
	 */
	public RenameMethodRefactoring(IMethod method)
			throws
				ArgumentNullException,
				ElementResourceNotFoundException,
				ElementResourceLocationNotFoundException,
				FileNotFoundException,
				JavaModelException,
				GraphReaderException {
		super(GraphTemplates.renameMethod(), method);
	}

	@Override
	public RefactoringInputsComposite createComposite(Composite parent)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(parent, "parent");
		return new RenameMethodInputsComposite(parent, (IMethod)this.getSelectedElement(), this.getGraph());
	}
}