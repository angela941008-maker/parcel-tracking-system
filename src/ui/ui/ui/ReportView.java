package ui;

public class ReportView {

    public static void showPackage(Package p) {
        System.out.println("============ 包裹資訊 ============");
        System.out.println("追蹤碼：" + p.getTrackingNumber());
        System.out.println("收件人：" + p.getReceiverName());
        System.out.println("地址：" + p.getReceiverAddress());
        System.out.println("重量：" + p.getWeight());
        System.out.println("狀態：" + p.getEventHistory()
                .get(p.getEventHistory().size() - 1)
                .getStatus());
        System.out.println("=================================");
    }
}
