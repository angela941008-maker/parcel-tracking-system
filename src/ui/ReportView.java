package ui;

import java.util.List;

public class ReportView {

    // 印出包裹詳細資料（對上你貼的 Package getter）
    public void printPackage(Package p) {
        System.out.println("========================================");
        System.out.println("追蹤碼: " + p.getTrackingNumber());
        System.out.println("寄件人ID: " + p.getSenderId());
        System.out.println("收件人: " + p.getReceiverName());
        System.out.println("收件地址: " + p.getReceiverAddress());
        System.out.println("重量(kg): " + p.getWeight());
        System.out.println("服務類型: " + p.getServiceType());
        System.out.println("尺寸: " + p.getDimensions());
        System.out.println("申報價值: " + p.getDeclaredValue());
        System.out.println("內容物: " + p.getContentDescription());
        System.out.println("目前狀態: " + getLastStatus(p));
        System.out.println("========================================");
    }

    // 印出追蹤事件列表
    public void printEvents(List<TrackingEvent> events) {
        if (events == null || events.isEmpty()) {
            System.out.println("（無追蹤事件）");
            return;
        }

        System.out.println("------------ 追蹤歷史 ------------");
        for (TrackingEvent e : events) {
            System.out.println(e); // 先用 toString()，之後我們再做更漂亮的表格
        }
        System.out.println("---------------------------------");
    }

    // 從最後一筆事件抓「目前狀態」
    private String getLastStatus(Package p) {
        List<TrackingEvent> list = p.getEventHistory();
        if (list == null || list.isEmpty()) return "未知";
        return list.get(list.size() - 1).getStatus();
    }
}
