public class BillingService {

    // --- 費率設定 (可依需求調整) ---
    private static final double BASE_RATE = 60.0;          // 基礎運費
    private static final double WEIGHT_RATE_PER_KG = 10.0; // 每公斤加價
    private static final double FRAGILE_SURCHARGE = 50.0;  // 易碎品加價
    private static final double EXPRESS_SURCHARGE = 100.0; // 急件加價

    /**
     * 計算運費
     * 規則：基礎費 + (重量 * 費率) + 服務類型加價 + 特殊處理費
     */
    public double calculatePrice(Package pkg) {
        if (pkg == null) return 0.0;

        double total = BASE_RATE;

        // 1. 重量計費
        double weight = pkg.getWeight();
        if (weight > 0) {
            total += weight * WEIGHT_RATE_PER_KG;
        }

        // 2. 服務類型計費 (忽略大小寫比較)
        String type = pkg.getServiceType();
        if (type != null) {
            if (type.equalsIgnoreCase("Express") || type.equalsIgnoreCase("Overnight")) {
                total += EXPRESS_SURCHARGE;
            }
        }

        // 3. 特殊處理費 (邏輯層補償方案)
        // 因為 Package 沒有 isFragile 欄位，我們改為檢查「內容描述」字串
        String content = pkg.getContentDescription();
        if (isFragile(content)) {
            total += FRAGILE_SURCHARGE;
            // 可以在此處印出 Log 確認是否觸發
            // System.out.println("[計費] 檢測到易碎品，加收費用。");
        }

        return total;
    }

    /**
     * 輔助方法：判斷內容物是否為易碎品
     */
    private boolean isFragile(String content) {
        if (content == null) return false;
        String c = content.toLowerCase();
        // 只要描述包含以下關鍵字，就視為易碎品
        return c.contains("glass") || c.contains("fragile") || 
               c.contains("易碎") || c.contains("玻璃") || c.contains("精密");
    }
}