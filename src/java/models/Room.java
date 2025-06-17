package models;

public class Room {

    private int roomNumber;
    private boolean isCleaner;
    private boolean isActive;
    private TypeRoom typeRoom;
    

    public Room() {
    }

    public Room(int roomNumber, boolean isCleaner, TypeRoom typeRoom) {
        this.roomNumber = roomNumber;
        this.isCleaner = isCleaner;
        this.typeRoom = typeRoom;
    }
    
    public Room(int roomNumber, boolean isCleaner, boolean isActive, TypeRoom typeRoom) {
        this.roomNumber = roomNumber;
        this.isCleaner = isCleaner;
        this.isActive = isActive;
        this.typeRoom = typeRoom;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean getIsCleaner() {
        return isCleaner;
    }

    public void setIsCleaner(boolean isCleaner) {
        this.isCleaner = isCleaner;
    }

    public TypeRoom getTypeRoom() {
        return typeRoom;
    }

    public void setTypeRoom(TypeRoom typeRoom) {
        this.typeRoom = typeRoom;
    }

    
}
