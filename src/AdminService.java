import java.util.List;

public class AdminService {

    // 列出目前系統中所有包裹的簡要資訊
    public void listAllPackages() {
        List<Package> pkgs = DataStore.packages;
        System.out.println("=== 系統包裹清單 ===");
        if (pkgs == null || pkgs.isEmpty()) {
            System.out.println("目前無包裹。");
            return;
        }
        
        for (Package p : pkgs) {
            // 直接呼叫 Package 的 toString
            System.out.println(p.toString());
        }
        System.out.println("==================");
    }
}