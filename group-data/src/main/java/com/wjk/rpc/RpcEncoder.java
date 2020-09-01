package com.wjk.rpc;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @program: GTM-WJK
 * @description: 编码器
 * @author: junkang.wei
 * @create: 2020-08-23 15:35
 **/
public class RpcEncoder extends MessageToByteEncoder {

    //目标对象类型进行编码
    private Class<?> target;

    public RpcEncoder(Class target) {
        this.target = target;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (target.isInstance (msg)) {
            byte[] data = JSON.toJSONBytes (msg);
            out.writeInt (data.length);
            out.writeBytes (data);
        }
    }

}
