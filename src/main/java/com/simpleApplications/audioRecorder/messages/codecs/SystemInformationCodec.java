package com.simpleApplications.audioRecorder.messages.codecs;

import com.simpleApplications.audioRecorder.messages.SystemInformation;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * @author Nico Moehring
 */
public class SystemInformationCodec implements MessageCodec<SystemInformation, SystemInformation> {
    @Override
    public void encodeToWire(Buffer buffer, SystemInformation systemInformation) {

    }

    @Override
    public SystemInformation decodeFromWire(int i, Buffer buffer) {
        return null;
    }

    @Override
    public SystemInformation transform(SystemInformation systemInformation) {
        return systemInformation;
    }

    @Override
    public String name() {
        return "SystemInformationCodec";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
