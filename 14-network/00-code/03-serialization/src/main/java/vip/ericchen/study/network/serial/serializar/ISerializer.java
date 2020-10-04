package vip.ericchen.study.network.serial.serializar;

/**
 * <p>
 * description
 * </p>
 */
public interface ISerializer {

    /**
     * 序列化 , 本质上是讲一个对象转化成byte数组
     */
    <T> byte[] serialize(T object);


    /**
     * 反序列化 , 本质上是将一个byte数组转换成java对象
     */
    <T> T deSerialize(byte[] bytes);
}
