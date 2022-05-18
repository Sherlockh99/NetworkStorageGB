package org.sherlock.netty.common.dto;


public class PartFile implements BasicRequest {

    byte[] bytes;

    public PartFile(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public String getType() {
        return "part";
    }
}
