package com.ATLCNND.networkmonitor;

import com.ATLCNND.networkmonitor.config.ModConfig;
import com.ATLCNND.networkmonitor.hud.NetworkHud;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkMonitor implements ClientModInitializer {
    
    public static final String MOD_ID = "networkmonitor";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    @Override
    public void onInitializeClient() {
        LOGGER.info("Network Monitor 模组加载中...");
        
        // 初始化配置系统
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        
        // 注册HUD渲染
        HudRenderCallback.EVENT.register(new NetworkHud());
        
        LOGGER.info("Network Monitor 模组加载完成！");
    }
}
