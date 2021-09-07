import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Blocks definition reader.
 */
public class BlocksDefinitionReader {
    private DefaultPackage defaultPackage = null;
    private List<String> blockInfo = new ArrayList<>(), spacerInfo = new ArrayList<>();

    /**
     * Instantiates a new Blocks definition reader.
     */
    public BlocksDefinitionReader() {

    }

    /**
     * Generate a factory from the file.
     *
     * @param reader the file
     * @return the factory
     * @throws IOException the exception
     */
    public BlocksFromSymbolsFactory fromReader(java.io.Reader reader) throws IOException {
        Map<String, Integer> spacerWidths = new HashMap<>();
        Map<String, BlockCreator> blockCreators = new HashMap<>();
        getStringLists(reader);
        addSpacers(spacerWidths);
        addBlockCreators(blockCreators);
        reader.close();
        return new BlocksFromSymbolsFactory(spacerWidths, blockCreators);
    }

    /**
     * Adding block creators to the map.
     * @param blockCreators the map to be added to
     * @throws MissingBlockDetailsException if there are missing block details
     */
    private void addBlockCreators(Map<String, BlockCreator> blockCreators) throws MissingBlockDetailsException {
        for (String s : blockInfo) {
            Integer width = null, height = null;
            String stroke = null, fill = null, symbol = "";
            if (defaultPackage != null) {
                width = defaultPackage.getWidth();
                height = defaultPackage.getHeight();
                stroke = defaultPackage.getStroke();
                fill = defaultPackage.getFill();
            }
            String[] splitted = s.split("\\s+");
            for (int i = 1; i < splitted.length; i++) {
                if (splitted[i].startsWith("symbol")) {
                    symbol = splitted[i].split(":")[1];
                } else if (splitted[i].startsWith("height")) {
                    height = Integer.parseInt(splitted[i].split(":")[1]);
                } else if (splitted[i].startsWith("width")) {
                    width = Integer.parseInt(splitted[i].split(":")[1]);
                } else if (splitted[i].startsWith("stroke")) {
                    stroke = splitted[i].split(":")[1];
                } else if (splitted[i].startsWith("fill")) {
                    fill = splitted[i].split(":")[1];
                }
            }
            if (height == null || width == null || fill == null) {
                throw new MissingBlockDetailsException("Missing block details");
            }
            String borderCol = null;
            if (stroke != null) {
                borderCol = stroke.substring(stroke.indexOf('(') + 1, stroke.length() - 1);
            }
            String col = fill.substring(fill.indexOf('(') + 1, fill.length() - 1);;
            BlockCreator creator = null;
            if (fill.startsWith("color")) {
                creator = new ColorBlockFactory(width, height, col, borderCol);
            } else if (fill.startsWith("image")) {
                creator = new ImageBlockFactory(width, height, col, stroke);
            }
            blockCreators.put(symbol, creator);
        }
    }

    /**
     * Adds spacers.
     * @param spacerWidths the map to be stored in
     */
    private void addSpacers(Map<String, Integer> spacerWidths) {
        for (String s : spacerInfo) {
            String[] splitted = s.split("\\s+");
            String symbol = "";
            int width = 0;
            for (int i = 1; i < splitted.length; i++) {
                if (splitted[i].startsWith("symbol")) {
                    symbol = (splitted[i].split(":"))[1];
                } else if (splitted[i].startsWith("width")) {
                    width = Integer.parseInt(splitted[i].split(":")[1]);
                }
            }
            spacerWidths.put(symbol, width);
        }
    }

    /**
     * Gets string lists representing blocks and spacers.
     * @param reader the file reader
     * @throws IOException exception
     */
    private void getStringLists(java.io.Reader reader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.length() == 0 || line.charAt(0) == '#') {
                continue;
            }
            String[] splitted = line.split("\\s+");
            switch (splitted[0]) {
                case "default":
                    defaultPackage = new DefaultPackage(line);
                    break;
                case "bdef":
                    blockInfo.add(line);
                    break;
                case "sdef":
                    spacerInfo.add(line);
                    break;
                default:
                    break;
            }
        }
    }
}
