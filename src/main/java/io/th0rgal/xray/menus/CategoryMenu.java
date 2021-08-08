package io.th0rgal.xray.menus;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import io.th0rgal.xray.XrayPlugin;
import io.th0rgal.xray.config.Config;
import io.th0rgal.xray.overlay.DisplayData;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class CategoryMenu {

    private ChestGui mainGui;

    public ChestGui create() {
        int rows = 2;
        mainGui = new ChestGui(rows, "XRay");
        ConfigurationSection categories = Config.MENU_CATEGORIES.toConfigSection();
        Map<ItemStack, ChestGui> icons = new HashMap<>();
        for (String name : categories.getKeys(false))
            icons.put(getItemStack(Config.MENU_CATEGORY_ITEM.toItem(name),
                    Config.MENU_CATEGORY_NAME.toSerializedString(name)), getCategoryGUI(name));

        final StaticPane pane = new StaticPane(0, 0, 9, rows);
        int i = 0;
        for (Map.Entry<ItemStack, ChestGui> entry : icons.entrySet())
            pane.addItem(new GuiItem(entry.getKey(),
                    event -> entry.getValue().show(event.getWhoClicked())), i++, 0);
        pane.addItem(new GuiItem(getItemStack(Material.BARRIER, Config.EXIT.toSerializedString()),
                event -> event.getWhoClicked().closeInventory()), 4, 1);
        mainGui.setOnTopClick(event -> event.setCancelled(true));
        mainGui.addPane(pane);
        return mainGui;
    }

    private ChestGui getCategoryGUI(String name) {
        ChestGui gui = new ChestGui(4, "XRay - " + name);
        final StaticPane pane = new StaticPane(0, 0, 9, 3);
        int i = 0;
        ItemStack changeColorButton = getItemStack(Material.LIME_CONCRETE, Config.COLOR.toSerializedString());
        for (String block : Config.MENU_CATEGORY_BLOCKS.toConfigSection(name).getKeys(false)) {
            ItemStack stack = Config.MENU_CATEGORY_BLOCKS_TYPE.toItem(name, block);
            pane.addItem(new GuiItem(stack,
                            (event) -> updateDisplayData(stack.getType(),
                                    Config.MENU_CATEGORY_BLOCKS_COLOR.toColor(name, block),
                                    event.getWhoClicked()))
                    , i++, 0);

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
        XrayPlugin.get().setDisplayData(player, data);
    }

    private ItemStack getItemStack(Material type, String name) {
        return getItemStack(new ItemStack(type), name);
    }

    private ItemStack getItemStack(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        return item;
    }

}
