package vip.ericchen.study.network.serial.serializar.impl;

import vip.ericchen.study.network.serial.serializar.ISerializer;

import java.io.*;

/**
 * <p>
 * JDK自带的序列化
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/10/04 10:56
 */
public class JavaSerializer implements ISerializer {
    public <T> byte[] serialize(T object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    public <T> T deSerialize(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
            return (T) inputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
