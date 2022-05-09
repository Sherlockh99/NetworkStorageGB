package org.sherlock.netty.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.sherlock.netty.client.ClientService;
import org.sherlock.netty.common.dto.GetFileListRequest;


// Используется при обработке pipeline на стороне клиента (NettyClient)
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final ClientService clientService = new ClientService();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
    //Для получения данных от сервера воспользуемся блокирующей очередью.
    //Она приостановит выполнение программы, пока в нее не будут добавлены новые данные
    //private final BlockingQueue<Command> answer = new LinkedBlockingQueue<>();


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //BasicResponse response = (BasicResponse) msg;
        //System.out.println(response.getResponse());
        //String responseText = response.getResponse();

        if(msg.equals(Enums.LOGIN_OK_RESPONSE)){
            clientService.loginSuccessful();
            ctx.writeAndFlush(new GetFileListRequest());
            return;
        }else if(msg.equals(Enums.LOGIN_BAD_RESPONSE)) {

        }
    }
    /*
        if ("login ok".equals(responseText)) {
            Enums.LOGIN_OK_RESPONSE
            clientService.loginSuccessful();
            ctx.writeAndFlush(new GetFileListRequest());
            return;
        }

        if (response instanceof GetFileListResponse) {
            List<File> serverItemsList = ((GetFileListResponse) response).getItemsList();
            clientService.putServerFileList(serverItemsList);
            return;
        }
    */
}
