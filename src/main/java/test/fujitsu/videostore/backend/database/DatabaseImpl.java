package test.fujitsu.videostore.backend.database;

import test.fujitsu.videostore.backend.database.domainrepository.CustomerRepository;
import test.fujitsu.videostore.backend.database.domainrepository.MovieRepository;
import test.fujitsu.videostore.backend.database.domainrepository.OrderRepository;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.RentOrder;

public class DatabaseImpl implements Database {

    DBConnector dbConnector;

    DatabaseImpl(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public DBTableRepository<Movie> getMovieTable() {
        return new MovieRepository(dbConnector);
    }

    @Override
    public DBTableRepository<Customer> getCustomerTable() {
        return new CustomerRepository(dbConnector);
    }

    @Override
    public DBTableRepository<RentOrder> getOrderTable() {
        return new OrderRepository(dbConnector);
    }
}
