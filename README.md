# Telephone Sim（电话模拟器）

一个使用 Java 21 与状态模式实现的传统电话通话流程模拟小玩具。项目以可扩展的骨架代码呈现完整状态机，涵盖闲置、摘机、拨号音、超时蜂鸣、拨号、试接通、忙音、振铃、接通、通话、播放信息与断线等典型场景。

## 特性

- 拨号与号码缓冲，支持号码有效性校验的扩展
- 拨号音、忙音、蜂鸣、振铃与语音通道的统一音频控制
- 试接通、占线、接通、断线等网络交换机行为模拟
- 状态模式驱动的清晰状态切换与事件处理

## 技术栈

- Java 21
- Maven 构建

## 目录结构

```
telephone-sim/
├─ docs/
│  └─ telephone-uml.md          # UML 类图与设计说明
├─ src/main/java/com/touhikari/telephone/
│  ├─ core/                     # 状态机核心与入口
│  │  ├─ state/                 # 具体电话状态（骨架类）
│  │  ├─ CallController.java    # 状态协调者
│  │  ├─ PhoneState.java        # 状态接口
│  │  └─ Telephone.java         # 程序入口
│  ├─ service/                  # 音频、拨号器、计时器、消息存储
│  ├─ network/                  # 交换机、连接、终端与状态枚举
│  └─ model/                    # 播放信息模型
├─ pom.xml                      # Maven 配置（Java 21）
└─ README.md
```

## 快速开始

1. 安装 JDK 21 与 Maven（3.9+ 建议）
2. 运行（任选 A、B 其一）：
   - 方式 A：可执行 Jar
     - 在项目根目录执行构建：
       `mvn clean package`
     - 执行 Jar：
       `java -jar target/telephone-1.0-SNAPSHOT.jar`
   - 方式 B：仅编译并运行
     - `mvn -q compile`
     - `mvn -q exec:java`

## 架构与职责

- `Telephone`：系统入口，接收用户动作并委派至控制器
- `CallController`：持有当前 `PhoneState` 与计时器、号码缓冲，统一处理事件与状态切换
- `PhoneState` 及各具体状态类：实现不同场景的进入/退出/事件行为
- `AudioService`：拨号音、忙音、蜂鸣、振铃与语音通道播放控制
- `Dialer`：数字输入缓冲与号码校验
- `Timer`：拨号音超时与其它时序控制
- `MessageStore`/`Message`：无效号码时的存储信息播放
- `NetworkSwitch`/`Connection`/`Endpoint`/`ConnectionStatus`：网络交换、连接与远端终端

完整 UML 与更详细说明参见：[telephone-uml.md](docs/telephone-uml.md)

## 状态一览

- Idle（闲置）
- OffHook（摘机）
- DialTone（拨号音）
- TimeoutBeep（超时蜂鸣）
- Dialing（拨号）
- Connecting（试接通）
- BusyTone（忙音）
- Ringing（振铃）
- Talking（通话）
- PlayInfo（播放信息）
- Disconnected（断线）

## 许可证

项目使用 [MIT 许可证](LICENSE)。
