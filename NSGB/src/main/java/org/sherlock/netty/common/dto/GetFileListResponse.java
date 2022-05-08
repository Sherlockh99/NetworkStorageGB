package org.sherlock.netty.common.dto;

import java.io.File;
import java.util.List;

public class GetFileListResponse extends BasicResponse {
    private final List<File> itemsList;

    public List<File> getItemsList() {
        return itemsList;
    }

    public GetFileListResponse(String response, List<File> itemsList) {
        super(response);
        this.itemsList = itemsList;
    }
}
