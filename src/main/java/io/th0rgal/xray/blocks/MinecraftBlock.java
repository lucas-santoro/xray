package io.th0rgal.xray.blocks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class MinecraftBlock extends BlockWrapper {

    private final Material type;

    public MinecraftBlock(String namespace, String name) {
        super(namespace, name);
        type = Material.getMaterial(name.toUpperCase());
    }

    @Override
    public boolean isSimilarTo(Block block) {
        return block.getType() == type;
    }

    @Override
    public ItemStack getAsItem() {
        return new ItemStack(type);
    }

}
