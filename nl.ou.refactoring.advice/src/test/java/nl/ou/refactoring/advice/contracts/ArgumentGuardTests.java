package nl.ou.refactoring.advice.contracts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ArgumentGuardTests {
	@Test
	@DisplayName("Should throw an exception if a value is null (cannot be null)")
	public void requireNotNullWithNullTest() {
		Object valueNull = null;
		ArgumentNullException exception =
				Assertions.assertThrowsExactly(ArgumentNullException.class, () -> {
					ArgumentGuard.requireNotNull(valueNull, "valueNull");
				});
		Assertions.assertEquals("valueNull", exception.getParameterName());
	}
	
	@Test
	@DisplayName("Should not throw an exception if a value is not null (cannot be null)")
	public void requireNotNullWithNotNullTest() {
		Object valueNotNull = "test";
		Assertions.assertDoesNotThrow(() -> {
			ArgumentGuard.requireNotNull(valueNotNull, "valueNotNull");
		});
	}
	
	@Test
	@DisplayName("Should throw an exception if a value is null (cannot be null, empty or contain only white spaces)")
	public void requireNotNullEmptyOrWhiteSpaceWithNullTest() {
		String valueNull = null;
		ArgumentNullException exception =
				Assertions.assertThrowsExactly(ArgumentNullException.class, () -> {
					ArgumentGuard.requireNotNullEmptyOrWhiteSpace(valueNull, "valueNull");
				});
		Assertions.assertEquals("valueNull", exception.getParameterName());
	}
	
	@Test
	@DisplayName("Should throw an exception if a value is empty (cannot be null, empty or contain only white spaces)")
	public void requireNotNullEmptyOrWhiteSpaceWithEmptyTest() {
		String valueEmpty = "";
		ArgumentEmptyException exception =
				Assertions.assertThrowsExactly(ArgumentEmptyException.class, () -> {
					ArgumentGuard.requireNotNullEmptyOrWhiteSpace(valueEmpty, "valueEmpty");
				});
		Assertions.assertEquals("valueEmpty", exception.getParameterName());
	}
	
	@Test
	@DisplayName("Should throw an exception if a value contains only white spaces (cannot be null, empty or contain only white spaces)")
	public void requireNotNullEmptyOrWhiteSpaceWithWhiteSpaceTest() {
		String valueWhiteSpace = " ";
		ArgumentEmptyException exception =
				Assertions.assertThrowsExactly(ArgumentEmptyException.class, () -> {
					ArgumentGuard.requireNotNullEmptyOrWhiteSpace(valueWhiteSpace, "valueWhiteSpace");
				});
		Assertions.assertEquals("valueWhiteSpace", exception.getParameterName());
	}
	
	@Test
	@DisplayName("Should not throw an exception if a value is not null, not empty and does not contain only white space (cannot be null, empty or contain only white spaces)")
	public void requireNotNullEmptyOrWhiteSpaceWithValidTest() {
		String valueValid = "valid";
		Assertions.assertDoesNotThrow(() -> {
			ArgumentGuard.requireNotNullEmptyOrWhiteSpace(valueValid, "valueValid");
		});
	}
}
