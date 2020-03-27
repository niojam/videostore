package test.fujitsu.videostore.backend.database;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import test.fujitsu.videostore.backend.database.domainrepository.CustomerRepository;
import test.fujitsu.videostore.backend.database.domainrepository.MovieRepository;
import test.fujitsu.videostore.backend.domain.RentOrder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;

import static test.fujitsu.videostore.backend.database.DBTableRepository.ENTITY_TYPE_ORDER;


public class JsonDBConnector implements DBConnector {

    private String filepath;

    public JsonDBConnector(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public List<?> readSimpleEntityData(String entityType, Type outputFormatType) {
        List<Object> dataList;
        Gson gson = new Gson();
        JsonObject jsonObject = readFile();
        JsonArray allData = jsonObject.getAsJsonArray(entityType);
        dataList = gson.fromJson(allData, outputFormatType);
        return dataList;
    }


    @Override
    public List<RentOrder> readOrder() {
        List<RentOrder> rentOrders = new ArrayList<>();
        CustomerRepository customerRepository = new CustomerRepository(this);
        JsonObject jsonObject = readFile();
        JsonArray allData = jsonObject.getAsJsonArray(ENTITY_TYPE_ORDER);
        allData.forEach(order -> {
            RentOrder newOrder = new RentOrder();
            JsonObject orderObject = order.getAsJsonObject();
            newOrder.setId(orderObject.get("id").getAsInt());
            newOrder.setCustomer(customerRepository.findById(orderObject.get("customer").getAsInt()));
            newOrder.setOrderDate(LocalDate.parse(orderObject.get("orderDate").getAsString()));
            newOrder.setItems(getOrderItems(orderObject.getAsJsonArray("items")));
            rentOrders.add(newOrder);
        });
        return rentOrders;
    }


    private List<RentOrder.Item> getOrderItems(JsonArray items) {
        MovieRepository movieRepository = new MovieRepository(this);
        List<RentOrder.Item> orderItems = new ArrayList<>();
        items.forEach(item -> {
            RentOrder.Item newItem = new RentOrder.Item();
            JsonObject itemObject = item.getAsJsonObject();
            newItem.setDays(itemObject.get("days").getAsInt());
            newItem.setMovie(movieRepository.findById(itemObject.get("movie").getAsInt()));
            newItem.setMovieType(newItem.getMovie().getType());
            newItem.setPaidByBonus(itemObject.get("paidByBonus").getAsBoolean());
            newItem.setReturnedDay(itemObject.get("returnedDay").isJsonNull() ? null : LocalDate.parse(itemObject.get("returnedDay").getAsString()));
            orderItems.add(newItem);
        });
        return orderItems;
    }

    @Override
    public void writeSimpleEntityData(List<?> writeData, String entityType, Type outputFormatType) {
        Gson gson = new Gson();
        JsonObject jsonObject = readFile();
        jsonObject.remove(entityType);
        jsonObject.add(entityType, new JsonParser().parse(gson.toJson(writeData, outputFormatType)));
        writeFile(jsonObject);
    }

    @Override
    public void writeOrderEntity(List<RentOrder> orders) {
        Gson gson = new Gson();
        JsonObject jsonObject = readFile();
        jsonObject.remove(ENTITY_TYPE_ORDER);
        JsonArray allOrders = new JsonArray();
        orders.forEach(order -> {
            JsonObject separateOrder = new JsonObject();
            separateOrder.addProperty("id", order.getId());
            separateOrder.addProperty("customer", order.getCustomer().getId());
            separateOrder.addProperty("orderDate", order.getOrderDate().toString());
            separateOrder.add("items", getOrderItemsToJson(order.getItems()));
            allOrders.add(separateOrder);
        });


        jsonObject.add("order", allOrders);
        writeFile(jsonObject);

    }


    private JsonArray getOrderItemsToJson(List<RentOrder.Item> items) {
        JsonArray orderItems = new JsonArray();
        items.forEach(item -> {
            JsonObject itemDetails = new JsonObject();
            itemDetails.addProperty("movie", item.getMovie().getId());
            itemDetails.addProperty("type", item.getMovie().getType().getDatabaseId());
            itemDetails.addProperty("paidByBonus", item.isPaidByBonus());
            itemDetails.addProperty("days", item.getDays());
            itemDetails.addProperty("returnedDay", item.getReturnedDay() == null ? null : item.getReturnedDay().toString());
            orderItems.add(itemDetails);
        });
        return orderItems;
    }

    private void writeFile(JsonObject jsonObject) {
        try {
            FileWriter file = new FileWriter("db-examples/database.json");
            file.write(jsonObject.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private JsonObject readFile() {
        try {
            return (JsonObject) new JsonParser().parse(new FileReader("db-examples/database.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new JsonObject();
    }
}