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
2. 运行（任选 A、B、C 其一）：
   - 方式 A：可执行 Jar
     - 在项目根目录执行构建：
       `mvn clean package`
     - 执行 Jar：
       `java -jar target/telephone-1.0-SNAPSHOT.jar`
   - 方式 B：仅编译并运行
     - `mvn -q compile`
     - `java -cp target/classes com.touhikari.telephone.core.Telephone`
   - 方式 C：使用 Maven Exec 插件
     - `mvn -q exec:java`

## 命令与玩法

- 启动后输入命令交互，输入 `help` 查看命令：
  - `pickup` 摘机
  - `hangup` 挂机
  - `digit <0-9>` 输入单个数字
  - `call <number>` 一次性输入整串号码（会先自动摘机）
  - `status` 查看当前状态、音频标志、号码缓冲与连接状态
  - `numbers` 查看内置号码与行为说明
  - `addinfo <number> <durationMs> <text>` 添加自定义信息播放（文本支持空格）
  - `delinfo <number>` 删除自定义信息
  - `listinfo` 列出所有信息号码
  - `busy <number>` 将号码标记为忙（占线）
  - `free <number>` 取消号码的忙标记
  - `listbusy` 列出当前自定义忙号
  - `answer` 模拟远端应答（振铃状态下转通话）
  - `remotehangup` 模拟远端挂断（转断线）
  - `timeout` 手动触发一次超时事件（便于测试）
  - `clear` 清空当前拨号缓冲
  - `exit` 退出程序

### 内置号码

- `111`：立即接通（跳过振铃）
- `222`：先振铃，约 3 秒后自动接通
- `555`：忙音（占线）
- `404`：播放信息“The number you dialed does not exist. Please check and try again.”
- `500`：播放信息“The number you dialed is currently busy. Please call later.”
- `999`：播放信息“The service is not available.”
- `888xxx`：占线（以 888 开头的前缀）
- `*00`：占线（任意以 00 结尾的号码）

### 示例流程

- 立即接通演示：
  - `pickup`
  - `call 111`
  - `status`
- 振铃后自动接通演示：
  - `pickup`
  - `call 222`
  - 等待约 3 秒或输入 `status` 观察状态变化
- 忙音演示：
  - `pickup`
  - `call 555`
  - 忙音约 3 秒后自动断线
- 信息播放演示：
  - `pickup`
  - `call 404`（或 `500` / `999`）
  - 播放到设定时长后自动结束并断线

### 动态配置示例

- 添加自定义信息：
  - `addinfo 333 3000 Hello, this is a demo message.`
  - `call 333`
- 管理忙号：
  - `busy 12345`
  - `listbusy`
  - `free 12345`
- 拨号缓冲清空：
  - `clear`

### 超时规则（默认）

- 拨号音超时：5 秒未输入数字 → 进入蜂鸣（TimeoutBeep）
- 蜂鸣持续：2 秒 → 回到闲置（Idle）
- 拨号输入间隔：3 秒无进一步数字 → 检查号码有效性并尝试接通/播放信息/蜂鸣
- 振铃：3 秒 → 进入通话（Talking）
- 忙音：3 秒 → 进入断线（Disconnected）
- 信息播放：按 `Message.durationMs` 控制结束 → 进入断线

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
