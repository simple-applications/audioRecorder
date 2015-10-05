package com.simpleApplications.audioRecorder.verticles;

import com.simpleApplications.audioRecorder.messages.MessageChannels;
import com.simpleApplications.audioRecorder.messages.SystemInformation;
import com.simpleApplications.audioRecorder.messages.codecs.SystemInformationCodec;
import com.sun.management.OperatingSystemMXBean;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

import java.lang.management.ManagementFactory;

/**
 * @author Nico Moehring
 */
public class SystemInformationVertical extends AbstractVerticle {

    private OperatingSystemMXBean systemMXBean;

    private EventBus eventBus;

    @Override
    public void start() throws Exception {
        this.systemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        this.eventBus = this.vertx.eventBus();
        this.eventBus.registerDefaultCodec(SystemInformation.class, new SystemInformationCodec());

        this.vertx.setPeriodic(500, this::collectSystemInformation);
    }

    /**
     *
     * @param id
     */
    private void collectSystemInformation(Long id) {
        int cpuUsage = (int) Math.floor(this.systemMXBean.getSystemCpuLoad() * 100);
        int memoryUsage = (int) Math.floor(this.systemMXBean.getFreePhysicalMemorySize() / 1024 / 1024);

        this.eventBus.publish(
                MessageChannels.SYSTEM_INFORMATION.toString(),
                new SystemInformation(cpuUsage, memoryUsage)
        );
    }
}
