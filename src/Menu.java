import java.util.Scanner;

public class Menu {
    private final Scanner sc = new Scanner(System.in);
    private final InputHelper input = new InputHelper(sc);

    private final TrackingService trackingService = new TrackingService();
    private final BillingService billingService = new BillingService();

    public void start() {
        while (true) {
            printMainMenu();
            int choice = input.readInt("請選擇：");

            switch (choice) {
                case 1 -> createPackage();       // 建立包裹（新增到 DataStore.packages）
                case 2 -> queryPackage();        // 查詢包裹 + 顯示歷史
                case 3 -> updateStatus();        // 更新狀態（呼叫 trackingService.updateStatus）
                case 4 -> calculateFee();        // 計算費用（billingService.calculatePrice）
                case 0 -> { System.out.println("系統結束"); return; }
                default -> System.out.println("無效選項");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n=== 包裹追蹤與計費系統 ===");
        System.out.println("1. 建立包裹");
        System.out.println("2. 查詢包裹/歷史紀錄");
        System.out.println("3. 更新包裹狀態");
        System.out.println("4. 計算運費");
        System.out.println("0. 離開");
    }

    // C 組員：建立包裹（這是 UI 串接 DataStore 的地方）
    private void createPackage() {
        System.out.println("\n[建立包裹]");

        String trackingNumber = input.readString("追蹤單號(自行輸入/規則由你們決定)：");
        String senderId = input.readString("寄件人ID：");
        String receiverName = input.readString("收件人姓名：");
        String receiverAddress = input.readString("收件人地址：");
        double weight = input.readDouble("重量(kg)：");
        String serviceType = input.readString("服務類型(Standard/Express/Overnight)：");

        String dimensions = input.readString("尺寸(例: 10x10x10)：");
        double declaredValue = input.readDouble("申報價值：");
        String contentDescription = input.readString("內容描述：");

        Package pkg = new Package(trackingNumber, senderId, receiverName, receiverAddress,
                weight, serviceType, dimensions, declaredValue, contentDescription);

        // 你們 TrackingService 是查詢用的，新增則直接丟 DataStore.packages（A已做假DB）
        if (DataStore.packages == null) {
            System.out.println("錯誤：DataStore.packages 尚未初始化");
            return;
        }

        DataStore.packages.add(pkg);
        System.out.println("包裹建立成功！");
        ReportView.printPackage(pkg);
    }

    private void queryPackage() {
        System.out.println("\n[查詢包裹]");
        String trackingNumber = input.readString("輸入追蹤單號：");

        Package pkg = trackingService.searchPackage(trackingNumber);
        if (pkg == null) {
            System.out.println("查無此包裹");
            return;
        }

        ReportView.printPackage(pkg);
        ReportView.printHistory(pkg);
    }

    private void updateStatus() {
        System.out.println("\n[更新狀態]");
        String trackingNumber = input.readString("輸入追蹤單號：");
        String newStatus = input.readString("新狀態(例: In Transit/Out for Delivery/Delivered 或 中文)：");
        String location = input.readString("地點(例: 台北轉運站)：");

        boolean ok = trackingService.updateStatus(trackingNumber, newStatus, location);
        if (ok) {
            System.out.println("更新成功！");
            Package pkg = trackingService.searchPackage(trackingNumber);
            if (pkg != null) {
                ReportView.printPackage(pkg);
                ReportView.printHistory(pkg);
            }
        } else {
            System.out.println("更新失敗");
        }
    }

    private void calculateFee() {
        System.out.println("\n[計算運費]");
        String trackingNumber = input.readString("輸入追蹤單號：");

        Package pkg = trackingService.searchPackage(trackingNumber);
        if (pkg == null) {
            System.out.println("查無此包裹");
            return;
        }

        double price = billingService.calculatePrice(pkg);
        System.out.printf("運費金額：%.2f%n", price);
    }
}
