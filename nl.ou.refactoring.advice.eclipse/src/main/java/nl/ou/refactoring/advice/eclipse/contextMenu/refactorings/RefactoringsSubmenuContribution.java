package nl.ou.refactoring.advice.eclipse.contextMenu.refactorings;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;

/**
 * Dynamically creates menu items for the "Refactoring Advice" sub-menu.
 * This contribution appears in the context menus of both the Package Explorer
 * and the Java Editor, providing refactoring advice options for the selected
 * or right-clicked Java element.
 */
public final class RefactoringsSubmenuContribution extends CompoundContributionItem {
	/**
	 * Initialises a new instance of {@link RefactoringsSubmenuContribution}.
	 */
	public RefactoringsSubmenuContribution() {
		super();
	}

	/**
	 * Initialises a new instance of {@link RefactoringsSubmenuContribution}.
	 * @param id The identifier of the contribution.
	 */
	public RefactoringsSubmenuContribution(String id) {
		super(id);
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	protected IContributionItem[] getContributionItems() {
		final IJavaElement element = getCurrentElement();
		if (element == null) {
			return new IContributionItem[0];
		}

		return this.createItemsForElement(element);
	}

	/**
	 * Gets the current Java element from either the selection or the active editor.
	 * For Package Explorer: gets the selected Java element.
	 * For Java Editor: gets the Java element at cursor position or from the selection.
	 * @return the current Java element, or null if none can be determined.
	 */
	private IJavaElement getCurrentElement() {
		// First try to get the element from the selection.
		final var window =
			PlatformUI
				.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window == null) return null;

		// Try selection-based approach first.
		final ISelection selection =
			window
				.getSelectionService()
				.getSelection();
		if (selection != null && !selection.isEmpty()) {
			final var element = this.getElementFromSelection(selection);
			if (element != null) {
				return element;
			}
		}

		// If no element found from selection, try to get it from the active editor.
		final var page = window.getActivePage();
		if (page != null) {
			final var editor = page.getActiveEditor();
			if (editor != null) {
				return this.getElementFromEditor(editor);
			}
		}

		return null;
	}

	/**
	 * Extracts a Java element from a structured or text selection.
	 * @param selection The current selection.
	 * @return The Java Element, or null if not applicable.
	 */
	private IJavaElement getElementFromSelection(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			final var first = ((IStructuredSelection)selection).getFirstElement();
			if (first instanceof IJavaElement) return (IJavaElement)first;
			if (first instanceof IAdaptable) {
				final var adapted = ((IAdaptable)first).getAdapter(IJavaElement.class);
				if (adapted instanceof IJavaElement) return (IJavaElement)adapted;
			}
		} else if (selection instanceof ITextSelection) {
			// For text selections in the Java Editor, try to get the element at the offset.
			final var textSelection = (ITextSelection)selection;
			final var window =
				PlatformUI
					.getWorkbench()
					.getActiveWorkbenchWindow();
			if (window != null) {
				final var page = window.getActivePage();
				if (page != null) {
					final var editor = page.getActiveEditor();
					if (editor != null) {
						return this.getElementAtOffset(editor, textSelection.getOffset());
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets the Java element from the active editor at a specific offset.
	 * @param editor The active editor.
	 * @return The Java element at cursor position, or null.
	 */
	private IJavaElement getElementFromEditor(IEditorPart editor) {
		// Try to get selection provider from the editor (works for text editors).
		if (editor instanceof ISelectionProvider) {
			final var selectionProvider = (ISelectionProvider)editor;
			final var editorSelection = selectionProvider.getSelection();
			if (editorSelection instanceof ITextSelection) {
				final var textSelection = (ITextSelection)editorSelection;
				return this.getElementAtOffset(editor, textSelection.getOffset());
			}
		}
		return null;
	}

	/**
	 * Gets the Java element at a specific offset within an editor.
	 * @param editor The editor.
	 * @param offset The character offset.
	 * @return The Java Element at the offset, or null.
	 */
	private IJavaElement getElementAtOffset(IEditorPart editor, int offset) {
		try {
			// Try to get the Java Element from the editor input.
			final var element = JavaUI.getEditorInputJavaElement(editor.getEditorInput());
			if (element instanceof ITypeRoot) {
				final var root = (ITypeRoot)element;
				final var elements = root.codeSelect(offset, 0);
				if (elements != null && elements.length > 0) {
					return elements[0];
				}
				// Fallback: try to get the element at the offset directly.
				if (element instanceof ICompilationUnit) {
					return ((ICompilationUnit)element).getElementAt(offset);
				}
			}
			return element;
		} catch (Exception e) {
			// Log or ignore; return null to hide the menu.
			System.err.println("Error getting Java element at offset: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private IContributionItem[] createItemsForElement(IJavaElement element) {
		final java.util.List<IContributionItem> items = new java.util.ArrayList<>();

		if (element instanceof IType) {
			items.add(this.createItem("Rename Class", "renameClass", element));
		} else if (element instanceof IMethod) {
			items.add(this.createItem("Rename Method", "renameMethod", element));
		} else if (element instanceof IField) {
			items.add(this.createItem("Rename Field", "renameField", element));
		} else if (element instanceof ILocalVariable) {
			items.add(createItem("Analyze Variable for Refactoring", "analyzeVariable", element));
			items.add(createItem("Replace Temp with Query", "replaceTempWithQuery", element));
		} else if (element instanceof ICompilationUnit) {
			items.add(createItem("Analyze File for Refactoring", "analyzeFile", element));
			items.add(createItem("Show File Smells", "showFileSmells", element));
		} else if (element instanceof IPackageFragment) {
			items.add(createItem("Analyze Package for Refactoring", "analyzePackage", element));
			items.add(createItem("Show Package Smells", "showPackageSmells", element));
		} else {
			items.add(createItem("Analyze Element for Refactoring", "analyzeElement", element));
		}

		items.add(new Separator());
		items.add(createItem("Show Refactoring Graph", "showRefactoringGraph", element));

		return items.toArray(new IContributionItem[0]);
	}

	private IContributionItem createItem(String text, String commandId, IJavaElement javaElement) {
		final IAction action = new Action(text) {
			@Override
			public void run() {
				System.out.println("Refactoring Advice: " + commandId + " on " + javaElement.getElementName());
			}
		};
		return new ActionContributionItem(action);
	}
}
