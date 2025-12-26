package ui;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private final InputHelper in = new InputHelper();
    private final ReportView view = new ReportView();

    // 目前先用 List 暫存（之後可接 DataStore）
    private final List<Package> packages = new ArrayList<>();

    public void start() {
        while (true) {
            System.out.println("\n====== 包裹追蹤系統 ======");
            System.out.println("1) 建立新包裹");
            System.out.println("2) 查詢包裹（追蹤碼）");
            System.out.println("3) 更新包裹狀態（新增追蹤事件）");
            System.out.println("0) 離開");

            int choice = in.readInt("請選擇：", 0, 3);

            switch (choice) {
                case 1 -> createPackage();
                case 2 -> queryPackage();
                case 3 -> updateStatus();
                case 0 -> { return; }
            }
        }
    }

    // ========== 功能 1：建立包裹 ==========
    private void createPackage() {
        System.out.println("\n[建立包裹]");

        String senderId = in.readString("寄件人ID：");
        String receiverName = in.readString("收件人姓名：");
        String receiverAddress = in.readString("收件地址：");
        double weight = in.readDouble("重量(kg)：", 0.01, 99999);
        String serviceType = in.readString("服務類型：");

        String dimensions = in.readString("尺寸(例 10x10x10)：");
        double declaredValue = in.readDouble("申報價值：", 0, 999999999);
        String contentDescription = in.readString("內容物描述：");

        // 產生追蹤碼（暫時在 C 做，之後可交給 B）
        String trackingNumber = "TRK" + System.currentTimeMillis();

        Package p = new Package(
                trackingNumber,
                senderId,
                receiverName,
                receiverAddress,
                weight,
                serviceType,
                dimensions,
                declaredValue,
                contentDescription
        );

        packages.add(p);

        System.out.println("✅ 包裹建立成功！");
        view.printPackage(p);
    }

    // ========== 功能 2：查詢包裹 ==========
    private void queryPackage() {
        System.out.println("\n[查詢包裹]");
        String tracking = in.readString("請輸入追蹤碼：");

        Package p = find(tracking);
        if (p == null) {
            System.out.println("❌ 查無此追蹤碼");
            return;
        }

        view.printPackage(p);
        view.printEvents(p.getEventHistory());
    }

    // ========== 功能 3：更新狀態 ==========
    private void updateStatus() {
        System.out.println("\n[更新包裹狀態]");
        String tracking = in.readString("追蹤碼：");

        Package p = find(tracking);
        if (p == null) {
            System.out.println("❌ 查無此追蹤碼");
            return;
        }

        System.out.println("1) 收件  2) 運送中  3) 派送中  4) 已送達");
        int s = in.readInt("更新成：", 1, 4);

        String status = switch (s) {
            case 1 -> "收件";
            case 2 -> "運送中";
            case 3 -> "派送中";
            case 4 -> "已送達";
            default -> "未知";
        };

        String location = in.readString("地點：");
        String desc = in.readString("描述：");

        p.addEvent(location, status, desc);

        System.out.println("✅ 狀態更新成功！");
        view.printPackage(p);
    }

    // ========== 小工具：依追蹤碼找包裹 ==========
    private Package find(String trackingNumber) {
        for (Package p : packages) {
            if (p.getTrackingNumber().equals(trackingNumber)) {
                return p;
            }
        }
        return null;
    }
}

