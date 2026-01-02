// 手動模擬單元測試 (Custom Unit Test)
import models.Package;
import models.TrackingEvent;
public class PackageTest {
    
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Start Unit Test (Package Core Logic)   ");
        System.out.println("============================================");
        
        int passed = 0;
        int failed = 0;

        // --- 測試案例 1: 測試建構子 ---
        System.out.print("Test Case 1 [testPackageCreation]: ");
        try {
            // 建立一個標準包裹
            Package p = new Package("TRK-001", "S01", "R01", "Addr", 5.0, "Standard", "10x10", 100.0, "Book");
            
            // 驗證資料是否正確
            if (!"TRK-001".equals(p.getTrackingNumber())) throw new Exception("Error: Tracking Number mismatch");
            if (p.getWeight() != 5.0) throw new Exception("Error: Weight mismatch");
            
            // 檢查是否自動產生了第一筆「收件」紀錄
            if (p.getEventHistory().isEmpty()) throw new Exception("Error: Initial event missing");
            
            // 成功 (使用純文字標記)
            System.out.println("[PASS]"); 
            passed++;
        } catch (Exception e) {
            System.out.println("[FAIL] -> " + e.getMessage());
            failed++;
        }

        // --- 測試案例 2: 測試物流更新 ---
        System.out.print("Test Case 2 [testAddEvent]:        ");
        try {
            Package p = new Package("TRK-002", "S01", "R01", "Addr", 1.0, "Standard", "5x5", 10.0, "Toy");
            
            // 新增一個物流事件
            p.addEvent("Hub A", "Shipping", "In transit");
            
            // 驗證事件數量 (應該是 2: 初始收件 + 新增的)
            if (p.getEventHistory().size() < 2) throw new Exception("Error: Event count mismatch");
            
            // 驗證最後一筆狀態
            TrackingEvent lastEvent = p.getEventHistory().get(p.getEventHistory().size() - 1);
            if (!"Shipping".equals(lastEvent.getStatus())) throw new Exception("Error: Status update failed");
            
            System.out.println("[PASS]");
            passed++;
        } catch (Exception e) {
            System.out.println("[FAIL] -> " + e.getMessage());
            e.printStackTrace(); 
            failed++;
        }

        System.out.println("============================================");
        System.out.println("Result: " + passed + " Passed, " + failed + " Failed");
        System.out.println("============================================");
    }
}