package test.fujitsu.videostore.backend.database.domainrepository;

import com.fasterxml.jackson.core.type.TypeReference;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.database.connector.DBConnector;
import test.fujitsu.videostore.backend.domain.Customer;

import java.util.List;

public class CustomerRepository implements DBTableRepository<Customer> {

    private List<Customer> customerList;
    private DBConnector<Customer> dbConnector;
    private TypeReference<?> type = new TypeReference<List<Customer>>() {
    };

    public CustomerRepository(DBConnector<Customer> dbConnector) {
        this.dbConnector = dbConnector;
        customerList = (List<Customer>) this.dbConnector.readData(ENTITY_TYPE_CUSTOMER, type);
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
            dbConnector.writeData(customerList);
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
            dbConnector.writeData(customerList);
            return object;
        }

        Customer customer = findById(object.getId());

        customer.setName(object.getName());
        customer.setPoints(object.getPoints());
        dbConnector.writeData(customerList);
        return customer;
    }

    @Override
    public int generateNextId() {
        return customerList.size() + 1;
    }
}

