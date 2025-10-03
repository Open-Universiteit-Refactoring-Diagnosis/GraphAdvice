package ou.graphAdvice.contracts;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class ArgumentGuardTests {
	@Test
	public void requireNotNullWithNullTest() {
		Object valueNull = null;
		ArgumentNullException exception =
				Assertions.assertThrowsExactly(ArgumentNullException.class, () -> {
					ArgumentGuard.requireNotNull(valueNull, "valueNull");
				});
		Assertions.assertEquals("valueNull", exception.getParameterName());
	}
	
	@Test
	public void requireNotNullWithNotNullTest() {
		Object valueNotNull = "test";
		Assertions.assertDoesNotThrow(() -> {
			ArgumentGuard.requireNotNull(valueNotNull, "valueNotNull");
		});
	}
	
	@Test
	public void requireNotNullEmptyOrWhiteSpaceWithNullTest() {
		String valueNull = null;
		ArgumentNullException exception =
				Assertions.assertThrowsExactly(ArgumentNullException.class, () -> {
					ArgumentGuard.requireNotNullEmptyOrWhiteSpace(valueNull, "valueNull");
				});
		Assertions.assertEquals("valueNull", exception.getParameterName());
	}
	
	@Test
	public void requireNotNullEmptyOrWhiteSpaceWithEmptyTest() {
		String valueEmpty = "";
		ArgumentEmptyException exception =
				Assertions.assertThrowsExactly(ArgumentEmptyException.class, () -> {
					ArgumentGuard.requireNotNullEmptyOrWhiteSpace(valueEmpty, "valueEmpty");
				});
		Assertions.assertEquals("valueEmpty", exception.getParameterName());
	}
	
	@Test
	public void requireNotNullEmptyOrWhiteSpaceWithWhiteSpaceTest() {
		String valueWhiteSpace = " ";
		ArgumentEmptyException exception =
				Assertions.assertThrowsExactly(ArgumentEmptyException.class, () -> {
					ArgumentGuard.requireNotNullEmptyOrWhiteSpace(valueWhiteSpace, "valueWhiteSpace");
				});
		Assertions.assertEquals("valueWhiteSpace", exception.getParameterName());
	}
	
	@Test
	public void requireNotNullEmptyOrWhiteSpaceWithValidTest() {
		String valueValid = "valid";
		Assertions.assertDoesNotThrow(() -> {
			ArgumentGuard.requireNotNullEmptyOrWhiteSpace(valueValid, "valueValid");
		});
	}
}
