package com.ecode.utils;

public class ByteUtil {

    /**
     * 将 int 转换为 byte 数组（大端序）
     *
     * @param value 需要转换的 int 值
     * @return 转换后的 byte 数组
     */
    public static byte[] intToBytes(int value) {
        byte[] result = new byte[4]; // int 占用 4 字节
        for (int i = 3; i >= 0; i--) {
            result[i] = (byte) (value & 0xFF); // 取最低 8 位
            value >>= 8; // 右移 8 位
        }
        return result;
    }

    /**
     * 将 byte 数组转换为 int（大端序）
     *
     * @param bytes 需要转换的 byte 数组
     * @return 转换后的 int 值
     */
    public static int bytesToInt(byte[] bytes) {
        if (bytes == null || bytes.length != 4) {
            throw new IllegalArgumentException("Input byte array must be exactly 4 bytes long.");
        }
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result <<= 8; // 左移 8 位
            result |= (bytes[i] & 0xFF); // 添加当前字节
        }
        return result;
    }

    public static void main(String[] args) {
        // 测试 intToBytes 和 bytesToInt
        int originalValue = 123456789;

        // 转换为字节数组
        byte[] byteArray = intToBytes(originalValue);
        System.out.println("Byte Array: " + java.util.Arrays.toString(byteArray));

        // 从字节数组还原为 int
        int restoredValue = bytesToInt(byteArray);
        System.out.println("Restored Int: " + restoredValue);

        // 验证是否一致
        System.out.println("Is Restored Correctly? " + (originalValue == restoredValue));
    }
}