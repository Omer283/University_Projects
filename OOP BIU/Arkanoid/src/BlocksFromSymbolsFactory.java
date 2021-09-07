import java.util.Map;

/**
 * The type Blocks from symbols factory.
 */
public class BlocksFromSymbolsFactory {
    private Map<String, Integer> spacerWidths;
    private Map<String, BlockCreator> blockCreators;

    /**
     * Instantiates a new Blocks from symbols factory.
     *
     * @param spacers         the spacers
     * @param blockCreatorMap the block creators
     */
    public BlocksFromSymbolsFactory(Map<String, Integer> spacers, Map<String, BlockCreator> blockCreatorMap) {
        spacerWidths = spacers;
        blockCreators = blockCreatorMap;
    }

    /**
     * Checks if symbol is space.
     *
     * @param s the symbol
     * @return the answer
     */
    public boolean isSpaceSymbol(String s) {
        return spacerWidths.containsKey(s);
    }

    /**
     * Checks if symbol is block.
     *
     * @param s the symbol
     * @return the answer
     */
    public boolean isBlockSymbol(String s) {
        return blockCreators.containsKey(s);
    }

    /**
     * Gets space width.
     *
     * @param s the symbol
     * @return the space width
     */
    public int getSpaceWidth(String s) {
        if (!isSpaceSymbol(s)) {
            return 0;
        }
        return this.spacerWidths.get(s);
    }

    /**
     * Gets block.
     *
     * @param s the symbol
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the block
     */
    public Block getBlock(String s, int x, int y) {
        if (!isBlockSymbol(s)) {
            return null;
        }
        return this.blockCreators.get(s).create(x, y);
    }
}
