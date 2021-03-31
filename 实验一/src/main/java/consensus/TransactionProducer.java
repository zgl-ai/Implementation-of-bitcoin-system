package consensus;

import data.Transaction;
import data.TransactionPool;

import java.util.UUID;

/**
 * 生成随机交易
 */
public class TransactionProducer extends Thread {

    private TransactionPool transactionPool;

    public TransactionProducer(TransactionPool transactionPool) {
        this.transactionPool = transactionPool;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (transactionPool) {
                while (transactionPool.isFull()) {
                    try {
                        transactionPool.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Transaction randomOne = getOneTransaction();
                transactionPool.put(randomOne);
                if (transactionPool.isFull()) {
                    transactionPool.notify();
                }
            }
        }
    }

    private Transaction getOneTransaction() {
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), System.currentTimeMillis());
        return transaction;
    }

}
