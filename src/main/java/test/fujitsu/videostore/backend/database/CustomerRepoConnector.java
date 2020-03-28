package test.fujitsu.videostore.backend.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import test.fujitsu.videostore.backend.domain.Customer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static test.fujitsu.videostore.backend.database.DBTableRepository.ENTITY_TYPE_CUSTOMER;

public class CustomerRepoConnector extends DBConnector<Customer> {

    public CustomerRepoConnector(String filepath) {
        super(filepath);
    }




    @Override
    public void writeData(List<Customer> writeData) {
        ObjectMapper mapper = super.getObjectMapper();
        Map<String, List<Map<String, Object>>> fileMap = super.readFile(mapper);
        List<Map<String, Object>> allCustomers = new ArrayList<>();
        try {
            writeData.forEach(customer -> {
                Map<String, Object> customerEntity = new LinkedHashMap<>();
                customerEntity.put("id", customer.getId());
                customerEntity.put("name", customer.getName());
                customerEntity.put("points", customer.getPoints());
                allCustomers.add(customerEntity);
            });
            fileMap.put(ENTITY_TYPE_CUSTOMER, allCustomers);
            mapper.writeValue(new File(super.getFilePath()), fileMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
