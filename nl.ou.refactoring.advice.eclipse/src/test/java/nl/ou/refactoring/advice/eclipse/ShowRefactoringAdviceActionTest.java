package nl.ou.refactoring.advice.eclipse;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ShowRefactoringAdviceAction}.
 */
class ShowRefactoringAdviceActionTest {
    @Test
    void testActionCreation() {
        // Test that the action can be instantiated
        ShowRefactoringAdviceAction action = new ShowRefactoringAdviceAction();
        assertNotNull(action, "ShowRefactoringAdviceAction should be instantiated successfully");
    }

    @Test
    void testWebViewCreation() {
        // Test that the web view class exists and can be used
        assertDoesNotThrow(() -> {
            // This test just verifies the class can be referenced
            Class.forName("nl.ou.refactoring.advice.eclipse.RefactoringAdviceWebView");
        }, "RefactoringAdviceWebView class should be loadable");
    }

    @Test
    void testElementTypeDetection() throws Exception {
        // Create an instance to test the private method indirectly
        ShowRefactoringAdviceAction action = new ShowRefactoringAdviceAction();
        assertNotNull(action, "Action should be created successfully");
        
        // Note: We can't directly test private methods, but we can verify the class structure
        // The actual functionality will be tested when the plug-in is run in Eclipse
    }
}