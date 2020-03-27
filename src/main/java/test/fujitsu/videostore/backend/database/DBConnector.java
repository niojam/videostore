package test.fujitsu.videostore.backend.database;

import test.fujitsu.videostore.backend.domain.RentOrder;

import java.lang.reflect.Type;
import java.util.List;

public interface DBConnector {

    List<?> readSimpleEntityData(String entityType, Type outputFormatType);


    List<RentOrder> readOrder();

    void writeSimpleEntityData(List<?> writeData, String entityType, Type outputFormatType);

    void writeOrderEntity(List<RentOrder> orders);
}
