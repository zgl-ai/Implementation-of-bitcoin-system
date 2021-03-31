package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Util {

    private static final String HEX_CHAR = "0123456789abcdef";

    public static String bytes2HexString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte x: data) {
            int y = x & 0xff;
            stringBuilder.append(HEX_CHAR.charAt(y >>> 4));
            stringBuilder.append(HEX_CHAR.charAt(0xf & y));
        }
        return stringBuilder.toString();
    }

    /**
     * 使用SHA256算法进行哈希值计算
     *
     * @param data 待进行哈希计算的字符串
     * @return SHA256哈希值
     */
    public static String sha256Digest(String data) {
        MessageDigest sha256Digest = null;
        try {
            sha256Digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        sha256Digest.update(data.getBytes(StandardCharsets.UTF_8));
        return bytes2HexString(sha256Digest.digest());
    }

}
