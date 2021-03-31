package data;

import java.util.*;

/**
 * 交易池
 */
public class TransactionPool {

    private final List<Transaction> transactions;
    private final int capacity;

    private final Set<UTXO> utxoSet;


    public TransactionPool(int capacity) {
        this.transactions = new ArrayList<>();
        this.capacity = capacity;
        utxoSet = new HashSet<>();
    }

    public void put(Transaction transaction) {
        // 检查交易池本次交易中是否出现双花交易，临时哈希表暂存本次
        for (UTXO utxo: transaction.getInUtxos()) {
            // 如果包含已使用的utxo，则拒绝本次交易进入交易池
            if (utxoSet.contains(utxo)) {
                return;
            }
        }
        // 存入本次所有的输入UTXO
        utxoSet.addAll(Arrays.asList(transaction.getInUtxos()));
        transactions.add(transaction);
    }

    public Transaction[] getAll() {
        Transaction[] ret = new Transaction[capacity];
        transactions.toArray(ret);
        transactions.clear();
        utxoSet.clear();
        return ret;
    }

    public boolean isFull() {
        return transactions.size() >= capacity;
    }

    public boolean isEmpty() {
        return transactions.isEmpty();
    }

    public int getCapacity() {
        return capacity;
    }
}


