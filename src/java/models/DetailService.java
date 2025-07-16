package models;

import java.math.BigInteger;

public class DetailService {

    private BookingDetail bookingDetail;  
    private Service service;            
    private int quantity;
    private BigInteger priceAtTime;

    public DetailService() {
    }

    public DetailService(BookingDetail bookingDetail, Service service, int quantity) {
        this.bookingDetail = bookingDetail;
        this.service = service;
        this.quantity = quantity;
    }

    public BigInteger getPriceAtTime() {
        return priceAtTime;
    }

    public void setPriceAtTime(BigInteger priceAtTime) {
        this.priceAtTime = priceAtTime;
    }

    public BookingDetail getBookingDetail() {
        return bookingDetail;
    }

    public void setBookingDetail(BookingDetail bookingDetail) {
        this.bookingDetail = bookingDetail;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}