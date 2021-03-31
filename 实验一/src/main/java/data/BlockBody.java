package data;

import java.util.Arrays;

/**
 * 对区块体的抽象，主要有两个字段：
 *    transactions: 从交易池中取得的一批次交易
 *
 *    merkleRootHash: 使用上述交易，计算得到的Merkle树根哈希值
 */
public class BlockBody {

    private final Transaction[] transactions;
    private final String merkleRootHash;

    public BlockBody(String merkleRootHash, Transaction[] transactions) {
        this.merkleRootHash = merkleRootHash;
        this.transactions = transactions;
    }

    public String getMerkleRootHash() {
        return merkleRootHash;
    }

    public Transaction[] getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "BlockBody{" +
                "merkleRootHash='" + merkleRootHash + '\'' +
                ", transactions=" + Arrays.toString(transactions) +
                '}';
    }
}
