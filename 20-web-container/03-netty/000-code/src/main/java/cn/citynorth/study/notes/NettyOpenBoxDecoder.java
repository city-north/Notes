package cn.citynorth.study.notes;

import cn.citynorth.study.notes.utils.RandomUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan Chen 2021/07/12 15:27
 */
public class NettyOpenBoxDecoder {
    public static final int MAGICCODE = 9999;
    public static final int VERSION = 100;
    static String content = "{\"deviceCode\":\"my_device_code\",\"timestamp\":1622616270976}";


    @Test
    public void testLengthFieldBasedFrameDecoder() throws Exception {
        final LengthFieldBasedFrameDecoder spliter =
                new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4);
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(spliter);
                ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                ch.pipeline().addLast(new StringProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        for (int j = 0; j < 100; j++) {
            //1-3之间的随机数
            int random = RandomUtil.randInMod(3);
            ByteBuf buf = Unpooled.buffer();
            byte[] bytes = content.getBytes("UTF-8");
            buf.writeInt(bytes.length * random);
            for (int k = 0; k < random; k++) {
                buf.writeBytes(bytes);
            }
            channel.writeInbound(buf);
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * LengthFieldBasedFrameDecoder 使用实例 3
     */
    @Test
    public void testLengthFieldBasedFrameDecoder3() {
        try {
            String s =  "发送->" + content;
            byte[] bytes = s.getBytes("UTF-8");


            final FixedLengthFrameDecoder spliter =
                    new FixedLengthFrameDecoder(bytes.length);
            ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
                protected void initChannel(EmbeddedChannel ch) {
                    ch.pipeline().addLast(spliter);
                    ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                    ch.pipeline().addLast(new StringProcessHandler());
                }
            };
            EmbeddedChannel channel = new EmbeddedChannel(i);

            for (int j = 1; j <= 1; j++) {
                ByteBuf buf = Unpooled.buffer();
                buf.writeChar(VERSION);
                buf.writeInt(bytes.length);
                buf.writeInt(MAGICCODE);
                buf.writeBytes(bytes);
                channel.writeInbound(buf);
            }

            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static final short OXEF = 0xEF;
    public static final short method = 0x01;

    @Test
    public void testLengthFieldBasedFrameDecoder4() {
        try {

            final LengthFieldBasedFrameDecoder spliter =
                    new LengthFieldBasedFrameDecoder(65541, 2, 2, 0, 0);
            ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
                protected void initChannel(EmbeddedChannel ch) {
                    ch.pipeline().addLast(spliter);
                    ch.pipeline().addLast(new StringProcessHandler());
                }
            };
            EmbeddedChannel channel = new EmbeddedChannel(i);

                ByteBuf buf = Unpooled.buffer();
                byte[] bytes = content.getBytes("UTF-8");
                byte[] bytes2 = "HELLO, WORLD".getBytes("UTF-8");
            final int length = bytes2.length;
            buf.writeByte(0xEF);//固定头 ef
                buf.writeByte(0x01);      //01
                buf.writeByte(0x00);      //01
                buf.writeByte(0x39);//57

                buf.writeBytes(bytes);
                channel.writeInbound(buf);
//7b22646576696365436f6465223a226d795f6465766963655f636f6465222c2274696d657374616d70223a313632323631363237303937367d
//397b22646576696365436f6465223a226d795f6465766963655f636f6465222c2274696d657374616d70223a313632323631363237303937367d
//ef0100397b22646576696365436f6465223a226d795f6465766963655f636f6465222c2274696d657374616d70223a313632323631363237303937367d




            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLengthFieldBasedFrameDecoder13() {
        try {

            final LengthFieldBasedFrameDecoder spliter =
                    new LengthFieldBasedFrameDecoder(1024, 2, 4, 4, 10);
            ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
                protected void initChannel(EmbeddedChannel ch) {
                    ch.pipeline().addLast(spliter);
                    ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                    ch.pipeline().addLast(new StringProcessHandler());
                }
            };
            EmbeddedChannel channel = new EmbeddedChannel(i);

            for (int j = 1; j <= 100; j++) {
                ByteBuf buf = Unpooled.buffer();
                String s = j + "次发送->" + content;
                byte[] bytes = s.getBytes("UTF-8");
                buf.writeChar(OXEF);
                buf.writeInt(bytes.length);
                buf.writeInt(MAGICCODE);
                buf.writeBytes(bytes);
                channel.writeInbound(buf);
            }

            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



}