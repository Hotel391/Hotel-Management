package models;

import java.util.ArrayList;
import java.util.List;

public class TypeRoom {

    private int typeId;
    private String typeName;
    private String description;
    private int price;
    private List<RoomNService> services = new ArrayList<>();
    private List<Service> otherServices = new ArrayList<>();
    private int numberOfAvailableRooms;
    private double averageRating;
    private List<String> urlImages = new ArrayList<>();
    private int numberOfReviews;
    private int originPrice;
    private int servicePrice;
    private List<Review> reviews = new ArrayList<>();
    private int adults;
    private int children;
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

    public int getNumberOfAvailableRooms() {
        return numberOfAvailableRooms;
    }

    public void setNumberOfAvailableRooms(int numberOfAvailableRooms) {
        this.numberOfAvailableRooms = numberOfAvailableRooms;
    }
    public double getAverageRating() {
        return averageRating;
    }
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
    public List<String> getImages() {
        return urlImages;
    }
    public void setImages(List<String> urlImages) {
        this.urlImages = urlImages;
    }
    public String getUriContextOfImages(){
        return "Image/" + typeName.replace(" ", "") + "/";
    }
    public int getNumberOfReviews() {
        return numberOfReviews;
    }
    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }
    public int getOriginPrice() {
        return originPrice;
    }
    public void setOriginPrice(int originPrice) {
        this.originPrice = originPrice;
    }
    public int getServicePrice() {
        return servicePrice;
    }
    public void setServicePrice(int servicePrice) {
        this.servicePrice = servicePrice;
    }
    public List<Review> getReviews() {
        return reviews;
    }
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    public int getAdults() {
        return adults;
    }
    public void setAdults(int adults) {
        this.adults = adults;
    }
    public int getChildren() {
        return children;
    }
    public void setChildren(int children) {
        this.children = children;
    }
    @Override
    public String toString() {
        return "TypeRoom{" + "typeId=" + typeId + ", typeName=" + typeName + ", description=" + description + ", price=" + price + ", services=" + services + '}';
    }
    
    
}
