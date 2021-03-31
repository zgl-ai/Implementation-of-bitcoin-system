package unit;

import config.MiniChainConfig;
import consensus.MinerNode;
import data.BlockBody;
import data.Transaction;
import org.junit.Assert;
import org.junit.Before;
import utils.SHA256Util;

public class Test {

    private MinerNode minerNode;

    @Before
    public void setUp() {
        minerNode = new MinerNode(null, null);
    }

    @org.junit.Test
    public void getBlockBodyTest() {
        Transaction[] transactions = new Transaction[MiniChainConfig.MAX_TRANSACTION_COUNT];
        for (int i = 0; i < MiniChainConfig.MAX_TRANSACTION_COUNT; i++) {
            transactions[i] = new Transaction("com.ecnu.dase.minichain" + i, 0);
        }
        BlockBody blockBody = minerNode.getBlockBody(transactions);
        Assert.assertTrue("ac81624dc5efd6e92dee9da722b00ce5bf4c06eb69b5f197807c96fabc2f947a".equals(SHA256Util.sha256Digest(blockBody.toString())));
    }

}
