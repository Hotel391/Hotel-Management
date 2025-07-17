package models;

public class DailyRevenue {
    private String weekdayName;
    private int day;
    private long totalPrice;

    public DailyRevenue() {
    }


    public DailyRevenue(String weekdayName, int day, long totalPrice) {
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

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }
    
}
