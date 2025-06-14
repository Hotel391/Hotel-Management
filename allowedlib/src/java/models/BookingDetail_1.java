package models;

import java.sql.Date;

public class BookingDetail_1 {

    private int bookingDetailId;
    private Date startDate;
    private Date endDate;
    private Booking booking;
    private Room room;

    public BookingDetail_1() {
    }

    public BookingDetail_1(int bookingDetailId, Date startDate, Date endDate, Booking booking, Room room) {
        this.bookingDetailId = bookingDetailId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.booking = booking;
        this.room = room;
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
    
    
}
