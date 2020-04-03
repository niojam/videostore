package test.fujitsu.videostore.ui.order;

import com.vaadin.flow.component.UI;
import test.fujitsu.videostore.backend.database.DBTableRepository;
import test.fujitsu.videostore.backend.database.domainrepository.CustomerRepository;
import test.fujitsu.videostore.backend.database.domainrepository.MovieRepository;
import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.Movie;
import test.fujitsu.videostore.backend.domain.RentOrder;
import test.fujitsu.videostore.backend.reciept.OrderToReceiptService;
import test.fujitsu.videostore.ui.database.CurrentDatabase;

import java.util.List;
import java.util.stream.Collectors;

public class OrderListLogic {

    private OrderList view;
    private DBTableRepository<RentOrder> repository;
    private OrderToReceiptService orderToReceiptService;

    public OrderListLogic(OrderList orderList) {
        view = orderList;

        orderToReceiptService = new OrderToReceiptService();
    }

    public void init() {
        if (CurrentDatabase.get() == null) {
            return;
        }

        repository = CurrentDatabase.get().getOrderTable();
        view.setNewItemEnabled(true);
        view.setOrders(repository.getAll());
    }

    public void cancelOrder() {
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

        UI.getCurrent().navigate(OrderList.class, fragmentParameter);
    }

    public void enter(String orderId) {
        if (orderId != null && !orderId.isEmpty()) {
            if (orderId.equals("new")) {
                newOrder();
            } else {
                int pid = Integer.parseInt(orderId);
                RentOrder order = findOrder(pid);
                view.selectRow(order);
            }
        } else {
            view.showForm(false);
        }
    }

    private RentOrder findOrder(int orderId) {
        return repository.findById(orderId);
    }

    public void saveOrder(RentOrder order) {
        boolean isNew = order.isNewObject();

        RentOrder updatedObject = repository.createOrUpdate(order);
        updateOrderMovieStock(order);

        if (isNew) {
            view.addOrder(updatedObject);
        } else {
            view.updateOrder(order);
        }

        view.clearSelection();
        setFragmentParameter("");
        view.showSaveNotification(order.getId() + (isNew ? " created" : " updated"));
    }

    public void deleteOrder(RentOrder order) {
        repository.remove(order);

        view.clearSelection();
        view.removeOrder(order);
        setFragmentParameter("");
        view.showSaveNotification(order.getId() + " removed");
    }

    public void editOrder(RentOrder order) {
        if (order == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(order.getId() + "");
        }
        view.editOrder(order);
    }

    public void newOrder() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editOrder(new RentOrder());
    }

    public void rowSelected(RentOrder order) {
        if (order == null) {
            return;
        }
        editOrder(order);
    }

    public OrderToReceiptService getOrderToReceiptService() {
        return orderToReceiptService;
    }

    public void updateOrderMovieStock(RentOrder order) {
        order.getItems().forEach(item -> {
            Movie rentMovie = item.getMovie();
            rentMovie.setStockCount(rentMovie.getStockCount() - 1);
            MovieRepository.getInstance().createOrUpdate(rentMovie);
        });
    }

    public void setCustomerBonuses(Customer customer, Integer remainingBonus) {
        customer.setPoints(remainingBonus);
        CustomerRepository.getInstance().createOrUpdate(customer);
    }

    public List<RentOrder> filterByIdAndCustomer(String input) {
        return repository.getAll().stream().filter(order -> order.getCustomer().getName().toLowerCase().startsWith(input)
                || order.getCustomer().getName().toLowerCase().contains(input)
                || Integer.toString(order.getId()).equals(input))
                .collect(Collectors.toList());
    }

    public DBTableRepository<RentOrder> getRepository() {
        return repository;
    }
}
