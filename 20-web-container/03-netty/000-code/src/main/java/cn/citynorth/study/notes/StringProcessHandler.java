package cn.citynorth.study.notes;

import cn.hutool.core.util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan Chen 2021/07/12 15:31
 */
public class StringProcessHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        // 读取消息内容进行解析
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        final String s = HexUtil.encodeHexStr(bytes);
        System.out.println(s);
    }
}
/*
*
 * ef 03 004d
* 7B 22 64 65 76 69 63 65 43 6F
* 64 65 22 3A 22 52 46 49 44 5F
* 44 45 56 49 43 45 5F 30 31 22
* 2C 22 74 69 6D 65 73 74 61 6D
* 70 22 3A 31 36 32 32 36 31 36
* 32 37 30 39 37 36 2C 22 6D 65
* 73 73 61 67 65 49 64 22 3A 22
* 31 32 33 35 36 22 7D
 * -> 77
*
*
*
* */