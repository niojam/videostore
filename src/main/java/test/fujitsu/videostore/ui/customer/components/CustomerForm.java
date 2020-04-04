package test.fujitsu.videostore.ui.customer.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.ui.customer.CustomerListLogic;

/**
 * Customer edit/creation form
 */
public class CustomerForm extends Div {

    public static final String SAME_NAME_NOTIFICATION = "Customer with this name already exists";
    public static final String INVALID_CUSTOMER_NITIFICATION = "Please check new Customer fields";
    private VerticalLayout content;

    private TextField name;
    private TextField points;
    private Button save;
    private Button cancel;
    private Button delete;

    private CustomerListLogic viewLogic;
    private Binder<Customer> binder;
    private Customer currentCustomer;

    public CustomerForm(CustomerListLogic customerListLogic) {
        setId("edit-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        add(content);

        viewLogic = customerListLogic;

        name = new TextField("Customer name");
        name.setId("customer-name");
        name.setWidth("100%");
        name.setRequired(true);
        name.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(name);

        points = new TextField("Bonus points");
        points.setId("bonus-points");
        points.setWidth("100%");
        points.setRequired(true);
        points.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(points);

        binder = new Binder<>(Customer.class);
        binder.forField(name)
                .bind("name");
        binder.forField(points)
                .withConverter(new StringToIntegerConverter("Invalid bonus points format"))
                .withValidator(newPoints -> newPoints == null || newPoints >= 0, "Points cannot be negative")
                .bind("points");

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
        });

        save = new Button("Save");
        save.setId("save");
        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            if (currentCustomer != null) {
                Notification validityNotification = new Notification("", 3000, Notification.Position.MIDDLE);
                binder.writeBeanIfValid(currentCustomer);
                if (viewLogic.validateCustomer(currentCustomer)) {
                    validityNotification.setText(INVALID_CUSTOMER_NITIFICATION);
                    validityNotification.open();
                    return;
                }
                if (viewLogic.checkNameAppearance(currentCustomer.getName())) {
                    validityNotification.setText(SAME_NAME_NOTIFICATION);
                    validityNotification.open();
                } else {
                    viewLogic.saveCustomer(currentCustomer);
                }
            }
        });

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.setId("cancel");
        cancel.addClickListener(event -> viewLogic.cancelCustomer());
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelCustomer())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.setId("delete");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentCustomer != null) {
                viewLogic.deleteCustomer(currentCustomer);
            }
        });

        content.add(save, delete, cancel);
    }

    public void editCustomer(Customer customer) {
        if (customer == null) {
            customer = new Customer();
        }
        currentCustomer = customer;
        binder.readBean(customer);

        delete.setEnabled(!viewLogic.canBeDeleted(customer));
    }
}
