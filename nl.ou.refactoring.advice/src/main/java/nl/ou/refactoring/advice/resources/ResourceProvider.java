package nl.ou.refactoring.advice.resources;

import java.util.Locale;
import java.util.ResourceBundle;

import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClassStereotype;

/**
 * Facilitates retrieving localised resource values.
 */
public final class ResourceProvider {
	private ResourceProvider() { }
	
	/**
	 * Retrieves localised exception message templates.
	 */
	public final class ExceptionMessages {
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
		 * @return The message template for the specified classType, in the specified locale.
		 */
		public static String getMessageTemplate(Class<? extends Exception> cls, Locale locale) {
			return ResourceBundle.getBundle(BUNDLE_NAME, locale).getString(cls.getSimpleName());
		}
	}
	
	/**
	 * Retrieves localised captions for Refactoring Advice Graph nodes.
	 */
	public final class GraphNodeCaptions {
		private static final String BUNDLE_NAME = "GraphNodeCaptions";
		private GraphNodeCaptions() { }
		
		/**
		 * Gets the localised caption for the specified classType, in the default {@link Locale}.
		 * @param classType The type of the class of {@link GraphNode}.
		 * @return The localised caption for the specified classType.
		 */
		public static String getCaption(Class<? extends GraphNode> classType) {
			return ResourceBundle.getBundle(BUNDLE_NAME).getString(classType.getSimpleName());
		}
		
		/**
		 * Gets the localised caption for the specified classType, in the specified locale.
		 * @param classType The type of the class of {@link GraphNode}.
		 * @return The localised caption for the specified classType, in the specified locale.
		 */
		public static String getCaption(Class<? extends GraphNode> classType, Locale locale) {
			return ResourceBundle.getBundle(BUNDLE_NAME, locale).getString(classType.getSimpleName());
		}
	}
	
	/**
	 * Retrieves localised display names for Class stereotypes.
	 */
	public final class GraphNodeClassStereotypeDisplayNames {
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
		 * @return The localised display name for the specified class stereotype, in the specified locale.
		 */
		public static String getDisplayName(GraphNodeClassStereotype stereotype, Locale locale) {
			return ResourceBundle.getBundle(BUNDLE_NAME, locale).getString(stereotype.getDisplayName());
		}
	}
}
