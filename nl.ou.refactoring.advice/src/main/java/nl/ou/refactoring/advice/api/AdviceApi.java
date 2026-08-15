package nl.ou.refactoring.advice.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphTemplates;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.contracts.ArgumentPatternException;
import nl.ou.refactoring.advice.io.GraphReaderException;
import nl.ou.refactoring.advice.io.html.GraphHtmlWriterSettings;
import nl.ou.refactoring.advice.io.html.text.GraphHtmlTextWriter;
import nl.ou.refactoring.advice.io.javaParser.GraphJavaReader;
import nl.ou.refactoring.advice.io.javaParser.GraphJavaReaderResolutionProvider;
import nl.ou.refactoring.advice.io.plantuml.GraphPlantUmlSvgGenerator;
import nl.ou.refactoring.advice.io.plantuml.classDiagrams.GraphPlantUmlClassDiagramWriter;
import nl.ou.refactoring.advice.nlp.grammar.NLPGrammarProcessor;
import nl.ou.refactoring.advice.nlp.languages.englishGreatBritain.NLPLanguageEnglishGreatBritain;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.GraphNodeType;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperationParameterSignature;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;

/**
 * An Application Programming Interface (API) for generating refactoring advice
 * using knowledge graphs.
 */
public final class AdviceApi {
	/**
	 * Features for managing the Abstract Syntax Tree (AST) subgraph.
	 */
	public static final class Code {
		/**
		 * Adds a new Class specification to the specified Refactoring Advice Graph
		 * (RAG) if it is not defined yet, otherwise returns the existing specification.
		 *
		 * @param graph       The Refactoring Advice Graph (RAG).
		 * @param packageName The name of the Package specification that should contain
		 *                    the Class specification.
		 * @param className   The name of the Class specification.
		 * @return A {@link GraphNodeClass} that represents the Class specification in
		 *         the Refactoring Advice Graph (RAG).
		 * @throws ArgumentNullException                              Thrown if graph,
		 *                                                            packageName or
		 *                                                            className is null.
		 * @throws ArgumentEmptyException                             Thrown if
		 *                                                            packageName or
		 *                                                            className is an
		 *                                                            empty string or
		 *                                                            contains only
		 *                                                            white spaces.
		 * @throws AdviceApiCodePackageSpecificationNotFoundException Thrown if the
		 *                                                            package
		 *                                                            specification with
		 *                                                            the specified
		 *                                                            package name is
		 *                                                            not found in the
		 *                                                            specified graph.
		 */
		public static GraphNodeClass computeClassSpecification(
				final Graph graph,
				final String packageName,
				final String className) throws ArgumentNullException, ArgumentEmptyException,
				AdviceApiCodePackageSpecificationNotFoundException {
			ArgumentGuard.requireNotNull(graph, "graph");
			ArgumentGuard.requireNotNullEmptyOrWhiteSpace(packageName, "packageName");
			ArgumentGuard.requireNotNullEmptyOrWhiteSpace(className, "className");
			LOGGER.debug(
					"Computing Class specification '{}' in Package '{}' in Refactoring Advice Graph (RAG) for refactoring '{}'",
					className,
					packageName,
					graph.getRefactoringName());
			final var packageNode =
					graph.getNode(packageName, GraphNodePackage.class)
							.orElseThrow(
									() -> new AdviceApiCodePackageSpecificationNotFoundException(graph, packageName));
			final var classNodeIdentifier = computeIdentifier(graph, className);
			return packageNode.computeClassNode(classNodeIdentifier);
		}

		/**
		 * Adds a new Field specification to the specified Refactoring Advice Graph
		 * (RAG) if it is not defined yet, otherwise returns the existing specification.
		 *
		 * @param graph                   The Refactoring Advice Graph (RAG).
		 * @param classFullyQualifiedName The Fully Qualified Name (FQN) of the Class
		 *                                specification that should contain the Field
		 *                                specification.
		 * @param fieldName               The name of the Field specification.
		 * @param fieldTypeName           The name of the Field specification's Type.
		 * @return A {@link GraphNodeAttribute} that represents the Field specification
		 *         in the Refactoring Advice Graph (RAG).
		 * @throws ArgumentNullException                              Thrown if graph,
		 *                                                            packageName or
		 *                                                            className is null.
		 * @throws ArgumentEmptyException                             Thrown if
		 *                                                            packageName or
		 *                                                            className is an
		 *                                                            empty string or
		 *                                                            contains only
		 *                                                            white spaces.
		 * @throws AdviceApiCodePackageSpecificationNotFoundException Thrown if the
		 *                                                            package
		 *                                                            specification with
		 *                                                            the specified
		 *                                                            package name is
		 *                                                            not found in the
		 *                                                            specified graph.
		 */
		public static GraphNodeAttribute computeFieldSpecification(
				final Graph graph,
				final String classFullyQualifiedName,
				final String fieldName,
				final String fieldTypeName)
				throws ArgumentNullException, ArgumentEmptyException, AdviceApiCodeClassSpecificationNotFoundException {
			ArgumentGuard.requireNotNull(graph, "graph");
			ArgumentGuard.requireNotNullEmptyOrWhiteSpace(classFullyQualifiedName, "classFullyQualifiedName");
			ArgumentGuard.requireNotNullEmptyOrWhiteSpace(fieldName, "fieldName");
			ArgumentGuard.requireNotNullEmptyOrWhiteSpace(fieldTypeName, "fieldTypeName");
			LOGGER.debug(
					"Computing Field specification '{}' in Class '{}' in Refactoring Advice Graph (RAG) for refactoring '{}'",
					fieldName,
					classFullyQualifiedName,
					graph.getRefactoringName());
			final var classNode =
					graph.getNode(classFullyQualifiedName, GraphNodeClass.class)
							.orElseThrow(
									() -> new AdviceApiCodeClassSpecificationNotFoundException(
											graph,
											classFullyQualifiedName));
			final var attributeNode = classNode.computeAttributeNode(fieldName);
			final var attributeTypeNode = computeType(graph, fieldTypeName);
			attributeNode.is(attributeTypeNode);
			return attributeNode;
		}

		/**
		 * Adds a new identifier node to the Refactoring Advice Graph (RAG) if it is not
		 * defined yet, otherwise returns the existing identifier.
		 * 
		 * @param graph      The Refactoring Advice Graph (RAG) that should contain the
		 *                   identifier node.
		 * @param identifier The value of the identifier node.
		 * @return The {@link GraphNodeIdentifier} that was either added or found.
		 * @throws ArgumentNullException    Thrown if graph or identifier is null.
		 * @throws ArgumentEmptyException   Thrown if identifier is an empty string or
		 *                                  contains only white spaces.
		 * @throws ArgumentPatternException Thrown if the identifier value does not
		 *                                  conform to the format of a Java identifier.
		 */
		static GraphNodeIdentifier computeIdentifier(Graph graph, String identifier)
				throws ArgumentNullException, ArgumentEmptyException, ArgumentPatternException {
			ArgumentGuard.requireNotNull(graph, "graph");
			ArgumentGuard.requireNotNullEmptyOrWhiteSpace(identifier, "identifier");
			LOGGER.debug(
					"Computing identifier '{}' in Refactoring Advice Graph (RAG) for refactoring '{}'",
					identifier,
					graph.getRefactoringName());
			return GraphNodeIdentifier.computeIdentifier(graph, identifier);
		}

		/**
		 * Adds a new Method specification to the specified Refactoring Advice Graph
		 * (RAG) if it is not defined yet, otherwise returns the existing specification.
		 *
		 * @param graph                   The Refactoring Advice Graph (RAG).
		 * @param classFullyQualifiedName The Fully Qualified Name (FQN) of the Class
		 *                                specification that should contain the Method
		 *                                specification.
		 * @param methodName              The name of the Method specification.
		 * @param methodParameters        The signatures of the method parameters in the
		 *                                Method specification.
		 * @return A {@link GraphNodeOperation} that represents the Method specification
		 *         in the Refactoring Advice Graph (RAG).
		 * @throws ArgumentNullException                            Thrown if any of the
		 *                                                          arguments of this
		 *                                                          computeMethodSpecification
		 *                                                          is null.
		 * @throws ArgumentEmptyException                           Thrown if
		 *                                                          classFullyQualifiedName
		 *                                                          or methodName is an
		 *                                                          empty string or
		 *                                                          contains only white
		 *                                                          spaces.
		 * @throws AdviceApiCodeClassSpecificationNotFoundException Thrown if the Class
		 *                                                          specification could
		 *                                                          not be found.
		 */
		public static GraphNodeOperation computeMethodSpecification(
				final Graph graph,
				final String classFullyQualifiedName,
				final String methodName,
				final List<GraphNodeOperationParameterSignature> methodParameters)
				throws ArgumentNullException, ArgumentEmptyException, AdviceApiCodeClassSpecificationNotFoundException {
			ArgumentGuard.requireNotNull(graph, "graph");
			ArgumentGuard.requireNotNullEmptyOrWhiteSpace(classFullyQualifiedName, "classFullyQualifiedName");
			ArgumentGuard.requireNotNullEmptyOrWhiteSpace(methodName, "methodName");
			ArgumentGuard.requireNotNull(methodParameters, "methodParameters");
			LOGGER.debug(
					"Computing Method specification '{}' in Class '{}' in Refactoring Advice Graph (RAG) for refactoring '{}'",
					methodName,
					classFullyQualifiedName,
					graph.getRefactoringName());
			final var classNode =
					graph.getNode(classFullyQualifiedName, GraphNodeClass.class)
							.orElseThrow(
									() -> new AdviceApiCodeClassSpecificationNotFoundException(
											graph,
											classFullyQualifiedName));
			return classNode.computeOperationNode(methodName, methodParameters);
		}

		/**
		 * Adds a new Package specification to the specified Refactoring Advice Graph
		 * (RAG) if it is not defined yet, otherwise returns the existing specification.
		 *
		 * @param graph       The Refactoring Advice Graph (RAG).
		 * @param packageName The name of the package specification that should contain
		 *                    the package specification.
		 * @return A {@link GraphNodePackage} that represents the Package specification
		 *         in the Refactoring Advice Graph (RAG).s
		 * @throws ArgumentNullException  Thrown if graph or packageName is null.
		 * @throws ArgumentEmptyException Thrown if packageName is an empty string or
		 *                                contains only white spaces.
		 */
		public static GraphNodePackage computePackageSpecification(final Graph graph, final String packageName)
				throws ArgumentNullException, ArgumentEmptyException {
			ArgumentGuard.requireNotNull(graph, "graph");
			ArgumentGuard.requireNotNullEmptyOrWhiteSpace(packageName, "packageName");
			LOGGER.debug(
					"Computing Package specification '{}' in Refactoring Advice Graph (RAG) for refactoring '{}'",
					packageName,
					graph.getRefactoringName());
			return GraphNodePackage.parse(graph, packageName);
		}

		private static GraphNodeType computeType(Graph graph, String typeName)
				throws ArgumentNullException, ArgumentEmptyException {
			ArgumentGuard.requireNotNull(graph, "graph");
			ArgumentGuard.requireNotNullEmptyOrWhiteSpace(typeName, "typeName");
			LOGGER.debug(
					"Computing type '{}' in Refactoring Advice Graph (RAG) for refactoring '{}'",
					typeName,
					graph.getRefactoringName());
			return graph.getNodes(GraphNodeType.class)
					.stream()
					.filter((node) -> node.getTypeName().equals(typeName))
					.findAny()
					.orElseGet(() -> new GraphNodeType(graph, typeName));
		}

		/**
		 * Reads Abstract Syntax Tree (AST) elements for a Refactoring Advice Graph
		 * (RAG) from a specified Java code file.
		 *
		 * @param graph              The Refactoring Advice Graph (RAG) that should
		 *                           contain the Abstract Syntax Tree (AST) elements.
		 * @param resolutionProvider Helps the reader resolve reference types
		 *                           encountered in the parsed code file.
		 * @param filePath           The path to the Java code file that contains the
		 *                           desired Abstract Syntax Tree (AST) elements.
		 * @throws ArgumentNullException                  Thrown if graph,
		 *                                                resolutionProvider or filePath
		 *                                                is null.
		 * @throws AdviceApiCodeFileDoesNotExistException Thrown if the specified Java
		 *                                                code file could not be found.
		 * @throws GraphReaderException                   Thrown if the specified file
		 *                                                could not be read or parsed
		 *                                                correctly.
		 */
		public static void read(
				final Graph graph,
				final GraphJavaReaderResolutionProvider resolutionProvider,
				final Path filePath)
				throws ArgumentNullException, AdviceApiCodeFileDoesNotExistException, GraphReaderException {
			ArgumentGuard.requireNotNull(graph, "graph");
			ArgumentGuard.requireNotNull(resolutionProvider, "resolutionProvider");
			ArgumentGuard.requireNotNull(filePath, "filePath");
			LOGGER.debug(
					"Reading Class from file '{}' into Refactoring Advice Graph (RAG) for refactoring '{}'",
					filePath.toString(),
					graph.getRefactoringName());
			final var fileNameFull = filePath.toString();
			final var fileName = filePath.getFileName().toString();
			try {
				final var file = new File(fileNameFull);
				if (!file.exists()) {
					throw new AdviceApiCodeFileDoesNotExistException(graph, fileNameFull);
				}
				final var reader = new BufferedReader(new FileReader(file));
				final var graphJavaReader =
						new GraphJavaReader(graph, resolutionProvider, reader, fileNameFull, fileName);
				graphJavaReader.read();
			} catch (FileNotFoundException exception) {
				throw new AdviceApiCodeFileDoesNotExistException(graph, fileNameFull);
			}
		}
	}

	/**
	 * Features for generating diagrams from a Refactoring Advice Graph (RAG).
	 */
	public static final class Diagrams {
		/**
		 * Appends a UML Class Diagram from a Refactoring Advice Graph (RAG) to an HTML
		 * Element's children.
		 *
		 * @param graph        The Refactoring Advice Graph (RAG) from which to export a
		 *                     UML Class Diagram.
		 * @param ownerElement The owner HTML Element to which to append the UML Class
		 *                     Diagram.
		 * @return The HTML Element that now contains the Scalable Vector Graphics (SVG)
		 *         element of the UML Class Diagram from the Refactoring Advice Graph
		 *         (RAG).
		 * @throws AdviceApiClassDiagramExportFailedException Thrown if the export of
		 *                                                    the Scalable Vector
		 *                                                    Graphics (SVG) of the UML
		 *                                                    Class Diagram from the
		 *                                                    Refactoring Advice Graph
		 *                                                    (RAG) failed.
		 */
		public static Element appendClassDiagramToHtml(final Graph graph, final Element ownerElement)
				throws AdviceApiClassDiagramExportFailedException {
			final var ownerDocument = ownerElement.getOwnerDocument();
			final var classDiagramSvgDocument = exportClassDiagram(graph);
			final var classDiagramSvgElement = classDiagramSvgDocument.getDocumentElement();
			ownerElement.appendChild(ownerDocument.importNode(classDiagramSvgElement, true));
			return ownerElement;
		}

		/**
		 * Exports a UML Class Diagram from a Refactoring Advice Graph (RAG).
		 *
		 * @param graph The Refactoring Advice Graph (RAG) from which to export a UML
		 *              Class Diagram.
		 * @return An XML document that contains Scalable Vector Graphics (SVG).
		 * @throws AdviceApiClassDiagramExportFailedException Thrown if the export
		 *                                                    failed.
		 */
		public static Document exportClassDiagram(final Graph graph) throws AdviceApiClassDiagramExportFailedException {
			try {
				final var classDiagramPlantUmlStringWriter = new StringWriter();
				final var classDiagramPlantUmlWriter =
						new GraphPlantUmlClassDiagramWriter(classDiagramPlantUmlStringWriter);
				classDiagramPlantUmlWriter.write(graph);
				final var classDiagramSvgString =
						GraphPlantUmlSvgGenerator.generate(classDiagramPlantUmlStringWriter.toString());

				final var classDiagramSvgDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				final var classDiagramSvgDocument =
						classDiagramSvgDocumentBuilder.parse(new InputSource(new StringReader(classDiagramSvgString)));

				return classDiagramSvgDocument;
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new AdviceApiClassDiagramExportFailedException(graph, ex);
			}
		}

		/**
		 * Exports a Mermaid Flowchart Diagram from a Refactoring Advice Graph (RAG).
		 *
		 * @param graph The Refactoring Advice Graph (RAG) from which to export a
		 *              Mermaid Flowchart Diagram.
		 * @return An XML document that contains Scalable Vector Graphics (SVG).
		 * @throws AdviceApiWorkflowExportFailedException Thrown if the export failed.
		 */
		public static Document exportWorkflow(final Graph graph) throws AdviceApiWorkflowExportFailedException {
			try {
				return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new AdviceApiWorkflowExportFailedException(graph, ex);
			}
		}
	}

	public static final class Text {
		public static void appendAdviceTextToHtml(
				final Graph graph,
				final Element containerElement,
				final URI resourceUriPrefix) throws AdviceApiWorkflowExportFailedException {
			try {
				// Generate advice text.
				final var nlpLanguage = NLPLanguageEnglishGreatBritain.INSTANCE;
				final var nlpGrammarProcessor = new NLPGrammarProcessor(nlpLanguage);

				final var graphHtmlTextWriter =
						new GraphHtmlTextWriter(
								new GraphHtmlWriterSettings(resourceUriPrefix),
								containerElement,
								nlpGrammarProcessor);
				graphHtmlTextWriter.write(graph);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new AdviceApiWorkflowExportFailedException(graph, ex);
			}
		}

		public static Element generateHtml(
				final Graph graph,
				final Element containerElement,
				final URI resourceUriPrefix) throws AdviceApiWorkflowExportFailedException {
			try {
				final var htmlDocument = containerElement.getOwnerDocument();

				final var classDiagramSection = htmlDocument.createElement("section");
				Diagrams.appendClassDiagramToHtml(graph, classDiagramSection);
				containerElement.appendChild(classDiagramSection);

				final var adviceTextSection = htmlDocument.createElement("section");
				appendAdviceTextToHtml(graph, adviceTextSection, resourceUriPrefix);
				containerElement.appendChild(adviceTextSection);

				return containerElement;
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new AdviceApiWorkflowExportFailedException(graph, ex);
			}
		}
	}

	/**
	 * Logs messages for debugging and troubleshooting.
	 */
	private static final Logger LOGGER = LogManager.getLogger(AdviceApi.class);

	/**
	 * Creates an empty Refactoring Advice Graph (RAG) with the specified
	 * refactoring name.
	 *
	 * @param refactoringName The refactoring name.
	 * @return An empty Refactoring Advice Graph (RAG) with the specified
	 *         refactoring name.
	 * @throws ArgumentNullException  Thrown if refactoringName is null.
	 * @throws ArgumentEmptyException Thrown if refactoringName is an empty string
	 *                                or contains only white spaces.
	 */
	public static Graph createRefactoringGraph(String refactoringName)
			throws ArgumentNullException, ArgumentEmptyException {
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(refactoringName, "refactoringName");
		LOGGER.debug("Creating Refactoring Advice Graph (RAG) for refactoring '{}'", refactoringName);
		return new Graph(refactoringName);
	}

	/**
	 * Creates a Refactoring Advice Graph (RAG) from a refactoring template.
	 *
	 * @param template The {@link RefactoringTemplateIdentifier} for which to get a
	 *                 Refactoring Advice Graph (RAG).
	 * @return A Refactoring Advice Graph (RAG) template for the specified
	 *         refactoring template.
	 * @throws ArgumentNullException                 Thrown if template is null.
	 * @throws RefactoringTemplateUndefinedException Thrown if the template is
	 *                                               undefined.
	 */
	public static Graph createRefactoringGraphByTemplate(RefactoringTemplateIdentifier template)
			throws ArgumentNullException, RefactoringTemplateUndefinedException {
		ArgumentGuard.requireNotNull(template, "template");
		LOGGER.debug("Retrieving Refactoring Advice Graph (RAG) template '{}'", template.toString());
		return switch (template) {
			case RefactoringTemplateIdentifier.MOVE_FIELD -> GraphTemplates.moveField();
			case RefactoringTemplateIdentifier.MOVE_METHOD -> GraphTemplates.moveMethod();
			case RefactoringTemplateIdentifier.RENAME_FIELD -> GraphTemplates.renameField();
			case RefactoringTemplateIdentifier.RENAME_METHOD -> GraphTemplates.renameMethod();
			default -> throw new RefactoringTemplateUndefinedException(template);
		};
	}

	/**
	 * Initialises a new instance of {@link AdviceApi}. This is a static class, so
	 * do not call this constructor.
	 */
	private AdviceApi() {
	}
}
