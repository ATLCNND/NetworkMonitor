package com.ATLCNND.networkmonitor.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "networkmonitor")
public class ModConfig implements ConfigData {
    
    @ConfigEntry.Category("显示设置")
    @ConfigEntry.Gui.Tooltip
    public HudPosition hudPosition = HudPosition.TOP_RIGHT;
    
    @ConfigEntry.Category("显示设置")
    @ConfigEntry.Gui.Tooltip
    public int backgroundColor = 0x90000000;
    
    @ConfigEntry.Category("显示设置")
    @ConfigEntry.Gui.Tooltip
    public int textColor = 0xFFFFFFFF;
    
    @ConfigEntry.Category("功能开关")
    @ConfigEntry.Gui.Tooltip
    public boolean enableNetworkMonitor = true;
    
    @ConfigEntry.Category("功能开关")
    @ConfigEntry.Gui.Tooltip
    public boolean enableFpsMonitor = true;
    
    @ConfigEntry.Category("功能开关")
    @ConfigEntry.Gui.Tooltip
    public boolean enableSystemMonitor = true;
    
    @ConfigEntry.Category("功能开关")
    @ConfigEntry.Gui.Tooltip
    public boolean enableResolution = true;
    
    @ConfigEntry.Category("功能开关")
    @ConfigEntry.Gui.Tooltip
    public boolean enablePingMonitor = true;
    
    @ConfigEntry.Category("功能开关")
    @ConfigEntry.Gui.Tooltip
    public boolean enableFrameTime = true;
    
    @ConfigEntry.Category("功能开关")
    @ConfigEntry.Gui.Tooltip
    public boolean enableServerTps = true;
    
    @ConfigEntry.Category("功能开关")
    @ConfigEntry.Gui.Tooltip
    public boolean enableGpuUsage = true;
    
    @ConfigEntry.Category("功能开关")
    @ConfigEntry.Gui.Tooltip
    public boolean enableEntityCount = true;
    
    @ConfigEntry.Category("网络监控细节")
    @ConfigEntry.Gui.Tooltip
    public boolean showSendRate = true;
    
    @ConfigEntry.Category("网络监控细节")
    @ConfigEntry.Gui.Tooltip
    public boolean showReceiveRate = true;
    
    @ConfigEntry.Category("网络监控细节")
    @ConfigEntry.Gui.Tooltip
    public boolean showAverage = true;
    
    @ConfigEntry.Category("网络监控细节")
    @ConfigEntry.Gui.Tooltip
    public boolean showPeak = true;
    
    @ConfigEntry.Category("网络监控细节")
    @ConfigEntry.Gui.Tooltip
    public boolean showTotal = true;
    
    public enum HudPosition {
        TOP_LEFT("左上角"),
        TOP_RIGHT("右上角"),
        BOTTOM_LEFT("左下角"),
        BOTTOM_RIGHT("右下角");
        
        private final String name;
        
        HudPosition(String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
}
