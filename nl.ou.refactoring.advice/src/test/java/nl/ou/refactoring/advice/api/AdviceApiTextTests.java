package nl.ou.refactoring.advice.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Element;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphTemplates;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;

public final class AdviceApiTextTests {
	private static Element ELEMENT_CONTAINER;
	private static URI RESOURCE_URI_PREFIX;

	@BeforeAll
	static void createContainerElement() throws ParserConfigurationException {
		final var document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		final var htmlElement = document.createElement("html");
		final var headElement = document.createElement("head");
		final var titleElement = document.createElement("title");
		titleElement.setTextContent("Test HTML content title");
		headElement.appendChild(titleElement);
		htmlElement.appendChild(headElement);
		final var bodyElement = document.createElement("body");
		final var divElement = document.createElement("div");
		bodyElement.appendChild(divElement);
		htmlElement.appendChild(bodyElement);
		document.appendChild(htmlElement);
		ELEMENT_CONTAINER = divElement;
	}

	@BeforeAll
	static void createResourceUriPrefix() throws URISyntaxException {
		RESOURCE_URI_PREFIX = new URI("eclipse-resource://nl.ou.refactoring.advice.eclipse/");
	}

	private static Arguments generateHtmlTestRenameMethod() throws ParserConfigurationException {
		// Graph
		final var graph = GraphTemplates.renameMethod();
		final var packageTest =
				new GraphNodePackage(graph, GraphNodeIdentifier.computeIdentifier(graph, "testPackage"));
		final var classTest = new GraphNodeClass(graph, GraphNodeIdentifier.computeIdentifier(graph, "testClass"));
		final var attributeTest =
				new GraphNodeAttribute(graph, GraphNodeIdentifier.computeIdentifier(graph, "testMember1"));
		final var operationAddedTest =
				new GraphNodeOperation(graph, GraphNodeIdentifier.computeIdentifier(graph, "testMember1"));
		final var operationRemovedTest =
				new GraphNodeOperation(graph, GraphNodeIdentifier.computeIdentifier(graph, "testMember"));
		classTest.has(attributeTest);
		classTest.has(operationAddedTest);
		classTest.has(operationRemovedTest);
		packageTest.has(classTest);
		final var microstepAddMethod = graph.getNodes(GraphNodeMicrostepAddMethod.class).stream().findAny().get();
		microstepAddMethod.adds(operationAddedTest);
		final var microstepRemoveMethod = graph.getNodes(GraphNodeMicrostepRemoveMethod.class).stream().findAny().get();
		microstepRemoveMethod.removes(operationRemovedTest);
		final var riskDoubleDefinition = new GraphNodeRiskDoubleDefinition(graph);
		microstepAddMethod.causes(riskDoubleDefinition);
		riskDoubleDefinition.affects(attributeTest);
		riskDoubleDefinition.affects(operationAddedTest);

		// Container element
		final var containerElement = (Element) ELEMENT_CONTAINER.cloneNode(true);

		return new GenerateHtmlTestArguments(graph, containerElement, RESOURCE_URI_PREFIX);
	}

	private static Stream<Arguments> getGenerateHtmlTestArguments() throws ParserConfigurationException {
		return Stream.of(generateHtmlTestRenameMethod());
	}

	@ParameterizedTest
	@MethodSource("getGenerateHtmlTestArguments")
	@DisplayName("Should generate HTML from a Refactoring Advice Graph (RAG)")
	public void generateHtmlTests(Graph graph, Element containerElement, URI resourceUriPrefix) {
		final var actual = AdviceApi.Text.generateHtml(graph, containerElement, resourceUriPrefix);
		assertNotNull(actual);
		assertEquals("div", actual.getTagName());
		final var sectionElements = actual.getElementsByTagName("section");
		assertNotEquals(0, sectionElements.getLength());
		assertEquals(2, sectionElements.getLength());
		final var classDiagramSectionElement = sectionElements.item(0);
		assertNotNull(classDiagramSectionElement);
		final var adviceTextSectionElement = sectionElements.item(1);
		assertNotNull(adviceTextSectionElement);
	}
}