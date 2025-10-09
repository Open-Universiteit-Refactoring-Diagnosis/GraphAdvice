package nl.ou.refactoring.advice.nodes.workflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepUpdateReferences;

public final class GraphNodeWorkflowActionTests {
	@Test
	@DisplayName("Should get the directly preceding microstep")
	public void getPreceding() {
		// Arrange
		final var graph = new Graph("Refactoring test");
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var updateReferences = new GraphNodeMicrostepUpdateReferences(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		addMethod.precedes(updateReferences);
		updateReferences.precedes(removeMethod);
		
		// Act
		final var precedingStart = addMethod.getPreceding();
		final var precedingMiddle = updateReferences.getPreceding();
		final var precedingEnd = removeMethod.getPreceding();
		
		// Assert
		assertEquals(null, precedingStart);
		assertEquals(addMethod, precedingMiddle);
		assertEquals(updateReferences, precedingEnd);
	}
	
	@Test
	@DisplayName("Should get the length of a chain of microsteps")
	public void getPrecedingLengthTest() {
		// Arrange
		final var graph = new Graph("Refactoring test");
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var updateReferences = new GraphNodeMicrostepUpdateReferences(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		addMethod.precedes(updateReferences);
		updateReferences.precedes(removeMethod);
		
		// Act
		final var sameNodeLength = removeMethod.getPrecedingLength(removeMethod);
		final var directlyPrecedingLength = removeMethod.getPrecedingLength(updateReferences);
		final var indirectlyPrecedingLength = removeMethod.getPrecedingLength(addMethod);
		final var middleLength = updateReferences.getPrecedingLength(addMethod);
		final var next0Length = updateReferences.getPrecedingLength(removeMethod);
		final var next1Length = addMethod.getPrecedingLength(updateReferences);
		final var reverseChainLength = addMethod.getPrecedingLength(removeMethod);
		
		// Assert
		assertEquals(-1, sameNodeLength);
		assertEquals(1, directlyPrecedingLength);
		assertEquals(2, indirectlyPrecedingLength);
		assertEquals(1, middleLength);
		assertEquals(-1, next0Length);
		assertEquals(-1, next1Length);
		assertEquals(-1, reverseChainLength);
	}
	
	@Test
	@DisplayName("Should correctly determine whether a microstep is preceded by another microstep")
	public void isPrecededByTest() {
		// Arrange
		final var graph = new Graph("Refactoring test");
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var updateReferences = new GraphNodeMicrostepUpdateReferences(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		addMethod.precedes(updateReferences);
		updateReferences.precedes(removeMethod);
		
		// Act
		final var isPrecededSame = addMethod.isPrecededBy(addMethod);
		final var isPrecededNext = addMethod.isPrecededBy(updateReferences);
		final var isPrecededDirect = updateReferences.isPrecededBy(addMethod);
		final var isPrecededIndirect = removeMethod.isPrecededBy(addMethod);
		
		// Assert
		assertFalse(isPrecededSame);
		assertFalse(isPrecededNext);
		assertTrue(isPrecededDirect);
		assertTrue(isPrecededIndirect);
	}
}
