package nl.ou.refactoring.advice.eclipse;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.javaParser.GraphJavaReader;
import nl.ou.refactoring.advice.io.plantuml.GraphPlantUmlSvgGenerator;
import nl.ou.refactoring.advice.io.plantuml.classDiagrams.GraphPlantUmlClassDiagramWriter;

/**
 * Action to show refactoring advice in a web view when a Java element is selected.
 */
public final class ShowRefactoringAdviceAction implements IObjectActionDelegate {
    private IWorkbenchPart targetPart;
    private IJavaElement selectedElement;

    /**
     * Initialises a new instance of {@link ShowRefactoringAdviceAction}.
     */
    public ShowRefactoringAdviceAction() {
        super();
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.targetPart = targetPart;
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    @Override
    public void run(IAction action) {
        if (this.selectedElement != null) {
            // Generate HTML content for the advice.
            Document htmlContent = null;
            try {
                htmlContent = generateHtmlAdvice(this.selectedElement);
            } catch (ArgumentNullException e) {
                if ("elementPath".equals(e.getParameterName())) {
                    MessageDialog.openError(
                        targetPart.getSite().getShell(),
                        "Refactoring Advice",
                        "Selected element does not have a valid file path. Only elements with physical files (like classes, methods, fields) can be analyzed.");
                } else {
                    e.printStackTrace();
                }
                return;
            } catch (ArgumentEmptyException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            // Show the HTML content in a web view.
            RefactoringAdviceWebView.showWebView(targetPart.getSite().getShell(), "Refactoring Advice", htmlContent);
        } else {
            MessageDialog.openInformation(
                targetPart.getSite().getShell(),
                "Refactoring Advice",
                "No Java element selected.");
        }
    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        if (selection instanceof StructuredSelection) {
            final var structuredSelection = (StructuredSelection) selection;
            final var firstElement = structuredSelection.getFirstElement();
            if (firstElement instanceof IJavaElement) {
                this.selectedElement = (IJavaElement) firstElement;
            } else {
                this.selectedElement = null;
            }
        } else {
            this.selectedElement = null;
        }
    }

    /**
     * Generate HTML content for refactoring advice.
     * @param element the Java element to generate advice for.
     * @return {@link Document} HTML content as a W3C DOM Document.
     * @throws ArgumentNullException Thrown if element is null.
     * @throws ParserConfigurationException Thrown if the parser configuration for HTML documents contains errors.
     * @throws IOException Thrown if SVG could not be written to output stream while generating from a PlantUML specification.
     * @throws ArgumentEmptyException Thrown if the PlantUML specification is an empty string or contains only white spaces.
     * @throws SAXException Thrown if SVG could not be parsed as HTML element.
     */
    private Document generateHtmlAdvice(IJavaElement element)
            throws
            ArgumentEmptyException,
            ArgumentNullException,
            ElementResourceLocationNotFoundException,
            ElementResourceNotFoundException,
            IOException,
            ParserConfigurationException,
            SAXException {
        ArgumentGuard.requireNotNull(element, "element");

        /*
         * Collect selected element's metadata.
         */
        final var resource = element.getResource();
        if (resource == null) {
            throw new ElementResourceNotFoundException(element);
        }

        final var resourceLocation = resource.getLocation();
        if (resourceLocation == null) {
            throw new ElementResourceLocationNotFoundException(element);
        }

        final var elementFile = resourceLocation.toFile();
        final var elementReader = new FileReader(elementFile);

        /*
         * Build Refactoring Advice Graph.
         */
        var graph = new Graph(element.getElementName());
        final var javaReader =
            new GraphJavaReader(
                graph,
                elementReader,
                elementFile.getAbsolutePath(),
                resource.getName()
        );
        graph = javaReader.read();

        /*
         * Write PlantUML output.
         */
        final var plantUmlStringWriter = new StringWriter();
        final var plantUmlWriter = new GraphPlantUmlClassDiagramWriter(plantUmlStringWriter);
        plantUmlWriter.write(graph);
        final var plantUml = plantUmlStringWriter.toString();

        /*
         * Generate SVG from PlantUML output.
         */
        final var svg = GraphPlantUmlSvgGenerator.generate(plantUml);
        final var svgDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        svgDocumentBuilderFactory.setNamespaceAware(true);
        final var svgDocument =
            svgDocumentBuilderFactory
                .newDocumentBuilder()
                .parse(new InputSource(new StringReader(svg)));

        /*
         * Create HTML document.
         */
        final var htmlDocument =
            DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .newDocument();

        /*
         * Add HTML boiler plating.
         */
        final var htmlElement = htmlDocument.createElement("html");
        htmlDocument.appendChild(htmlElement);

        // <head>
        final var htmlHeadElement = htmlDocument.createElement("head");
        htmlElement.appendChild(htmlHeadElement);
        final var htmlTitleElement = htmlDocument.createElement("title");
        htmlTitleElement.setTextContent("Refactoring Advice");
        htmlHeadElement.appendChild(htmlTitleElement);

        // <body>
        final var htmlBodyElement = htmlDocument.createElement("body");
        htmlElement.appendChild(htmlBodyElement);
        final var h1Element = htmlDocument.createElement("h1");
        h1Element.setTextContent("Refactoring Advice");
        htmlBodyElement.appendChild(h1Element);
        final var classDiagramSectionElement = htmlDocument.createElement("section");
        classDiagramSectionElement.setAttribute("width", "500");
        classDiagramSectionElement.setAttribute("height", "500");
        htmlBodyElement.appendChild(classDiagramSectionElement);
        final var svgElement = htmlDocument.importNode(svgDocument.getDocumentElement(), true);
        classDiagramSectionElement.appendChild(svgElement);

        return htmlDocument;
    }
}