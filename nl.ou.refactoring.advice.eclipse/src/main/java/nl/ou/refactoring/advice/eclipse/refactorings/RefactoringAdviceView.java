package nl.ou.refactoring.advice.eclipse.refactorings;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.eclipse.RefactoringGraphBuilder;
import nl.ou.refactoring.advice.io.html.GraphHtmlWriterSettings;
import nl.ou.refactoring.advice.io.html.text.GraphHtmlTextWriter;
import nl.ou.refactoring.advice.io.plantuml.GraphPlantUmlSvgGenerator;
import nl.ou.refactoring.advice.io.plantuml.classDiagrams.GraphPlantUmlClassDiagramWriter;
import nl.ou.refactoring.advice.nlp.languages.englishGreatBritain.NLPLanguageEnglishGreatBritain;
import nl.ou.refactoring.advice.nlp.processors.NLPGrammarProcessor;

/**
 * Web view for displaying refactoring advice in HTML format.
 */
public final class RefactoringAdviceView {
	private static final Logger LOGGER = LogManager.getLogger(RefactoringAdviceView.class);
	private final URI resourceUriPrefix;
	private final Shell parentShell;
	private final Refactoring refactoring;

	/**
	 * Initialises a new instance of {@link RefactoringAdviceView}.
	 * 
	 * @param parentShell The parent shell.
	 * @param refactoring The refactoring that should be displayed.
	 * @throws ArgumentNullException Thrown if parentShell or refactoringAdviceGraph
	 *                               is null.
	 */
	public RefactoringAdviceView(Shell parentShell, Refactoring refactoring) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(parentShell, "parentShell");
		ArgumentGuard.requireNotNull(refactoring, "refactoring");
		try {
			this.resourceUriPrefix = new URI("eclipse-resource://nl.ou.refactoring.advice.eclipse/");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to initialize resource URI prefix");
		}
		this.parentShell = parentShell;
		this.refactoring = refactoring;
	}

	/**
	 * Composes and shows the Refactoring Advice view.
	 */
	public void show() {
		try {
			final var graphClone = this.refactoring.getGraph();
			RefactoringGraphBuilder.append(graphClone, this.refactoring.getSelectedElement());

			// Create a new shell for the web view.
			final var shell = new Shell(parentShell, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
			shell.setText(graphClone.getRefactoringName());
			shell.setSize(800, 600);
			shell.setLayout(new FillLayout());

			// A SashForm enables vertical stacking of UI components.
			final var sashForm = new SashForm(shell, SWT.VERTICAL);
			sashForm.setLayout(new FillLayout());

			// Create a browser widget.
			final var browser = new Browser(sashForm, SWT.NONE);
			browser.addLocationListener(new LocationListener() {
				@Override
				public void changing(LocationEvent event) {
					final var url = event.location;
					final var resourceUriPrefixString = resourceUriPrefix.toString();
					if (url.startsWith(resourceUriPrefixString)) {
						event.doit = false; // Prevent navigation
						final var urlArgument = url.substring(resourceUriPrefixString.length());
						openJavaFileInEditor(urlArgument);
					}
				}

				@Override
				public void changed(LocationEvent event) {
					// Do nothing
				}
			});

			// Set the initial HTML content.
			browser.setText(renderRefactoringAdviceHtml(graphClone, this.resourceUriPrefix));
			browser.setSize(800, 400);

			// This composite will contain input controls for refactoring parameters.
			final var inputsComposite = this.refactoring.createComposite(sashForm);
			inputsComposite.addGraphChangedListener(event -> {
				final var changedGraph = event.getGraph();
				final var changedRefactoringAdviceHtml =
						renderRefactoringAdviceHtml(changedGraph, this.resourceUriPrefix);
				browser.setText(changedRefactoringAdviceHtml);
			});

			// Centre the shell on the parent.
			centerShell(shell, parentShell);

			// Open the shell.
			shell.open();

			// Event loop.
			final var display = parentShell.getDisplay();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Centre a shell relative to its parent.
	 * 
	 * @param shell       The shell to centre.
	 * @param parentShell The parent shell.
	 */
	private static void centerShell(Shell shell, Shell parentShell) {
		if (parentShell != null) {
			// Calculate position to centre relative to parent.
			final var parentX = parentShell.getLocation().x;
			final var parentY = parentShell.getLocation().y;
			final var parentWidth = parentShell.getSize().x;
			final var parentHeight = parentShell.getSize().y;

			final var shellWidth = shell.getSize().x;
			final var shellHeight = shell.getSize().y;

			var x = parentX + (parentWidth - shellWidth) / 2;
			var y = parentY + (parentHeight - shellHeight) / 2;

			// Ensure the shell stays within screen bounds.
			x = Math.max(0, x);
			y = Math.max(0, y);

			shell.setLocation(x, y);
		} else {
			// Centre on screen if no parent.
			shell.setLocation(
					(Display.getDefault().getBounds().width - shell.getSize().x) / 2,
					(Display.getDefault().getBounds().height - shell.getSize().y) / 2);
		}
	}

	private static String renderRefactoringAdviceHtml(final Graph graph, final URI eclipseResourceUriPrefix) {
		Document htmlDocument;
		Element htmlElement;
		try {
			htmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			htmlDocument.setXmlStandalone(true);

			// <html>
			htmlElement = htmlDocument.createElement("html");
			htmlDocument.appendChild(htmlElement);

			// <head>
			final var headElement = htmlDocument.createElement("head");
			final var titleElement = htmlDocument.createElement("title");
			titleElement.appendChild(htmlDocument.createTextNode(graph.getRefactoringName()));
			headElement.appendChild(titleElement);
			htmlElement.appendChild(headElement);

			// <body>
			final var bodyElement = htmlDocument.createElement("body");

			// PlantUML Class Diagram » SVG Class Diagram » <section>
			appendClassDiagram(graph, bodyElement);

			// Advice Text
			appendAdviceText(graph, bodyElement, eclipseResourceUriPrefix);

			htmlElement.appendChild(bodyElement);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "<html><head><title>Error creating HTML document</title></head><body></body></html>";
		}

		final var htmlWriter = new StringWriter();
		try {
			final var transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(new DOMSource(htmlElement), new StreamResult(htmlWriter));
		} catch (TransformerException ex) {
			ex.printStackTrace();
			htmlWriter.write("<html><head><title>Error loading HTML</title></head><body></body></html>");
		}
		return htmlWriter.toString();
	}

	private static void appendClassDiagram(final Graph graph, final Element bodyElement) {
		try {
			// Generate Class Diagram.
			final var htmlDocument = bodyElement.getOwnerDocument();
			final var classDiagramSection = htmlDocument.createElement("section");
			final var classDiagramPlantUmlStringWriter = new StringWriter();
			final var classDiagramPlantUmlWriter =
					new GraphPlantUmlClassDiagramWriter(classDiagramPlantUmlStringWriter);
			classDiagramPlantUmlWriter.write(graph);
			final var classDiagramSvgString =
					GraphPlantUmlSvgGenerator.generate(classDiagramPlantUmlStringWriter.toString());
			final var classDiagramSvgDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			final var classDiagramSvgDocument =
					classDiagramSvgDocumentBuilder.parse(new InputSource(new StringReader(classDiagramSvgString)));
			final var classDiagramSvgElement = classDiagramSvgDocument.getDocumentElement();
			classDiagramSection.appendChild(htmlDocument.importNode(classDiagramSvgElement, true));
			bodyElement.appendChild(classDiagramSection);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void appendAdviceText(
			final Graph graph,
			final Element bodyElement,
			final URI eclipseResourceUriPrefix) {
		try {
			// Generate advice text.
			final var htmlDocument = bodyElement.getOwnerDocument();
			final var adviceTextSection = htmlDocument.createElement("section");

			final var nlpLanguage = NLPLanguageEnglishGreatBritain.INSTANCE;
			final var nlpGrammarProcessor = new NLPGrammarProcessor(nlpLanguage);

			final var graphHtmlTextWriter =
					new GraphHtmlTextWriter(
							new GraphHtmlWriterSettings(eclipseResourceUriPrefix),
							adviceTextSection,
							nlpGrammarProcessor);
			graphHtmlTextWriter.write(graph);

			bodyElement.appendChild(adviceTextSection);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void openJavaFileInEditor(String path) {
		try {
			final var filePathAndLocation = path.split("#");
			final var file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(filePathAndLocation[0]));

			final var workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			final var editor = IDE.openEditor(workbenchPage, file);

			if (editor instanceof ITextEditor) {
				final var textEditor = (ITextEditor) editor;
				final var document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
				final var documentRange = filePathAndLocation[1];
				final var documentRangeStartEnd = documentRange.split("_");
				final var documentRangeStart = documentRangeStartEnd[0];
				final var documentRangeStartLineColumn = documentRangeStart.split(":");
				final var documentRangeEnd = documentRangeStartEnd[1];
				final var documentRangeEndLineColumn = documentRangeEnd.split(":");
				
				final var lineStart = Integer.parseInt(documentRangeStartLineColumn[0]);
				final var columnStart = Integer.parseInt(documentRangeStartLineColumn[1]);
				final var lineStartRegion = document.getLineInformation(lineStart - 1);
				final var startOffset = lineStartRegion.getOffset() + Math.min(columnStart - 1, lineStartRegion.getLength());
				
				final var lineEnd = Integer.parseInt(documentRangeEndLineColumn[0]);
				final var columnEnd = Integer.parseInt(documentRangeEndLineColumn[1]);
				final var lineEndRegion = document.getLineInformation(lineEnd - 1);
				final var endOffset = lineEndRegion.getOffset() + Math.min(columnEnd - 1, lineEndRegion.getLength());
				
				textEditor.selectAndReveal(startOffset, endOffset - startOffset + 1);
			}
		} catch (CoreException e) {
			e.printStackTrace();
			LOGGER.error("'Failed to open file " + path, e);
		} catch (BadLocationException e) {
			e.printStackTrace();
			LOGGER.error("Failed to focus on relevant line", e);
		}
	}
}