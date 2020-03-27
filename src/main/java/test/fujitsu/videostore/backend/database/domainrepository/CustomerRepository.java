package test.fujitsu.videostore.backend.database.domainrepository;

import com.google.gson.reflect.TypeToken;
import test.fujitsu.videostore.backend.database.DBConnector;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.domain.Customer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements DBTableRepository<Customer> {

    private List<Customer> customerList;
    private DBConnector dbConnector;
    private Type type = new TypeToken<ArrayList<Customer>>() {
    }.getType();

    public CustomerRepository(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
        customerList = (List<Customer>) this.dbConnector.readSimpleEntityData(ENTITY_TYPE_CUSTOMER, type);
    }


    @Override
    public List<Customer> getAll() {
        return customerList;
    }

    @Override
    public Customer findById(int id) {
        return getAll().stream().filter(customer -> customer.getId() == id).findFirst().get();
    }

    @Override
    public boolean remove(Customer object) {
        if (customerList.remove(object)) {
            dbConnector.writeSimpleEntityData(customerList, ENTITY_TYPE_CUSTOMER, type);
            return true;
        }
        return false;
    }

    @Override
    public Customer createOrUpdate(Customer object) {
        if (object == null) {
            return null;
        }

        if (object.isNewObject()) {
            object.setId(generateNextId());
            customerList.add(object);
            dbConnector.writeSimpleEntityData(customerList, ENTITY_TYPE_CUSTOMER, type);
            return object;
        }

        Customer customer = findById(object.getId());

        customer.setName(object.getName());
        customer.setPoints(object.getPoints());
        dbConnector.writeSimpleEntityData(customerList, ENTITY_TYPE_CUSTOMER, type);
        return customer;
    }

    @Override
    public int generateNextId() {
        return customerList.size() + 1;
    }
}

