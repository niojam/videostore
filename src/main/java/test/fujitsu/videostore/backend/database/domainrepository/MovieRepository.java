package test.fujitsu.videostore.backend.database.domainrepository;

import com.fasterxml.jackson.core.type.TypeReference;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.database.connector.DBConnector;
import test.fujitsu.videostore.backend.database.connector.MovieRepoConnector;
import test.fujitsu.videostore.backend.domain.Movie;

import java.util.List;

public class MovieRepository implements DBTableRepository<Movie> {


    private List<Movie> movieList;
    private DBConnector<Movie> dbConnector;
    private TypeReference<?> type = new TypeReference<List<Movie>>() {
    };


    private static MovieRepository movieRepository = new MovieRepository();

    private MovieRepository() {
        this.dbConnector = MovieRepoConnector.getInstance();
        movieList = this.dbConnector.readData(ENTITY_TYPE_MOVIE, type);
    }
    synchronized  public static MovieRepository getInstance() {
        return movieRepository;
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
            dbConnector.writeData(movieList);
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
            dbConnector.writeData(movieList);
            return object;
        }

        Movie movie = findById(object.getId());

        movie.setName(object.getName());
        movie.setStockCount(object.getStockCount());
        movie.setType(object.getType());
        dbConnector.writeData(movieList);
        return movie;
    }

    @Override
    public int generateNextId() {
        return movieList.size() + 1;
    }
}