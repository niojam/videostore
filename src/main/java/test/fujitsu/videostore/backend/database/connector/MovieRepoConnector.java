package test.fujitsu.videostore.backend.database.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import test.fujitsu.videostore.backend.domain.Movie;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static test.fujitsu.videostore.backend.database.DBTableRepository.ENTITY_TYPE_MOVIE;

public class MovieRepoConnector extends DBConnector<Movie> {
    private static MovieRepoConnector movieRepoConnector = new MovieRepoConnector();

    private MovieRepoConnector() {
    }

    public static MovieRepoConnector getInstance() {
        return movieRepoConnector;
    }

    @Override
    public void writeData(List<Movie> writeData) {
        ObjectMapper mapper = super.getObjectMapper();
        Map<String, List<Map<String, Object>>> fileMap = super.readFile(mapper);
        List<Map<String, Object>> allMovies = new ArrayList<>();
        writeData.forEach(movie -> {
            Map<String, Object> movieEntity = new LinkedHashMap<>();
            movieEntity.put("id", movie.getId());
            movieEntity.put("name", movie.getName());
            movieEntity.put("stockCount", movie.getStockCount());
            movieEntity.put("type", movie.getType().getDatabaseId());
            allMovies.add(movieEntity);
        });
        fileMap.put(ENTITY_TYPE_MOVIE, allMovies);
        super.updateDataBase(fileMap);
    }


}
