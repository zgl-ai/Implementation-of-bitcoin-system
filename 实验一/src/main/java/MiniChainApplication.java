import consensus.MinerNode;
import consensus.TransactionProducer;
import data.BlockBody;
import data.Transaction;
import data.TransactionPool;
import network.NetWork;
import utils.SHA256Util;

public class MiniChainApplication {

    public static void main(String[] args) {
        NetWork netWork = new NetWork();
        netWork.start();
    }

}
