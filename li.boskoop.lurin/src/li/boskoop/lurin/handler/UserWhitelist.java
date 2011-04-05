package li.boskoop.lurin.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.log4j.Logger;

/**
 * Provides access to the whitelist.conf file.
 * 
 * @author buergich
 * 
 */
public class UserWhitelist {

	private static final String WHITELIST_FILE = "whitelist.conf";

	private static Logger log = Logger.getLogger(UserWhitelist.class);

	private File whitelist;

	/**
	 * Default constructor. Creates the whitelist.conf file, if it doesn't
	 * exist.
	 */
	public UserWhitelist() {
		whitelist = new File(WHITELIST_FILE);
		if (!whitelist.exists()) {
			try {
				whitelist.createNewFile();
			} catch (IOException e) {
				log.warn("Could not create a whitelist file.", e);
			}
		}
	}

	/**
	 * Reads the valid users' names from the config file.
	 * 
	 * @return
	 */
	public String[] getValidUsers() {
		BufferedReader reader = null;
		LinkedList<String> lines = new LinkedList<String>();
		try {
			reader = new BufferedReader(new FileReader(whitelist));
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (!line.startsWith("#") || !line.equals("")) {
					lines.add(line);
				}
			}
		} catch (FileNotFoundException e) {
			log.error(
					"Could find whitelist. There are no valid users! Exiting",
					e);
			System.exit(1);
		} catch (IOException e) {
			log.error(
					"Could not read from whitelist. There are no valid users! Exiting",
					e);
			System.exit(1);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}
		if (lines.size() == 0) {
			log.warn("There are no users in the whitelist!");
			return new String[0];
		}
		return lines.toArray(new String[lines.size()]);
	}
}
