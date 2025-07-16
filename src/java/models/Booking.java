package models;

import java.math.BigInteger;
import java.sql.Date;

public class Booking {

    private int bookingId;
    private BigInteger totalPrice;
    private BigInteger paidAmount;
    private Date payDay;
    private String status;
    private Customer customer;
    private PaymentMethod paymentMethod;
    private PaymentMethod paymentMethodCheckIn;

    public Booking() {
    }

    public Booking(int bookingId, BigInteger totalPrice, BigInteger paidAmount, Date payDay, String status, Customer customer, PaymentMethod paymentMethod) {
        this.bookingId = bookingId;
        this.totalPrice = totalPrice;
        this.paidAmount = paidAmount;
        this.payDay = payDay;
        this.status = status;
        this.customer = customer;
        this.paymentMethod = paymentMethod;
    }

    public BigInteger getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigInteger paidAmount) {
        this.paidAmount = paidAmount;
    }
    
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public BigInteger getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigInteger totalPrice) {
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

    public PaymentMethod getPaymentMethodCheckIn() {
        return paymentMethodCheckIn;
    }

    public void setPaymentMethodCheckIn(PaymentMethod paymentMethodCheckIn) {
        this.paymentMethodCheckIn = paymentMethodCheckIn;
    }

    @Override
    public String toString() {
        return "Booking{" + "bookingId=" + bookingId + ", totalPrice=" + totalPrice + ", payDay=" + payDay + ", status=" + status + ", customer=" + customer + ", paymentMethod=" + paymentMethod + '}';
    }
}