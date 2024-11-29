package com.ecode.utils;

/**
 * 邀请码工具类
 * 参考 <a href="https://huzb.me/2018/03/23/%E7%AE%80%E5%8D%95%E7%9A%84%E5%AF%86%E7%A0%81%E5%AD%A6%E7%94%9F%E6%88%90%E5%94%AF%E4%B8%80%E9%82%80%E8%AF%B7%E7%A0%81/">实现</a>
 *
 * @author 竹林听雨
 * @date 2024/11/23
 */
public class InvitationCodeUtil {
    private static final int PRIME1 = 7;
    private static final int PRIME2 = 4;
    private static final int ARY = 36;
    private static final int CODE_LENGTH = 7;
    private static final int SALT = 47;
    private static final String HEX_36_Array = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    /**
     * 邀请码生成
     *
     * @param id id
     * @return 字符串
     */
    public static String inviCodeGenerator(int id) {
        id = id * PRIME1;
        id = id + SALT;
        int[] b = new int[CODE_LENGTH];
        b[0] = id;
        for (int i = 0; i < 5; ++i) {
            b[i + 1] = b[i] / ARY;
            b[i] = (b[i] + b[0] * i) % ARY;
        }
        b[5] = (b[0] + b[1] + b[2]) * PRIME1 % ARY;
        b[6] = (b[3] + b[4] + b[5]) * PRIME1 % ARY;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; ++i) {
            sb.append(HEX_36_Array.charAt(b[(i * PRIME2) % CODE_LENGTH]));
        }
        return sb.toString();
    }


    /**
     * 邀请码解码
     *
     * @param inviCode 邀请码
     * @return int
     */
    public static int inviDecoding(String inviCode) {
        if (inviCode.length() != CODE_LENGTH) {
            return -1;
        }
        int res = 0;
        int a[] = new int[CODE_LENGTH];
        int b[] = new int[CODE_LENGTH];
        char[] c = new char[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; ++i) {
            a[(i * PRIME2) % CODE_LENGTH] = i;
        }
        for (int i = 0; i < CODE_LENGTH; ++i) {
            c[i] = inviCode.charAt(a[i]);
        }
        for (int i = 0; i < CODE_LENGTH; ++i) {
            a[i] = HEX_36_Array.indexOf(c[i]);
        }
        b[5] = (a[0] + a[1] + a[2]) * PRIME1 % ARY;
        b[6] = (a[3] + a[4] + a[5]) * PRIME1 % ARY;
        if (a[5] != b[5] || a[6] != b[6]) {
            return -1;
        }
        for (int i = 4; i >= 0; --i) {
            b[i] = (a[i] - a[0] * i + ARY * i) % ARY;
        }
        for (int i = 4; i > 0; --i) {
            res += b[i];
            res *= ARY;
        }
        res = ((res + b[0]) - SALT) / PRIME1;
        return res;
    }
}
