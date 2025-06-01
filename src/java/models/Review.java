package models;

import java.util.Date;

public class Review {

    private int reviewId;
    private int rating;
    private String feedBack;
    private Date date;
    private BookingDetail bookingDetail;
    private CustomerAccount customerAccount;

    public Review() {
    }

    public Review(int reviewId, int rating, String feedBack, Date date, BookingDetail bookingDetail, CustomerAccount customerAccount) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.feedBack = feedBack;
        this.date = date;
        this.bookingDetail = bookingDetail;
        this.customerAccount = customerAccount;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BookingDetail getBookingDetail() {
        return bookingDetail;
    }

    public void setBookingDetail(BookingDetail bookingDetail) {
        this.bookingDetail = bookingDetail;
    }

    public CustomerAccount getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(CustomerAccount customerAccount) {
        this.customerAccount = customerAccount;
    }

    @Override
    public String toString() {
        return "Review{" + "reviewId=" + reviewId + ", rating=" + rating + ", feedBack=" + feedBack + ", date=" + date + ", bookingDetail=" + bookingDetail.getBooking().getBookingId() + ", customerAccount=" + customerAccount.getCustomer().getFullName() + '}';
    }
    
    
}
