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

    private DBTableRepository<Customer> customerTableRepositoryJson;
    private DBTableRepository<Movie> movieTableRepositoryJson;
    private DBTableRepository<RentOrder> rentOrderTableRepositoryJson;

    private DBTableRepository<Customer> customerTableRepositoryYaml;
    private DBTableRepository<Movie> movieTableRepositoryYaml;
    private DBTableRepository<RentOrder> rentOrderTableRepositoryYaml;


    @BeforeEach
    public void SetUp() {
        Database jsonDatabase = DatabaseFactory.from("db-examples/database.json");
        Database yamlDatabase = DatabaseFactory.from("db-examples/database.yaml");

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

        private void removeCustomer(DBTableRepository<Customer> customerTableRepository) {
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

        private void getCustomerById(DBTableRepository<Customer> customerTableRepository) {
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
            getAllMoviesFromDB(movieTableRepositoryJson);
            getAllMoviesFromDB(movieTableRepositoryYaml);
        }

        private void getAllMoviesFromDB(DBTableRepository<Movie> movieTableRepository) {
            assertEquals(12, movieTableRepository.getAll().size());
            assertEquals(12, movieTableRepository.getAll().size());
        }

        @Test
        public void addNewMovieToDB() {
            addNewMovieToDB(movieTableRepositoryYaml);
            addNewMovieToDB(movieTableRepositoryJson);
        }

        private void addNewMovieToDB(DBTableRepository<Movie> movieTableRepository) {
            int sizeBeforeAdding = movieTableRepository.getAll().size();
            Movie newMovie = new Movie();
            newMovie.setStockCount(10);
            newMovie.setName(NEW_MOVIE_NAME);
            newMovie.setType(MovieType.NEW);
            Movie movie = movieTableRepository.createOrUpdate(newMovie);
            assertEquals(sizeBeforeAdding + 1, movieTableRepository.getAll().size());
            movieTableRepository.remove(movie);
        }

        @Test
        public void removeMovieFromDB() {
          removeMovieFromDB(movieTableRepositoryJson);
          removeMovieFromDB(movieTableRepositoryYaml);
        }

        private void removeMovieFromDB(DBTableRepository<Movie> movieTableRepository) {
            Movie newMovie = new Movie();
            newMovie.setName(NEW_MOVIE_NAME);
            newMovie.setType(MovieType.NEW);
            newMovie.setStockCount(100);
            Movie movie = movieTableRepository.createOrUpdate(newMovie);
            int sizeBeforeDelete = movieTableRepository.getAll().size();
            movieTableRepository.remove(movie);
            assertEquals(sizeBeforeDelete - 1, movieTableRepository.getAll().size());
        }

        @Test
        public void getMovieById() {
            getMovieById(movieTableRepositoryYaml);
            getMovieById(movieTableRepositoryJson);
        }

        private void getMovieById(DBTableRepository<Movie> movieTableRepository) {
            String nameFromExampleDB = "The Avengers";
            Movie movie = movieTableRepository.findById(1);
            assertEquals(nameFromExampleDB, movie.getName());
        }


    }


    @Nested
    public class TestOrderRepository {


        public static final String NEW_ORDER_CUSTOMER_NAME = "Maria Kusk";
        public static final String NEW_MOVIE_NAME = "Film";

        @Test
        public void getAllOrdersFromDB() {
            getAllOrdersFromDB(rentOrderTableRepositoryJson);
            getAllOrdersFromDB(rentOrderTableRepositoryYaml);
        }

        private void getAllOrdersFromDB(DBTableRepository<RentOrder> rentOrderTableRepository) {
            assertEquals(1, rentOrderTableRepository.getAll().size());
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
            addNewOrder(rentOrderTableRepositoryYaml);
            addNewOrder(rentOrderTableRepositoryJson);
        }

        private void addNewOrder(DBTableRepository<RentOrder> rentOrderTableRepository) {
            int sizeBeforeAdding = rentOrderTableRepository.getAll().size();
            RentOrder order = createOrder();
            RentOrder newOrder = rentOrderTableRepository.createOrUpdate(order);
            assertEquals(sizeBeforeAdding + 1, rentOrderTableRepository.getAll().size());
            rentOrderTableRepository.remove(newOrder);
        }


        @Test
        public void removeOrderFromDB() {
            removeOrderFromDB(rentOrderTableRepositoryJson);
            removeOrderFromDB(rentOrderTableRepositoryYaml);
        }

        private void removeOrderFromDB(DBTableRepository<RentOrder> rentOrderTableRepository) {
            RentOrder order = createOrder();
            RentOrder newOrder = rentOrderTableRepository.createOrUpdate(order);
            int sizeBeforeDelete = rentOrderTableRepository.getAll().size();
            rentOrderTableRepository.remove(newOrder);
            assertEquals(sizeBeforeDelete - 1, rentOrderTableRepository.getAll().size());
        }


        @Test
        public void getOrderById() {
            getOrderById(rentOrderTableRepositoryYaml);
            getOrderById(rentOrderTableRepositoryJson);
        }

        private void getOrderById(DBTableRepository<RentOrder> rentOrderTableRepository) {
            RentOrder order = createOrder();
            RentOrder createdOrder = rentOrderTableRepository.createOrUpdate(order);
            RentOrder orderById = rentOrderTableRepository.findById(createdOrder.getId());
            assertEquals(createdOrder, orderById);
            rentOrderTableRepository.remove(createdOrder);
        }

    }

}
