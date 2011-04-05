package li.boskoop.lurin;

import java.util.Arrays;

import org.apache.log4j.Logger;

import li.boskoop.lurin.fact.FactGrabber;
import li.boskoop.lurin.handler.TweetHandler;
import li.boskoop.lurin.tweet.LurinTweeter;

/**
 * Starting class for Lurin.
 * 
 * @author buergich
 */
public class Lurin {

	private static Logger log = Logger.getLogger(Lurin.class);

	/**
	 * Prints the usage of the bot jar.
	 */
	private static void printUsage() {
		System.out.println("Usage: java -jar lurin.jar [option]");
		System.out.println();
		System.out.println("where option could be:");
		System.out.println("    -t --tweet    tweet a random fact");
		System.out.println("    -h --handle   handle messages");
	}

	/**
	 * Main method and start point of the Lurin bot.
	 * 
	 * @param args
	 *            commandline arguments
	 */
	public static void main(String[] args) {

		log.debug("Started Lurin with arguments " + Arrays.toString(args));
		if (args == null || args.length != 1) {
			printUsage();
		} else if (args[0].equals("-t") || args[0].equals("--tweet")) {
			Lurin lurin = new Lurin();
			lurin.doTweet();
		} else if (args[0].equals("-h") || args[0].equals("--handle")) {
			Lurin lurin = new Lurin();
			lurin.handleTweets();
		} else {
			printUsage();
		}
	}

	/**
	 * Gets mentions from Twitter and handles them.
	 */
	private void handleTweets() {

		TweetHandler handler = new TweetHandler();
		handler.handleMentions();

	}

	/**
	 * Does a tweet with a random lurin fact.
	 */
	public void doTweet() {
		FactGrabber grabber = new FactGrabber();
		LurinTweeter tweeter = new LurinTweeter();

		tweeter.tweet(grabber.grabRandomFact());
	}
}
