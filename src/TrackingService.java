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
     * 輔助方法：從歷史紀錄中抓取最新的一筆狀態
     */
    private String getCurrentStatusFromHistory(Package pkg) {
        List<TrackingEvent> history = pkg.getEventHistory();
        
        // 1. 防呆：如果歷史紀錄是空的 (剛建立包裹時可能發生)
        if (history == null || history.isEmpty()) {
            return "Unknown"; 
        }

        // 2. 取得清單中的「最後一個」元素 (即最新的狀態)
        // history.size() - 1 就是最後一項的索引
        TrackingEvent lastEvent = history.get(history.size() - 1);
        
        // 3. 回傳該事件的狀態
        // 您確認過 TrackingEvent 有 getStatus()，這樣寫絕對安全
        return lastEvent.getStatus(); 
    }
}