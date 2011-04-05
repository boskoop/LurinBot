package li.boskoop.lurin.rss;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

/**
 * An RSS parser using StAX.
 * 
 * @author buergich
 */
public class RSSParser {

	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String LANGUAGE = "language";
	public static final String COPYRIGHT = "copyright";
	public static final String LINK = "link";
	public static final String AUTHOR = "author";
	public static final String ITEM = "item";
	public static final String PUB_DATE = "pubDate";
	public static final String GUID = "guid";

	/**
	 * XML elements which are treated as content.
	 */
	public static final String[] CONTENT_ELEMENTS = { TITLE, DESCRIPTION,
			LANGUAGE, COPYRIGHT, LINK, AUTHOR, PUB_DATE, GUID };

	private static Logger log = Logger.getLogger(RSSParser.class);

	private final URL url;

	/**
	 * Default constructor.
	 * 
	 * @param feedUrl
	 */
	public RSSParser(String feedUrl) {
		try {
			this.url = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Checks for a StartElement of the given elementName.
	 * 
	 * @param event
	 * @param elementName
	 * @return
	 */
	private boolean checkStartElement(XMLEvent event, String elementName) {
		if (event.isStartElement()
				&& event.asStartElement().getName().getLocalPart()
						.toLowerCase().equals(elementName)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks for a StartElement of one of the {@link CONTENT_ELEMENTS}
	 * 
	 * @param event
	 * @return
	 */
	private boolean checkContentStartElement(XMLEvent event) {
		if (event.isStartElement()
				&& Arrays.asList(CONTENT_ELEMENTS).contains(
						event.asStartElement().getName().getLocalPart()
								.toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * Checks for an EndElement of the given elementName.
	 * 
	 * @param event
	 * @param elementName
	 * @return
	 */
	private boolean checkEndElement(XMLEvent event, String elementName) {
		if (event.isEndElement()
				&& event.asEndElement().getName().getLocalPart().toLowerCase()
						.equals(elementName)) {
			return true;
		}
		return false;
	}

	/**
	 * Parses the RSS-feed given to the constructor.
	 * 
	 * @return The parsed feed as an {@link RSSFeed} object.
	 */
	public RSSFeed parseFeed() {
		RSSFeed feed = null;
		InputStream in = null;
		XMLEventReader eventReader = null;

		try {
			boolean isFeedHeader = true;

			// Set header values intial to the empty string
			Map<String, String> values = new HashMap<String, String>();

			// First create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();

			// Setup a new eventReader
			in = url.openStream();
			eventReader = inputFactory.createXMLEventReader(in);

			// Read the XML document
			while (eventReader.hasNext()) {

				XMLEvent event = eventReader.nextEvent();

				if (checkStartElement(event, ITEM)) {

					// The first ITEM-Tag
					if (isFeedHeader) {
						isFeedHeader = false;
						feed = new RSSFeed(values.get(TITLE), values.get(LINK),
								values.get(DESCRIPTION), values.get(LANGUAGE),
								values.get(COPYRIGHT), values.get(PUB_DATE));
						values = new HashMap<String, String>();
					}
					event = eventReader.nextEvent();
				} else if (checkContentStartElement(event)) {
					String eventName = event.asStartElement().getName()
							.getLocalPart();
					event = eventReader.nextEvent();
					String data = "";
					while (event.isCharacters()) {
						data += event.asCharacters().getData();
						event = eventReader.nextEvent();
					}
					values.put(eventName, data);
				} else if (checkEndElement(event, ITEM)) {
					RSSItem message = new RSSItem(values.get(TITLE),
							values.get(DESCRIPTION), values.get(LINK),
							values.get(PUB_DATE), values.get(AUTHOR),
							values.get(GUID));
					feed.addItem(message);
					values = new HashMap<String, String>();
					event = eventReader.nextEvent();
				}

			}
		} catch (XMLStreamException e) {
			log.warn("XML-Stream is not parseable", e);
			return null;
		} catch (IOException e) {
			log.warn("Could not read from RSS-feed", e);
			return null;
		} finally {

			// Close all resources
			if (eventReader != null) {
				try {
					eventReader.close();
				} catch (XMLStreamException e) {

					// No problem
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {

							// No problem
						}
					}
				}
			}
		}

		return feed;
	}
}
