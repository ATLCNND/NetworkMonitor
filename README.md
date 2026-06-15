# Network Monitor - Minecraft 系统与网络监控模组
## 功能介绍
全方位监控 Minecraft 客户端状态，支持 Mod Menu 图形配置界面
### 📊 系统监控
- **FPS 帧率** - 实时显示，带颜色指示（绿/黄/红）
- **分辨率** - 当前窗口缩放分辨率
- **内存占用** - Java 堆内存使用
- **CPU 使用率** - 进程 CPU 负载
- **GPU 信息** - 渲染器信息
### 🌐 网络监控
- 📤 **发送速率** - 每秒上传字节数 + 数据包数
- 📥 **接收速率** - 每秒下载字节数 + 数据包数
- 📊 **平均速率** - 最近5秒滑动窗口平均
- ⚡ **峰值速率** - 本次连接最高速率
- 📈 **总计流量** - 总上传/下载流量
### 🎛️ 配置界面
- **4个显示位置** - 左上/右上/左下/右下
- **独立开关** - 每个功能可单独开启/关闭
- **自定义颜色** - 背景色、文字色
- **细节控制** - 网络监控各子项开关
## 显示效果
```
[系统监控]
FPS: 120
分辨率: 1920x1080
内存: 2048/4096 MB
CPU: 15.2%
GPU: Intel UHD 630
[网络监控]
↑ 发送: 12.5 KB/s (15 pkt/s)
↓ 接收: 45.2 KB/s (32 pkt/s)
平均↑: 10.2 KB/s  平均↓: 38.1 KB/s
峰值↑: 89.5 KB/s  峰值↓: 156.2 KB/s
总计: ↑1.25 MB / ↓5.68 MB
```
## 配置界面
通过 Mod Menu 进入模组设置：
- 显示位置选择
- 各功能开关
- 颜色自定义
- 网络细节控制
## 适用版本
- Minecraft: 1.20.1
- Fabric Loader: 0.15.11+
- Fabric API: 0.107.0+
- Mod Menu: 10.0.0+
- Cloth Config: 13.0.0+
- Java: 17+
## 依赖模组（必须安装）
1. [Fabric API](https://modrinth.com/mod/fabric-api)
2. [Mod Menu](https://modrinth.com/mod/modmenu)
3. [Cloth Config API](https://modrinth.com/mod/cloth-config)
## 安装方法
1. 安装 Fabric Loader
2. 安装上述3个依赖模组
3. 将对应版本的 jar 文件放入 mods 文件夹
4. 启动游戏，在 Mod Menu 中配置
## 特性
- ✅ Mod Menu 图形配置界面
- ✅ 4个角落位置可选
- ✅ 纯客户端模组，无需服务器安装
- ✅ 半透明背景面板
- ✅ 自动隐藏在F3调试界面
- ✅ 暂停游戏时自动隐藏
- ✅ 每个功能独立开关控制
## 编译方法
```bash
./gradlew build
```
编译后的 jar 文件在 `build/libs/` 目录下
## 版本历史
### v1.2.0 (最新版)
- 新增GPU信息、CPU使用率监控
- 优化HUD显示样式，增加颜色指示
- 修复网络统计峰值计算错误
- 适配 Minecraft 1.20.1
### v1.1.0 (历史归档)
- 基础FPS、内存、网络速率监控
- 支持4个显示位置切换
- Mod Menu图形配置界面
## 下载地址
最新版: https://github.com/ATLCNND/NetworkMonitor/releases
## 作者
ATLCNND 

##该项目大部分代码由豆包AI提供
## License
MIT
