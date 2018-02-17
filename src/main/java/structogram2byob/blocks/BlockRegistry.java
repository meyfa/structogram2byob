package structogram2byob.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Mapping of block descriptions to the respective block, with the ability to
 * look blocks up with respect to
 * {@link BlockDescription#isAssignableFrom(BlockDescription)}.
 */
public class BlockRegistry
{
    private final BlockRegistry base;
    private final Map<BlockDescription, Block> blocks = new HashMap<>();

    /**
     * Constructs an empty block registry.
     */
    public BlockRegistry()
    {
        this(null);
    }

    /**
     * Constructs a block registry that will extend the given base registry
     * without modifying it.
     *
     * @param base The block registry to be used as the base for this one.
     */
    public BlockRegistry(BlockRegistry base)
    {
        this.base = base;
    }

    /**
     * Registers the given block with this registry.
     *
     * @param block The block to register.
     *
     * @throws IllegalArgumentException If the block is already registered.
     */
    public void register(Block block)
    {
        if (blocks.containsKey(block.getDescription())) {
            throw new IllegalArgumentException("block already registered");
        }
        blocks.put(block.getDescription(), block);
    }

    /**
     * Returns the block matching the given block description as per
     * {@link BlockDescription#isAssignableFrom(BlockDescription)}.
     *
     * @param desc The description to look up.
     * @return The block matching the given description.
     */
    public Block lookup(BlockDescription desc)
    {
        Block baseLookup = base != null ? base.lookup(desc) : null;
        if (baseLookup != null) {
            return baseLookup;
        }

        for (Entry<BlockDescription, Block> e : blocks.entrySet()) {
            if (e.getKey().isAssignableFrom(desc)) {
                return e.getValue();
            }
        }

        return null;
    }
}
