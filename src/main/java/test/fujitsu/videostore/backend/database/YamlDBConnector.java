package test.fujitsu.videostore.backend.database;

import test.fujitsu.videostore.backend.domain.RentOrder;

import java.lang.reflect.Type;
import java.util.List;

public class YamlDBConnector implements DBConnector {

    private String filepath;

    public YamlDBConnector(String filepath) {
        this.filepath = filepath;
    }


    @Override
    public List<Object> readSimpleEntityData(String entityType, Type outputFormatType) {
        return null;
    }

    @Override
    public List<RentOrder> readOrder() {
        return null;
    }

    @Override
    public void writeSimpleEntityData(List<?> writeData, String entityType, Type outputFormatType) {

    }

    @Override
    public void writeOrderEntity(List<RentOrder> orders) {

    }
}
