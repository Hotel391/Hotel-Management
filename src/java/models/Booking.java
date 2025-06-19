package models;

import java.sql.Date;

public class Booking {

    private int bookingId;
    private int totalPrice;
    private Date payDay;
    private String status;
    private Customer customer;
    private PaymentMethod paymentMethod;

    public Booking() {
    }

    public Booking(int bookingId, int totalPrice, Date payDay, String status, Customer customer, PaymentMethod paymentMethod) {
        this.bookingId = bookingId;
        this.totalPrice = totalPrice;
        this.payDay = payDay;
        this.status = status;
        this.customer = customer;
        this.paymentMethod = paymentMethod;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getPayDay() {
        return payDay;
    }

    public void setPayDay(Date payDay) {
        this.payDay = payDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Booking{" + "bookingId=" + bookingId + ", totalPrice=" + totalPrice + ", payDay=" + payDay + ", status=" + status + ", customer=" + customer + ", paymentMethod=" + paymentMethod + '}';
    }

}
