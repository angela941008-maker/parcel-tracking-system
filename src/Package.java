import java.util.ArrayList;
import java.util.List;

public class Package {
    private String trackingNumber;
    private String senderId;
    private String receiverName;
    private String receiverAddress;
    private double weight;
    private String serviceType;
    private String currentStatus;
    // 這裡對應你圖上的 List<TrackingEvent>
    private List<TrackingEvent> eventHistory;

    public Package(String trackingNumber, String senderId, String receiverName, String receiverAddress, double weight, String serviceType) {
        this.trackingNumber = trackingNumber;
        this.senderId = senderId;
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.weight = weight;
        this.serviceType = serviceType;
        this.currentStatus = "收件"; // 預設狀態
        
        // ★重要：一定要初始化 List，不然會空指針錯誤
        this.eventHistory = new ArrayList<>();
        
        // 建立時自動產生第一筆紀錄
        addEvent("系統", "收件", "包裹已建立");
    }

    // 新增追蹤紀錄的方法
    public void addEvent(String location, String status, String description) {
        // 這裡暫時用簡單的時間字串，之後可以改用 new Date().toString()
        String time = java.time.LocalDateTime.now().toString(); 
        TrackingEvent event = new TrackingEvent(time, location, status, description);
        this.eventHistory.add(event);
        this.currentStatus = status; // 更新當前狀態
    }

    @Override
    public String toString() {
        return "包裹單號: " + trackingNumber + " | 狀態: " + currentStatus + " | 寄件人: " + senderId;
    }

    // Getters
    public String getTrackingNumber() { return trackingNumber; }
    public String getSenderId() { return senderId; }
    public double getWeight() { return weight; }
    public String getServiceType() { return serviceType; }
    public List<TrackingEvent> getEventHistory() { return eventHistory; }
    public String getReceiverName() { return receiverName;}
    public String getReceiverAddress() { return receiverAddress;}
}