package test.fujitsu.videostore.ui.customer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.ui.MainLayout;
import test.fujitsu.videostore.ui.ModelList;
import test.fujitsu.videostore.ui.customer.components.CustomerForm;
import test.fujitsu.videostore.ui.customer.components.CustomerGrid;

import java.util.ArrayList;
import java.util.List;

@Route(value = CustomerList.VIEW_NAME, layout = MainLayout.class)
public class CustomerList extends ModelList<Customer> implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "CustomerList";
    public static final String NEW_CUSTOMER_BTN_TEXT = "New Customer";
    private CustomerGrid grid;
    private CustomerForm form;

    private ListDataProvider<Customer> dataProvider = new ListDataProvider<>(new ArrayList<>());
    private CustomerListLogic viewLogic = new CustomerListLogic(this);

    public CustomerList() {
        setId(VIEW_NAME);
        setSizeFull();
        grid = new CustomerGrid();
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));
        grid.setDataProvider(dataProvider);
        form = new CustomerForm(viewLogic);
        add(buildVerticalLayout(grid));
        add(form);
        viewLogic.init();
    }


    @Override
    public TextField createFilter() {
        TextField filter = new TextField();
        filter.setId("filter");
        filter.setPlaceholder("Filter by customer name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(event -> {
            setCustomers(viewLogic.filterByName(event.getValue().toLowerCase()));
        });
        return filter;
    }

    @Override
    public void setNewItemTextAndLogic() {
        Button newItemBtn = getNewItemButton();
        newItemBtn.setText(NEW_CUSTOMER_BTN_TEXT);
        newItemBtn.addClickListener(click -> viewLogic.newCustomer());
    }



    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(Customer row) {
        grid.getSelectionModel().select(row);
    }

    public void addCustomer(Customer customer) {
        dataProvider.getItems().add(customer);
        grid.getDataProvider().refreshAll();
    }

    public void updateCustomer(Customer customer) {
        dataProvider.refreshItem(customer);
    }

    public void removeCustomer(Customer customer) {
        dataProvider.getItems().remove(customer);
        dataProvider.refreshAll();
    }

    public void editCustomer(Customer customer) {
        showForm(customer != null);
        form.editCustomer(customer);
    }

    public void showForm(boolean show) {
        form.setVisible(show);
    }

    public void setCustomers(List<Customer> customers) {
        dataProvider.getItems().clear();
        dataProvider.getItems().addAll(customers);
        dataProvider.refreshAll();
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}
