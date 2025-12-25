public class TrackingEvent {
    private String timestamp;
    private String location;
    private String status;
    private String description;

    public TrackingEvent(String timestamp, String location, String status, String description) {
        this.timestamp = timestamp;
        this.location = location;
        this.status = status;
        this.description = description;
    }

    // 為了方便之後印出來看，我們覆寫 toString
    @Override
    public String toString() {
        return "[" + timestamp + "] " + location + " - " + status + " (" + description + ")";
    }

    // Getter methods
    public String getTimestamp() { return timestamp; }
    public String getLocation() { return location; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
}