package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TypeRoom {

    private int typeId;
    private String typeName;
    private String description;
    private int price;
    private List<RoomImage> roomImage = new ArrayList<>();
    private List<RoomNService> services = new ArrayList<>();

    // Constructors
    public TypeRoom() {
    }

    public TypeRoom(int typeId, String typeName, String description, int price) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.description = description;
        this.price = price;
    }

    public TypeRoom(int typeId, String typeName, String description, int price, List<RoomImage> roomImage) {
        this(typeId, typeName, description, price);
        this.roomImage = roomImage;
    }

    // Getter & Setter
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<RoomImage> getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(List<RoomImage> roomImage) {
        this.roomImage = roomImage;
    }

    public List<RoomNService> getServices() {
        return services;
    }

    public void setServices(List<RoomNService> services) {
        this.services = services;
    }

    public void addRoomNService(RoomNService rns) {
        if (rns != null) {
            services.add(rns);
        }
    }

    // Optional: For using as Map key (equals + hashCode)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeRoom)) return false;
        TypeRoom typeRoom = (TypeRoom) o;
        return typeId == typeRoom.typeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeId);
    }
}
