# parcel-tracking-system

## 📦 系統類別圖 (Class Diagram)

```mermaid
classDiagram
    class Customer {
        -String customerId
        -String name
        -String phone
        -boolean isContract
        +Customer(id, name, phone, isContract)
        +toString() String
        +getCustomerId() String
        +getName() String
        +getPhone() String
        +isContract() boolean
    }

    class TrackingEvent {
        -String timestamp
        -String location
        -String status
        -String description
        +TrackingEvent(time, loc, status, desc)
        +toString() String
        +getTimestamp() String
        +getLocation() String
        +getStatus() String
        +getDescription() String
    }

    class Package {
        -String trackingNumber
        -String senderId
        -String receiverName
        -String receiverAddress
        -double weight
        -String serviceType
        -String currentStatus
        -List~TrackingEvent~ eventHistory
        +Package(trackNum, sender, receiver, addr, w, type)
        +addEvent(loc, status, desc) void
        +toString() String
        +getTrackingNumber() String
        +getSenderId() String
        +getReceiverName() String
        +getReceiverAddress() String
        +getWeight() double
        +getServiceType() String
        +getEventHistory() List~TrackingEvent~
    }

    class DataStore {
        +static List~Package~ packages
        +static List~Customer~ customers
        +static initData() void
        +static saveToFile() void
        +static loadFromFile() void
    }

    %% Relationships
    DataStore o--> "0..*" Customer : manages
    DataStore o--> "0..*" Package : manages
    Package *--> "0..*" TrackingEvent : contains history
    Package ..> Customer : senderId refers to >
---

## 🏗 系統架構圖（System Architecture Diagram）

```mermaid
flowchart TB
  U[使用者 User<br/>Console / UI 操作介面]

  P[Presentation Layer<br/>(UI / Menu)<br/><br/>顯示選單<br/>接收使用者輸入<br/>顯示結果]

  B[Business Logic Layer<br/>(Service / Controller)<br/><br/>PackageService<br/>CustomerService<br/>計費 / 查詢 / 追蹤邏輯]

  D[Data Layer<br/>(DataStore)<br/><br/>List&lt;Package&gt;<br/>List&lt;Customer&gt;<br/>loadFromFile()<br/>saveToFile()]

  F[File System<br/>packages.txt / data.dat]

  U -->|呼叫| P
  P -->|呼叫| B
  B -->|存取| D
  D --> F

