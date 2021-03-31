package data;

/**
 * 区块的类抽象，组合了区块头和区块体
 *
 */
public class Block {

    private final BlockHeader blockHeader;
    private final BlockBody blockBody;

    public Block(BlockHeader blockHeader, BlockBody blockBody) {
        this.blockHeader = blockHeader;
        this.blockBody = blockBody;
    }

    public BlockHeader getBlockHeader() {
        return blockHeader;
    }

    public BlockBody getBlockBody() {
        return blockBody;
    }

    @Override
    public String toString() {
        return "Block{" +
                "blockHeader=" + blockHeader +
                ", blockBody=" + blockBody +
                '}';
    }
}
