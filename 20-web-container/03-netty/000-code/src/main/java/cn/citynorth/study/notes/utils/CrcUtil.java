package cn.citynorth.study.notes.utils;

/**
 * @author zhangjian02@efort.com.cn
 * @date 2021/5/17 9:15
 */
public class CrcUtil {
    /**
     * crc16_ccitt_modbus算法（四字节）
     *
     * @param buf    字节数组
     * @param offset 偏移量
     * @param length 长度
     * @return 校验码
     */
    public static int crc16CcittModbus(byte[] buf, int offset, int length) {
        int i;
        int j;
        int c;
        int crc = 0xFFFF;
        for (i = offset; i < length; i++) {
            c = buf[i] & 0x00FF;
            crc ^= c;
            for (j = 0; j < 8; j++) {
                if ((crc & 0x0001) != 0) {
                    crc >>= 1;
                    crc ^= 0xA001;
                } else {
                    crc >>= 1;
                }
            }
        }
        return crc;
    }

    /**
     * crc16_ccitt_modbus算法（四字节）
     *
     * @param buf 字节数组
     * @return 校验码
     */
    public static int crc16CcittModbus(byte[] buf) {
        return crc16CcittModbus(buf, 0, buf.length);
    }


    /**
     * crc16_ccitt_modbus算法（两字节）
     *
     * @param buf    字节数组
     * @param offset 偏移量
     * @param length 长度
     * @return 校验码
     */
    public static int crc16CcittModbusShort(byte[] buf, int offset, int length) {
        return (short) crc16CcittModbus(buf, offset, length);
    }

    /**
     * crc16_ccitt_modbus算法（两字节）
     *
     * @param buf 字节数组
     * @return 校验码
     */
    public static int crc16CcittModbusShort(byte[] buf) {
        return crc16CcittModbusShort(buf, 0, buf.length);
    }

    /**
     * 将int转换成byte数组，低位在前，高位在后
     * 改变高低位顺序只需调换数组序号
     *
     * @param value 整数值
     * @return 字节数组
     */
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[2];
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 校验crc
     *
     * @param buf 待校验字节数组
     * @param crc 目标字节数组
     * @return 校验结果
     */
    public static boolean checkCrc(byte[] buf, byte[] crc) {
        int calCrc = crc16CcittModbus(buf);
        byte[] bytes = intToBytes(calCrc);
        return bytes[0] == crc[1] && bytes[1] == crc[0];
    }
}
