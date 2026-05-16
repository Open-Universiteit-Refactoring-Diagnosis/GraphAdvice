package nl.ou.refactoring.advice.eclipse;

import org.eclipse.jdt.core.ILocalVariable;

/**
 * Resolves type signatures from the Eclipse JDT to the Refactoring Advice Graph (RAG) model.
 */
public final class TypeSignatureResolver {
	private TypeSignatureResolver() { }
	
	/**
	 * Resolves the type name of a local variable.
	 * @param localVariable The local variable of which to resolve the type name.
	 * @return The type name of the local variable.
	 */
	public static String resolveName(ILocalVariable localVariable) {
		final var typeSignature = localVariable.getTypeSignature();
		
		return switch (typeSignature) {
			case "B" -> "byte";
			case "C" -> "char";
			case "D" -> "double";
			case "F" -> "float";
			case "I" -> "int";
			case "J" -> "long";
			case "S" -> "short";
			case "V" -> "void";
			case "Z" -> "boolean";
			default -> {
				if (typeSignature.startsWith("T")) {
					// "T" + Identifier + ";" // type variable
					yield typeSignature.substring(1, typeSignature.length() - 1);
				}
				if (typeSignature.startsWith("[")) {
					// "[" + TypeSignature // array X[]
					yield ""; // TODO not supported yet
				}
				if (typeSignature.startsWith("!")) {
					// "!" + TypeSignature // capture-of ?
					yield ""; // TODO not supported yet
				}
				if (typeSignature.startsWith("|")) {
					// "|" + TypeSignature + (":" + TypeSignature)+ // intersection type
					yield ""; // TODO not supported yet
				}
				if (typeSignature.startsWith("L")) {
					yield resolveResolvedClassTypeSignature(typeSignature);
				}
				if (typeSignature.startsWith("Q")) {
					yield resolveUnresolvedClassTypeSignature(typeSignature);
				}
				
				yield "object";
			}
		};
	}
	
	private static String resolveResolvedClassTypeSignature(String typeSignatureString) {
		return typeSignatureString.substring(1, typeSignatureString.length() - 1);
	}
	
	private static String resolveUnresolvedClassTypeSignature(String typeSignatureString) {
		return typeSignatureString.substring(1, typeSignatureString.length() - 1);
	}
}