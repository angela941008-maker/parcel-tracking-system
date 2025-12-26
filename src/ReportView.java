import java.util.List;

public class ReportView {

    public static void printPackage(Package p) {
        System.out.println("\n========== 包裹資訊 ==========");
        System.out.printf("追蹤單號: %s%n", p.getTrackingNumber());
        System.out.printf("寄件人ID: %s%n", p.getSenderId());
        System.out.printf("收件人  : %s%n", p.getReceiverName());
        System.out.printf("地址    : %s%n", p.getReceiverAddress());
        System.out.printf("重量(kg): %.2f%n", p.getWeight());
        System.out.printf("服務類型: %s%n", p.getServiceType());
        System.out.printf("尺寸    : %s%n", p.getDimensions());
        System.out.printf("申報價值: %.2f%n", p.getDeclaredValue());
        System.out.printf("內容描述: %s%n", p.getContentDescription());

        // 目前狀態：用最後一筆 event 的 status 來顯示（因為 Package 沒有 getter currentStatus）
        String status = "(未知)";
        List<TrackingEvent> history = p.getEventHistory();
        if (history != null && !history.isEmpty()) {
            TrackingEvent last = history.get(history.size() - 1);
            // 假設 TrackingEvent 有 getStatus()；如果沒有，至少 history.toString 也能印
            try {
                status = last.getStatus();
            } catch (Exception e) {
                status = last.toString();
            }
        }
        System.out.printf("目前狀態: %s%n", status);
        System.out.println("==============================");
    }

    public static void printHistory(Package p) {
        System.out.println("\n------ 追蹤歷史紀錄 ------");
        List<TrackingEvent> history = p.getEventHistory();
        if (history == null || history.isEmpty()) {
            System.out.println("(無紀錄)");
            return;
        }

        for (TrackingEvent e : history) {
            // 假設 TrackingEvent 有 getters：getTime/getLocation/getStatus/getDescription
            // 若沒有，就用 toString()
            try {
                System.out.printf("[%s] %s | %s | %s%n",
                        e.getTime(), e.getLocation(), e.getStatus(), e.getDescription());
            } catch (Exception ex) {
                System.out.println(e.toString());
            }
        }
        System.out.println("--------------------------");
    }
}
