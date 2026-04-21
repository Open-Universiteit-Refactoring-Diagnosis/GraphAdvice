package nl.ou.refactoring.advice.eclipse;

import java.io.StringWriter;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Document;

/**
 * Web view for displaying refactoring advice in HTML format.
 */
public final class RefactoringAdviceWebView {
    /**
     * Show a web view with the given HTML content.
     * @param parentShell The parent shell.
     * @param title The title of the web view.
     * @param htmlContent The HTML content to display.
     */
    public static void showWebView(Shell parentShell, String title, Document htmlContent) {
        // Create a new shell for the web view.
        final var shell = new Shell(parentShell, SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
        shell.setText(title);
        shell.setSize(800, 600);
        shell.setLayout(new FillLayout());

        // Create a browser widget.
        final var browser = new Browser(shell, SWT.NONE);
        
        // Set the HTML content.
        final var htmlWriter = new StringWriter();
        try {
	        final var transformer =
	        	TransformerFactory
	        		.newInstance()
	        		.newTransformer();
	        transformer.transform(new DOMSource(htmlContent), new StreamResult(htmlWriter));
        } catch (TransformerException ex) {
        	ex.printStackTrace();
        	htmlWriter.write("<html><head><title>Error loading HTML</title></head><body></body></html>");
        }
        browser.setText(htmlWriter.toString());

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
    }

    /**
     * Centre a shell relative to its parent.
     * @param shell The shell to centre.
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
                (Display.getDefault().getBounds().height - shell.getSize().y) / 2
            );
        }
    }
}