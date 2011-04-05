package li.boskoop.lurin.fact;

import java.util.Random;

import org.apache.log4j.Logger;

import li.boskoop.lurin.rss.RSSFeed;
import li.boskoop.lurin.rss.RSSParser;

/**
 * Provides functionality to grab facts from lurinfacts.ch.
 * 
 * @author buergich
 * 
 */
public class FactGrabber {

	private static Logger log = Logger.getLogger(FactGrabber.class);

	/**
	 * Parses the feed from lurinfacts to a RSSFeed object.
	 * 
	 * @return
	 */
	public RSSFeed grabFacts() {
		RSSParser parser = new RSSParser("http://lurinfacts.ch/facts/rss");

		RSSFeed factFeed = parser.parseFeed();

		if (factFeed == null) {
			return RSSFeed.createEmptyFeed();
		}
		return factFeed;

	}

	/**
	 * Selects a random fact from the RSS feed.
	 * 
	 * @return
	 */
	public String grabRandomFact() {
		RSSFeed factFeed = grabFacts();
		if (factFeed == null) {
			log.warn("Nothing to tweet from the feed. Exiting");
			System.exit(0);
		}
		String[] facts = new String[factFeed.getItems().size()];
		for (int i = 0; i < facts.length; i++) {
			facts[i] = factFeed.getItems().get(i).getTitle();
		}

		return selectFact(facts);
	}

	/**
	 * Selects a random String from an array.
	 * 
	 * @param grabFacts
	 * @return
	 */
	private String selectFact(String[] grabFacts) {
		Random rand = new Random();
		int i = rand.nextInt(grabFacts.length);
		return grabFacts[i];
	}
}
