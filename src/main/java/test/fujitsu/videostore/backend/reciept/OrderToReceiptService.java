package test.fujitsu.videostore.backend.reciept;

import test.fujitsu.videostore.backend.domain.Customer;
import test.fujitsu.videostore.backend.domain.RentOrder;
import test.fujitsu.videostore.backend.domain.ReturnOrder;
import test.fujitsu.videostore.backend.reciept.price.PriceCalculationStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple receipt creation service
 * <p>
 * Note! All calculations should be in another place. Here we just setting already calculated data. Feel free to refactor.
 */
public class OrderToReceiptService {

    public static final int NEW_REALISE_BONUS_PRICE = 25;

    /**
     * Converts rent order to printable receipt
     *
     * @param order rent object
     * @return Printable receipt object
     */
    public PrintableOrderReceipt convertRentOrderToReceipt(RentOrder order) {
        PrintableOrderReceipt printableOrderReceipt = new PrintableOrderReceipt();

        printableOrderReceipt.setOrderId(order.isNewObject() ? "new" : Integer.toString(order.getId()));
        printableOrderReceipt.setOrderDate(order.getOrderDate());
        printableOrderReceipt.setCustomerName(order.getCustomer().getName());

        List<PrintableOrderReceipt.Item> itemList = new ArrayList<>();
        printableOrderReceipt.setOrderItems(itemList);

        int bonusToPay = 0;
        BigDecimal orderTotalPrice = BigDecimal.ZERO;
        Customer customer = order.getCustomer();
        Integer customerBonusPoints = customer.getPoints();
        for (RentOrder.Item orderItem : order.getItems()) {
            PrintableOrderReceipt.Item item = new PrintableOrderReceipt.Item();
            item.setDays(orderItem.getDays());
            item.setMovieName(orderItem.getMovie().getName());
            item.setMovieType(orderItem.getMovieType());

            if (orderItem.isPaidByBonus()) {
                bonusToPay += NEW_REALISE_BONUS_PRICE * item.getDays();
                item.setPaidBonus(bonusToPay);
            } else {
                PriceCalculationStrategy calculationStrategy = item.getMovieType().getPriceCalculationStrategy();
                BigDecimal itemPrice = calculationStrategy.getPrice(item.getDays());
                orderTotalPrice = orderTotalPrice
                        .add(itemPrice);
                item.setPaidMoney(itemPrice);
                customerBonusPoints += calculationStrategy.getBonus(); // Add bonus
            }

            itemList.add(item);
        }

        printableOrderReceipt.setTotalPrice(orderTotalPrice);

        int remainingBonusPoints = customerBonusPoints - bonusToPay;
        printableOrderReceipt.setRemainingBonusPoints(remainingBonusPoints);
        return printableOrderReceipt;
    }

    /**
     * Converts return order to printable receipt
     *
     * @param order return object
     * @return Printable receipt object
     */
    public PrintableReturnReceipt convertRentOrderToReceipt(ReturnOrder order) {
        PrintableReturnReceipt receipt = new PrintableReturnReceipt();

        receipt.setOrderId(Integer.toString(order.getRentOrder().getId()));
        receipt.setCustomerName(order.getRentOrder().getCustomer().getName());
        receipt.setRentDate(order.getRentOrder().getOrderDate());
        receipt.setReturnDate(order.getReturnDate());

        List<PrintableReturnReceipt.Item> returnedItems = new ArrayList<>();
        BigDecimal totalCharge = BigDecimal.ZERO;
        if (order.getItems() != null) {
            LocalDate orderDate = order.getReturnDate();
            for (RentOrder.Item rentedItem : order.getItems()) {
                PrintableReturnReceipt.Item item = new PrintableReturnReceipt.Item();
                item.setMovieName(rentedItem.getMovie().getName());
                item.setMovieType(rentedItem.getMovieType());
                int daysInRent = (int) ChronoUnit.DAYS.between(orderDate, LocalDate.now());
                item.setExtraDays(daysInRent <= rentedItem.getDays() ? 0: daysInRent - rentedItem.getDays());
                item.setExtraPrice(rentedItem.getMovieType().getPriceCalculationStrategy().getPrice(item.getExtraDays()));
                totalCharge  = totalCharge.add(item.getExtraPrice());
                returnedItems.add(item);
            }
        }
        receipt.setReturnedItems(returnedItems);
        receipt.setTotalCharge(totalCharge);
        return receipt;
    }

}
