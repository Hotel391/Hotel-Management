package models;

public class DailyRevenue {
    private String weekdayName;
    private int day;
    private double totalPrice;

    public DailyRevenue() {
    }

    
    public DailyRevenue(String weekdayName, int day, double totalPrice) {
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
}
