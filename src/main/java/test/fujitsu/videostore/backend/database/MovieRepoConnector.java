package test.fujitsu.videostore.backend.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.RentOrder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static test.fujitsu.videostore.backend.database.DBTableRepository.ENTITY_TYPE_MOVIE;

public class MovieRepoConnector extends DBConnector<Movie> {
    MovieRepoConnector(String filepath) {
        super(filepath);
    }

    @Override
    public void writeSimpleEntityData(List<Movie> writeData) {
        ObjectMapper mapper = super.getObjectMapper();
        Map<String, List<Map<String, Object>>> fileMap = super.readFile(mapper);
        List<Map<String, Object>> allMovies = new ArrayList<>();
        try {
            writeData.forEach(movie -> {
                Map<String, Object> movieEntity = new LinkedHashMap<>();
                movieEntity.put("id", movie.getId());
                movieEntity.put("name", movie.getName());
                movieEntity.put("stockCount", movie.getStockCount());
                movieEntity.put("type", movie.getType().getDatabaseId());
                allMovies.add(movieEntity);
            });
            fileMap.put(ENTITY_TYPE_MOVIE, allMovies);
            mapper.writeValue(new File(super.getFilePath()), fileMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<RentOrder> readOrder() {
        return null;
    }

    @Override
    public void writeOrderEntity(List<RentOrder> orders) {

    }
}
