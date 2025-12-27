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

        String status = "(未知)";
        List<TrackingEvent> history = p.getEventHistory();
        if (history != null && !history.isEmpty()) {
            TrackingEvent last = history.get(history.size() - 1);
            status = last.toString();
        }
        System.out.printf("目前狀態(最後事件): %s%n", status);
        System.out.println("==============================");
    }

    public static void printHistory(Package p) {
        System.out.println("\n------ 追蹤歷史紀錄 ------");
        List<TrackingEvent> history = p.getEventHistory();

        if (history == null || history.isEmpty()) {
            System.out.println("(無紀錄)");
            System.out.println("--------------------------");
            return;
        }

        for (TrackingEvent e : history) {
            System.out.println(e.toString());
        }

        System.out.println("--------------------------");
    }
}

