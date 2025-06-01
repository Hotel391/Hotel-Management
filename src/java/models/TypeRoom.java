package models;

import java.util.List;

public class TypeRoom {

    private int typeId;
    private String typeName;
    private String description;
    private int price;
    private List<RoomImage> roomImage;

    public TypeRoom() {
    }

    public TypeRoom(int typeId, String typeName, String description, int price, List<RoomImage> roomImage) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.description = description;
        this.price = price;
        this.roomImage = roomImage;
    }

    public List<RoomImage> getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(List<RoomImage> roomImage) {
        this.roomImage = roomImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
    
}
