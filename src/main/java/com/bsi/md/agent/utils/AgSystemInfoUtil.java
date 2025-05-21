package com.bsi.md.agent.utils;

import com.alibaba.fastjson.JSON;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AgSystemInfoUtil {

    private static final DecimalFormat df = new DecimalFormat("#.##");

    public static Map<String, Object> getSystemInfo() {
        Map<String, Object> result = new HashMap<>();
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        OperatingSystem os = systemInfo.getOperatingSystem();

        result.put("SYSTEM_OS", os.toString());
        result.put("SYSTEM_PROCESSORS", os.getProcessCount());

        // CPU
        result.putAll(getCpuInfo(hardware.getProcessor()));

        // Memory
        result.putAll(getMemoryInfo(hardware.getMemory()));

        // Disk
        result.put("disks", getDiskInfo(os.getFileSystem()));

        return result;
    }

    private static Map<String, Object> getCpuInfo(CentralProcessor processor) {
        Map<String, Object> cpuInfo = new HashMap<>();

        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();

        long user = ticks[CentralProcessor.TickType.USER.ordinal()] - prevTicks[CentralProcessor.TickType.USER.ordinal()];
        long nice = ticks[CentralProcessor.TickType.NICE.ordinal()] - prevTicks[CentralProcessor.TickType.NICE.ordinal()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.ordinal()] - prevTicks[CentralProcessor.TickType.SYSTEM.ordinal()];
        long idle = ticks[CentralProcessor.TickType.IDLE.ordinal()] - prevTicks[CentralProcessor.TickType.IDLE.ordinal()];
        long totalCpu = user + nice + sys + idle;
        double cpuUsage = totalCpu == 0 ? 0 : 100d * (totalCpu - idle) / totalCpu;

        cpuInfo.put("SYSTEM_CPU_USAGE_PERCENT", df.format(cpuUsage));
        //cpuInfo.put("logicalProcessors", processor.getLogicalProcessorCount());

        return cpuInfo;
    }

    private static Map<String, Object> getMemoryInfo(GlobalMemory memory) {
        Map<String, Object> memInfo = new HashMap<>();

        long available = memory.getAvailable();
        long total = memory.getTotal();
        long used = total - available;
        double memUsage = total == 0 ? 0 : 100d * (used) / total;
        memInfo.put("SYSTEM_MEM_TOTAL", df.format(total / (1024.0 * 1024 * 1024)));
        memInfo.put("SYSTEM_MEM_USAGE_PERCENT", df.format(memUsage));
        memInfo.put("SYSTEM_MEM_USED", df.format(used / (1024.0 * 1024 * 1024)));

        return memInfo;
    }

    private static List<Map<String, Object>> getDiskInfo(FileSystem fileSystem) {

        return fileSystem.getFileStores().stream().map(fs -> {
            Map<String, Object> disk = new HashMap<>();
            long total = fs.getTotalSpace();
            long used = total - fs.getUsableSpace();

            disk.put("SYSTEM_DISK_NAME", fs.getName());
            disk.put("SYSTEM_DISK_MOUNT", fs.getMount());
            disk.put("SYSTEM_DISK_SIZE", df.format(total / (1000.0 * 1000 * 1000)));
            disk.put("SYSTEM_DISK_USED", df.format(used / (1000.0 * 1000 * 1000)));
            disk.put("SYSTEM_DISK_FREE", df.format(fs.getUsableSpace() / (1000.0 * 1000 * 1000)));
            disk.put("SYSTEM_DISK_USAGE_PERCENT", df.format(100d * used / total));
            return disk;
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.print(JSON.toJSONString(getSystemInfo()));
    }
}