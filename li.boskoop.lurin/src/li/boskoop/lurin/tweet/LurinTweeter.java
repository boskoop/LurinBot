package li.boskoop.lurin.tweet;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

/**
 * Provides call functionality against the Twitter API for the Lurin bot.
 * 
 * Configuration for this class is done in the twitter4j.properties file
 * containing at least the following options:
 * 
 * <pre>
 * oauth.consumerKey
 * oauth.consumerSecret
 * oauth.accessToken
 * oauth.accessTokenSecret
 * </pre>
 * 
 * @author buergich
 */
public class LurinTweeter {

	private static Logger log = Logger.getLogger(LurinTweeter.class);

	private Twitter twitter;

	/**
	 * Default constructor. Creates a connection to the Twitter API which is
	 * used throughout the class.
	 */
	public LurinTweeter() {
		TwitterFactory tf = new TwitterFactory();
		twitter = tf.getInstance();
	}

	/**
	 * Sends a tweet.
	 * 
	 * @param tweet
	 *            the tweet to send
	 */
	public void tweet(String tweet) {
		try {
			twitter.updateStatus(tweet);
			log.info("Tweeted status '" + tweet + "'");
		} catch (TwitterException e) {
			log.error("Twitter service ist not available. Exiting", e);
			System.exit(1);
		}

	}

	/**
	 * Get the mentions from the Twitter API
	 * 
	 * @return A List containing at max 20 mentions
	 */
	public List<Status> getMentions() {
		ResponseList<Status> mentions = null;
		try {
			mentions = twitter.getMentions();
		} catch (TwitterException e) {
			log.error("Twitter service ist not available. Exiting", e);
			System.exit(1);
		}
		return mentions;
	}

	/**
	 * Returns user ids for user names.
	 * 
	 * @param usernames
	 *            the names to get the ids from
	 * @return a long[] containing the ids
	 */
	public long[] getUserIds(String[] usernames) {
		ArrayList<User> users = new ArrayList<User>();
		try {
			for (String name : usernames) {
				User user = twitter.showUser(name);
				if (user != null) {
					users.add(user);
				}
			}
		} catch (TwitterException e) {
			log.error("Twitter service ist not available. Exiting", e);
			System.exit(1);
		}
		long[] ids = new long[users.size()];
		for (int i = 0; i < users.size(); i++) {
			ids[i] = users.get(i).getId();
		}
		return ids;
	}
}
