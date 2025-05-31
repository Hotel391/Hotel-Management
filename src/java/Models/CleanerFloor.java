package Models;

public class CleanerFloor {
    private Employee employee;
    private int floor;

    public CleanerFloor() {
    }

    public CleanerFloor(Employee employee, int floor) {
        this.employee = employee;
        this.floor = floor;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
    
    
}
