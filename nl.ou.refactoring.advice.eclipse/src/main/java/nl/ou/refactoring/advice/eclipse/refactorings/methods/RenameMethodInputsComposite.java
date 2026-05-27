package nl.ou.refactoring.advice.eclipse.refactorings.methods;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentPatternException;
import nl.ou.refactoring.advice.eclipse.TypeSignatureResolver;
import nl.ou.refactoring.advice.eclipse.refactorings.GraphChangedEvent;
import nl.ou.refactoring.advice.eclipse.refactorings.RefactoringInputsComposite;
import nl.ou.refactoring.advice.nodes.code.GraphNodeType;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperationParameter;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperationParameterSignature;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.validation.GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidator;
import nl.ou.refactoring.advice.validation.GraphValidationEngine;
import nl.ou.refactoring.advice.validation.GraphValidationFixableResult;

/**
 * A composite for inputs of a Rename Method refactoring.
 */
public final class RenameMethodInputsComposite extends RefactoringInputsComposite {
	private static final Logger LOGGER = LogManager.getLogger(RenameMethodInputsComposite.class);
	private Label originalNameLabelWidget;
	private Label originalNameValueLabelWidget;
	private Label newNameLabelWidget;
	private Text newNameTextWidget;

	/**
	 * Initialises a new instance of {@link RenameMethodInputsComposite}.
	 * 
	 * @param parent         The parent composite.
	 * @param selectedMethod The selected method for renaming.
	 * @param graph          The Refactoring Advice Graph.
	 * @throws IllegalArgumentException Thrown if parent or graph is null.
	 */
	public RenameMethodInputsComposite(Composite parent, IMethod selectedMethod, Graph graph)
			throws IllegalArgumentException {
		super(parent, graph);

		final var gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 5;
		this.setLayout(gridLayout);

		this.originalNameLabelWidget = new Label(this, SWT.NONE);
		this.originalNameLabelWidget.setText("Original method name:");

		this.originalNameValueLabelWidget = new Label(this, SWT.NONE);
		this.originalNameValueLabelWidget.setText(selectedMethod.getElementName());

		this.newNameLabelWidget = new Label(this, SWT.NONE);
		this.newNameLabelWidget.setText("New method name:");

		this.newNameTextWidget = new Text(this, SWT.BORDER);
		this.newNameTextWidget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		this.newNameTextWidget.setText(selectedMethod.getElementName());
		this.newNameTextWidget.addModifyListener(_ -> {
			try {
				final var newName = this.newNameTextWidget.getText();
				final var graphClone = graph.clone(graph.getRefactoringName());
				
				GraphNodeIdentifier newNameIdentifier;
				try {
					newNameIdentifier = new GraphNodeIdentifier(graphClone, newName);
				} catch (ArgumentPatternException patternException) {
					this.newNameTextWidget.setBackground(new Color(255, 123, 123));
					return;
				}
				this.newNameTextWidget.setBackground(new Color(255, 255, 255));

				// Get parent Class.
				final var className = selectedMethod.getDeclaringType().getFullyQualifiedName();
				final var classNode = graphClone.getNode(className, GraphNodeClass.class)
						.orElseThrow(() -> new RuntimeException("Could not find class node '" + className + "'"));

				// Find OperationNode that matches Selected Method.
				final var operationParameterSignatures = new ArrayList<GraphNodeOperationParameterSignature>();
				final var methodParameters = selectedMethod.getParameters();
				for (final var methodParameter : methodParameters) {
					operationParameterSignatures.add(new GraphNodeOperationParameterSignature(
							methodParameter.getElementName(), TypeSignatureResolver.resolveName(methodParameter)));
				}
				final var operationNodeSelected = classNode
						.getOperationNode(selectedMethod.getElementName(), operationParameterSignatures).get();

				// Create new Method.
				final var operationNodeSelectedReturnTypeOptional = operationNodeSelected.getReturnType();
				final var operationNodeRenamedParameters = operationNodeSelected.getOperationParameters().stream()
						.map((node) -> (GraphNodeOperationParameter) node.clone(graphClone))
						.collect(Collectors.toUnmodifiableList());
				final var operationNodeRenamed = new GraphNodeOperation(graphClone, newNameIdentifier,
						operationNodeRenamedParameters);
				if (operationNodeSelectedReturnTypeOptional.isPresent()) {
					operationNodeRenamed.hasReturnType(
							(GraphNodeType) operationNodeSelectedReturnTypeOptional.get().clone(graphClone));
				}
				classNode.has(operationNodeRenamed);

				// Get relevant Microsteps.
				final var addMethodNode = graphClone.getNodes(GraphNodeMicrostepAddMethod.class).stream().findAny()
						.get();
				addMethodNode.adds(operationNodeRenamed);
				final var removeMethodNode = graphClone.getNodes(GraphNodeMicrostepRemoveMethod.class).stream()
						.findAny().get();
				removeMethodNode.removes(operationNodeSelected);

				// Validate and fix risk.
				final var validationEngine = new GraphValidationEngine();
				validationEngine.addValidator(GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidator.INSTANCE);
				final var validationResults = validationEngine.validate(graphClone);
				for (final var validationResult : validationResults) {
					if (!validationResult.getIsValid() && validationResult instanceof GraphValidationFixableResult) {
						((GraphValidationFixableResult) validationResult).fix(false); // Graph is already a clone.
					}
				}

				// Emit Graph Changed event.
				this.onGraphChanged(new GraphChangedEvent(graphClone));
			} catch (Exception ex) {
				ex.printStackTrace();
				LOGGER.error("Failed to update Refactoring Advice Graph", ex);
			}
		});
	}
}