package nl.ou.refactoring.advice.eclipse.refactorings;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;

/**
 * A composite that displays refactoring inputs.
 */
public abstract class RefactoringInputsComposite extends Composite {
	private final Set<GraphChangedListener> listeners = new HashSet<>();
	private final Graph graph;

	/**
	 * Initialises a new instance of {@link RefactoringInputsComposite}.
	 * @param parent The parent composite.
	 * @param graph The Refactoring Advice Graph.
	 * @throws IllegalArgumentException Thrown if parent or graph is null.
	 */
	protected RefactoringInputsComposite(Composite parent, Graph graph) throws IllegalArgumentException {
		super(parent, SWT.NONE);
		ArgumentGuard.requireNotNull(graph, "graph");
		this.graph = graph;
	}
	
	/**
	 * Gets the Refactoring Advice Graph.
	 * @return The Refactoring Advice Graph.
	 */
	protected final Graph getGraph() {
		return this.graph;
	}

	/**
	 * Triggers a {@link GraphChangedEvent}.
	 * @param event The triggered event.
	 */
	protected void onGraphChanged(GraphChangedEvent event) {
		for (final var listener : this.listeners) {
			listener.onGraphChanged(event);
		}
	}
	
	/**
	 * Adds a listener for a change event if the Refactoring Advice Graph has changed.
	 * @param listener The listener to add.
	 */
	public final void addGraphChangedListener(GraphChangedListener listener) {
		this.listeners.add(listener);
	}
}