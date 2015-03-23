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
package com.my.netty.json;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler implementation for the object echo client. It initiates the ping-pong
 * traffic between the object echo client and server by sending the first
 * message to the server.
 */
public class ObjectEchoClientHandler extends
		SimpleChannelInboundHandler<String> {

	private final List<Integer> firstMessage;

	/**
	 * Creates a client-side handler.
	 */
	public ObjectEchoClientHandler(int firstMessageSize) {
		if (firstMessageSize <= 0) {
			throw new IllegalArgumentException("firstMessageSize: "
					+ firstMessageSize);
		}
		firstMessage = new ArrayList<Integer>(firstMessageSize);
		for (int i = 0; i < firstMessageSize; i++) {
			firstMessage.add(Integer.valueOf(i));
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		// TODO Auto-generated method stub
		  System.out.println("get message from server:" +msg);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
/*		User user = new User();
		user.setName("cz");
		user.setAge("12");
		user.setAddress("changzhou 化龙苍");
		String value = JSONValue.toJSONString(user);*/
		super.channelActive(ctx);
		ctx.writeAndFlush("abc");
		System.out.println("client send ok");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		ctx.close();
	}

}
