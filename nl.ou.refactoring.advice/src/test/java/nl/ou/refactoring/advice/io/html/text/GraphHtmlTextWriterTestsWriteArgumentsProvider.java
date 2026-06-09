package nl.ou.refactoring.advice.io.html.text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.GraphTemplates;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.GraphNodeProgramLocation;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskMissingDefinition;

public class GraphHtmlTextWriterTestsWriteArgumentsProvider implements ArgumentsProvider {
	@Override
	public Stream<? extends Arguments> provideArguments
	(
		ParameterDeclarations parameters,
		ExtensionContext context
	) throws RefactoringMayContainOnlyOneStartNodeException {
		final var argumentsList = new ArrayList<Arguments>();
		argumentsList.add(createMissingDefinition());
		return argumentsList.stream();
	}
	
	private static Arguments createMissingDefinition() {
		final var graph = GraphTemplates.moveMethod().clone("HTML Text Writer test");
		final var microstepAddMethodNode =
			graph
				.getNodes(GraphNodeMicrostepAddMethod.class)
				.stream()
				.findAny()
				.get();
		final var microstepRemoveMethodNode =
			graph
				.getNodes(GraphNodeMicrostepRemoveMethod.class)
				.stream()
				.findAny()
				.get();
		
		final var packageIdentifier = new GraphNodeIdentifier(graph, "ou");
		final var packageNode = new GraphNodePackage(graph, packageIdentifier);
		final var class1Identifier = new GraphNodeIdentifier(graph, "MyClass");
		final var class1Node = new GraphNodeClass(graph, class1Identifier);
		final var class1ProgramLocationNode =
			new GraphNodeProgramLocation(
				graph,
				"nl/ou/refactoring/MyClass.java",
				"MyClass.java",
				1,
				2,
				10,
				2
			);
		class1Node.has(class1ProgramLocationNode);
		packageNode.has(class1Node);
		final var class1OperationFooIdentifier = new GraphNodeIdentifier(graph, "foo");
		final var class1OperationFooNode = new GraphNodeOperation(graph, class1OperationFooIdentifier, List.of());
		final var class1OperationFooProgramLocationNode =
			new GraphNodeProgramLocation(
				graph,
				"nl/ou/refactoring/MyClass.java",
				"MyClass.java",
				2,
				3,
				4,
				5
			);
		class1OperationFooNode.has(class1OperationFooProgramLocationNode);
		class1Node.has(class1OperationFooNode);
		microstepRemoveMethodNode.removes(class1OperationFooNode);

		final var class1OperationBarIdentifier = new GraphNodeIdentifier(graph, "bar");
		new GraphNodeOperation(graph, class1OperationBarIdentifier, List.of());
		
		final var class2Identifier = new GraphNodeIdentifier(graph, "OtherClass");
		final var class2Node = new GraphNodeClass(graph, class2Identifier);
		final var class2ProgramLocationNode =
			new GraphNodeProgramLocation(
				graph,
				"nl/ou/refactoring/OtherClass.java",
				"OtherClass.java",
				1,
				1,
				10,
				1
			);
		class2Node.has(class2ProgramLocationNode);
		packageNode.has(class2Node);
		final var class2OperationFooIdentifier = new GraphNodeIdentifier(graph, "foo");
		final var class2OperationFooNode = new GraphNodeOperation(graph, class2OperationFooIdentifier, List.of());
		final var class2OperationFooProgramLocationNode =
			new GraphNodeProgramLocation(
				graph,
				"nl/ou/refactoring/OtherClass.java",
				"OtherClass.java",
				1,
				2,
				3,
				4
			);
		class2OperationFooNode.has(class2OperationFooProgramLocationNode);
		class2Node.has(class2OperationFooNode);
		microstepAddMethodNode.adds(class2OperationFooNode);
		
		final var missingDefinitionRiskNode = new GraphNodeRiskMissingDefinition(graph);
		microstepRemoveMethodNode.causes(missingDefinitionRiskNode);
		missingDefinitionRiskNode.affects(class1OperationFooNode);
		
		final var htmlStringExpected =
			"<html>\r\n"
			+ "    <head>\r\n"
			+ "        <META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n"
			+ "    </head>\r\n"
			+ "    <body>\r\n"
			+ "        <article>\r\n"
			+ "            HTML Text Writer test, adds method '<a href=\"eclipse-resource://nl.ou.refactoring.advice.eclipse/nl/ou/refactoring/OtherClass.java#1:2_3:4\">foo</a>' to class '<a href=\"eclipse-resource://nl.ou.refactoring.advice.eclipse/nl/ou/refactoring/OtherClass.java#1:1_10:1\">OtherClass</a>', removes method '<a href=\"eclipse-resource://nl.ou.refactoring.advice.eclipse/nl/ou/refactoring/MyClass.java#2:3_4:5\">foo</a>' from class '<a href=\"eclipse-resource://nl.ou.refactoring.advice.eclipse/nl/ou/refactoring/MyClass.java#1:2_10:2\">MyClass</a>' which will cause a Missing definition on <a href=\"eclipse-resource://nl.ou.refactoring.advice.eclipse/nl/ou/refactoring/MyClass.java#2:3_4:5\">foo</a>\r\n"
			+ "        </article>\r\n"
			+ "    </body>\r\n"
			+ "</html>\r\n";
		
		return Arguments.of(graph, htmlStringExpected);
	}
}
