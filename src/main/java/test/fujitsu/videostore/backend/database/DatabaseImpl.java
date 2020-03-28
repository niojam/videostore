package test.fujitsu.videostore.backend.database;

import test.fujitsu.videostore.backend.database.domainrepository.CustomerRepository;
import test.fujitsu.videostore.backend.database.domainrepository.MovieRepository;
import test.fujitsu.videostore.backend.database.domainrepository.OrderRepository;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.RentOrder;

public class DatabaseImpl implements Database {

    String filepath;

    DatabaseImpl(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public DBTableRepository<Movie> getMovieTable() {
        return new MovieRepository(new MovieRepoConnector(filepath));
    }

    @Override
    public DBTableRepository<Customer> getCustomerTable() {
        return new CustomerRepository(new CustomerRepoConnector(filepath));
    }

    @Override
    public DBTableRepository<RentOrder> getOrderTable() {
        return new OrderRepository(new OrderRepoConnector(filepath));
    }
}
