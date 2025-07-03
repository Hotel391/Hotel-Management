package models;

import java.util.ArrayList;
import java.util.List;

public class TypeRoom {

    private int typeId;
    private String typeName;
    private String description;
    private int price;
    private List<RoomImage> images = new ArrayList<>();
    private List<RoomNService> services = new ArrayList<>();
    private List<Service> otherServices = new ArrayList<>();
    
    // Constructors
    public TypeRoom() {
    }


    public TypeRoom(int typeId, String typeName, String description, int price) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.description = description;
        this.price = price;
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

    public List<RoomNService> getServices() {
        return services;
    }

    public void setServices(List<RoomNService> services) {
        this.services = services;
    }

    public void addRoomNService(RoomNService rns){
        this.services.add(rns);
    }

    public List<Service> getOtherServices() {
        return otherServices;
    }

    public void setOtherServices(List<Service> otherServices) {
        this.otherServices = otherServices;
    }
    
    public List<RoomImage> getImages() {
        return images;
    }

    public void setImages(List<RoomImage> images) {
        this.images = images;
    }

    public void addImage(RoomImage image) {
        this.images.add(image);
    }
    
    

    @Override
    public String toString() {
        return "TypeRoom{" + "typeId=" + typeId + ", typeName=" + typeName + ", description=" + description + ", price=" + price + ", services=" + services + '}';
    }
    
    
}
