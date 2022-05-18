package org.sherlock.netty.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class BasicResponse implements Serializable {

    private String response;

    public BasicResponse(String response) {
        this.response = response;
    }
}
