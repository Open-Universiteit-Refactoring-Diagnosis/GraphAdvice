package nl.ou.refactoring.advice.integrationTests;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphTemplates;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphReaderException;
import nl.ou.refactoring.advice.io.javaParser.GraphJavaReader;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.GraphNodeType;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeBlock;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeMethodInvocationExpression;
import nl.ou.refactoring.advice.nodes.code.operations.statements.GraphNodeExpressionStatement;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskMissingDefinition;
import nl.ou.refactoring.advice.resources.ResourceProvider;

public final class RefactoringTestsArgumentsProvider implements ArgumentsProvider {
	@Override
	public Stream<? extends Arguments> provideArguments
	(
		ParameterDeclarations parameters,
		ExtensionContext context
	) throws RefactoringMayContainOnlyOneStartNodeException, ArgumentNullException, GraphReaderException {
		return
			Stream.of(
				moveFieldFromClassToClassReferenceWithinSourceClass(),
				moveMethodFromClassToClassInvocationInSourceClass(),
				moveMethodFromClassToClassInvocationOutsideClass()
			)
			.map(Arguments::of);
	}
	
	private static Graph moveFieldFromClassToClassReferenceWithinSourceClass() throws GraphReaderException {
		/* Load template. */
		final var graph = GraphTemplates.moveField().clone("Move Field from Class to Class with reference from within the source Class");
		final var addFieldMicrostepNode =
				graph
					.getNodes(GraphNodeMicrostepAddField.class)
					.stream()
					.findAny()
					.get();
			final var removeFieldMicrostepNode =
				graph
					.getNodes(GraphNodeMicrostepRemoveField.class)
					.stream()
					.findAny()
					.get();
		
		/* Introduce code nodes and edges. */
		final var classLoader = RefactoringTestsArgumentsProvider.class.getClassLoader();
		final var alphaFileNameFull = "refactorings/moveField/Alpha.java";
		final var alphaFileName = "Alpha.java";
		final var alphaReader = ResourceProvider.getReader(classLoader, alphaFileNameFull);
		final var betaFileNameFull = "refactorings/moveField/Beta.java";
		final var betaFileName = "Beta.java";
		final var betaReader = ResourceProvider.getReader(classLoader, betaFileNameFull); 
		var graphJavaReader = new GraphJavaReader(graph, alphaReader, alphaFileNameFull, alphaFileName);
		graphJavaReader.read();
		graphJavaReader = new GraphJavaReader(graph, betaReader, betaFileNameFull, betaFileName);
		graphJavaReader.read();
		
		final var alphaClassNode =
			graph
				.getNode("nl.ou.refactorings.moveField.classToClass.Alpha", GraphNodeClass.class)
				.get();
		final var betaClassNode =
			graph
				.getNode("nl.ou.refactorings.moveField.classToClass.Beta", GraphNodeClass.class)
				.get();

		final var alphaTwoAttributeNode = alphaClassNode.getAttributeNode("two").get();
		final var alphaBarOperationNode = alphaClassNode.getOperationNode("bar", List.of()).get();
		removeFieldMicrostepNode.removes(alphaTwoAttributeNode);
		
		final var stringType =
			graph
				.getNodes(GraphNodeType.class)
				.stream()
				.filter(node -> node.getTypeName().equals("String"))
				.findAny()
				.get();
		final var betaTwoAttributeNode = new GraphNodeAttribute(graph, "two");
		betaTwoAttributeNode.is(stringType);
		betaClassNode.has(betaTwoAttributeNode);
		addFieldMicrostepNode.adds(betaTwoAttributeNode);
		
		final var missingDefinitionRiskNode = new GraphNodeRiskMissingDefinition(graph);
		removeFieldMicrostepNode.causes(missingDefinitionRiskNode);
		missingDefinitionRiskNode.affects(alphaBarOperationNode);
		missingDefinitionRiskNode.affects(alphaTwoAttributeNode);
		
		return graph;
	}
	
	private static Graph moveMethodFromClassToClassInvocationInSourceClass() throws GraphReaderException {
		/* Load template. */
		final var graph = GraphTemplates.moveMethod().clone("Move Method from Class to Class with source invocation");
		final var addMethodMicrostepNode =
			graph
				.getNodes(GraphNodeMicrostepAddMethod.class)
				.stream()
				.findAny()
				.get();
		final var removeMethodMicrostepNode =
			graph
				.getNodes(GraphNodeMicrostepRemoveMethod.class)
				.stream()
				.findAny()
				.get();
		
		/* Introduce code nodes and edges. */
		GraphNodePackage.parse(graph, "nl.ou.refactoring.moveMethod.classToClass");
		final var packageNodeLeaf =
			graph
				.getNode("nl.ou.refactoring.moveMethod.classToClass", GraphNodePackage.class)
				.get();
		
		final var alphaIdentifier = new GraphNodeIdentifier(graph, "Alpha");
		final var alphaClassNode = new GraphNodeClass(graph, alphaIdentifier);
		final var alphaOneAttributeNode = new GraphNodeAttribute(graph, "one");
		final var alphaTwoAttributeNode = new GraphNodeAttribute(graph, "two");
		final var barIdentifier = new GraphNodeIdentifier(graph, "bar");
		final var alphaBarOperationNode = new GraphNodeOperation(graph, barIdentifier);
		final var fooIdentifier = new GraphNodeIdentifier(graph, "foo");
		final var alphaFooOperationNode = new GraphNodeOperation(graph, fooIdentifier);
		packageNodeLeaf.has(alphaClassNode);
		alphaClassNode.has(alphaOneAttributeNode);
		alphaClassNode.has(alphaTwoAttributeNode);
		alphaClassNode.has(alphaBarOperationNode);
		alphaClassNode.has(alphaFooOperationNode);
		removeMethodMicrostepNode.removes(alphaFooOperationNode);
		final var alphaBarBlock = new GraphNodeBlock(graph);
		final var alphaBarExpression = new GraphNodeMethodInvocationExpression(graph);
		final var alphaBarStatement = new GraphNodeExpressionStatement(graph, alphaBarExpression);
		alphaBarOperationNode.hasBody(alphaBarBlock);
		alphaBarBlock.has(alphaBarStatement);
		alphaBarExpression.invokes(alphaFooOperationNode);
		
		final var betaIdentifier = new GraphNodeIdentifier(graph, "Beta");
		final var betaClassNode = new GraphNodeClass(graph, betaIdentifier);
		final var betaField1AttributeNode = new GraphNodeAttribute(graph, "field1");
		final var betaField2AttributeNode = new GraphNodeAttribute(graph, "field2");
		final var betaFooOperationNode = new GraphNodeOperation(graph, fooIdentifier);
		packageNodeLeaf.has(betaClassNode);
		betaClassNode.has(betaField1AttributeNode);
		betaClassNode.has(betaField2AttributeNode);
		betaClassNode.has(betaFooOperationNode);
		addMethodMicrostepNode.adds(betaFooOperationNode);
		
		final var missingDefinitionRiskNode = new GraphNodeRiskMissingDefinition(graph);
		removeMethodMicrostepNode.causes(missingDefinitionRiskNode);
		missingDefinitionRiskNode.affects(alphaBarOperationNode);
		missingDefinitionRiskNode.affects(alphaFooOperationNode);
		
		return graph;
	}
	
	private static Graph moveMethodFromClassToClassInvocationOutsideClass() throws GraphReaderException {
		/* Load template. */
		final var graph = GraphTemplates.moveMethod().clone("Move Method from Class to Class with exterior invocation");
		final var addMethodMicrostepNode =
			graph
				.getNodes(GraphNodeMicrostepAddMethod.class)
				.stream()
				.findAny()
				.get();
		final var removeMethodMicrostepNode =
			graph
				.getNodes(GraphNodeMicrostepRemoveMethod.class)
				.stream()
				.findAny()
				.get();
		
		/* Introduce code nodes and edges. */
		GraphNodePackage.parse(graph, "nl.ou.refactoring.moveMethod.classToClass");
		final var packageNodeLeaf =
			graph
				.getNode("nl.ou.refactoring.moveMethod.classToClass", GraphNodePackage.class)
				.get();
		
		final var alphaIdentifier = new GraphNodeIdentifier(graph, "Alpha");
		final var alphaClassNode = new GraphNodeClass(graph, alphaIdentifier);
		final var alphaOneAttributeNode = new GraphNodeAttribute(graph, "one");
		final var alphaTwoAttributeNode = new GraphNodeAttribute(graph, "two");
		final var barIdentifier = new GraphNodeIdentifier(graph, "bar");
		final var alphaBarOperationNode = new GraphNodeOperation(graph, barIdentifier);
		final var fooIdentifier = new GraphNodeIdentifier(graph, "foo");
		final var alphaFooOperationNode = new GraphNodeOperation(graph, fooIdentifier);
		packageNodeLeaf.has(alphaClassNode);
		alphaClassNode.has(alphaOneAttributeNode);
		alphaClassNode.has(alphaTwoAttributeNode);
		alphaClassNode.has(alphaBarOperationNode);
		alphaClassNode.has(alphaFooOperationNode);
		removeMethodMicrostepNode.removes(alphaFooOperationNode);
		
		final var betaIdentifier = new GraphNodeIdentifier(graph, "Beta");
		final var betaClassNode = new GraphNodeClass(graph, betaIdentifier);
		final var betaField1AttributeNode = new GraphNodeAttribute(graph, "field1");
		final var betaField2AttributeNode = new GraphNodeAttribute(graph, "field2");
		final var betaFooOperationNode = new GraphNodeOperation(graph, fooIdentifier);
		packageNodeLeaf.has(betaClassNode);
		betaClassNode.has(betaField1AttributeNode);
		betaClassNode.has(betaField2AttributeNode);
		betaClassNode.has(betaFooOperationNode);
		addMethodMicrostepNode.adds(betaFooOperationNode);
		
		final var gammaIdentifier = new GraphNodeIdentifier(graph, "Gamma");
		final var gammaClassNode = new GraphNodeClass(graph, gammaIdentifier);
		final var callerIdentifier = new GraphNodeIdentifier(graph, "caller");
		final var gammaCallerOperationNode = new GraphNodeOperation(graph, callerIdentifier);
		final var gammaCallerBlock = new GraphNodeBlock(graph);
		final var gammaCallerExpression = new GraphNodeMethodInvocationExpression(graph);
		final var gammaCallerStatement = new GraphNodeExpressionStatement(graph, gammaCallerExpression);
		packageNodeLeaf.has(gammaClassNode);
		gammaClassNode.has(gammaCallerOperationNode);
		gammaCallerOperationNode.hasBody(gammaCallerBlock);
		gammaCallerBlock.has(gammaCallerStatement);
		gammaCallerExpression.invokes(alphaFooOperationNode);
		
		final var missingDefinitionRiskNode = new GraphNodeRiskMissingDefinition(graph);
		removeMethodMicrostepNode.causes(missingDefinitionRiskNode);
		missingDefinitionRiskNode.affects(gammaCallerOperationNode);
		missingDefinitionRiskNode.affects(alphaFooOperationNode);
		
		return graph;
	}
}
