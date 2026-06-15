package com.ATLCNND.networkmonitor.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class NetworkStats {
    private static final NetworkStats INSTANCE = new NetworkStats();
    
    // 帧时间统计 (1% Low 计算)
    private final List<Float> frameTimes = new ArrayList<>();
    private static final int MAX_FRAME_TIMES = 200; // 保存最近200帧
    
    // 每秒统计
    private long bytesSentThisSecond = 0;
    private long bytesReceivedThisSecond = 0;
    private int packetsSentThisSecond = 0;
    private int packetsReceivedThisSecond = 0;
    
    // 历史数据（用于计算每秒平均）
    private final Queue<Long> sentBytesHistory = new LinkedList<>();
    private final Queue<Long> receivedBytesHistory = new LinkedList<>();
    private final Queue<Integer> sentPacketsHistory = new LinkedList<>();
    private final Queue<Integer> receivedPacketsHistory = new LinkedList<>();
    
    // 总计
    private long totalBytesSent = 0;
    private long totalBytesReceived = 0;
    private long totalPacketsSent = 0;
    private long totalPacketsReceived = 0;
    
    // 峰值
    private long peakSendRate = 0;
    private long peakReceiveRate = 0;
    
    private long lastSecondTime = System.currentTimeMillis();
    
    private NetworkStats() {}
    
    public static NetworkStats getInstance() {
        return INSTANCE;
    }
    
    public void onPacketSent(int size) {
        bytesSentThisSecond += size;
        packetsSentThisSecond++;
        totalBytesSent += size;
        totalPacketsSent++;
    }
    
    public void onPacketReceived(int size) {
        bytesReceivedThisSecond += size;
        packetsReceivedThisSecond++;
        totalBytesReceived += size;
        totalPacketsReceived++;
    }
    
    public void tick() {
        long now = System.currentTimeMillis();
        if (now - lastSecondTime >= 1000) {
            // 保存历史数据
            sentBytesHistory.offer(bytesSentThisSecond);
            receivedBytesHistory.offer(bytesReceivedThisSecond);
            sentPacketsHistory.offer(packetsSentThisSecond);
            receivedPacketsHistory.offer(packetsReceivedThisSecond);
            
            // 保持最近5秒的数据
            while (sentBytesHistory.size() > 5) sentBytesHistory.poll();
            while (receivedBytesHistory.size() > 5) receivedBytesHistory.poll();
            while (sentPacketsHistory.size() > 5) sentPacketsHistory.poll();
            while (receivedPacketsHistory.size() > 5) receivedPacketsHistory.poll();
            
            // 更新峰值
            peakSendRate = Math.max(peakSendRate, bytesSentThisSecond);
            peakReceiveRate = Math.max(peakReceiveRate, bytesReceivedThisSecond);
            
            // 重置计数器
            bytesSentThisSecond = 0;
            bytesReceivedThisSecond = 0;
            packetsSentThisSecond = 0;
            packetsReceivedThisSecond = 0;
            lastSecondTime = now;
        }
    }
    
    public long getCurrentSendRate() {
        return bytesSentThisSecond;
    }
    
    public long getCurrentReceiveRate() {
        return bytesReceivedThisSecond;
    }
    
    public int getCurrentPacketsSent() {
        return packetsSentThisSecond;
    }
    
    public int getCurrentPacketsReceived() {
        return packetsReceivedThisSecond;
    }
    
    public long getAvgSendRate() {
        if (sentBytesHistory.isEmpty()) return 0;
        return sentBytesHistory.stream().mapToLong(Long::longValue).sum() / sentBytesHistory.size();
    }
    
    public long getAvgReceiveRate() {
        if (receivedBytesHistory.isEmpty()) return 0;
        return receivedBytesHistory.stream().mapToLong(Long::longValue).sum() / receivedBytesHistory.size();
    }
    
    public long getTotalBytesSent() {
        return totalBytesSent;
    }
    
    public long getTotalBytesReceived() {
        return totalBytesReceived;
    }
    
    public long getTotalPacketsSent() {
        return totalPacketsSent;
    }
    
    public long getTotalPacketsReceived() {
        return totalPacketsReceived;
    }
    
    public long getPeakSendRate() {
        return peakSendRate;
    }
    
    public long getPeakReceiveRate() {
        return peakReceiveRate;
    }
    
    public void reset() {
        totalBytesSent = 0;
        totalBytesReceived = 0;
        totalPacketsSent = 0;
        totalPacketsReceived = 0;
        peakSendRate = 0;
        peakReceiveRate = 0;
        sentBytesHistory.clear();
        receivedBytesHistory.clear();
        sentPacketsHistory.clear();
        receivedPacketsHistory.clear();
    }
    
    public static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
    }
    
    // ========== 帧时间统计 ==========
    
    public void addFrameTime(float frameTimeMs) {
        frameTimes.add(frameTimeMs);
        while (frameTimes.size() > MAX_FRAME_TIMES) {
            frameTimes.remove(0);
        }
    }
    
    public float getAverageFrameTime() {
        if (frameTimes.isEmpty()) return 0;
        float sum = 0;
        for (float t : frameTimes) sum += t;
        return sum / frameTimes.size();
    }
    
    public float get1PercentLow() {
        if (frameTimes.size() < 10) return 0;
        List<Float> sorted = new ArrayList<>(frameTimes);
        Collections.sort(sorted, Collections.reverseOrder());
        int count = Math.max(1, (int) (sorted.size() * 0.01));
        float sum = 0;
        for (int i = 0; i < count; i++) {
            sum += sorted.get(i);
        }
        return sum / count;
    }
    
    public static String formatRate(long bytesPerSecond) {
        return formatBytes(bytesPerSecond) + "/s";
    }
}
