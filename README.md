# parcel-tracking-system

## 1. 系統架構設計 (System Architecture) 類別圖
本專案採用 **三層式架構 (3-Tier Architecture)** 設計，確保高內聚與低耦合。下圖為系統完整類別圖，包含視圖層、邏輯層與資料層的互動關係。

![系統完整架構圖](./系統類別圖_final.png)

---

## 2. 物流包裹追蹤系統 - 系統架構圖 (System Architecture Diagram)
展示程式運作時，資料是如何從使用者的鍵盤 (UI)，流向邏輯判斷 (Service)，最後存入檔案 (File) 的。

```mermaid
graph TD
    %% 定義樣式
    classDef ui fill:#e1f5fe,stroke:#01579b,stroke-width:2px;
    classDef logic fill:#fff9c4,stroke:#fbc02d,stroke-width:2px;
    classDef data fill:#e8f5e9,stroke:#2e7d32,stroke-width:2px;
    classDef file fill:#eeeeee,stroke:#616161,stroke-width:2px,stroke-dasharray: 5 5;

    subgraph "表現層 (Presentation Layer)"
        direction TB
        Main(Main.java<br>程式入口)
        Menu(Menu.java<br>主選單控制)
        Input(InputHelper.java<br>輸入驗證)
        View(ReportView.java<br>結果顯示)
    end

    subgraph "商業邏輯層 (Business Logic Layer)"
        direction TB
        TrackService(TrackingService.java<br>追蹤與狀態管理)
        BillService(BillingService.java<br>運費計算策略)
    end

    subgraph "資料存取層 (Data Access Layer)"
        DataStore(DataStore.java<br>資料庫模擬/Repository)
    end

    subgraph "實體儲存 (Physical Storage)"
        CSV[(".csv Files<br>(Customers, Packages, Events)")]
    end

    %% 關係連線
    Main -->|啟動| Menu
    Menu -->|讀取輸入| Input
    Menu -->|顯示結果| View
    
    Menu -->|查詢/更新| TrackService
    Menu -->|請求計算| BillService
    
    TrackService -->|存取資料| DataStore
    BillService -->|讀取包裹資訊| DataStore
    
    DataStore <-->|讀取/寫入| CSV
    
    %% 套用樣式
    class Main,Menu,Input,View ui;
    class TrackService,BillService logic;
    class DataStore data;
    class CSV file;