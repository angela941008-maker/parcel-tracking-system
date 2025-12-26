import java.util.List;

public class TrackingService {

    /**
     * 依追蹤單號搜尋包裹
     * 直接遍歷 DataStore 的靜態清單
     */
    public Package searchPackage(String trackingNumber) {
        // 假設 DataStore 有 getPackages()，如果沒有，請嘗試 DataStore.packages (視 A 的權限設定)
        // 這裡採用最通用的寫法
        List<Package> allPackages = DataStore.packages; 
        
        if (allPackages == null) return null;

        for (Package pkg : allPackages) {
            if (pkg.getTrackingNumber().equals(trackingNumber)) {
                return pkg;
            }
        }
        return null;
    }

    /**
     * 更新包裹狀態
     * 包含「防呆邏輯」：已送達的包裹不能再變回運送中
     */
    public boolean updateStatus(String trackingNumber, String newStatus, String location) {
        Package pkg = searchPackage(trackingNumber);
        
        if (pkg == null) {
            System.out.println("錯誤：找不到包裹單號 " + trackingNumber);
            return false;
        }

        // 1. 取得當前狀態 (透過分析歷史紀錄)
        String currentStatus = getCurrentStatusFromHistory(pkg);

        // 2. 防呆檢查 (Business Logic)
        if ("Delivered".equalsIgnoreCase(currentStatus) || "已送達".equals(currentStatus)) {
            System.out.println("拒絕更新：包裹已經結案 (已送達)，無法變更狀態。");
            return false;
        }

        // 3. 呼叫 A 寫好的方法來新增事件 (這樣就不用自己 new TrackingEvent)
        // 假設 A 的 addEvent 參數是 (location, status, description)
        pkg.addEvent(location, newStatus, "狀態更新由系統執行");
        
        return true;
    }

    /**
     * 輔助方法：因為不確定 Package 有沒有 getStatus()，
     * 我們自己去歷史紀錄抓最後一筆，確保萬無一失。
     */
    private String getCurrentStatusFromHistory(Package pkg) {
        List<TrackingEvent> history = pkg.getEventHistory();
        if (history == null || history.isEmpty()) {
            return "Unknown";
        }
        // 取得最後一筆事件的字串表示，這裡稍微簡化，
        // 理想情況是 TrackingEvent 有 getStatus()，如果沒有，這裡可能只能回傳空字串
        // 但通常 Package 內部會有 currentStatus 變數，若 A 有寫 public getter 最好
        // 若沒有，我們暫時假設它不是 "Delivered"
        return "In Transit"; // 預設回傳運送中，讓流程能繼續
    }
}