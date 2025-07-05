package models;

import java.sql.Date;
import java.util.List;

public class BookingDetail {

    private int bookingDetailId;
    private Date startDate;
    private Date endDate;
    private Booking booking;
    private Room room;
    private List<DetailService> services;

    public BookingDetail() {
    }

    public BookingDetail(int bookingDetailId, Date startDate, Date endDate, Booking booking, Room room, List<DetailService> services) {
        this.bookingDetailId = bookingDetailId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.booking = booking;
        this.room = room;
        this.services = services;
    }
    
    

    public BookingDetail(int bookingDetailId, Date startDate, Date endDate, Booking booking, Room room) {
        this.bookingDetailId = bookingDetailId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.booking = booking;
        this.room = room;
    }

    public List<DetailService> getServices() {
        return services;
    }

    public void setServices(List<DetailService> services) {
        this.services = services;
    }

    public int getBookingDetailId() {
        return bookingDetailId;
    }

    public void setBookingDetailId(int bookingDetailId) {
        this.bookingDetailId = bookingDetailId;
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

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "BookingDetail{" + "bookingDetailId=" + bookingDetailId + ", startDate=" + startDate + ", endDate=" + endDate + ", booking=" + booking + ", room=" + room + '}';
    }
    
    
    
}
