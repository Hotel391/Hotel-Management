package models;

import java.sql.Date;

public class Cart {

    private int cartId;
    private int totalPrice;
    private Date payDay;
    private String status;
    private Date startDate;
    private Date endDate;
    private boolean isActive;
    private boolean isPayment;
    private Customer customer;
    private PaymentMethod paymentMethod;

    public Cart() {
    }

    public Cart(int cartId, int totalPrice, Date payDay, String status, Date startDate, Date endDate, boolean isActive, boolean isPayment, Customer customer, PaymentMethod paymentMethod) {
        this.cartId = cartId;
        this.totalPrice = totalPrice;
        this.payDay = payDay;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.isPayment = isPayment;
        this.customer = customer;
        this.paymentMethod = paymentMethod;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isIsPayment() {
        return isPayment;
    }

    public void setIsPayment(boolean isPayment) {
        this.isPayment = isPayment;
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
    
}
