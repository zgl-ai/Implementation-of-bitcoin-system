package data;

import config.MiniChainConfig;

/**
 * 对区块头的抽象（参考比特币中的区块头结构），主要有以下字段：
 *    version: 版本号，默认为1，无需提供该参数
 *
 *    preBlockHash: 前一个区块的哈希值，创建新的区块头对象时需要提供该参数
 *
 *    merkleRootHash: 该区块头对应区块体中的交易的Merkle根哈希值，创建新的区块头对象时需要提供该参数
 *
 *    timestamp: 时间戳，创建区块头对象时会自动填充，无需提供该参数
 *
 *    difficulty: 挖矿难度，默认为系统配置中的难度值，无需提供该参数
 *
 *    nonce: 随机字段，创建新的区块头对象时需要提供该参数
 *
 */
public class BlockHeader {

    private final int version = 1;
    private final String preBlockHash;
    private final String merkleRootHash;
    private final long timestamp;
    private final int difficulty = MiniChainConfig.DIFFICULTY;
    private long nonce;

    public BlockHeader(String preBlockHash, String merkleRootHash, long nonce) {
        this.preBlockHash = preBlockHash;
        this.merkleRootHash = merkleRootHash;
        this.timestamp = System.currentTimeMillis();
        this.nonce = nonce;
    }

    public int getVersion() {
        return version;
    }

    public String getPreBlockHash() {
        return preBlockHash;
    }

    public String getMerkleRootHash() {
        return merkleRootHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    @Override
    public String toString() {
        return "BlockHeader{" +
                "version=" + version +
                ", preBlockHash='" + preBlockHash + '\'' +
                ", merkleRootHash='" + merkleRootHash + '\'' +
                ", timestamp=" + timestamp +
                ", difficulty=" + difficulty +
                ", nonce=" + nonce +
                '}';
    }
}
