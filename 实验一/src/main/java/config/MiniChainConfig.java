package config;

/**
 * 该类为配置类，主要有两个字段：
 *    DIFFICULTY: 挖矿的难度值，即规定了新的区块的哈希值至少以几个0开头才满足难度条件
 *
 *    MAX_TRANSACTION_COUNT: 交易池大小；TransactionProducer需要随机生成交易，放入交易池中，直至达到该大小
 */
public class MiniChainConfig {

    public static final int DIFFICULTY = 4;

    public static final int MAX_TRANSACTION_COUNT = 64;

}
