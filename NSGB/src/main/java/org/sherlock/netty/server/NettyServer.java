package org.sherlock.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.sherlock.netty.server.autorization.AuthService;
import org.sherlock.netty.server.autorization.DBAuthService;
import org.sherlock.netty.server.autorization.SQLHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NettyServer {

    private static final Logger logger = Logger.getLogger(NettyServer.class.getName());

    private static final int MB_20 = 20 * 1_000_000;
    private static final int PORT = 45004;

    private static AuthService authService;

    //public static void main(String[] args) throws InterruptedException {
    public NettyServer(){
        if(!SQLHandler.connect()){
            logger.log(Level.SEVERE, "Не удалось подключиться к БД");
            throw new RuntimeException("Не удалось подключиться к БД");
        }
        authService = new DBAuthService();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline inbound = socketChannel.pipeline();
                            inbound.addLast(
                                    new ObjectDecoder(MB_20, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new BasicHandler()
                            );
                        }
                    });

            ChannelFuture channelFuture = null; // (7)
            try {
                channelFuture = serverBootstrap.bind(PORT).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static AuthService getAuthService() {
        return authService;
    }

}
