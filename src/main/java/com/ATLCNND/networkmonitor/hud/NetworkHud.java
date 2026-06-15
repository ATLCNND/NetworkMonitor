package com.ATLCNND.networkmonitor.hud;

import com.ATLCNND.networkmonitor.config.ModConfig;
import com.ATLCNND.networkmonitor.stats.NetworkStats;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.world.World;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;

public class NetworkHud implements HudRenderCallback {
    
    private static final int PADDING = 8;
    private static final int LINE_HEIGHT = 12;
    private static final int MARGIN = 5;
    
    private final OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    private long lastFrameTime = System.nanoTime();
    
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        
        // 不显示调试界面、F3界面或暂停时
        if (client.options.debugEnabled || client.options.hudHidden || client.isPaused()) {
            return;
        }
        
        NetworkStats stats = NetworkStats.getInstance();
        stats.tick();
        
        // 记录帧时间
        long now = System.nanoTime();
        float frameTimeMs = (now - lastFrameTime) / 1_000_000.0f;
        stats.addFrameTime(frameTimeMs);
        lastFrameTime = now;
        
        List<String> lines = new ArrayList<>();
        
        // 标题
        lines.add("§l系统监控");
        
        // FPS监控
        if (config.enableFpsMonitor) {
            int fps = client.getCurrentFps();
            String fpsColor = fps >= 60 ? "§a" : (fps >= 30 ? "§e" : "§c");
            lines.add(String.format("FPS: %s%d", fpsColor, fps));
        }
        
        // ========== 新增: 帧时间 + 1% Low ==========
        if (config.enableFrameTime) {
            float avgFrameTime = stats.getAverageFrameTime();
            float onePercentLow = stats.get1PercentLow();
            String ftColor = avgFrameTime < 16.7f ? "§a" : (avgFrameTime < 33.3f ? "§e" : "§c");
            if (onePercentLow > 0) {
                lines.add(String.format("帧时: %s%.1fms §7(1%%低: %.1fms)", ftColor, avgFrameTime, onePercentLow));
            } else {
                lines.add(String.format("帧时: %s%.1fms", ftColor, avgFrameTime));
            }
        }
        
        // 分辨率
        if (config.enableResolution) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            lines.add(String.format("分辨率: %dx%d", width, height));
        }
        
        // 系统监控
        if (config.enableSystemMonitor) {
            // 内存
            Runtime runtime = Runtime.getRuntime();
            long usedMem = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
            long maxMem = runtime.maxMemory() / 1024 / 1024;
            lines.add(String.format("内存: %d/%d MB", usedMem, maxMem));
            
            // CPU
            double cpuLoad = getProcessCpuLoad();
            String cpuColor = cpuLoad < 50 ? "§a" : (cpuLoad < 80 ? "§e" : "§c");
            lines.add(String.format("CPU: %s%.1f%%", cpuColor, cpuLoad));
        }
        
        // ========== 新增: GPU 信息 ==========
        if (config.enableGpuUsage) {
            lines.add("GPU: " + getGpuInfo());
        }
        
        // ========== 新增: Ping 延迟 ==========
        if (config.enablePingMonitor && client.player != null && client.getNetworkHandler() != null) {
            PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
            if (entry != null) {
                int ping = entry.getLatency();
                String pingColor = ping < 50 ? "§a" : (ping < 100 ? "§e" : (ping < 200 ? "§6" : "§c"));
                lines.add(String.format("延迟: %s%dms", pingColor, ping));
            }
        }
        
        // ========== 新增: 服务器 TPS/MSPT ==========
        if (config.enableServerTps) {
            double tps = 20.0;
            double mspt = 0;
            // 客户端估算TPS (实际服务器需要插件支持，这里显示占位)
            String tpsColor = tps >= 19 ? "§a" : (tps >= 15 ? "§e" : "§c");
            lines.add(String.format("服务器: %s%.1f TPS", tpsColor, tps));
        }
        
        // ========== 新增: 实体数量 ==========
        if (config.enableEntityCount && client.world != null) {
            int entityCount = client.world.getEntities().size();
            lines.add(String.format("实体: %d", entityCount));
        }
        
        // 网络监控
        if (config.enableNetworkMonitor) {
            lines.add("");
            lines.add("§l网络监控");
            
            if (config.showSendRate) {
                lines.add(String.format("↑ 发送: %s (%d pkt/s)", 
                    NetworkStats.formatRate(stats.getCurrentSendRate()), 
                    stats.getCurrentPacketsSent()));
            }
            
            if (config.showReceiveRate) {
                lines.add(String.format("↓ 接收: %s (%d pkt/s)", 
                    NetworkStats.formatRate(stats.getCurrentReceiveRate()), 
                    stats.getCurrentPacketsReceived()));
            }
            
            if (config.showAverage) {
                lines.add(String.format("平均↑: %s  平均↓: %s", 
                    NetworkStats.formatRate(stats.getAvgSendRate()), 
                    NetworkStats.formatRate(stats.getAvgReceiveRate())));
            }
            
            if (config.showPeak) {
                lines.add(String.format("峰值↑: %s  峰值↓: %s", 
                    NetworkStats.formatRate(stats.getPeakSendRate()), 
                    NetworkStats.formatRate(stats.getPeakReceiveRate())));
            }
            
            if (config.showTotal) {
                lines.add(String.format("总计: ↑%s / ↓%s", 
                    NetworkStats.formatBytes(stats.getTotalBytesSent()), 
                    NetworkStats.formatBytes(stats.getTotalBytesReceived())));
            }
        }
        
        // 移除空行
        lines.removeIf(String::isEmpty);
        
        if (lines.isEmpty()) return;
        
        // 计算面板尺寸
        int maxWidth = 0;
        for (String line : lines) {
            int width = client.textRenderer.getWidth(line.replaceAll("§.", ""));
            maxWidth = Math.max(maxWidth, width);
        }
        
        int panelWidth = maxWidth + PADDING * 2;
        int panelHeight = lines.size() * LINE_HEIGHT + PADDING * 2;
        
        // 根据配置计算位置
        int x, y;
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        
        switch (config.hudPosition) {
            case TOP_LEFT:
                x = MARGIN;
                y = MARGIN;
                break;
            case TOP_RIGHT:
                x = screenWidth - panelWidth - MARGIN;
                y = MARGIN;
                break;
            case BOTTOM_LEFT:
                x = MARGIN;
                y = screenHeight - panelHeight - MARGIN;
                break;
            case BOTTOM_RIGHT:
                x = screenWidth - panelWidth - MARGIN;
                y = screenHeight - panelHeight - MARGIN;
                break;
            default:
                x = screenWidth - panelWidth - MARGIN;
                y = MARGIN;
        }
        
        // 绘制背景
        drawContext.fill(x, y, x + panelWidth, y + panelHeight, config.backgroundColor);
        
        // 绘制每行文本
        for (int i = 0; i < lines.size(); i++) {
            int textY = y + PADDING + i * LINE_HEIGHT;
            String line = lines.get(i);
            drawContext.drawTextWithShadow(client.textRenderer, line, x + PADDING, textY, config.textColor);
        }
    }
    
    private double getProcessCpuLoad() {
        try {
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunOsBean = 
                    (com.sun.management.OperatingSystemMXBean) osBean;
                return sunOsBean.getProcessCpuLoad() * 100;
            }
        } catch (Exception e) {
            // 忽略
        }
        return 0.0;
    }
    
    private String getGpuInfo() {
        try {
            String renderer = org.lwjgl.opengl.GL11.glGetString(org.lwjgl.opengl.GL11.GL_RENDERER);
            if (renderer != null) {
                return renderer.contains("NVIDIA") ? "NVIDIA" : 
                       renderer.contains("AMD") ? "AMD" : 
                       renderer.contains("Intel") ? "Intel" : renderer;
            }
        } catch (Exception e) {
            // 忽略
        }
        return "OpenGL";
    }
}
