package models;

import java.util.ArrayList;
import java.util.List;

public class TypeRoom {

    private int typeId;
    private String typeName;
    private String description;
    private int price;
    private int maxAdult;
    private int maxChildren;
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
    
    public TypeRoom(int typeId, String typeName, String description, int price, int maxAdult, int maxChildren) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.description = description;
        this.price = price;
        this.maxAdult = maxAdult;
        this.maxChildren = maxChildren;
    }

    public int getMaxAdult() {
        return maxAdult;
    }

    public void setMaxAdult(int maxAdult) {
        this.maxAdult = maxAdult;
    }

    public int getMaxChildren() {
        return maxChildren;
    }

    public void setMaxChildren(int maxChildren) {
        this.maxChildren = maxChildren;
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
