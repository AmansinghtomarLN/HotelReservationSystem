import java.util.*;

public class Hotel {
    private String id;
    private String name;
    private Map<String, RoomType> roomTypes = new HashMap<>(); // Room type code to RoomType
    private List<Room> rooms = new ArrayList<>(); // List of rooms

    public Hotel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addRoomType(RoomType roomType) {
        roomTypes.put(roomType.getCode(), roomType);
    }

    public RoomType getRoomType(String code) {
        return roomTypes.get(code);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public int getAvailableRooms(String roomTypeCode, String startDate, String endDate) {
        int totalRooms = 0;
        for (Room room : rooms) {
            if (room.getRoomType().equals(roomTypeCode)) {
                totalRooms++;
            }
        }
        return totalRooms;
    }
}
