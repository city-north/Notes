package cn.eccto.study.java.basic.serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Example of using {@link java.io.Serializable}
 * </p>
 * 测试序列化与反序列化
 *
 * @author Jonathan 2020/01/19 15:18
 */
public class SerializableExample {


    public static void main(String[] args) {
        SerializableDto serializableDto = writeObject();
        SerializableDto serializableDto1 = readObject();
        System.out.println(serializableDto == serializableDto1);
    }

    private static SerializableDto writeObject() {
        try (//创建一个ObjectOutputStream输出流
             ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("object.txt"))) {
            //将对象序列化到文件s
            SerializableDto person = new SerializableDto("Jonathan", 23);
            oos.writeObject(person);
            System.out.println(person);
            return person;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static SerializableDto readObject() {
        try (//创建一个ObjectInputStream输入流
             ObjectInputStream ois = new ObjectInputStream(new FileInputStream("object.txt"))) {
            SerializableDto brady = (SerializableDto) ois.readObject();
            System.out.println(brady);
            return brady;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
