package com.bankcomm.demobankcomm.utils;

import java.util.*;

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
    public static byte[] string2Byte(String string) {
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
}

