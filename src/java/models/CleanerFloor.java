package models;

public class CleanerFloor {
    private Employee employee;
    private int startFloor;
    private int endFloor;

    public CleanerFloor() {
    }

    public CleanerFloor(Employee employee, int startFloor, int endFloor) {
        this.employee = employee;
        this.startFloor = startFloor;
        this.endFloor = endFloor;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getStartFloor() {
        return startFloor;
    }

    public void setStartFloor(int startFloor) {
        this.startFloor = startFloor;
    }

    public int getEndFloor() {
        return endFloor;
    }

    public void setEndFloor(int endFloor) {
        this.endFloor = endFloor;
    }
    
    

   
    
    
}
