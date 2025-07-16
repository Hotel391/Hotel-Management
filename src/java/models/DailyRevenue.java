package models;

import java.math.BigInteger;

public class DailyRevenue {
    private String weekdayName;
    private int day;
    private BigInteger totalPrice;

    public DailyRevenue() {
    }

    public DailyRevenue(String weekdayName, int day, BigInteger totalPrice) {
        this.weekdayName = weekdayName;
        this.day = day;
        this.totalPrice = totalPrice;
    }

    public String getWeekdayName() {
        return weekdayName;
    }

    public void setWeekdayName(String weekdayName) {
        this.weekdayName = weekdayName;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public BigInteger getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigInteger totalPrice) {
        this.totalPrice = totalPrice;
    }
    
}
