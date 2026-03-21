package nl.ou.refactoring.advice.contracts;

import java.util.Collection;

import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if an argument is not a member of a specified collection.
 */
public final class ArgumentMembershipException extends RuntimeException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 7817403788987340302L;
	private final Object item;
	private final Collection<Object> collection;
	private final String collectionName;
	
	/**
	 * Initialises a new instance of {@link ArgumentMembershipException}.
	 * @param item The item that is not a member of the specified collection.
	 * @param collection The collection to which the specified item should have belonged.
	 * @param collectionName The name of the collection.
	 */
	public ArgumentMembershipException(Object item, Collection<Object> collection, String collectionName) {
		this.item = item;
		this.collection = collection;
		this.collectionName = collectionName;
	}
	
	@Override
	public String getLocalizedMessage() {
		final var messageTemplate =
			ResourceProvider
				.ExceptionMessages
				.getMessageTemplate(ArgumentMembershipException.class);
		return String.format(messageTemplate, this.item, this.collectionName);
	}
	
	/**
	 * Gets the item that is not a member of the specified collection.
	 * @return The collection to which the item should have belonged.
	 */
	public Object getItem() {
		return this.item;
	}
	
	/**
	 * Gets the collection to which the specified item should have belonged.
	 * @return The collection to which the specified item should have belonged.
	 */
	public Collection<Object> getCollection() {
		return this.collection;
	}
	
	/**
	 * Gets the name of the collection.
	 * @return The name of the collection.
	 */
	public String getCollectionName() {
		return this.collectionName;
	}
}
