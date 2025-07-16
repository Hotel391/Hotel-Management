package models;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TypeRoom {

    private int typeId;
    private String typeName;
    private String description;
    private BigInteger price;
    private int maxAdult;
    private int maxChildren;
    private List<RoomImage> images = new ArrayList<>();
    private List<RoomNService> services = new ArrayList<>();
    private List<Service> otherServices = new ArrayList<>();
    private int numberOfAvailableRooms;
    private double averageRating;
    private List<String> urlImages = new ArrayList<>();
    private int numberOfReviews;
    private BigInteger originPrice;
    private BigInteger servicePrice;
    private List<Review> reviews = new ArrayList<>();
    private int adults;
    private int children;
    private int totalRooms;
    // Constructors
    public TypeRoom() {
    }


    public TypeRoom(int typeId, String typeName, String description, BigInteger price) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.description = description;
        this.price = price;
    }

    public TypeRoom(int typeId, String typeName, String description, BigInteger price, int maxAdult, int maxChildren) {
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

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
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
    
    public List<RoomImage> getRoomImages() {
        return images;
    }

    public void setImages(List<?> images) {
        if (images != null && !images.isEmpty()) {
            Object first = images.get(0);
            if (first instanceof String) {
                this.urlImages = (List<String>) images;
            } else if (first instanceof RoomImage) {
                this.images = (List<RoomImage>) images;
            }
        }
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
    public BigInteger getOriginPrice() {
        return originPrice;
    }
    public void setOriginPrice(BigInteger originPrice) {
        this.originPrice = originPrice;
    }
    public BigInteger getServicePrice() {
        return servicePrice;
    }
    public void setServicePrice(BigInteger servicePrice) {
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
    public int getTotalRooms() {
        return totalRooms;
    }
    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }
    
    @Override
    public String toString() {
        return "TypeRoom{" + "typeId=" + typeId + ", typeName=" + typeName + ", description=" + description + ", price=" + price + ", services=" + services + '}';
    }
    
    
}
