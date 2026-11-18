package nl.ou.refactoring.advice.eclipse.refactorings.methods;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.eclipse.refactorings.GraphChangedEvent;
import nl.ou.refactoring.advice.eclipse.refactorings.RefactoringInputsComposite;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;

/**
 * A composite for inputs of a Push Down Method refactoring.
 */
public final class PushDownMethodInputsComposite extends RefactoringInputsComposite {
	private static final Logger LOGGER = LogManager.getLogger(PushDownMethodInputsComposite.class);
	private final Label selectedMethodNameLabelWidget;
	private final Label selectedMethodNameValueLabelWidget;
	private final Label selectedMethodDeclaringClassNameLabelWidget;
	private final Label selectedMethodDeclaringClassValueLabelWidget;
	private final Label targetClassLabelWidget;
	private final Combo targetClassComboWidget;

	/**
	 * Initialises a new instance of {@link PushDownMethodInputsComposite}.
	 *
	 * @param parent         The parent composite.
	 * @param selectedMethod The selected method for pushing down.
	 * @param graph          The Refactoring Advice Graph.
	 * @throws IllegalArgumentException Thrown if parent or graph is null.
	 */
	public PushDownMethodInputsComposite(Composite parent, IMethod selectedMethod, Graph graph)
			throws IllegalArgumentException {
		super(parent, graph);

		final var selectedMethodName = selectedMethod.getElementName();
		final var selectedMethodDeclaringClass = selectedMethod.getDeclaringType();
		final var selectedMethodDeclaringClassFQName = selectedMethodDeclaringClass.getFullyQualifiedName();

		final var gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 5;
		this.setLayout(gridLayout);

		// Selected Method
		this.selectedMethodNameLabelWidget = new Label(this, SWT.NONE);
		this.selectedMethodNameLabelWidget.setText("Method to push down:");

		this.selectedMethodNameValueLabelWidget = new Label(this, SWT.NONE);
		this.selectedMethodNameValueLabelWidget.setText(selectedMethodName);

		// Source Class
		this.selectedMethodDeclaringClassNameLabelWidget = new Label(this, SWT.NONE);
		this.selectedMethodDeclaringClassNameLabelWidget.setText("Origin Class:");

		this.selectedMethodDeclaringClassValueLabelWidget = new Label(this, SWT.NONE);
		this.selectedMethodDeclaringClassValueLabelWidget.setText(selectedMethodDeclaringClassFQName);

		// Target Class
		this.targetClassLabelWidget = new Label(this, SWT.NONE);
		this.targetClassLabelWidget.setText("Class to push method down to:");

		this.targetClassComboWidget = new Combo(this, SWT.NONE);
		try {
			LOGGER.debug(
					"Finding implementing classes of class '{}' that declares method '{}'.",
					selectedMethodDeclaringClassFQName,
					selectedMethodName);
			final var descendingClasses =
					getAllDescendantsOfMethodDeclaringClass(selectedMethod, new NullProgressMonitor());
			for (final var descendingClass : descendingClasses) {
				this.targetClassComboWidget.add(descendingClass.getFullyQualifiedName());
			}
			// runnable will run in the wrong thread, it would need to be performed after an
			// appropriate event.
			// PlatformUI.getWorkbench().getProgressService().busyCursorWhile(runnable);
			/*
			 * If this takes a very long time use this
			 *
			 * final var shell =
			 * PlatformUI.getWorkbench().getModalDialogShellProvider().getShell(); final var
			 * progressMonitorDialog = new ProgressMonitorDialog(shell);
			 * progressMonitorDialog.run(true, true, runnable);
			 */
		} catch (Exception ex) {
			LOGGER.error("Failed to populate target class combo", ex);
			ex.printStackTrace();
		}
		this.targetClassComboWidget.addModifyListener((_) -> {
			try {
				final var targetClassFQName =
						this.targetClassComboWidget.getItem(this.targetClassComboWidget.getSelectionIndex());
				final var targetClass = selectedMethod.getJavaProject().findType(targetClassFQName);
				if (targetClass == null) {
					LOGGER.warn("Could not resolve class '{}' from project", targetClassFQName);
				}
				this.updateGraph(selectedMethod, targetClass, graph);
			} catch (Exception ex) {
				LOGGER.error(
						"Failed to update Refactoring Advice Graph after selecting a new target class to push method down to.",
						ex);
				ex.printStackTrace();
			}
		});
	}

	private static IType[] getAllDescendantsOfMethodDeclaringClass(IMethod selectedMethod, IProgressMonitor monitor) {
		final var result = new ArrayList<IType>();
		try {
			LOGGER.debug(
					"Finding descendants of declaring class '{}' of method '{}' to push down",
					selectedMethod.getDeclaringType().getFullyQualifiedName(),
					selectedMethod.getElementName());
			final var declaringClass = selectedMethod.getDeclaringType();
			final var hierarchy = declaringClass.newTypeHierarchy(monitor);
			final var allSubtypes = hierarchy.getAllSubtypes(declaringClass);
			for (final var subtype : allSubtypes) {
				result.add(subtype);
			}
		} catch (Exception ex) {
			LOGGER.error("Failed to scan all descendants of declaring class", ex);
			ex.printStackTrace();
		}
		return result.toArray(new IType[] {});
	}

	private void updateGraph(IMethod selectedMethod, IType targetClass, Graph graph) {
		try {
			final var graphClone = graph.clone(graph.getRefactoringName());

			// Get declaring Class
			final var declaringClass = selectedMethod.getDeclaringType();
			final var declaringClassFQName = declaringClass.getFullyQualifiedName();
			final var declaringClassNode =
					graphClone.getNode(declaringClassFQName, GraphNodeClass.class)
							.orElseThrow(
									() -> new RuntimeException(
											"Could not find class node '" + declaringClassFQName + "'"));

			// Get selected Method
			final var selectedMethodName = selectedMethod.getElementName();
			final var selectedOperationNode =
					declaringClassNode.getOperationNodes()
							.stream()
							.filter((on) -> on.getOperationName().equals(selectedMethodName))
							.findAny()
							.orElseThrow(
									() -> new RuntimeException(
											"Could not find method '" + selectedMethodName + "' associated with class '"
													+ declaringClassFQName + "'"));

			// Get target Class
			final var targetClassFQName = targetClass.getFullyQualifiedName();
			final var targetClassNode =
					graphClone.getNode(targetClassFQName, GraphNodeClass.class)
							.orElseThrow(
									() -> new RuntimeException(
											"Could not find class node '" + targetClassFQName + "'"));

			// Emit Graph Changed Event
			this.onGraphChanged(new GraphChangedEvent(graphClone));
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error("Failed to update Refactoring Advice Graph", ex);
		}
	}
}