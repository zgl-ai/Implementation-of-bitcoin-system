package data;

/**
 * 对交易的抽象
 */
public class Transaction {

    private final String data;
    private final long timestamp;

    public Transaction(String data, long timestamp) {
        this.data = data;
        this.timestamp = timestamp;
    }

    public String getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "data='" + data + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}
