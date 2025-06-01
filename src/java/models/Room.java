package Models;

import java.util.List;

public class Room {

    private int roomNumber;
    private boolean isCleaner;
    private TypeRoom typeRoom;
    

    public Room() {
    }

    public Room(int roomNumber, boolean isCleaner, TypeRoom typeRoom) {
        this.roomNumber = roomNumber;
        this.isCleaner = isCleaner;
        this.typeRoom = typeRoom;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isIsCleaner() {
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
