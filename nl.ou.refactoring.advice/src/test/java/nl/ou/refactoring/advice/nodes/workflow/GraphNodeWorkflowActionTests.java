package nl.ou.refactoring.advice.nodes.workflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;

public final class GraphNodeWorkflowActionTests {
	@Test
	@DisplayName("Should get the directly preceding microstep")
	public void getPreceding() {
		// Arrange
		final var graph = new Graph("Refactoring test");
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var addExpression = new GraphNodeMicrostepAddExpression(graph);
		final var removeExpression = new GraphNodeMicrostepRemoveExpression(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		addMethod.precedes(addExpression);
		addExpression.precedes(removeExpression);
		removeExpression.precedes(removeMethod);
		
		// Act
		final var precedingStart = addMethod.getPreceding();
		final var precedingSecond = addExpression.getPreceding();
		final var precedingThird = removeExpression.getPreceding();
		final var precedingEnd = removeMethod.getPreceding();
		
		// Assert
		assertEquals(null, precedingStart);
		assertEquals(addMethod, precedingSecond);
		assertEquals(addExpression, precedingThird);
		assertEquals(removeExpression, precedingEnd);
	}
	
	@Test
	@DisplayName("Should get the length of a chain of microsteps")
	public void getPrecedingLengthTest() {
		// Arrange
		final var graph = new Graph("Refactoring test");
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var addExpression = new GraphNodeMicrostepAddExpression(graph);
		final var removeExpression = new GraphNodeMicrostepRemoveExpression(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		addMethod.precedes(addExpression);
		addExpression.precedes(removeExpression);
		removeExpression.precedes(removeMethod);
		
		// Act
		final var sameNodeLength = removeMethod.getPrecedingLength(removeMethod);
		final var directlyPrecedingLength = removeMethod.getPrecedingLength(removeExpression);
		final var indirectlyPrecedingLength = removeMethod.getPrecedingLength(addMethod);
		final var middleLength = addExpression.getPrecedingLength(addMethod);
		final var next0Length = removeExpression.getPrecedingLength(removeMethod);
		final var next1Length = addMethod.getPrecedingLength(addExpression);
		final var reverseChainLength = addMethod.getPrecedingLength(removeMethod);
		
		// Assert
		assertEquals(-1, sameNodeLength);
		assertEquals(1, directlyPrecedingLength);
		assertEquals(3, indirectlyPrecedingLength);
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
		final var addExpression = new GraphNodeMicrostepAddExpression(graph);
		final var removeExpression = new GraphNodeMicrostepRemoveExpression(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		addMethod.precedes(addExpression);
		addExpression.precedes(removeExpression);
		removeExpression.precedes(removeMethod);
		
		// Act
		final var isPrecededSame = addMethod.isPrecededBy(addMethod);
		final var isPrecededNext = addMethod.isPrecededBy(addExpression);
		final var isPrecededDirect = addExpression.isPrecededBy(addMethod);
		final var isPrecededIndirect = removeMethod.isPrecededBy(addMethod);
		
		// Assert
		assertFalse(isPrecededSame);
		assertFalse(isPrecededNext);
		assertTrue(isPrecededDirect);
		assertTrue(isPrecededIndirect);
	}
}
