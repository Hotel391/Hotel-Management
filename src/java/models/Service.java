package models;

public class Service {

    public static final int MAX_SERVICE_QUANTITY = 1000;
    public static final int MIN_SERVICE_QUANTITY = 1;
    private int serviceId;
    private String serviceName;
    private boolean isActive;
    private int price;

    public Service() {
    }

    public Service(int serviceId, String serviceName, int price) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.price = price;
    }

    public Service(int serviceId, String serviceName, boolean isActive, int price) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.isActive = isActive;
        this.price = price;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    
    
    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
    
}
