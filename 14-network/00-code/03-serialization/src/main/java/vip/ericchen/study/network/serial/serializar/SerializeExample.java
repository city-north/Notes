package vip.ericchen.study.network.serial.serializar;

import vip.ericchen.study.network.serial.User;
import vip.ericchen.study.network.serial.serializar.impl.JavaSerializer;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/10/04 11:05
 */
public class SerializeExample {

    public static void main(String[] args) {
        User user = new User();
        user.setAge(12);
        user.setName("jack");
        testJavaSerializer(user);//104
    }

    private static void testJavaSerializer(User user) {
        ISerializer serializer = new JavaSerializer();
        final byte[] serialize = serializer.serialize(user);
        System.out.println(serialize.length);//104
        for (byte b : serialize) {
            System.out.print(b +" ");
        }
    }
}
