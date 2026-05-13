package nl.ou.refactoring.advice.eclipse.refactorings;

/**
 * A listener for Refactoring Advice Graph change events.
 */
public interface GraphChangedListener {
	/**
	 * Triggered when a Refactoring Advice Graph has changed.
	 * @param event The event that has been triggered.
	 */
	void onGraphChanged(GraphChangedEvent event);
}