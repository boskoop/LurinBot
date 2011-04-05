package li.boskoop.lurin.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import li.boskoop.lurin.fact.FactGrabber;
import li.boskoop.lurin.handler.persist.IdPersister;
import li.boskoop.lurin.handler.persist.IdSaver;
import li.boskoop.lurin.tweet.LurinTweeter;
import twitter4j.Status;

/**
 * Handles tweets which are received by the Lurin bot.
 * 
 * @author buergich
 * 
 */
public class TweetHandler {

	private static final String PERSISTENCE_FILE = "data.ser";

	private static Logger log = Logger.getLogger(TweetHandler.class);

	private LurinTweeter tweeter = new LurinTweeter();

	/**
	 * Handles mentions received on twitter.
	 */
	public void handleMentions() {
		List<Status> mentions = tweeter.getMentions();
		mentions = filterWhitelistMentions(mentions);
		mentions = filterNewMentions(mentions);
		for (Status status : mentions) {
			handleMention(status);
		}
	}

	/**
	 * Handles a single mention (Status).
	 * 
	 * @param status
	 */
	private void handleMention(Status status) {
		if (status.getText().toLowerCase().contains("#fact")) {
			FactGrabber grabber = new FactGrabber();
			tweeter.tweet("@" + status.getUser().getScreenName() + " "
					+ grabber.grabRandomFact());
		} else if (status.getText().contains("?")) {
			tweeter.tweet("@" + status.getUser().getScreenName()
					+ " Why don't you ask that @Krugi_tha_one?");
		} else {
			int random = new Random().nextInt(6000) + 1;
			tweeter.tweet("@" + status.getUser().getScreenName()
					+ " I already knew that! (Read RFC " + random
					+ ": http://tools.ietf.org/html/rfc" + random + ")");
		}
	}

	/**
	 * Filters mentions which are not from users in the whitelist.
	 * 
	 * @param unfilteredMentions
	 * @return
	 */
	private List<Status> filterWhitelistMentions(List<Status> unfilteredMentions) {
		if (unfilteredMentions.size() == 0) {
			return unfilteredMentions;
		}
		List<Status> mentions = new ArrayList<Status>();
		long[] validUserIds = loadValidUserIds();
		Arrays.sort(validUserIds);
		for (Status status : unfilteredMentions) {
			if (Arrays.binarySearch(validUserIds, status.getUser().getId()) >= 0) {
				mentions.add(status);
			}
		}
		return mentions;
	}

	/**
	 * Returns the user ids from the users in the whitelist.
	 * 
	 * @return
	 */
	private long[] loadValidUserIds() {
		UserWhitelist whitelist = new UserWhitelist();
		String[] validUsers = whitelist.getValidUsers();
		return tweeter.getUserIds(validUsers);
	}

	/**
	 * Filters mentions which are not new.
	 * 
	 * @param unfilteredMentions
	 * @return
	 */
	private List<Status> filterNewMentions(List<Status> unfilteredMentions) {
		if (unfilteredMentions.size() == 0) {
			return unfilteredMentions;
		}
		IdPersister persister = null;
		try {
			persister = new IdPersister(PERSISTENCE_FILE);
		} catch (IOException e) {
			log.error("Could not create persistence file " + PERSISTENCE_FILE,
					e);
			log.error("Exiting");
			System.exit(1);
		}
		IdSaver persistedId = persister.loadSaver();
		if (!persistedId.isValidId()) {

			// We don't have any previously persisted id
			List<Status> mentions = new ArrayList<Status>();

			// We add only the newest Status (We have at least one Status in
			// unfilteredMentions, see above)
			Status newest = unfilteredMentions.get(0);
			mentions.add(newest);

			persistedId.setId(newest.getId());
			persistedId.setValidId(true);
			persister.persistSaver(persistedId);

			return mentions;

		}

		// We have a persisted (valid) id

		int firstIdOccurence = getIndexOfFirstIdOccurence(unfilteredMentions,
				persistedId.getId());

		if (firstIdOccurence < 0) {
			log.warn("Persisted id not found in mentions. Dropping all mentions.");
			persistedId.setId(unfilteredMentions.get(0).getId());
			persister.persistSaver(persistedId);
			return new ArrayList<Status>();
		} else if (firstIdOccurence == 0) {
			// no new mentions
			return new ArrayList<Status>();
		}

		ArrayList<Status> mentions = new ArrayList<Status>();
		mentions.addAll(unfilteredMentions.subList(0, firstIdOccurence));

		Status newest = mentions.get(0);
		persistedId.setId(newest.getId());
		persister.persistSaver(persistedId);

		return mentions;

	}

	/**
	 * Returns the index of the first mention in the statusList given by the
	 * Status id.
	 * 
	 * @param statusList
	 * @param id
	 * @return The index of the occurence if found, -1 otherwise
	 */
	private int getIndexOfFirstIdOccurence(List<Status> statusList, long id) {
		for (int i = 0; i < statusList.size(); i++) {
			if (statusList.get(i).getId() == id) {
				return i;
			}
		}
		return -1;
	}
}
