package test.fujitsu.videostore.backend.database;

/**
 * Database Factory.
 * <p>
 * TODO: Should be re-implemented with your file database. Current implementation is just demo for UI testing.
 */
public class DatabaseFactory {


    /**
     * Creates database "connection"/opens database from path.
     * <p>
     * Two example files, /db-examples/database.json and /db-examples/database.yaml.
     * Hint: MovieType.databaseId == type field in database files.
     * <p>
     *
     * @param filePath file path to database
     * @return database proxy for different tables
     */
    public static Database from(String filePath) {
        return new DatabaseImpl(filePath);
    }
}
