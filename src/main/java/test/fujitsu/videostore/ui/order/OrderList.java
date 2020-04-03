package test.fujitsu.videostore.ui.order;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import test.fujitsu.videostore.backend.domain.RentOrder;
import test.fujitsu.videostore.ui.MainLayout;
import test.fujitsu.videostore.ui.ModelList;
import test.fujitsu.videostore.ui.order.components.OrderForm;
import test.fujitsu.videostore.ui.order.components.OrderGrid;

import java.util.ArrayList;
import java.util.List;

@Route(value = OrderList.VIEW_NAME, layout = MainLayout.class)
public class OrderList extends ModelList<RentOrder> implements HasUrlParameter<String> {

    static final String VIEW_NAME = "OrderList";
    public static final String NEW_ORDER_BTN_TEXT = "New Order";
    private OrderGrid grid;
    private OrderForm form;
    private TextField filter;

    private ListDataProvider<RentOrder> dataProvider = new ListDataProvider<>(new ArrayList<>());
    private OrderListLogic viewLogic = new OrderListLogic(this);
    private Button newOrder;

    public OrderList() {

        setId(VIEW_NAME);
        setSizeFull();

        grid = new OrderGrid();
        grid.setDataProvider(dataProvider);
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        form = new OrderForm(viewLogic);
        VerticalLayout barAndGridLayout = buildVerticalLayout(grid);
        add(barAndGridLayout);
        add(form);
        setFlexGrow(0, barAndGridLayout);
        setFlexGrow(1, form);
        viewLogic.init();
    }

    @Override
    public TextField createFilter() {
        TextField filter = new TextField();
        filter.setId("filter");
        filter.setPlaceholder("Filter by ID or Customer name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(event -> {
            setOrders(viewLogic.filterByIdAndCustomer(event.getValue().toLowerCase()));
        });
        return filter;
    }

    @Override
    public void setNewItemTextAndLogic() {
        Button newItemBtn = getNewItemButton();
        newItemBtn.setText(NEW_ORDER_BTN_TEXT);
        newItemBtn.addClickListener(click -> viewLogic.newOrder());

    }


    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(RentOrder row) {
        grid.getSelectionModel().select(row);
    }

    public void addOrder(RentOrder order) {
        dataProvider.getItems().add(order);
        grid.getDataProvider().refreshAll();
    }

    public void updateOrder(RentOrder order) {
        dataProvider.refreshItem(order);
    }

    public void removeOrder(RentOrder order) {
        dataProvider.getItems().remove(order);
        grid.getDataProvider().refreshAll();
    }

    public void editOrder(RentOrder order) {
        showForm(order != null);
        form.editOrder(order);
    }

    public void showForm(boolean show) {
        form.setVisible(show);
    }

    public void setOrders(List<RentOrder> orders) {
        dataProvider.getItems().clear();
        dataProvider.getItems().addAll(orders);
        grid.getDataProvider().refreshAll();
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}
