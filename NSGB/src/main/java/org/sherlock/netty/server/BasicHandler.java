package org.sherlock.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.sherlock.netty.common.Enums;
import org.sherlock.netty.common.dto.*;
import org.sherlock.netty.server.autorization.AuthService;
import org.sherlock.netty.server.autorization.DBAuthService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class BasicHandler extends ChannelInboundHandlerAdapter {

    private static User user;
    private String currentPath;

    private static AuthService authService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        authService = new DBAuthService();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        BasicRequest request = (BasicRequest) msg;

        if (request instanceof GetFileListRequest) {
            currentPath = currentPath + "\\"+((GetFileListRequest) request).getNewDirectory();

            Path serverPath = Paths.get(currentPath);
            List<File> pathList = Files.list(serverPath)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            BasicResponse basicResponse = new GetFileListResponse("OK", pathList, currentPath);
            ctx.writeAndFlush(basicResponse);

        }else if (request instanceof User) {
            user = (User) request;
            boolean isAuthorization = false;
            if(user.isRegistration()){
              if(authService.registration(user.getLogin().toLowerCase(), user.getPassword())){
                  isAuthorization = true;
                  ctx.writeAndFlush(Enums.LOGIN_OK_RESPONSE);
              }else{
                  ctx.writeAndFlush(Enums.REGISTRATION_BAD_RESPONSE);
              }
          }else {
              if (authService.isCurrentLogin(user.getLogin().toLowerCase(), user.getPassword())) {
                  isAuthorization = true;
                  ctx.writeAndFlush(Enums.LOGIN_OK_RESPONSE);
              } else {
                  ctx.writeAndFlush(Enums.LOGIN_BAD_RESPONSE);
              }
          }
            if(isAuthorization){
                user.setAuthorization(true);
                currentPath = NettyServer.ROOT_DIRECTORY + user.getLogin();
            }
        } else if (request instanceof Enums) {
            if(request == Enums.LEVEL_UP){

                Path serverPath;
                if(currentPath.equals(NettyServer.ROOT_DIRECTORY + user.getLogin())){
                    serverPath = Paths.get(currentPath);
                }else{
                    serverPath = Paths.get(currentPath).getParent();
                    currentPath = serverPath.toString();
                }

                List<File> pathList = Files.list(serverPath)
                        .map(Path::toFile)
                        .collect(Collectors.toList());
                BasicResponse basicResponse = new GetFileListResponse("OK", pathList, currentPath);
                ctx.writeAndFlush(basicResponse);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
