package test.fujitsu.videostore.backend.database;

/**
 * Database Factory.
 * <p>
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
