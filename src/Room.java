public class Room {
    private String roomId; // Unique identifier for the room
    private String roomType; // Room type, e.g., "SGL", "DBL"

    public Room(String roomId, String roomType) {
        this.roomId = roomId;
        this.roomType = roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomType() {
        return roomType;
    }
}
