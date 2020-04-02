package test.fujitsu.videostore.backend.database.connector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.StringUtils;
import test.fujitsu.videostore.backend.database.domainrepository.CustomerRepository;
import test.fujitsu.videostore.backend.database.domainrepository.MovieRepository;
import test.fujitsu.videostore.backend.domain.RentOrder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static test.fujitsu.videostore.backend.database.DBTableRepository.ENTITY_TYPE_ORDER;

public class OrderRepoConnector extends DBConnector<RentOrder> {

    private static OrderRepoConnector orderRepoConnector = new OrderRepoConnector();

    private OrderRepoConnector() {
    }

    public static OrderRepoConnector getInstance() {
        return orderRepoConnector;
    }

    @Override
    public List<RentOrder> readData(String entityType, TypeReference<?> outputFormatType) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        List<RentOrder> allOrders = new ArrayList<>();
        List<Map<String, Object>> rentOrdersFromDB = readFile(mapper).get(entityType);
        rentOrdersFromDB.forEach(rentOrderFromDB -> {
            RentOrder newOrder = new RentOrder();
            newOrder.setId((Integer) rentOrderFromDB.get("id"));
            newOrder.setOrderDate(LocalDate.parse((String) rentOrderFromDB.get("orderDate")));
            newOrder.setCustomer(CustomerRepository.getInstance().findById((Integer) rentOrderFromDB.get("customer")));
            newOrder.setItems(readOrderItems((List<Map<String, Object>>) rentOrderFromDB.get("items")));
            allOrders.add(newOrder);
        });
        return allOrders;
    }

    private List<RentOrder.Item> readOrderItems(List<Map<String, Object>> rentOrderItemsFromDB) {
        List<RentOrder.Item> orderItems = new ArrayList<>();
        rentOrderItemsFromDB.forEach(rentOrderItem -> {
            RentOrder.Item newItem = new RentOrder.Item();
            newItem.setMovie(MovieRepository.getInstance().findById((Integer) rentOrderItem.get("movie")));
            newItem.setMovieType(newItem.getMovie().getType());
            newItem.setPaidByBonus((boolean) rentOrderItem.get("paidByBonus"));
            newItem.setDays((Integer) rentOrderItem.get("days"));
            newItem.setReturnedDay(StringUtils.isEmpty((String) rentOrderItem.get("returnedDay")) ? null
                    : LocalDate.parse((String) rentOrderItem.get("returnedDay")));
            orderItems.add(newItem);
        });
        return orderItems;
    }

    @Override
    public void writeData(List<RentOrder> writeData) {
        ObjectMapper mapper = super.getObjectMapper();
        Map<String, List<Map<String, Object>>> fileMap = super.readFile(mapper);
        List<Map<String, Object>> allOrders = new ArrayList<>();
        writeData.forEach(order -> {
            Map<String, Object> orderEntity = new LinkedHashMap<>();
            orderEntity.put("id", order.getId());
            orderEntity.put("orderDate", order.getOrderDate().toString());
            orderEntity.put("customer", order.getCustomer().getId());
            orderEntity.put("items", getOrderItems(order));
            allOrders.add(orderEntity);
        });
        fileMap.put(ENTITY_TYPE_ORDER, allOrders);
        super.updateDataBase(fileMap);
    }

    private List<Map<String, Object>> getOrderItems(RentOrder order) {
        List<Map<String, Object>> orderItems = new ArrayList<>();
        order.getItems().forEach(item -> {
            Map<String, Object> itemEntity = new LinkedHashMap<>();
            itemEntity.put("movie", item.getMovie().getId());
            itemEntity.put("type", item.getMovie().getType().getDatabaseId());
            itemEntity.put("paidByBonus", item.isPaidByBonus());
            itemEntity.put("days", item.getDays());
            itemEntity.put("returnedDay", item.getReturnedDay() == null ? "" : item.getReturnedDay().toString());
            orderItems.add(itemEntity);
        });
        return orderItems;
    }


}
