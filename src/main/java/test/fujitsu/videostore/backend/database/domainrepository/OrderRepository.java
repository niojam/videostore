package test.fujitsu.videostore.backend.database.domainrepository;

import test.fujitsu.videostore.backend.database.DBConnector;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.domain.RentOrder;

import java.util.List;

public class OrderRepository implements DBTableRepository<RentOrder> {
    private List<RentOrder> orderList;
    private DBConnector<RentOrder> dbConnector;

    public OrderRepository(DBConnector<RentOrder> dbConnector) {
        this.dbConnector = dbConnector;
        orderList = this.dbConnector.readOrder();
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
            dbConnector.writeOrderEntity(orderList);
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
            dbConnector.writeOrderEntity(orderList);
            return object;
        }

        RentOrder order = findById(object.getId());

        order.setCustomer(object.getCustomer());
        order.setOrderDate(order.getOrderDate());
        order.setItems(object.getItems());
        dbConnector.writeOrderEntity(orderList);
        return order;
    }

    @Override
    public int generateNextId() {
        return orderList.size() + 1;
    }
}
