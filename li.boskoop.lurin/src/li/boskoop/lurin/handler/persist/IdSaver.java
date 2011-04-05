package li.boskoop.lurin.handler.persist;

import java.io.Serializable;

/**
 * A serializeable class for saving the id of the last handled status id.
 * 
 * @author buergich
 * 
 */
public class IdSaver implements Serializable {

	private static final long serialVersionUID = 6092439592007872025L;

	private long id;

	private boolean validId;

	/**
	 * Default constructor. Creates a IdSaver with an invalid id.
	 */
	public IdSaver() {
		this.id = 0L;
		this.validId = false;
	}

	/**
	 * Getter for id.
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter for id.
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Getter for validId.
	 * 
	 * @return
	 */
	public boolean isValidId() {
		return validId;
	}

	/**
	 * Setter for validId.
	 * 
	 * @param validId
	 */
	public void setValidId(boolean validId) {
		this.validId = validId;
	}

}
