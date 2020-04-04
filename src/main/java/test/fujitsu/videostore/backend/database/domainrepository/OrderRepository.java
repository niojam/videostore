package test.fujitsu.videostore.backend.database.domainrepository;

import com.fasterxml.jackson.core.type.TypeReference;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.database.connector.DBConnector;
import test.fujitsu.videostore.backend.database.connector.OrderRepoConnector;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.RentOrder;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OrderRepository implements DBTableRepository<RentOrder> {

    private static AtomicInteger uniqueId;
    private List<RentOrder> orderList;
    private DBConnector<RentOrder> dbConnector;

    private static OrderRepository orderRepository = new OrderRepository();

    private OrderRepository() {
        this.dbConnector = OrderRepoConnector.getInstance();
        orderList = this.dbConnector.readData(ENTITY_TYPE_ORDER, new TypeReference<List<RentOrder>>() {
        });
        if (uniqueId == null) {
            uniqueId = new AtomicInteger(orderList.stream()
                    .max(Comparator.comparing(RentOrder::getId)).orElse(new RentOrder()).getId() + 1);
        }
    }

    public static OrderRepository getInstance() {
        return orderRepository;
    }

    @Override
    public List<RentOrder> getAll() {
        return orderList;
    }

    @Override
    public RentOrder findById(int id) {
        return getAll().stream().filter(order -> order.getId() == id).findFirst().get();
    }

    @Override
    public boolean remove(RentOrder object) {
        if (orderList.remove(object)) {
            dbConnector.writeData(orderList);
            return true;
        }
        return false;
    }

    @Override
    public RentOrder createOrUpdate(RentOrder object) {
        if (object == null) {
            return null;
        }

        if (object.isNewObject()) {
            object.setId(generateNextId());
            orderList.add(object);
            dbConnector.writeData(orderList);
            return object;
        }

        RentOrder order = findById(object.getId());

        order.setCustomer(object.getCustomer());
        order.setOrderDate(order.getOrderDate());
        order.setItems(object.getItems());
        dbConnector.writeData(orderList);
        return order;
    }

    public void removeMovies(Movie movieToRemove) {
        orderList.forEach(order -> {
            order.setItems(order.getItems().stream()
                    .filter(item -> !item.getMovie().equals(movieToRemove))
                    .collect(Collectors.toList()));
        });
    }

    public void removeCustomers(Customer customerToRemove) {
        orderList = orderList.stream()
                .filter(order -> !order.getCustomer().equals(customerToRemove))
                .collect(Collectors.toList());
    }


    @Override
    public int generateNextId() {
        return uniqueId.getAndIncrement();
    }
}
