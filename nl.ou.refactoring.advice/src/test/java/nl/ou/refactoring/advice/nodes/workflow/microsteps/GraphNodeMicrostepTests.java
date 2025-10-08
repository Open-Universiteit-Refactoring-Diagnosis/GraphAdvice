package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;

public final class GraphNodeMicrostepTests {
	@Test
	@DisplayName("Should get the directly preceding microstep")
	public void getPreceding() {
		// Arrange
		final var graph = new Graph();
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var updateReferences = new GraphNodeMicrostepUpdateReferences(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		addMethod.precedes(updateReferences);
		updateReferences.precedes(removeMethod);
		
		// Act
		final var preceding0 = addMethod.getPreceding();
		final var preceding1 = updateReferences.getPreceding();
		final var preceding2 = removeMethod.getPreceding();
		
		// Assert
		assertEquals(null, preceding0);
		assertEquals(addMethod, preceding1);
		assertEquals(updateReferences, preceding2);
	}
	
	@Test
	@DisplayName("Should get the length of a chain of microsteps")
	public void getPrecedingLengthTest() {
		// Arrange
		final var graph = new Graph();
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var updateReferences = new GraphNodeMicrostepUpdateReferences(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		addMethod.precedes(updateReferences);
		updateReferences.precedes(removeMethod);
		
		// Act
		final var length0 = removeMethod.getPrecedingLength(removeMethod);
		final var length1 = removeMethod.getPrecedingLength(updateReferences);
		final var length2 = removeMethod.getPrecedingLength(addMethod);
		
		// Assert
		assertEquals(-1, length0);
		assertEquals(1, length1);
		assertEquals(2, length2);
	}
	
	@Test
	@DisplayName("Should correctly determine whether a microstep is preceded by another microstep")
	public void isPrecededByTest() {
		// Arrange
		final var graph = new Graph();
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var updateReferences = new GraphNodeMicrostepUpdateReferences(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		addMethod.precedes(updateReferences);
		updateReferences.precedes(removeMethod);
		
		// Act
		final var isPreceded0 = addMethod.isPrecededBy(addMethod); // same
		final var isPreceded1 = addMethod.isPrecededBy(updateReferences); // next, not preceding
		final var isPreceded2 = updateReferences.isPrecededBy(addMethod); // directly preceding
		final var isPreceded3 = removeMethod.isPrecededBy(addMethod); // indirectly preceding
		
		// Assert
		assertFalse(isPreceded0);
		assertFalse(isPreceded1);
		assertTrue(isPreceded2);
		assertTrue(isPreceded3);
	}
}
