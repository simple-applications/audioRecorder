package com.simpleApplications.audioRecorder.messages;

/**
 * @author Nico Moehring
 */
public class SystemInformation {
    public int cpuUsage;

    public int memoryUsage;

    public SystemInformation(int cpuUsage, int memoryUsage) {
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
    }
}
