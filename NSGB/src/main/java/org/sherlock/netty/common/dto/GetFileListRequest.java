package org.sherlock.netty.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetFileListRequest implements BasicRequest {

    private String newDirectory = "";
    private boolean isLevelUp = false;

    public GetFileListRequest(String newDirectory){
        this.newDirectory = newDirectory;
    }

    @Override
    public String getType() {
        return "getFileList";
    }
}
