package spv;

import consensus.MinerPeer;
import data.Account;
import data.BlockHeader;
import data.Transaction;
import network.Network;
import utils.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

public class SpvPeer {

    private final List<BlockHeader> headers = new ArrayList<>();
    private final Account account;
    private final Network network;

    public SpvPeer(Account account, Network network) {
        this.account = account;
        this.network = network;
    }

    public void accept(BlockHeader blockHeader) {
        headers.add(blockHeader);

        //验证接收到的最新的区块中是不是有与自己相关的交易
        verifyLatest();
    }

    public void verifyLatest() {
        List<Transaction> transactions = network.getTransactionsInLatestBlock(account.getWalletAddress());
        //如果为空代表没有与自己相关的交易，不用验证
        if (transactions.isEmpty()) {
            return;
        }
        System.out.println("Account[" + account.getWalletAddress() + "] began to verify the transaction...");
        for (Transaction transaction: transactions) {
            if (!simplifiedPaymentVerify(transaction)) {
                System.out.println("verification failed!");
                System.exit(-1);
            }
        }
        System.out.println("Account[" + account.getWalletAddress() + "] verifies all transactions are successful!\n");
    }

    public boolean simplifiedPaymentVerify(Transaction transaction) {
        String txHash = SecurityUtil.sha256Digest(transaction.toString());

        //矿工是一个全节点
        MinerPeer minerPeer = network.getMinerPeer();

        //获得验证该交易所需要的根路径
        Proof proof = minerPeer.getProof(txHash);

        if (proof == null) {
            return false;
        }

        //计算得到的路径求和得到的merkle值
        String hash = proof.getTxHash();
        for (Proof.Node node: proof.getPath()) {
            switch (node.getOrientation()) {
                case LEFT: hash = SecurityUtil.sha256Digest(node.getTxHash() + hash); break;
                case RIGHT: hash = SecurityUtil.sha256Digest(hash + node.getTxHash()); break;
                default: return false;
            }
        }

        int height = proof.getHeight();


        String localMerkleRootHash = headers.get(height).getMerkleRootHash();

        String remoteMerkleRootHash= proof.getMerkleRootHash();

        System.out.println("\n-------> verify hash: \t" + txHash);
        System.out.println("callMerkleRootHash: \t\t" + hash);
        System.out.println("localMerkleRootHash: \t" + localMerkleRootHash);
        System.out.println("remoteMerkleRootHash: \t" + remoteMerkleRootHash);
        System.out.println();

        //算的merkle值与远程全节点存储的merkle值和轻节点本地存储的merkle值相等就返回正确
        return hash.equals(localMerkleRootHash) && hash.equals(remoteMerkleRootHash);
    }
}
