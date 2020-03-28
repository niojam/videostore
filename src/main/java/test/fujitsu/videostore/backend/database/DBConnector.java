package test.fujitsu.videostore.backend.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.apache.commons.io.FilenameUtils;
import test.fujitsu.videostore.backend.domain.RentOrder;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class DBConnector<T> {

    public static final String JSON_EXTENSION = "json";
    public static final String YAML_EXTENSION = "yaml";
    public static final String EXCEPTION_MESSAGE = "Application supports only .json or .yaml formats";

    private String filePath;

    DBConnector(String filepath) {
        this.filePath = filepath;
        this.filePath = "db-examples/database.yaml";
    }

    public List<T> readData(String entityType, TypeReference<?> outputFormatType) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<T> simpleEntityData;
        Map<String, List<Map<String, Object>>> fileMap = readFile(mapper);
        simpleEntityData = mapper.convertValue(fileMap.get(entityType), outputFormatType);
        return simpleEntityData;
    }

    public Map<String, List<Map<String, Object>>> readFile(ObjectMapper mapper) {
        try {
            return mapper.readValue(
                    new File("db-examples/database.yaml"),
                    new TypeReference<Map<String, List<Object>>>() {
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedHashMap<>();
    }

    public abstract void writeSimpleEntityData(List<T> writeData);

    public abstract List<RentOrder> readOrder();

    public abstract void writeOrderEntity(List<RentOrder> orders);

    public ObjectMapper getObjectMapper() {
        return FilenameUtils.getExtension(filePath).equals(JSON_EXTENSION) ? new ObjectMapper() :
                new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
    }

    public String getFilePath() {
        return filePath;
    }
}
