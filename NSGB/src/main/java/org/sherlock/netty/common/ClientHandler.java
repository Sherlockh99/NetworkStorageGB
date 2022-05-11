package org.sherlock.netty.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.sherlock.netty.client.ClientService;
import org.sherlock.netty.common.dto.BasicResponse;
import org.sherlock.netty.common.dto.GetFileListRequest;
import org.sherlock.netty.common.dto.GetFileListResponse;

import java.io.File;
import java.util.List;


// Используется при обработке pipeline на стороне клиента (NettyClient)
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final ClientService clientService = new ClientService();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg.equals(Enums.LOGIN_OK_RESPONSE)) {
            clientService.loginSuccessful();
            ctx.writeAndFlush(new GetFileListRequest());
            //ctx.writeAndFlush(Enums.GET_FILE_LIST_RESPONSE);
            return;
        } else if (msg.equals(Enums.LOGIN_BAD_RESPONSE)) {
            clientService.loginBad();
            return;
        } else if (msg.equals(Enums.REGISTRATION_BAD_RESPONSE)) {
            clientService.loginBusy();
            return;
        } else {
            BasicResponse response = (BasicResponse) msg;
            if (response instanceof GetFileListResponse) {
                List<File> serverItemsList = ((GetFileListResponse) response).getItemsList();
                clientService.putServerFileList(serverItemsList);
                return;
            }
        }
    }
}
