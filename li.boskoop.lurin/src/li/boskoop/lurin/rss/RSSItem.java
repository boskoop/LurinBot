package li.boskoop.lurin.rss;

/**
 * Data class for feed items.
 * 
 * @author buergich
 */
public class RSSItem {

	private static final String EMPTY = "";

	private final String title;
	private final String description;
	private final String link;
	private final String pubDate;
	private final String author;
	private final String guid;

	/**
	 * Default constructor. All data must be provided at instantiation since
	 * data is read-only.
	 * 
	 * @param title
	 * @param description
	 * @param link
	 * @param pubDate
	 * @param author
	 * @param guid
	 */
	public RSSItem(String title, String description, String link,
			String pubDate, String author, String guid) {
		this.title = (title == null) ? EMPTY : title;
		this.description = (description == null) ? EMPTY : description;
		this.link = (link == null) ? EMPTY : link;
		this.pubDate = (pubDate == null) ? EMPTY : pubDate;
		this.author = (author == null) ? EMPTY : author;
		this.guid = (guid == null) ? EMPTY : guid;
	}

	/**
	 * Getter for title.
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Getter for description.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Getter for link.
	 * 
	 * @return
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Getter for pubDate.
	 * 
	 * @return
	 */
	public String getPubDate() {
		return pubDate;
	}

	/**
	 * Getter for author.
	 * 
	 * @return
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Getter for guid.
	 * 
	 * @return
	 */
	public String getGuid() {
		return guid;
	}

}
