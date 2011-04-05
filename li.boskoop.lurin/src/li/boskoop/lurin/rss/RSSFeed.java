package li.boskoop.lurin.rss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data class for RSS feeds.
 * 
 * @author buergich
 */
public class RSSFeed {

	private static final String EMPTY = "";

	private final String title;
	private final String link;
	private final String description;
	private final String language;
	private final String copyright;
	private final String pubDate;

	private final List<RSSItem> items = new ArrayList<RSSItem>();

	/**
	 * Default constructor. All data must be provided at instantiation since
	 * data is read-only (except items).
	 * 
	 * @param title
	 * @param link
	 * @param description
	 * @param language
	 * @param copyright
	 * @param pubDate
	 */
	public RSSFeed(String title, String link, String description,
			String language, String copyright, String pubDate) {
		this.title = (title == null) ? EMPTY : title;
		this.link = (link == null) ? EMPTY : link;
		this.description = (description == null) ? EMPTY : description;
		this.language = (language == null) ? EMPTY : language;
		this.copyright = (copyright == null) ? EMPTY : copyright;
		this.pubDate = (pubDate == null) ? EMPTY : pubDate;
	}

	/**
	 * Creates an empty feed for dummy use.
	 * 
	 * @return
	 */
	public static RSSFeed createEmptyFeed() {
		return new RSSFeed(null, null, null, null, null, null);
	}

	/**
	 * Returns a List of all RSSItems in this feed.
	 * 
	 * @return
	 */
	public List<RSSItem> getItems() {
		return Collections.unmodifiableList(items);
	}

	/**
	 * Adds a RSSItem to this feed.
	 * 
	 * @return
	 */

	public void addItem(RSSItem item) {
		items.add(item);
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
	 * Getter for link.
	 * 
	 * @return
	 */
	public String getLink() {
		return link;
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
	 * Getter for language.
	 * 
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Getter for copyright.
	 * 
	 * @return
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * Getter for pubDate.
	 * 
	 * @return
	 */
	public String getPubDate() {
		return pubDate;
	}

}
