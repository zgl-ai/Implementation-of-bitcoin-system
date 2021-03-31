package data;

import utils.SHA256Util;

import java.util.LinkedList;
import java.util.Random;

/**
 * 区块链的类抽象，创建该对象时会自动生成创世纪块，加入区块链中
 */
public class BlockChain {

    private final LinkedList<Block> chain = new LinkedList<>();

    public BlockChain() {
        BlockHeader genesisBlockHeader = new BlockHeader(null, null,
                                                            Math.abs(new Random().nextLong()));
        Block genesisBlock = new Block(genesisBlockHeader, null);
        System.out.println("Create the genesis Block! ");
        System.out.println("And the hash of genesis Block is : " + SHA256Util.sha256Digest(genesisBlock.toString()) +
                ", you will see the hash value in next Block's preBlockHash field.");
        System.out.println();
        chain.add(genesisBlock);
    }

    /**
     * 向区块链中添加新的满足难度条件的区块
     *
     * @param block 新的满足难度条件的区块
     */
    public void addNewBlock(Block block) {
        chain.offer(block);
    }

    /**
     * 获取区块链的最后一个区块，矿工在组装新的区块时，需要获取上一个区块的哈希值，通过该方法获得
     *
     * @return 区块链的最后一个区块
     */
    public Block getNewestBlock() {
        return chain.peekLast();
    }

}
