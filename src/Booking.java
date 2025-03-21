public class Booking {
    private String hotelId; // Hotel ID where the booking was made
    private String arrival; // Arrival date in the format "yyyyMMdd"
    private String departure; // Departure date in the format "yyyyMMdd"
    private String roomType; // Room type (e.g., "SGL", "DBL")
    private String roomRate; // Room rate type (e.g., "Prepaid", "Standard")

    public Booking(String hotelId, String arrival, String departure, String roomType, String roomRate) {
        this.hotelId = hotelId;
        this.arrival = arrival;
        this.departure = departure;
        this.roomType = roomType;
        this.roomRate = roomRate;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getArrival() {
        return arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomRate() {
        return roomRate;
    }
}
