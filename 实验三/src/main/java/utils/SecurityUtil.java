package utils;

import data.Transaction;
import data.UTXO;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SecurityUtil {

    /**
     * 十六进制映射字符串
     */
    private static final String HEX_CHAR = "0123456789abcdef";

    /**
     * 比特数据转为相应的十六进制字符串
     * @param data
     * @return
     */
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

    /**
     * 字节版本哈希函数
     * @param data
     * @return
     */
    public static byte[] sha256Digest(byte[] data) {
        MessageDigest sha256Digest = null;
        try {
            sha256Digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        sha256Digest.update(data);
        return sha256Digest.digest();
    }

    /**
     * 由于java标准库没有提供RIPEMD160哈希摘要算法，故暂不做任何操作
     * @param data
     * @return
     */
    public static byte[] ripemd160Digest(byte[] data) {
        return data;
    }

    /**
     * secp256k1密钥生成
     * @return
     */
    public static KeyPair secp256k1Generate() {
        KeyPair keyPair = null;
        try {
            // ECC(Elliptic Curve Cryptography) 椭圆曲线密钥生成器
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            // 指定secp256k1曲线
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
            // 随机数保证每次生成不同的密钥
            keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
            // 生成公私密钥对
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            // 异常直接退出
            e.printStackTrace();
            System.exit(-1);
        }
        return keyPair;
    }

    /**
     * 私钥签名
     * @param data 签名数据
     * @param privateKey 签名私钥
     * @return 签名后的比特数据
     */
    public static byte[] signature(byte[] data, PrivateKey privateKey) {
        byte[] sign = null;
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(data);
            sign = signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return sign;
    }

    /**
     * 公钥验签
     * @param data  签名数据
     * @param publicKey 验签公钥
     * @param sign 签名数据
     * @return 签名是否正确
     */
    public static boolean verify(byte[] data, byte[] sign, PublicKey publicKey) {
        boolean result = false;
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initVerify(publicKey);
            signature.update(data);
            result = signature.verify(sign);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return result;
    }

    /**
     * utxo数组（包含输入和输出）转化为byte数据供交易签名
     * @param inUtxos
     * @param outUtxos
     * @return
     */
    public static byte[] utxos2Bytes(UTXO[] inUtxos, UTXO[] outUtxos) {
        return (Arrays.toString(inUtxos) + Arrays.toString(outUtxos)).getBytes(StandardCharsets.UTF_8);
    }


}
