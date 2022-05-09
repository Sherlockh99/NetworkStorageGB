package org.sherlock.netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.sherlock.netty.common.Enums;
import org.sherlock.netty.common.dto.*;
import org.sherlock.netty.server.autorization.DBAuthService;
import org.sherlock.netty.server.autorization.SQLHandler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BasicHandler extends ChannelInboundHandlerAdapter {

    //список всех подключившихся клиентов
    private static final List<Channel> channels = new ArrayList<>();
    private String clientName;
    private static int newClientIndex = 1;

    //private static final BasicResponse LOGIN_BAD_RESPONSE = new BasicResponse("login bad");
    //private static final BasicResponse LOGIN_OK_RESPONSE = new BasicResponse("login ok");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clientName = "Клиент №" + newClientIndex;
        newClientIndex++;
        System.out.println("Подключился " + clientName + " с адреса " + ctx.channel().remoteAddress() + ", контекст =" + ctx);
        //формируем список подключившихся клиентов (точнее занятых ими каналов)
        channels.add(ctx.channel());
    }

    //Метод для рассылки сообщений по всем активным каналам
    public void broadCastMessage(String clientName, String message) {
        String out = String.format("[%s]:%s\n", clientName, message);
        BasicResponse bR = new BasicResponse(out);
        for (Channel c : channels) {
            c.writeAndFlush(bR);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BasicRequest request = (BasicRequest) msg;
        //System.out.println(request.getType());

        //Consumer<ChannelHandlerContext> channelHandlerContextConsumer = REQUEST_HANDLERS.get(request.getClass());
        //channelHandlerContextConsumer.accept(ctx);

      if (request instanceof GetFileListRequest) {
            Path serverPath = Paths.get("c:\\Programming\\Java\\GB\\NetworkStorageCourse\\");
            List<File> pathList = Files.list(serverPath)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            BasicResponse basicResponse = new GetFileListResponse("OK", pathList);
            ctx.writeAndFlush(basicResponse);
        }else if (request instanceof User) {
            if(NettyServer.getAuthService().checkedLogin(((User) request).getLogin(), ((User) request).getPassword())){
                ((User) request).setAuthorization(true);
                ctx.writeAndFlush(Enums.LOGIN_OK_RESPONSE);
            }else{
                ctx.writeAndFlush(Enums.LOGIN_BAD_RESPONSE);
            };
        }

        //else if (request instanceof UploadFileRequest) {


        //}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(clientName + " отключился");
        //cause.printStackTrace();
        //удаляем из списка активных каналов канал отключившегося клиента
        channels.remove(ctx.channel());
        ctx.close();
    }
}
