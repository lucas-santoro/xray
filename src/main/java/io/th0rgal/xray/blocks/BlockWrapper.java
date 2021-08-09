package io.th0rgal.xray.blocks;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public abstract class BlockWrapper {

    private final String namespace;
    private final String name;

    public BlockWrapper(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean isSimilarTo(Block block);

    public abstract ItemStack getAsItem();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BlockWrapper other)
            return other.namespace.equals(namespace) && other.name.equals(name);
        return false;
    }

    @Override
    public int hashCode() {
        return namespace.hashCode() / 2 + name.hashCode() / 2;
    }

    public static BlockWrapper wrap(String item) {
        String[] values = item.split(":"); // namespace:type
        return switch (values[0]) {
            case "minecraft" -> new MinecraftBlock(values[1]);
            case "oraxen" -> new OraxenBlock(values[1]);
            default -> null;
        };
    }
}
