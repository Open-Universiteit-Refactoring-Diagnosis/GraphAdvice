package nl.ou.refactoring.advice.eclipse.refactorings;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.swt.widgets.Composite;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Represents a refactoring in the Eclipse plug-in.
 */
public abstract class Refactoring {
	private final Graph graph;
	private final IJavaElement selectedElement;
	
	/**
	 * Initialises a new instance of {@link Refactoring}.
	 * @param graph The Refactoring Advice Graph that contains the microsteps of the refactoring.
	 * @param selectedElement The selected element.
	 * @throws ArgumentNullException Thrown if graph or selectedElement is null.
	 */
	protected Refactoring(Graph graph, IJavaElement selectedElement)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNull(selectedElement, "selectedElement");
		this.graph = graph;
		this.selectedElement = selectedElement;
	}

	/**
	 * Creates the composite with inputs for configuring the refactoring.
	 * @param parent The parent composite.
	 * @return The composite with inputs for configuring the refactoring.
	 * @throws ArgumentNullException Thrown if parent is null.
	 */
	public abstract RefactoringInputsComposite createComposite(Composite parent) throws ArgumentNullException;
	
	/**
	 * Gets the Refactoring Advice Graph that contains the Refactoring Advice.
	 * @return The Refactoring Advice Graph that contains the Refactoring Advice.
	 */
	public final Graph getGraph() {
		return this.graph;
	}
	
	/**
	 * Gets the selected element for the refactoring.
	 * @return The selected element for the refactoring.
	 */
	public final IJavaElement getSelectedElement() {
		return this.selectedElement;
	}
}