import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class HotelReservationSystem {

    private static Map<String, Hotel> hotels = new HashMap<>();
    private static List<Booking> bookings = new ArrayList<>();

    private static List<RoomType> roomTypes = new ArrayList<>();;

    public static void main(String[] args) {
        try {
            if (args.length < 4) {
                System.out.println("Usage: myapp --hotels hotels.json --bookings bookings.json");
                return;
            }

            String hotelsFile = args[1];
            String bookingsFile = args[3];

            loadHotelsData(hotelsFile);
            loadBookingsData(bookingsFile);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                handleCommand(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will extract the amenities for rooms from the room type data
     * and will return the list of amenities
     */
    private static ArrayList<String> extractAmenities(String amenities){
        ArrayList<String> amenitiesList = new ArrayList<>();
        int start = 0;
        int end = 0;

        for(int i =0; i<amenities.length(); i++) {
            if (amenities.charAt(i) == '[') {
                start = i;
            }
            if (amenities.charAt(i) == ']') {
                end = i;
                break;
            }
        }

        if(start == 0 && end == 0){
            return amenitiesList;
        }

        String[] amenititesArray = amenities.substring(start+1, end).split(",");
        for(String s : amenititesArray){
            amenitiesList.add(s);
        }
        return amenitiesList;
    }

    /**
     * This method will extract the features for rooms from the room type data
     * and will return the list of features
     */
    private static ArrayList<String> extractFeatures(String features){
        ArrayList<String> featuresList = new ArrayList<>();
        int start = 0;
        int end = 0;

        for(int i =0; i<features.length(); i++) {
            if (features.charAt(i) == '[') {
                start = i;
            }
            if (features.charAt(i) == ']') {
                end = i;
            }
        }
        if(start == 0 && end == 0){
            return featuresList;
        }

        String[] featuresArray = features.substring(start+1, end).split(",");
        for(String s : featuresArray){
            featuresList.add(s);
        }
        return featuresList;
    }


    public static int countOccurrences(String str, String substring) {
        if (str == null || substring == null || substring.isEmpty()) {
            return 0; // Return 0 if string or substring is null or empty
        }

        int count = 0;
        int index = 0;

        // Loop to find all occurrences of the substring
        while ((index = str.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length(); // Move index forward to continue searching
        }

        return count;
    }

    /**
     * This method will read the Hotels.json and will populate the hotels map
     * and will also populate the roomTypes and rooms for each hotel
     */
    private static void loadHotelsData(String filePath) throws IOException {
        String content = readFile(filePath);
        String[] hotelEntries = content.split("\\},\\{");
        for (String hotelEntry : hotelEntries) {
            // Clean up the entries to remove extra characters
            hotelEntry = hotelEntry.trim();
            String[] hotelData = hotelEntry.split(",");

            String hotelId = extractValue(hotelData[0]);
            String hotelName = extractValue(hotelData[1]);
            Hotel hotel = new Hotel(hotelId, hotelName);

            hotel = addRoomTypesData(hotelEntry, hotel);
            hotel = addRoomData(hotelEntry, hotel);

            hotels.put(hotel.getId(), hotel);
        }
    }

    /**
     * This method will extract the room data from the hotel entry and will add the rooms to the hotel
     */
    public static Hotel addRoomData(String hotelEntry, Hotel hotel){
        String searchRooms = "\"rooms\": [";
        int startIndexOfRooms = hotelEntry.indexOf(searchRooms);

        String endSearchRooms = "]";
        int endIndexOfRooms = hotelEntry.substring(startIndexOfRooms).indexOf(endSearchRooms);

        String extractedRoomsData = hotelEntry.substring(startIndexOfRooms).substring(searchRooms.length()-1, endIndexOfRooms+1);

        // Rooms section
        String roomsData = extractArray(extractedRoomsData);
        for (String roomData : roomsData.split("},\\s*")) {
            String[] roomParts = roomData.split(",");
            String roomId = extractValue(roomParts[0]);
            String roomType = extractValue(roomParts[1]);

            hotel.addRoom(new Room(roomType, roomId));
        }
    return hotel;
    }

    /**
     * This method will extract the room types data from the hotel entry and will add the room types to the hotel
     */
    public static Hotel addRoomTypesData(String hotelEntry, Hotel hotel){
        String searchRoomTypes = "\"roomTypes\": [";
        int startIndexOfRoomsData = hotelEntry.indexOf(searchRoomTypes);

        String endSearchRoomTypesData = "\"rooms\": [";
        int endIndexOfRoomsData = hotelEntry.indexOf(endSearchRoomTypesData);

        String roomsTypesData = hotelEntry.substring(startIndexOfRoomsData + searchRoomTypes.length()-1, endIndexOfRoomsData-2);

        String roomTypes = extractArray(roomsTypesData);

        for (String roomTypeData : roomTypes.split("},\\s*")) {
            String[] roomTypeParts = roomTypeData.split(",");
            String roomTypeCode = extractValue(roomTypeParts[0]);
            String roomTypeDescription = extractValue(roomTypeParts[1]);
            List<String> amenities = extractAmenities(roomTypeData);
            List<String> features =  extractFeatures(roomTypeData);

            RoomType roomType = new RoomType(roomTypeCode, roomTypeDescription);
            roomType.setAmenities(amenities);
            roomType.setFeatures(features);

            hotel.addRoomType(roomType);
        }
        return hotel;
    }

    /**
     * This method will read the bookings.json and will populate the bookings list
     */
    private static void loadBookingsData(String filePath) throws IOException {
        String content = readFile(filePath);
        String[] bookingEntries = content.split("},");

        for (String bookingEntry : bookingEntries) {
            String[] bookingData = bookingEntry.split(",");

            String hotelId = extractValue(bookingData[0]);
            String arrival = extractValue(bookingData[1]);
            String departure = extractValue(bookingData[2]);
            String roomType = extractValue(bookingData[3]);
            String roomRate = extractValue(bookingData[4]);

            Booking booking = new Booking(hotelId, arrival, departure, roomType, roomRate);
            bookings.add(booking);
        }
    }


    /**
     * This method will read the file content and will return the content as string
     */
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString();
    }

    /**
     * These are the utility methods will extract the value from the data
     */
    private static String extractValue(String data) {
        return data.split(":")[1].replaceAll("\"", "").trim();
    }

    /**
     * These are the utility methods will extract the value from the data
     */
    private static String extractArray(String data) {
        return data.substring(data.indexOf("[") + 1, data.lastIndexOf("]"));
    }

    /**
     * This method will handle the command and will call the respective methods
     */
    private static void handleCommand(String line) {
        if (line.startsWith("Availability")) {
            handleAvailabilityCommand(line);
        } else if (line.startsWith("Search")) {
            handleSearchCommand(line);
        } else {
            System.out.println("Unknown command.");
        }
    }

    /**
     * This method will handle the availability command and will calculate the availability
     */
    private static void handleAvailabilityCommand(String line) {
        String[] parts = line.split("\\(");
        String[] args = parts[1].split("\\)")[0].split(", ");
        String hotelId = args[0];
        String roomTypeCode = args[2];
        String dateRange = args[1];

        Hotel hotel = hotels.get(hotelId);
        if (hotel == null) {
            System.out.println("Hotel not found.");
            return;
        }

        RoomType roomType = hotel.getRoomType(roomTypeCode);
        if (roomType == null) {
            System.out.println("Room type not found.");
            return;
        }
        int availableRooms = calculateAvailability(hotel, roomType, dateRange);

        System.out.println("Availability for " + roomTypeCode + " in " + hotelId + ": " + availableRooms);
    }

    /**
     * This method will handle the search command and will search the availability
     */
    private static void handleSearchCommand(String line) {
        String[] parts = line.split("\\(");
        String[] args = parts[1].split("\\)")[0].split(", ");
        String hotelId = args[0];
        int daysAhead = Integer.parseInt(args[1]);
        String roomTypeCode = args[2];

        Hotel hotel = hotels.get(hotelId);
        if (hotel == null) {
            System.out.println("Hotel not found.");
            return;
        }

        RoomType roomType = hotel.getRoomType(roomTypeCode);
        if (roomType == null) {
            System.out.println("Room type not found.");
            return;
        }

        List<String> availableRanges = searchAvailability(hotel, roomType, daysAhead);
        if (availableRanges.isEmpty()) {
            System.out.println("");
        } else {
            System.out.println(String.join(", ", availableRanges));
        }
    }

    /**
     * This method will calculate the availability of the rooms
     */
    private static int calculateAvailability(Hotel hotel, RoomType roomType, String dateRange) {
        String[] dates = dateRange.split("-");
        String startDate = dates[0];
        String endDate = dates.length > 1 ? dates[1] : startDate;

        int availableRooms = hotel.getAvailableRooms(roomType.getCode(), startDate, endDate);

        // Apply booking logic
        for (Booking booking : bookings) {
            if (booking.getHotelId().equals(hotel.getId()) && booking.getRoomType().equals(roomType.getCode())) {
                if (isBookingInDateRange(booking, startDate, endDate)) {
                    availableRooms--;
                }
            }
        }

        return availableRooms;
    }

    /**
     * This method will check if the booking is in the date range
     */
    private static boolean isBookingInDateRange(Booking booking, String startDate, String endDate) {
        return (booking.getArrival().compareTo(endDate) <= 0) && (booking.getDeparture().compareTo(startDate) >= 0);
    }

    /**
     * This method will search the availability for the given days ahead
     */
    private static List<String> searchAvailability(Hotel hotel, RoomType roomType, int daysAhead) {
        List<String> availableRanges = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, daysAhead);

        for (int i = 0; i < daysAhead; i++) {
            String startDate = dateFormat.format(calendar.getTime());
            String endDate = startDate;

            int availableRooms = calculateAvailability(hotel, roomType, startDate);
            if (availableRooms > 0) {
                availableRanges.add("(" + startDate + "-" + endDate + ", " + availableRooms + ")");
            }

            calendar.add(Calendar.DATE, 1);
        }

        return availableRanges;
    }


}
