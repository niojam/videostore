package test.fujitsu.videostore.backend.database;

import test.fujitsu.videostore.backend.domain.RentOrder;

import java.util.List;

public class OrderRepoConnector extends DBConnector<RentOrder> {
    OrderRepoConnector(String filepath) {
        super(filepath);
    }

    @Override
    public void writeSimpleEntityData(List<RentOrder> writeData) {

    }

    @Override
    public List<RentOrder> readOrder() {
        return null;
    }

    @Override
    public void writeOrderEntity(List<RentOrder> orders) {

    }

}
