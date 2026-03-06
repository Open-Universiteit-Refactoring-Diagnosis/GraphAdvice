package nl.ou.refactoring.advice.resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClassStereotype;

/**
 * Facilitates retrieving localised resource values.
 */
public final class ResourceProvider {
	private ResourceProvider() { }
	
	/**
	 * Retrieves localised exception message templates.
	 */
	public static final class ExceptionMessages {
		private static final String BUNDLE_NAME = "ExceptionMessages";
		private ExceptionMessages() { }
		
		/**
		 * Gets the message template for the specified classType, in the default {@link Locale}.
		 * @param classType The type of the class of {@link Exception}.
		 * @return The message template for the specified classType, in the default {@link Locale}.
		 */
		public static String getMessageTemplate(Class<? extends Exception> classType) {
			return ResourceBundle.getBundle(BUNDLE_NAME).getString(classType.getSimpleName());
		}
		
		/**
		 * Gets the message template for the specified classType, in the specified locale.
		 * @param classType The type of the class of {@link Exception}.
		 * @param locale The locale for which to retrieve the message template.
		 * @return The message template for the specified classType, in the specified locale.
		 */
		public static String getMessageTemplate(Class<? extends Exception> classType, Locale locale) {
			return ResourceBundle.getBundle(BUNDLE_NAME, locale).getString(classType.getSimpleName());
		}
	}
	
	/**
	 * Retrieves localised labels for Refactoring Advice Graph edges.
	 */
	public static final class GraphEdgeLabels {
		private static final String BUNDLE_NAME = "GraphEdgeLabels";
		private GraphEdgeLabels() { }
		
		/**
		 * Gets the localised label for the specified edgeType.
		 * @param edgeType The type of the edge.
		 * @return The localised label for the specified edgeType.
		 */
		public static String getLabel(Class<? extends GraphEdge> edgeType) {
			return ResourceBundle.getBundle(BUNDLE_NAME).getString(edgeType.getSimpleName());
		}
		
		/**
		 * Gets the localised label for the specified edgeType, in the specified locale.
		 * @param edgeType The type of the edge.
		 * @param locale The locale for which to retrieve the localised label.
		 * @return The localised label for the specified edgeType, in the specified locale.
		 */
		public static String getLabel(Class<? extends GraphEdge> edgeType, Locale locale) {
			return ResourceBundle.getBundle(BUNDLE_NAME, locale).getString(edgeType.getSimpleName());
		}
	}
	
	/**
	 * Retrieves localised captions for Refactoring Advice Graph nodes.
	 */
	public static final class GraphNodeCaptions {
		private static final String BUNDLE_NAME = "GraphNodeCaptions";
		private GraphNodeCaptions() { }
		
		/**
		 * Gets the localised caption for the specified classType, in the default {@link Locale}.
		 * @param classType The type of the class of {@link GraphNodeBase}.
		 * @return The localised caption for the specified classType.
		 */
		public static String getCaption(Class<? extends GraphNodeBase> classType) {
			return ResourceBundle.getBundle(BUNDLE_NAME).getString(classType.getSimpleName());
		}
		
		/**
		 * Gets the localised caption for the specified classType, in the specified locale.
		 * @param classType The type of the class of {@link GraphNodeBase}.
		 * @param locale The {@link Locale} for which to get the caption.
		 * @return The localised caption for the specified classType, in the specified locale.
		 */
		public static String getCaption(Class<? extends GraphNodeBase> classType, Locale locale) {
			return ResourceBundle.getBundle(BUNDLE_NAME, locale).getString(classType.getSimpleName());
		}
	}
	
	/**
	 * Retrieves localised display names for Class stereotypes.
	 */
	public static final class GraphNodeClassStereotypeDisplayNames {
		private static final String BUNDLE_NAME = "GraphNodeClassStereotypeDisplayNames";
		private GraphNodeClassStereotypeDisplayNames() { }
		
		/**
		 * Gets the localised display name for the specified class stereotype, in the default {@link Locale}.
		 * @param stereotype The class stereotype.
		 * @return The localised display name for the specified class stereotype.
		 */
		public static String getDisplayName(GraphNodeClassStereotype stereotype) {
			return ResourceBundle.getBundle(BUNDLE_NAME).getString(stereotype.getDisplayName());
		}
		
		/**
		 * Gets the localised display name for the specified class stereotype, in the specified locale.
		 * @param stereotype The class stereotype.
		 * @param locale The {@link Locale} for which to get the display name.
		 * @return The localised display name for the specified class stereotype, in the specified locale.
		 */
		public static String getDisplayName(GraphNodeClassStereotype stereotype, Locale locale) {
			return ResourceBundle.getBundle(BUNDLE_NAME, locale).getString(stereotype.getDisplayName());
		}
	}
	
	/**
	 * Gets a reader for a resource file.
	 * @param classLoader The class loader that is aware of resources.
	 * @param fileName The file name of the resource file.
	 * @return A reader for a resource file.
	 */
	public static Reader getReader(ClassLoader classLoader, String fileName) {
		final var inputStream = classLoader.getResourceAsStream(fileName);
		if (inputStream == null) {
			throw new IllegalArgumentException(String.format("Resource file '%s' not found", fileName));
		}
		return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	}
}
