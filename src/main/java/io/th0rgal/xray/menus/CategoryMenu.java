package io.th0rgal.xray.menus;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import io.th0rgal.xray.XrayPlugin;
import io.th0rgal.xray.config.Config;
import io.th0rgal.xray.overlay.DisplayData;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

public class CategoryMenu {

    private ChestGui mainGui;

    public ChestGui create() {
        int rows = 2;
        mainGui = new ChestGui(rows, "XRay");
        ConfigurationSection categories = Config.MENU_CATEGORIES.toConfigSection();

        final StaticPane pane = new StaticPane(0, 0, 9, rows);
        int i = 0;
        for (String name : categories.getKeys(false).stream().sorted().collect(Collectors.toList())) {
            pane.addItem(new GuiItem(getItemStack(Config.MENU_CATEGORY_ITEM.toItem(name),
                    Config.MENU_CATEGORY_NAME.toSerializedString(name)),
                    event -> getCategoryGUI(event.getWhoClicked(), name).show(event.getWhoClicked())), i++, 0);
        }
        pane.addItem(new GuiItem(getItemStack(Material.BARRIER, Config.EXIT.toSerializedString()),
                event -> event.getWhoClicked().closeInventory()), 4, 1);

        mainGui.addPane(pane);
        return mainGui;
    }

    private ChestGui getCategoryGUI(HumanEntity owner, String name) {
        final DisplayData data = XrayPlugin.get().getDisplayData(owner);
        ChestGui gui = new ChestGui(4, "XRay - " + name);
        final StaticPane pane = new StaticPane(0, 0, 9, 4);
        int i = 0;
        for (String block : Config.MENU_CATEGORY_BLOCKS.toConfigSection(name).getKeys(false)) {
            ItemStack stack = Config.MENU_CATEGORY_BLOCKS_TYPE.toItem(name, block);
            ItemMeta meta = stack.getItemMeta();
            assert meta != null;
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            if (data.getColor(stack.getType()) != 0)
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
            stack.setItemMeta(meta);
            pane.addItem(new GuiItem(stack, event -> {
                updateDisplayData(stack.getType(),
                        Config.MENU_CATEGORY_BLOCKS_COLOR.toColor(name, block),
                        event.getWhoClicked());
                if (data.getColor(stack.getType()) != 0)
                    meta.addEnchant(Enchantment.DURABILITY, 1, true);
                else
                    meta.removeEnchant(Enchantment.DURABILITY);
                stack.setItemMeta(meta);
                gui.update();
            }), i++, 0);
        }

        pane.addItem(new GuiItem(getItemStack(Material.BARRIER, Config.BACK.toSerializedString()),
                (event) -> mainGui.show(event.getWhoClicked())), 4, 3);
        gui.setOnTopClick(event -> event.setCancelled(true));
        gui.addPane(pane);
        return gui;
    }

    private void updateDisplayData(Material type, int color, HumanEntity player) {
        DisplayData data = XrayPlugin.get().getDisplayData(player);
        data.toggle(type, color);
    }

    private ItemStack getItemStack(Material type, String name) {
        return getItemStack(new ItemStack(type), name);
    }

    private ItemStack getItemStack(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

}
