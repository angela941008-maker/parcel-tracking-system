package view;
import java.util.Scanner;

import controller.BillingService;
import controller.TrackingService;
import models.LoginMgr;
import models.Account;
import models.DataStore;
import models.LoginToken;
import models.Package;
import models.TrackingGenerator;
import models.Customer;
public class Menu {
    private final Scanner sc = new Scanner(System.in);
    private final InputHelper input = new InputHelper(sc);

    private final TrackingService trackingService = new controller.TrackingService();
    private final BillingService billingService = new BillingService();
    private final LoginMgr loginMgr = new LoginMgr();
    
    public void start() {
    	System.out.println("\n=== 包裹追蹤與計費系統 ===");
    	LoginToken logintoken=null;
 
        while (true) {
        	if (logintoken==null)
        	{
        		//登入帳號密碼取的票(Token), token內有登入者帳號及登入者的角色
        		logintoken=showLoginPageAndChkRole();
	        	if (logintoken==null){
	        		System.out.println("帳號不存在或密碼錯誤"); 
	        	}
        	}
  
        	//_role:0是客戶, 只能查自己的單號 , 1, 2, 3,4 是系統帳號的角色,依角色權限列出可用的作業
        	if (logintoken!=null)
        	{
        		printMainMenu(logintoken.getRole());
            	
                int choice = input.readInt("請選擇：");

                switch (choice) 
                {
                	case 1:
                		createCustomer(logintoken);
                		break;
                    case 2 : 
                    	createPackage(logintoken);       // 建立包裹（新增到 DataStore.packages）
                    	break;
                    case 3 : 
                    	queryPackage(logintoken);        // 查詢包裹 + 顯示歷史
                    	break;
                    case 4 : 
                    	updateStatus(logintoken);        // 更新狀態（呼叫 trackingService.updateStatus）
                    	break;
                    case 5 : 
                    	calculateFee(logintoken);        // 計算費用（billingService.calculatePrice）
                    	break;
                    case 9 : 
                    	logintoken=null;        // 登出, 進入login狀態
                    	System.out.println("\n=== 包裹追蹤與計費系統 ===");
                    	break;
                    case 0 : 
                    	System.out.println("系統結束"); 
                    	return; 
                    default : 
                    	System.out.println("無效選項");
                }
        	}
        	
        }
        
    }

   
    private LoginToken showLoginPageAndChkRole() {
    	String _account = input.readString("請輸入帳號:");
    	
    	String pass = input.readString("請輸入密碼(客戶請輸入手機號碼):");
            
            //帳號的角色: 1-系統管理員 2-客服人員 3-倉儲人員 4-駕駛員
        	LoginToken act=loginMgr.doLogin(_account, pass);
        	return act;
    }
    private void printMainMenu(int role) {
    	//帳號的角色: 1-系統管理員 2-客服人員 3-倉儲人員 4-駕駛員

    	System.out.println("\n=== 請輸入作業代碼  ===");
    	
    	int[] opers=DataStore.roleOpers.get(role);
    	for(int item:opers)
    	{
    		int operId=item;
    		String operDesc=DataStore.OperDesc.get(operId);
    		System.out.println(""+operId+"."+operDesc);
    	
    	}
    	
    	/*
    	switch (role) 
    	{
	    	case 0 : 
	            System.out.println("3. 查詢包裹/歷史紀錄");
	            System.out.println("9. 登出");

	        	break;
	        case 1 : 
	        	System.out.println("1. 建立客戶資料");
	            System.out.println("2. 建立包裹");
	            System.out.println("3. 查詢包裹/歷史紀錄");
	            System.out.println("4. 更新包裹狀態");
	            System.out.println("5. 計算運費");
	            System.out.println("9. 登出");
	            System.out.println("0. 離開");
	        	break;
	        case 2 : 
	        	System.out.println("1. 建立客戶資料");
	        	System.out.println("2. 建立包裹");
	            System.out.println("3. 查詢包裹/歷史紀錄");
	            System.out.println("5. 計算運費");
	            System.out.println("9. 登出");
	        	break;
	        case 3 : 
	        	System.out.println("3. 查詢包裹/歷史紀錄");
	            System.out.println("4. 更新包裹狀態");
	            System.out.println("9. 登出");
	        	break;
	        case 4 : 
	        	System.out.println("3. 查詢包裹/歷史紀錄");
	            System.out.println("4. 更新包裹狀態");
	            System.out.println("9. 登出");
	        	break;
	        default : 
	        	System.out.println("無效選項");
    	}
    	*/
       
    }

    // ✅ C 組員：建立包裹（這是 UI 串接 DataStore 的地方）
    private void createPackage(LoginToken _token) {
        System.out.println("\n[建立包裹]");
        //檢查登入者是否有此作業的權限
        int theOperID=2;
        boolean isHavePrivilege=loginMgr.chkHavePrivilege(theOperID,DataStore.roleOpers.get(_token.getRole()));
        if (!isHavePrivilege){
        	System.out.println("權限不足");
        	return;
        }
        
        //String trackingNumber = input.readString("追蹤單號(自行輸入/規則由你們決定)：");
        
        String trackingNumber = TrackingGenerator.generate();
        
        String senderId = input.readString("客戶ID：");
        if (!DataStore.chkCustomerExist(senderId))
        {
        	System.out.println("客戶ID 不存在, 請先建立客戶資料");
        	return;
        }
        String receiverName = input.readString("收件人姓名：");
        String receiverAddress = input.readString("收件人地址：");
        double weight = input.readDouble("重量(kg)：");
        String serviceType = input.readString("服務類型(Standard(S)/Express(E)/Overnight(D))：");
        if (serviceType.equalsIgnoreCase("S"))
        {
        	serviceType="Standard";
        }
        else if (serviceType.equalsIgnoreCase("E"))
        {
        	serviceType="Express";
        }
        else if (serviceType.equalsIgnoreCase("D"))
        {
        	serviceType="Overnight";
        }
        
        String dimensions = input.readString("尺寸(例: 10x10x10)：");
        double declaredValue = input.readDouble("申報價值：");
        String contentDescription = input.readString("內容描述：");

        Package pkg = new Package(trackingNumber, senderId, receiverName, receiverAddress,
                weight, serviceType, dimensions, declaredValue, contentDescription);

        // TrackingService 是查詢用的，新增則直接丟 DataStore.packages（A已做假DB）
        if (DataStore.packages == null) {
            System.out.println("錯誤：DataStore.packages 尚未初始化");
            return;
        }

        double price = billingService.calculatePrice(pkg);
        pkg.setFee(price);
        DataStore.packages.add(pkg);
        
        System.out.println("✅ 包裹建立成功！ 單號 :"+trackingNumber);
        System.out.println("費用 :"+price);
       
        
        
        ReportView.printPackage(pkg);
        DataStore.saveToFile();
    }

    
    private void createCustomer(LoginToken _token) {
        System.out.println("\n[建立客戶資料]");
        //檢查登入者是否有此作業的權限
        int theOperID=1;
        boolean isHavePrivilege=loginMgr.chkHavePrivilege(theOperID,DataStore.roleOpers.get(_token.getRole()));
        if (!isHavePrivilege){
        	System.out.println("權限不足");
        	return;
        }
        
        String customerId = input.readString("客戶ID(統編或身分證ID)：");
        String customerName = input.readString("客戶姓名：");
        String customerPhone = input.readString("客戶手機：");
        String customerEmail = input.readString("email：");
        String customerAddress = input.readString("客戶地址：");
        String customerType = input.readString("合約客戶(A),非合約客戶(B),預付客戶(C):");
        

        Customer customer = new Customer(customerId, customerName, customerPhone, customerEmail,
        		customerAddress, customerType);

        
        if (DataStore.customers == null) {
            System.out.println("錯誤：DataStore.customers 尚未初始化");
            return;
        }

        DataStore.customers.add(customer);
        System.out.println("客戶資料已建立");
        
       
        
        
        ReportView.printCustomer(customer);
        DataStore.saveToFile();
    }

    private void queryPackage(LoginToken loginToken) {
    	
        System.out.println("\n[查詢包裹]");
        String trackingNumber = input.readString("輸入追蹤單號：");

        Package pkg = trackingService.searchPackage(trackingNumber);
        
        if (pkg == null) {
            System.out.println("查無此包裹");
            return;
        }

        if (loginToken!=null && loginToken.getRole()>0)
        {
        	ReportView.printPackage(pkg);
            ReportView.printHistory(pkg);
        }
        else if (loginToken!=null && loginToken.getRole()==0)
        {
        	if (pkg.getSenderId().equalsIgnoreCase(loginToken.getLoginId()))
        	{
        		ReportView.printPackage(pkg);
                ReportView.printHistory(pkg);
        	}
        	else
        	{
        		System.out.println("您無法查詢此包裹");
                return;
        	}
        }
       
    }

    private void updateStatus(LoginToken _token) {
        System.out.println("\n[更新狀態]");
      //檢查登入者是否有此作業的權限
        int theOperID=4;
        boolean isHavePrivilege=loginMgr.chkHavePrivilege(theOperID,DataStore.roleOpers.get(_token.getRole()));
        if (!isHavePrivilege){
        	System.out.println("權限不足");
        	return;
        }
        
        
        
        String trackingNumber = input.readString("輸入追蹤單號：");
        String newStatus = input.readString("新狀態(例: In Transit/Out for Delivery/Delivered 或 中文)：");
        String location = input.readString("地點(例: 台北轉運站)：");

        boolean ok = trackingService.updateStatus(trackingNumber, newStatus, location);
        if (ok) {
            System.out.println("✅ 更新成功！");
            Package pkg = trackingService.searchPackage(trackingNumber);
            if (pkg != null) {
                ReportView.printPackage(pkg);
                ReportView.printHistory(pkg);
            }
            DataStore.saveToFile();
        } else {
            System.out.println("❌ 更新失敗");
        }
    }

    private void calculateFee(LoginToken _token) {
        System.out.println("\n[計算運費]");
        //檢查登入者是否有此作業的權限
        int theOperID=5;
        boolean isHavePrivilege=loginMgr.chkHavePrivilege(theOperID,DataStore.roleOpers.get(_token.getRole()));
        if (!isHavePrivilege){
        	System.out.println("權限不足");
        	return;
        }
        
        
        String trackingNumber = input.readString("輸入追蹤單號：");

        Package pkg = trackingService.searchPackage(trackingNumber);
        if (pkg == null) {
            System.out.println("查無此包裹");
            return;
        }

        double price = billingService.calculatePrice(pkg);
        System.out.printf("📦 運費金額：%.2f%n", price);
    }
}
