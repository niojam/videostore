package test.fujitsu.videostore.backend.database.domainrepository;

import com.google.gson.reflect.TypeToken;
import test.fujitsu.videostore.backend.database.DBConnector;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.domain.Movie;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository implements DBTableRepository<Movie> {


    private List<Movie> movieList;
    private DBConnector dbConnector;
    private Type type = new TypeToken<ArrayList<Movie>>() {
    }.getType();

    public MovieRepository(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
        movieList = (List<Movie>) this.dbConnector.readSimpleEntityData(ENTITY_TYPE_MOVIE, type);
    }


    @Override
    public List<Movie> getAll() {
        return movieList;
    }

    @Override
    public Movie findById(int id) {
        return movieList.stream().filter(movie -> movie.getId() == id).findFirst().get();
    }

    @Override
    public boolean remove(Movie object) {
        if (movieList.remove(object)) {
            dbConnector.writeSimpleEntityData(movieList, ENTITY_TYPE_MOVIE, type);
            return true;
        }
        return false;
    }

    @Override
    public Movie createOrUpdate(Movie object) {
        if (object == null) {
            return null;
        }

        if (object.isNewObject()) {
            object.setId(generateNextId());
            movieList.add(object);
            dbConnector.writeSimpleEntityData(movieList, ENTITY_TYPE_MOVIE, type);
            return object;
        }

        Movie movie = findById(object.getId());

        movie.setName(object.getName());
        movie.setStockCount(object.getStockCount());
        movie.setType(object.getType());
        dbConnector.writeSimpleEntityData(movieList, ENTITY_TYPE_MOVIE, type);
        return movie;
    }

    @Override
    public int generateNextId() {
        return movieList.size() + 1;
    }
}