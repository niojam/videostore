package test.fujitsu.videostore.backend.database.connector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static test.fujitsu.videostore.backend.database.DatabaseImpl.FILEPATH;

public  abstract class DBConnector<T> {

    public static final String JSON_EXTENSION = "json";
    public static final String YAML_EXTENSION = "yaml";

    private String filePath = FILEPATH;


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
                    new File(filePath),
                    new TypeReference<Map<String, List<Object>>>() {
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedHashMap<>();
    }

    public abstract void writeData(List<T> writeData);

    public void updateDataBase(Map<String, List<Map<String, Object>>> fileMap){
        try {
            getObjectMapper().writeValue(new File(filePath), fileMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectMapper getObjectMapper() {
        return FilenameUtils.getExtension(filePath).equals(JSON_EXTENSION) ? new ObjectMapper() :
                new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
    }

    public String getFilePath() {
        return filePath;
    }
}
