package nl.ou.refactoring.advice.eclipse.refactorings.methods;

import java.util.ArrayList;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.eclipse.refactorings.GraphChangedEvent;
import nl.ou.refactoring.advice.eclipse.refactorings.RefactoringInputsComposite;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;

/**
 * A composite for inputs of a Rename Method refactoring.
 */
public final class RenameMethodInputsComposite extends RefactoringInputsComposite {
	private Label newNameLabelWidget;
	private Text newNameTextWidget;
	
	/**
	 * Initialises a new instance of {@link RenameMethodInputsComposite}.
	 * @param parent The parent composite.
	 * @param selectedMethod The selected method for renaming.
	 * @param graph The Refactoring Advice Graph.
	 * @throws IllegalArgumentException Thrown if parent or graph is null.
	 */
	public RenameMethodInputsComposite(
		Composite parent,
		IMethod selectedMethod,
		Graph graph
	) throws IllegalArgumentException {
		super(parent, graph);
		
		final var gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 5;
		this.setLayout(gridLayout);
		
		this.newNameLabelWidget = new Label(this, SWT.NONE);
		this.newNameLabelWidget.setText("New method name:");
		
		this.newNameTextWidget = new Text(this, SWT.BORDER);
		this.newNameTextWidget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		this.newNameTextWidget.addModifyListener(_ -> {
			final var newName = this.newNameTextWidget.getText();
			final var graphClone = graph.clone(graph.getRefactoringName());
			
			// Get parent Class.
			final var className = selectedMethod.getDeclaringType().getFullyQualifiedName();
			final var classNode =
				graphClone
					.getNode(className, GraphNodeClass.class)
					.get();
			
			// Create new Method.
			final var operationRenamedNode =
				new GraphNodeOperation(
					graphClone,
					new GraphNodeIdentifier(graphClone, newName),
					new ArrayList<>()
				);
			classNode.has(operationRenamedNode);
			
			// Get relevant microsteps.
			final var addMethodNode =
				graphClone
					.getNodes(GraphNodeMicrostepAddMethod.class)
					.stream()
					.findAny()
					.get();
			addMethodNode.adds(operationRenamedNode);
			// TODO also do remove node.
			
			// Emulate double definition
			final var doubleDefinitionRisk = new GraphNodeRiskDoubleDefinition(graphClone);
			doubleDefinitionRisk.affects(operationRenamedNode);
			addMethodNode.causes(doubleDefinitionRisk);
			
			this.onGraphChanged(new GraphChangedEvent(graphClone));
		});
	}
}