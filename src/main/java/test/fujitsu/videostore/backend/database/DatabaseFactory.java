package test.fujitsu.videostore.backend.database;

import org.apache.commons.io.FilenameUtils;

/**
 * Database Factory.
 * <p>
 * TODO: Should be re-implemented with your file database. Current implementation is just demo for UI testing.
 */
public class DatabaseFactory {

    public static final String JSON_EXTENSION = "json";
    public static final String YAML_EXTENSION = "yaml";
    public static final String EXCEPTION_MESSAGE = "Application supports only .json or .yaml formats";

    /**
     * Creates database "connection"/opens database from path.
     * <p>
     * TODO: Implement database parsing, fetching, creation, modification, removing from JSON or YAML file database.
     * Two example files, /db-examples/database.json and /db-examples/database.yaml.
     * Hint: MovieType.databaseId == type field in database files.
     * <p>
     * TODO: Current way of creating next ID is incorrect, make better implementation.
     *
     * @param filePath file path to database
     * @return database proxy for different tables
     */
    public static Database from(String filePath) {
        String extension = FilenameUtils.getExtension("db-examples/database.json");
        return extension.equals(JSON_EXTENSION) ? new DatabaseImpl(new JsonDBConnector(filePath)) : new DatabaseImpl(new YamlDBConnector(filePath));
    }
}
