package test.fujitsu.videostore.ui.order.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import test.fujitsu.videostore.backend.domain.RentOrder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class OrderGrid extends Grid<RentOrder> {

    public OrderGrid() {
        setSizeFull();
        setId("data-grid");

        addColumn(RentOrder::getId)
                .setSortable(true)
                .setHeader("Order ID")
                .setId("order-id");
        addColumn(order -> order.getCustomer().getName())
                .setFlexGrow(20)
                .setSortable(true)
                .setHeader("Customer name")
                .setId("customer-name");
        addColumn(new LocalDateRenderer<>(RentOrder::getOrderDate, "dd-MM-yyyy"))
                .setFlexGrow(5)
                .setSortable(true)
                .setHeader("Order date")
                .setId("order-date");
        final String availabilityTemplate = "<iron-icon icon=\"vaadin:circle\" class-name=\"[[item.statusClass]]\"></iron-icon>";
        addColumn(TemplateRenderer.<RentOrder>of(availabilityTemplate)
                .withProperty("statusClass", order -> {
                    long returnedInTime = order.getItems().stream()
                            .filter(item -> {
                                LocalDate finalDate = item.getReturnedDay() == null ? LocalDate.now() : item.getReturnedDay();
                                int f = (int) ChronoUnit.DAYS.between(order.getOrderDate(), finalDate);
                                return item.getDays() >= (int) ChronoUnit.DAYS.between(order.getOrderDate(), finalDate);
                            })
                            .count();
                    if (returnedInTime == order.getItems().size()) {
                        return "Ok";
                    } else if (returnedInTime == 0) {
                        return "Horrible";
                    } else {
                        return "SoSo";
                    }
                    // TODO: Implement flagging system using rules below:
                    // Return "Ok" if there is no any overdue, if all movies are returned
                    // Return "Horrible" if all movies were not returned in time
                    // Return "SoSo" if ablest one movie was not returned in time
                }))
                .setHeader("Status")
                .setFlexGrow(3)
                .setId("availability");
    }
}
