package network;

import config.MiniChainConfig;
import consensus.MinerNode;
import consensus.TransactionProducer;
import data.BlockChain;
import data.TransactionPool;

/**
 * 该类模拟一个网络环境，在该网络中主要有区块链和矿工，另外地，出于工程实现的角度，还有一个交易池和一个生成随机交易的线程
 *
 */
public class NetWork {

    private final BlockChain blockChain = new BlockChain();
    private MinerNode minerNode;
    private TransactionPool transactionPool;
    private TransactionProducer transactionProducer;

    /**
     * 系统中几个主要成员的初始化
     */
    public NetWork() {
        transactionPool = new TransactionPool(MiniChainConfig.MAX_TRANSACTION_COUNT);
        transactionProducer = new TransactionProducer(transactionPool);
        minerNode = new MinerNode(transactionPool, blockChain);
    }

    /**
     * 启动挖矿线程和生成随机交易的线程
     */
    public void start() {
        transactionProducer.start();
        minerNode.start();
    }

}
