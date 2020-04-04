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
    private DBTableRepository<Customer> customerTableRepository;
    private DBTableRepository<Movie> movieTableRepository;
    private DBTableRepository<RentOrder> rentOrderTableRepository;

    @BeforeEach
    public void SetUp() {
        jsonDatabase = DatabaseFactory.from("db-examples/database.json");
        Database yamlDatabase = DatabaseFactory.from("db-examples/database.yaml");

        customerTableRepository = jsonDatabase.getCustomerTable();
        movieTableRepository = jsonDatabase.getMovieTable();
        rentOrderTableRepository = jsonDatabase.getOrderTable();
    }

    @Nested
    public class TestCustomerRepository {

        public static final String NEW_CUSTOMER_NAME = "Nikita O";

        @Test
        public void getAllCustomersFromDB() {
            assertEquals(4, customerTableRepository.getAll().size());
        }

        @Test
        public void addNewCustomerToDB() {
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
            assertEquals(12, movieTableRepository.getAll().size());
        }

        @Test
        public void addNewMovieToDB() {
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
            int sizeBeforeAdding = rentOrderTableRepository.getAll().size();
            RentOrder order = createOrder();
            RentOrder newOrder = rentOrderTableRepository.createOrUpdate(order);
            assertEquals(sizeBeforeAdding + 1, rentOrderTableRepository.getAll().size());
            rentOrderTableRepository.remove(newOrder);
        }


        @Test
        public void removeOrderFromDB() {
            RentOrder order = createOrder();
            RentOrder newOrder = rentOrderTableRepository.createOrUpdate(order);
            int sizeBeforeDelete = rentOrderTableRepository.getAll().size();
            rentOrderTableRepository.remove(newOrder);
            assertEquals(sizeBeforeDelete - 1, rentOrderTableRepository.getAll().size());
        }


        @Test
        public void getOrderById() {
            RentOrder order = createOrder();
            RentOrder createdOrder = rentOrderTableRepository.createOrUpdate(order);
            RentOrder orderById = rentOrderTableRepository.findById(createdOrder.getId());
            assertEquals(createdOrder, orderById);
            rentOrderTableRepository.remove(createdOrder);
        }

    }

}
