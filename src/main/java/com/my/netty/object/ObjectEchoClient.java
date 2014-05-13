/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.my.netty.object;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;



/**
 * Modification of {@link EchoClient} which utilizes Java object serialization.
 */
public class ObjectEchoClient {

    private final String host;
    private final int port;
    private final int firstMessageSize;

    public ObjectEchoClient(String host, int port, int firstMessageSize) {
        this.host = host;
        this.port = port;
        this.firstMessageSize = firstMessageSize;
    }

    public void run() throws Exception {
        Bootstrap b = new Bootstrap();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            b.group(workerGroup)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.SO_KEEPALIVE, true)
             .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ObjectEncoder()).
                    addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null))).
                    addLast(new ObjectEchoClientHandler(firstMessageSize));
                }
             });

            // Start the connection attempt.
            System.out.println("client prepare connect");
            ChannelFuture f1 = b.connect(host, port).sync();
            ChannelFuture f2 = b.connect(host, port).sync();
             
            f1.channel().closeFuture().sync();
            f2.channel().closeFuture().sync();
        } finally {
        	System.out.println("client do finally");
        	workerGroup.shutdownGracefully();
        	System.out.println("client closing");
        }
    }

    public static void main(String[] args) throws Exception {
        // Print usage if no argument is specified.
      
        // Parse options.
        final String host = "localhost";
        final int port = 8080;
        final int firstMessageSize;

        if (args.length == 3) {
            firstMessageSize = Integer.parseInt("256");
        } else {
            firstMessageSize = 256;
        }

        new ObjectEchoClient(host, port, firstMessageSize).run();
    }
}
