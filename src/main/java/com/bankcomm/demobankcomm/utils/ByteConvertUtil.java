package com.bankcomm.demobankcomm.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Phantom Sean
 * Date: 2023/9/27
 * Time: 10:36
 */
public class ByteConvertUtil {
    /**
     * 将从数据库获得的字节数组转化为二进制字符串
     * @param bytes 要转化的字节数组
     * @return 转化为二进制的字符串
     */
    public static String byteArray2BinaryString(byte[] bytes) {
        StringBuilder binaryString = new StringBuilder();

        for (byte b : bytes) {
            for (int i = 7; i >= 0; i--) {
                int bit = (b >> i) & 1;
                binaryString.append(bit);
            }
        }

        return binaryString.toString();
    }

    /**
     * 将二进制的字符串不满32位右边补0，然后转化为byte数组
     * @param string 要转化的字符串
     * @return byte数组
     */
    public static byte[] string2ByteArray(String string) {
        String binaryString = string.length() == 32 ? string : add0(string);
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            String byteString = binaryString.substring(i * 8, (i + 1) * 8);
            int byteValue = Integer.parseInt(byteString, 2);
            bytes[i] = (byte) byteValue;
        }
        return bytes;
    }

    // 不满32位右边补0
    public static String add0(String string) {
        int n = 32 - string.length();
        StringBuilder sb = new StringBuilder(string);
        for (int i = 0; i < n; i++) {
            sb.append(0);
        }
        return sb.toString();
    }

    // 字节数组转long
    public static Long byteArrayToLong(byte[] byteArray) {
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result <<= 8; // 左移8位，为下一个字节腾出位置
            result |= (byteArray[i] & 0xFF); // 使用位或操作将字节添加到结果中
        }

        return result;
    }

    // long转字节数组
    public static byte[] longToByteArray(long number) {
        byte[] byteArray = new byte[4];
        for (int i = 3; i >= 0; i--) {
            byteArray[i] = (byte) (number & 0xFF);
            number >>= 8; // 右移8位，继续处理下一个字节
        }
        return byteArray;
    }

    // 字节数组转为二进制后1的个数
    public static int countOnesInByteArray(byte[] byteArray) {
        int count = 0;
        for (byte b : byteArray) {
            int bits = b & 0xFF; // 将字节转化为无符号整数
            while (bits > 0) {
                count += bits & 1;
                bits >>= 1;
            }
        }
        return count;
    }

    // 字节数组转位二进制后最大连续1的次数
    public static int maxContinue(byte[] byteArray) {
        int maxCount = 0; // 存储最大连续1的个数
        int currentCount = 0; // 存储当前连续1的个数

        for (int i = 3; i >= 0; i--) {
            int bits = byteArray[i] & 0xFF; // 将字节转化为无符号整数

            while (bits > 0) {
                currentCount = (bits & 1) == 1 ? currentCount + 1 : 0; // 更新当前连续1的计数
                maxCount = Math.max(maxCount, currentCount); // 更新最大连续1的计数
                bits >>= 1;
            }
        }
        return maxCount;
    }
}

