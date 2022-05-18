package org.sherlock.netty.common;

import org.sherlock.netty.common.dto.BasicRequest;

import java.io.Serializable;

public enum Enums implements Serializable, BasicRequest {
    LOGIN_BAD_RESPONSE, REGISTRATION_BAD_RESPONSE, LOGIN_OK_RESPONSE, LEVEL_UP;

    @Override
    public String getType() {
        return "ENUMS";
    }
}
