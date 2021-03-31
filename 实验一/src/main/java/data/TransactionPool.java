package data;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易池
 */
public class TransactionPool {

    private List<Transaction> transactions;
    private int capacity;

    public TransactionPool(int capacity) {
        this.transactions = new ArrayList<>();
        this.capacity = capacity;
    }

    public void put(Transaction transaction) {
        transactions.add(transaction);
    }

    public Transaction[] getAll() {
        Transaction[] ret = new Transaction[capacity];
        transactions.toArray(ret);
        transactions.clear();
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


