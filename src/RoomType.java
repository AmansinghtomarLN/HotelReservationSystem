import java.util.List;

public class RoomType {
    private String code; // Room type code, e.g., "SGL", "DBL"
    private String description; // Description of the room type
    private List<String> amenities; // List of amenities available in the room
    private List<String> features; // List of features of the room

    public RoomType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<String> getFeatures() {
        return features;
    }
}
