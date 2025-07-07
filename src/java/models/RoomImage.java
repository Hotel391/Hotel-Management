package models;

public class RoomImage {
    private int imageId;
    private String image;
    

    public RoomImage() {
    }

    public RoomImage(int imageId, String image) {
        this.image = image;
        this.imageId = imageId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "RoomImage{" + "imageId=" + imageId + ", image=" + image + '}';
    }
    
    
}
