package data;

import utils.SecurityUtil;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.Stack;

public class UTXO {

    private final String walletAddress;
    private final int amount;
    private final byte[] publicKeyHash;

    /**
     * 构建一个UTXO
     * @param walletAddress 交易获得方的钱包地址
     * @param amount 比特币数额
     * @param publicKey 交易获得方的公钥（公钥是公开的）
     */
    public UTXO(String walletAddress, int amount, PublicKey publicKey) {
        this.walletAddress = walletAddress;
        this.amount = amount;
        // 对公钥进行哈希摘要: RIPEMD160(SHA256(PubK)，作为解锁脚本数据
        publicKeyHash = SecurityUtil.ripemd160Digest(
                SecurityUtil.sha256Digest(publicKey.getEncoded()));
    }

    /**
     * 模拟utxo的解锁脚本，只有使用对应的私钥签名和公钥，正确解锁才能使用该utxo作为交易输入
     * @param sign 账户私钥签名，这里我们这么约定:签名数据为公钥二进制数据
     * @param publicKey 公钥
     * @return
     */
    public boolean unlockScript(byte[] sign, PublicKey publicKey) {
        Stack<byte[]> stack = new Stack<>();
        // <sig> 签名入栈
        // 栈内: <Sig>
        stack.push(sign);
        // <PubK> 公钥入栈
        // 栈内: <Sig> <PubK>
        stack.push(publicKey.getEncoded());
        // DUP 复制一份栈顶数据, peek()为java栈容器获取栈顶元素的函数
        // 栈内: <Sig> <PubK> <PubK>
        stack.push(stack.peek());
        // HASH160 弹出栈顶元素，进行哈希摘要: RIPEMD160(SHA256(PubK)，并将其入栈
        // 栈内: <Sig> <PubK> <PubHash>
        byte[] data = stack.pop();  // 栈顶元素就是PubK
        stack.push(SecurityUtil.ripemd160Digest(SecurityUtil.sha256Digest(data)));
        // <PubHash> utxo先前保存的公钥哈希入栈
        // 栈内: <Sig> <PubK> <PubHash> <PubHash>
        stack.push(publicKeyHash);
        // EQUALVERIFY 比较栈顶的两个公钥哈希是否相同，不相同则解锁失败
        // 栈内: <Sig> <PubK>
        byte[] publicKeyHash1 = stack.pop();    // 出栈并返回栈顶元素
        byte[] publicKeyHash2 = stack.pop();
        if (!Arrays.equals(publicKeyHash1, publicKeyHash2)) {   // 一一比较比特数组内的每一个数据是否相等
            return false;
        }
        // CHECKSIG 检查签名是否正确，正确则入栈 TRUE;
        // 栈内:
        byte[] publicKeyEncoded = stack.pop(); // 这里弹出的是二进制，在这里无法用来验签，故仍用 PublicKey形式的公钥验签
        byte[] sign1 = stack.pop();
        // 比特币网络中因为其脚本支持操作少的特性，需要入栈再检查，这里验证正确我们就直接返回了
        // 栈内: TRUE (验证正确情况下）
        return SecurityUtil.verify(publicKey.getEncoded(), sign1, publicKey);
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public int getAmount() {
        return amount;
    }

    public byte[] getPublicKeyHash() {
        return publicKeyHash;
    }

    @Override
    public String toString() {
        return "\n\tUTXO{" +
                "walletAddress='" + walletAddress + '\'' +
                ", amount=" + amount +
                ", publicKeyHash=" + SecurityUtil.bytes2HexString(publicKeyHash) +
                '}';
    }
}
