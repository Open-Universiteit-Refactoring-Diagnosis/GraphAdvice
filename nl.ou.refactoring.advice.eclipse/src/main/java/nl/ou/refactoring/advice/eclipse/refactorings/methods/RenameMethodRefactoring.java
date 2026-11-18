package nl.ou.refactoring.advice.eclipse.refactorings.methods;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.swt.widgets.Composite;

import nl.ou.refactoring.advice.GraphTemplates;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.eclipse.refactorings.Refactoring;
import nl.ou.refactoring.advice.eclipse.refactorings.RefactoringInputsComposite;
import nl.ou.refactoring.advice.io.GraphReaderException;

/**
 * Represents a Rename Method refactoring.
 */
public final class RenameMethodRefactoring extends Refactoring {
	/**
	 * Initialises a new instance of {@link RenameMethodRefactoring}.
	 * @param selectedMethod The method to rename.
	 * @throws ArgumentNullException Thrown if method is null.
	 * @throws GraphReaderException Thrown if the refactoring template could not be loaded.
	 */
	public RenameMethodRefactoring(IMethod selectedMethod)
			throws
				ArgumentNullException,
				GraphReaderException {
		super(GraphTemplates.renameMethod(), selectedMethod);
	}

	@Override
	public RefactoringInputsComposite createComposite(Composite parent)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(parent, "parent");
		return new RenameMethodInputsComposite(parent, (IMethod)this.getSelectedElement(), this.getGraph());
	}
}