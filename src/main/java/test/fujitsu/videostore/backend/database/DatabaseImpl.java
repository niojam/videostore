package test.fujitsu.videostore.backend.database;

import test.fujitsu.videostore.backend.database.domainrepository.CustomerRepository;
import test.fujitsu.videostore.backend.database.domainrepository.MovieRepository;
import test.fujitsu.videostore.backend.database.domainrepository.OrderRepository;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.RentOrder;

public class DatabaseImpl implements Database {

    public static String FILEPATH = "";

    DatabaseImpl(String filepath) {
        DatabaseImpl.FILEPATH = filepath;
    }


    @Override
    public DBTableRepository<Movie> getMovieTable() {
        return MovieRepository.getInstance();
    }

    @Override
    public DBTableRepository<Customer> getCustomerTable() {
        return CustomerRepository.getInstance();
    }

    @Override
    public DBTableRepository<RentOrder> getOrderTable() {
        return OrderRepository.getInstance();
    }
}
