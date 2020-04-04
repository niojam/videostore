package test.fujitsu.videostore.backend.database.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import test.fujitsu.videostore.backend.domain.Customer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static test.fujitsu.videostore.backend.database.DBTableRepository.ENTITY_TYPE_CUSTOMER;

public class CustomerRepoConnector extends DBConnector<Customer> {

    private static CustomerRepoConnector customerRepositoryConnector;

    private CustomerRepoConnector() {
    }

    public static CustomerRepoConnector getInstance() {
        if (customerRepositoryConnector == null) {
            customerRepositoryConnector = new CustomerRepoConnector();
        }
        return customerRepositoryConnector;
    }

    @Override
    public void writeData(List<Customer> writeData) {
        ObjectMapper mapper = super.getObjectMapper();
        Map<String, List<Map<String, Object>>> fileMap = super.readFile(mapper);
        List<Map<String, Object>> allCustomers = new ArrayList<>();
        writeData.forEach(customer -> {
            Map<String, Object> customerEntity = new LinkedHashMap<>();
            customerEntity.put("id", customer.getId());
            customerEntity.put("name", customer.getName());
            customerEntity.put("points", customer.getPoints());
            allCustomers.add(customerEntity);
        });
        fileMap.put(ENTITY_TYPE_CUSTOMER, allCustomers);
        super.updateDataBase(fileMap);
    }


}
