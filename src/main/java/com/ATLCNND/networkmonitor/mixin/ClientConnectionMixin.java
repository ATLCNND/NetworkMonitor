package com.ATLCNND.networkmonitor.mixin;

import com.ATLCNND.networkmonitor.stats.NetworkStats;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    
    // 1.20.1 版本: sendInternal(Packet, PacketCallbacks, NetworkState, NetworkState)
    @Inject(method = "sendInternal", at = @At("HEAD"))
    private void onSendPacket(Packet<?> packet, PacketCallbacks callbacks, 
                             net.minecraft.network.NetworkState state1, 
                             net.minecraft.network.NetworkState state2, 
                             CallbackInfo ci) {
        try {
            NetworkStats.getInstance().onPacketSent(estimatePacketSize(packet));
        } catch (Exception e) {
            // 忽略错误
        }
    }
    
    // 1.20.1 版本: channelRead0
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Object;)V", 
            at = @At("HEAD"), remap = false)
    private void onReceivePacket(io.netty.channel.ChannelHandlerContext ctx, Object msg, CallbackInfo ci) {
        if (msg instanceof Packet<?>) {
            try {
                NetworkStats.getInstance().onPacketReceived(estimatePacketSize((Packet<?>) msg));
            } catch (Exception e) {
                // 忽略错误
            }
        }
    }
    
    private int estimatePacketSize(Packet<?> packet) {
        String className = packet.getClass().getSimpleName();
        if (className.contains("Chunk")) return 8000;
        if (className.contains("Entity")) return 200;
        if (className.contains("PlayerMove")) return 50;
        if (className.contains("Chat")) return 300;
        return 100;
    }
}
