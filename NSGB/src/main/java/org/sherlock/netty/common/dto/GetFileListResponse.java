package org.sherlock.netty.common.dto;

import lombok.Getter;

import java.io.File;
import java.util.List;
@Getter
public class GetFileListResponse extends BasicResponse {
    private final List<File> itemsList;
    private final String actualDirectory;


    public GetFileListResponse(String response, List<File> itemsList, String actualDirectory) {
        super(response);
        this.itemsList = itemsList;
        this.actualDirectory = actualDirectory;
    }
}
