import java.util.ArrayList;
import java.util.List;

public class Package {
    private String trackingNumber;
    private String senderId;
    private String receiverName;
    private String receiverAddress;
    private double weight;
    private String serviceType;
    
    // ★ [新增] 依據需求補上: 尺寸、申報價值、內容物
    private String dimensions;       // e.g. "10x10x10"
    private double declaredValue;    // 申報價值 (用來算保險費)
    private String contentDescription; // 內容物描述

    private String currentStatus;
    private List<TrackingEvent> eventHistory;

    // ★ [修改] 建構子增加新欄位 (dimensions, declaredValue, contentDescription)
    public Package(String trackingNumber, String senderId, String receiverName, String receiverAddress, 
                   double weight, String serviceType, 
                   String dimensions, double declaredValue, String contentDescription) {
        this.trackingNumber = trackingNumber;
        this.senderId = senderId;
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.weight = weight;
        this.serviceType = serviceType;
        
        // ★ [新增] 初始化新欄位
        this.dimensions = dimensions;
        this.declaredValue = declaredValue;
        this.contentDescription = contentDescription;

        this.currentStatus = "收件"; // 預設狀態
        this.eventHistory = new ArrayList<>();
        addEvent("系統", "收件", "包裹已建立");
    }

    public void addEvent(String location, String status, String description) {
        String time = java.time.LocalDateTime.now().toString(); 
        TrackingEvent event = new TrackingEvent(time, location, status, description);
        this.eventHistory.add(event);
        this.currentStatus = status;
    }

    @Override
    public String toString() {
        return "包裹單號: " + trackingNumber + " | 內容: " + contentDescription + " | 狀態: " + currentStatus;
    }

    // Getters
    public String getTrackingNumber() { return trackingNumber; }
    public String getSenderId() { return senderId; }
    public double getWeight() { return weight; }
    public String getServiceType() { return serviceType; }
    public List<TrackingEvent> getEventHistory() { return eventHistory; }
    public String getReceiverName() { return receiverName;}
    public String getReceiverAddress() { return receiverAddress;}
    
    // ★ [新增] 新欄位的 Getters
    public String getDimensions() { return dimensions; }
    public double getDeclaredValue() { return declaredValue; }
    public String getContentDescription() { return contentDescription; }
}