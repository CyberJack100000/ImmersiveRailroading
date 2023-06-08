package cam72cam.immersiverailroading.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Merged implements DataBlock {
    Map<String, DataBlock.Value> primitives;
    Map<String, List<DataBlock.Value>> primitiveSets;
    Map<String, DataBlock> blocks;
    Map<String, List<DataBlock>> blockSets;

    public Merged(DataBlock base, DataBlock override) {
        this.primitives = new LinkedHashMap<>(base.getValueMap());
        this.primitiveSets = new LinkedHashMap<>(base.getValuesMap());
        this.blocks = new LinkedHashMap<>(base.getBlockMap());
        this.blockSets = new LinkedHashMap<>(base.getBlocksMap());

        primitives.putAll(override.getValueMap());
        override.getValuesMap().forEach((key, values) -> {
            if (primitiveSets.containsKey(key)) {
                // Merge into new list
                values = new ArrayList<>(values);
                values.addAll(primitiveSets.get(key));
            }
            primitiveSets.put(key, values);
        });
        override.getBlockMap().forEach((key, block) -> {
            if (blocks.containsKey(key)) {
                block = new Merged(blocks.get(key), block);
            }
            blocks.put(key, block);
        });
        override.getBlocksMap().forEach((key, blocks) -> {
            if (blockSets.containsKey(key)) {
                blocks = new ArrayList<>(blocks);
                blocks.addAll(blockSets.get(key));
            }
            blockSets.put(key, blocks);
        });
    }

    @Override
    public Map<String, Value> getValueMap() {
        return primitives;
    }

    @Override
    public Map<String, List<Value>> getValuesMap() {
        return primitiveSets;
    }

    @Override
    public Map<String, DataBlock> getBlockMap() {
        return blocks;
    }

    @Override
    public Map<String, List<DataBlock>> getBlocksMap() {
        return blockSets;
    }
}
