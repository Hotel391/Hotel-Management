package models;

public class RoomNService {

    private TypeRoom typeRoom;
    private Service service;
    private int quantity;

    public RoomNService() {
    }

    public RoomNService(TypeRoom typeRoom, Service service, int quantity) {
        this.typeRoom = typeRoom;
        this.service = service;
        this.quantity = quantity;
    }

    public TypeRoom getTypeRoom() {
        return typeRoom;
    }

    public void setTypeRoom(TypeRoom typeRoom) {
        this.typeRoom = typeRoom;
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
