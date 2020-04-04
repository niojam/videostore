package test.fujitsu.videostore.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.database.Database;
import test.fujitsu.videostore.backend.database.DatabaseFactory;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.MovieType;
import test.fujitsu.videostore.backend.domain.RentOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest {

    private Database jsonDatabase;
    private Database yamlDatabase;
    private DBTableRepository<Customer> customerTableRepositoryJson;
    private DBTableRepository<Movie> movieTableRepositoryJson;
    private DBTableRepository<RentOrder> rentOrderTableRepositoryJson;

    private DBTableRepository<Customer> customerTableRepositoryYaml;
    private DBTableRepository<Movie> movieTableRepositoryYaml;
    private DBTableRepository<RentOrder> rentOrderTableRepositoryYaml;


    @BeforeEach
    public void SetUp() {
        jsonDatabase = DatabaseFactory.from("db-examples/database.json");
        yamlDatabase = DatabaseFactory.from("db-examples/database.yaml");

        customerTableRepositoryJson = jsonDatabase.getCustomerTable();
        movieTableRepositoryJson = jsonDatabase.getMovieTable();
        rentOrderTableRepositoryJson = jsonDatabase.getOrderTable();

        customerTableRepositoryYaml = yamlDatabase.getCustomerTable();
        movieTableRepositoryYaml = yamlDatabase.getMovieTable();
        rentOrderTableRepositoryYaml = yamlDatabase.getOrderTable();


    }

    @Nested
    public class TestCustomerRepository {

        public static final String NEW_CUSTOMER_NAME = "Nikita O";

        @Test
        public void getAllCustomersFromDB() {
            getAllCustomers(customerTableRepositoryJson);
            getAllCustomers(customerTableRepositoryYaml);
        }


        private void getAllCustomers(DBTableRepository<Customer> tableRepository) {
            assertEquals(4, tableRepository.getAll().size());
        }


        @Test
        public void addNewCustomerToDB() {
            addNewCustomer(customerTableRepositoryJson);
            addNewCustomer(customerTableRepositoryYaml);
        }

        private void addNewCustomer(DBTableRepository<Customer> customerTableRepository) {
            int sizeBeforeAdding = customerTableRepository.getAll().size();
            Customer newCustomer = new Customer();
            newCustomer.setPoints(100);
            newCustomer.setName(NEW_CUSTOMER_NAME);
            Customer createdCustomer = customerTableRepository.createOrUpdate(newCustomer);
            assertEquals(sizeBeforeAdding + 1, customerTableRepository.getAll().size());
            customerTableRepository.remove(createdCustomer);
        }


        @Test
        public void removeCustomerFromDB() {
            removeCustomer(customerTableRepositoryYaml);
            removeCustomer(customerTableRepositoryJson);
        }

        public void removeCustomer(DBTableRepository<Customer> customerTableRepository) {
            Customer newCustomer = new Customer();
            newCustomer.setPoints(100);
            newCustomer.setName(NEW_CUSTOMER_NAME);
            Customer customer = customerTableRepository.createOrUpdate(newCustomer);

            int sizeBeforeDelete = customerTableRepository.getAll().size();

            customerTableRepository.remove(customer);
            assertEquals(sizeBeforeDelete - 1, customerTableRepository.getAll().size());
        }

        @Test
        public void getCustomerById() {
            getCustomerById(customerTableRepositoryJson);
            getCustomerById(customerTableRepositoryYaml);
        }

        public void getCustomerById(DBTableRepository<Customer> customerTableRepository) {
            String nameFromExampleDB = "Maria Kusk";
            Customer customer = customerTableRepository.findById(1);
            assertEquals(nameFromExampleDB, customer.getName());
        }

    }

    @Nested
    public class TestMovieRepository {


        public static final String NEW_MOVIE_NAME = "Film";

        @Test
        public void getAllMoviesFromDB() {
            assertEquals(12, movieTableRepositoryJson.getAll().size());
        }

        @Test
        public void addNewMovieToDB() {
            int sizeBeforeAdding = movieTableRepositoryJson.getAll().size();
            Movie newMovie = new Movie();
            newMovie.setStockCount(10);
            newMovie.setName(NEW_MOVIE_NAME);
            newMovie.setType(MovieType.NEW);
            Movie movie = movieTableRepositoryJson.createOrUpdate(newMovie);
            assertEquals(sizeBeforeAdding + 1, movieTableRepositoryJson.getAll().size());
            movieTableRepositoryJson.remove(movie);
        }

        @Test
        public void removeMovieFromDB() {
            Movie newMovie = new Movie();
            newMovie.setName(NEW_MOVIE_NAME);
            newMovie.setType(MovieType.NEW);
            newMovie.setStockCount(100);
            Movie movie = movieTableRepositoryJson.createOrUpdate(newMovie);
            int sizeBeforeDelete = movieTableRepositoryJson.getAll().size();
            movieTableRepositoryJson.remove(movie);
            assertEquals(sizeBeforeDelete - 1, movieTableRepositoryJson.getAll().size());
        }


        @Test
        public void getMovieById() {
            String nameFromExampleDB = "The Avengers";
            Movie movie = movieTableRepositoryJson.findById(1);
            assertEquals(nameFromExampleDB, movie.getName());
        }

    }


    @Nested
    public class TestOrderRepository {


        public static final String NEW_ORDER_CUSTOMER_NAME = "Maria Kusk";
        public static final String NEW_MOVIE_NAME = "Film";

        @Test
        public void getAllOrdersFromDB() {
            assertEquals(1, rentOrderTableRepositoryJson.getAll().size());
        }

        public RentOrder createOrder() {
            Movie newMovie = new Movie();
            newMovie.setStockCount(10);
            newMovie.setName(NEW_MOVIE_NAME);
            newMovie.setType(MovieType.NEW);

            Customer newCustomer = new Customer();
            newCustomer.setPoints(32);
            newCustomer.setName(NEW_ORDER_CUSTOMER_NAME);
            newCustomer.setId(1);

            RentOrder order = new RentOrder();
            order.setCustomer(newCustomer);
            RentOrder.Item orderItem = new RentOrder.Item();
            orderItem.setMovieType(newMovie.getType());
            orderItem.setDays(10);
            orderItem.setMovie(newMovie);
            order.setItems(List.of(orderItem));
            return order;
        }

        @Test
        public void addNewOrderToDB() {
            int sizeBeforeAdding = rentOrderTableRepositoryJson.getAll().size();
            RentOrder order = createOrder();
            RentOrder newOrder = rentOrderTableRepositoryJson.createOrUpdate(order);
            assertEquals(sizeBeforeAdding + 1, rentOrderTableRepositoryJson.getAll().size());
            rentOrderTableRepositoryJson.remove(newOrder);
        }


        @Test
        public void removeOrderFromDB() {
            RentOrder order = createOrder();
            RentOrder newOrder = rentOrderTableRepositoryJson.createOrUpdate(order);
            int sizeBeforeDelete = rentOrderTableRepositoryJson.getAll().size();
            rentOrderTableRepositoryJson.remove(newOrder);
            assertEquals(sizeBeforeDelete - 1, rentOrderTableRepositoryJson.getAll().size());
        }


        @Test
        public void getOrderById() {
            RentOrder order = createOrder();
            RentOrder createdOrder = rentOrderTableRepositoryJson.createOrUpdate(order);
            RentOrder orderById = rentOrderTableRepositoryJson.findById(createdOrder.getId());
            assertEquals(createdOrder, orderById);
            rentOrderTableRepositoryJson.remove(createdOrder);
        }

    }

}
