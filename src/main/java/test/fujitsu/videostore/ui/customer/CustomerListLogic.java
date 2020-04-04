package test.fujitsu.videostore.ui.customer;

import com.vaadin.flow.component.UI;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.database.domainrepository.CustomerRepository;
import test.fujitsu.videostore.backend.database.domainrepository.OrderRepository;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.RentOrder;
import test.fujitsu.videostore.ui.database.CurrentDatabase;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerListLogic {

    private CustomerList view;

    private DBTableRepository<Customer> customerDBTableRepository;

    public CustomerListLogic(CustomerList customerList) {
        view = customerList;
    }

    public void init() {
        if (CurrentDatabase.get() == null) {
            return;
        }

        customerDBTableRepository = CurrentDatabase.get().getCustomerTable();

        view.setNewItemEnabled(true);
        view.setCustomers(customerDBTableRepository.getAll());
    }

    public void cancelCustomer() {
        setFragmentParameter("");
        view.clearSelection();
    }

    private void setFragmentParameter(String movieId) {
        String fragmentParameter;
        if (movieId == null || movieId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = movieId;
        }

        UI.getCurrent().navigate(CustomerList.class, fragmentParameter);
    }

    public void enter(String customerId) {
        if (customerId != null && !customerId.isEmpty()) {
            if (customerId.equals("new")) {
                newCustomer();
            } else {
                int pid = Integer.parseInt(customerId);
                Customer customer = findCustomer(pid);
                view.selectRow(customer);
            }
        } else {
            view.showForm(false);
        }
    }

    private Customer findCustomer(int customerId) {
        return customerDBTableRepository.findById(customerId);
    }

    public void saveCustomer(Customer customer) {
        boolean isNew = customer.isNewObject();

        Customer updatedObject = customerDBTableRepository.createOrUpdate(customer);

        if (isNew) {
            view.addCustomer(updatedObject);
        } else {
            view.updateCustomer(customer);
        }

        view.clearSelection();
        setFragmentParameter("");
        view.showSaveNotification(customer.getName() + (isNew ? " created" : " updated"));
    }

    public void deleteCustomer(Customer customer) {
        customerDBTableRepository.remove(customer);
        cleanCustomerOrders(customer);

        view.clearSelection();
        view.removeCustomer(customer);
        setFragmentParameter("");
        view.showSaveNotification(customer.getName() + " removed");
    }

    private void cleanCustomerOrders(Customer customer) {
        OrderRepository orderRepository = OrderRepository.getInstance();
        List<RentOrder> ordersToDelete = orderRepository.getAll().stream()
                .filter(order -> order.getCustomer().getId() == customer.getId())
                .collect(Collectors.toList());
        ordersToDelete.forEach(orderRepository::remove);
    }

    public void editCustomer(Customer customer) {
        if (customer == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(customer.getId() + "");
        }
        view.editCustomer(customer);
    }

    public void newCustomer() {
        setFragmentParameter("new");
        view.clearSelection();
        view.editCustomer(new Customer());
    }

    public boolean canBeDeleted(Customer customer) {
        return OrderRepository.getInstance().getAll().stream()
                .anyMatch(order -> order.getCustomer().getId() == customer.getId()
                        & order.getItems().stream().anyMatch((item -> item.getReturnedDay() == null)))
                || customer.getId() == -1;
    }

    public List<Customer> filterByName(String inputName) {
        return customerDBTableRepository.getAll().stream()
                .filter(customer -> customer.getName().toLowerCase().contains(inputName))
                .collect(Collectors.toList());
    }

    public boolean validateCustomer(Customer customerToValidate) {
        return customerToValidate.getName() == null || customerToValidate.getName().equals("")
                || customerToValidate.getPoints() < 0;
    }

    public boolean checkNameAppearance(String name) {
        return CustomerRepository.getInstance().getAll().stream()
                .anyMatch(customer -> customer.getName().equals(name));
    }

    public void rowSelected(Customer customer) {
        editCustomer(customer);
    }
}
