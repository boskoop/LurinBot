package li.boskoop.lurin.handler.persist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/**
 * Provides functionality for saving and restoring the id of the last handled
 * status id.
 * 
 * @author buergich
 * 
 */
public class IdPersister {

	private static Logger log = Logger.getLogger(IdPersister.class);

	private File serFile;

	/**
	 * Default constructor. Creates the persistence file if not exists and saves
	 * an invalid id in it.
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public IdPersister(String filename) throws IOException {
		serFile = new File(filename);
		if (!serFile.exists()) {
			serFile.createNewFile();
			IdSaver saver = new IdSaver();
			persistSaver(saver);
		}
	}

	/**
	 * Persists an IdSaver object.
	 * 
	 * @param saver
	 */
	public void persistSaver(IdSaver saver) {
		ObjectOutputStream os = null;
		try {
			FileOutputStream fs = new FileOutputStream(serFile);
			os = new ObjectOutputStream(fs);
			os.writeObject(saver);
		} catch (FileNotFoundException e) {
			log.error("Could not persist id. Exiting", e);
			System.exit(1);
		} catch (IOException e) {
			log.error("Could not persist id. Exiting", e);
			System.exit(1);
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Restores the IdSaver object.
	 * 
	 * @return
	 */
	public IdSaver loadSaver() {
		ObjectInputStream ois = null;
		IdSaver saver = null;
		try {
			FileInputStream fis = new FileInputStream(serFile);
			ois = new ObjectInputStream(fis);
			saver = (IdSaver) ois.readObject();
		} catch (FileNotFoundException e) {
			log.error("Could not load persisted id. Exiting", e);
			System.exit(1);
		} catch (IOException e) {
			log.error("Could not load persisted id. Exiting", e);
			System.exit(1);
		} catch (ClassNotFoundException e) {
			log.error("Could not restore id from " + serFile.getName()
					+ ". Exiting", e);
			System.exit(1);
		} catch (ClassCastException e) {
			log.error("Could not restore id from " + serFile.getName()
					+ ". Exiting", e);
			System.exit(1);
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
			} catch (IOException e) {

			}
		}
		return saver;
	}
}
