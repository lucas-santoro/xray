package io.th0rgal.xray;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import io.th0rgal.xray.XrayPlugin;
import io.th0rgal.xray.blocks.BlockWrapper;
import io.th0rgal.xray.config.Config;
import io.th0rgal.xray.overlay.DisplayData;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XrayMenu {

    private ChestGui mainGui;

    public ChestGui create() {
        mainGui = new ChestGui(6, "XRay");
        ConfigurationSection categories = Config.MENU_CATEGORIES.toConfigSection();

        final StaticPane pane = new StaticPane(0, 0, 9, 5);
        int i = 0;
        for (String name : categories.getKeys(false).stream().sorted().collect(Collectors.toList())) {
            pane.addItem(new GuiItem(getItemStack(Config.MENU_CATEGORY_ITEM.toWrappedBlock(name).getAsItem(),
                    Config.MENU_CATEGORY_NAME.toSerializedString(name)),
                            event -> getCategoryGUI(event.getWhoClicked(), name).show(event.getWhoClicked())),
                    i % 9, i / 9);
            i++;
        }
        final StaticPane exit = new StaticPane(4, 5, 1, 1);
        exit.addItem(new GuiItem(getItemStack(Material.BARRIER, Config.EXIT.toSerializedString()),
                event -> event.getWhoClicked().closeInventory()), 0, 0);

        mainGui.addPane(pane);
        mainGui.addPane(exit);
        return mainGui;
    }

    private ChestGui getCategoryGUI(HumanEntity owner, String name) {
        final DisplayData data = XrayPlugin.get().getDisplayData(owner);
        ChestGui gui = new ChestGui(6, "XRay - " + name);
        final PaginatedPane pane = new PaginatedPane(9, 5);

        List<GuiItem> items = new ArrayList<>();
        for (String block : Config.MENU_CATEGORY_BLOCKS.toConfigSection(name).getKeys(false)) {
            BlockWrapper wrapped = Config.MENU_CATEGORY_BLOCKS_TYPE.toWrappedBlock(name, block);
            ItemStack stack = wrapped.getAsItem();
            ItemMeta meta = stack.getItemMeta();
            assert meta != null;
            meta.setDisplayName(Config.MENU_CATEGORY_BLOCKS_NAME.toSerializedString(name, block));
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            if (data.getColor(wrapped) != 0)
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
            stack.setItemMeta(meta);
            items.add(new GuiItem(stack, event -> {
                updateDisplayData(wrapped,
                        Config.MENU_CATEGORY_BLOCKS_COLOR.toColor(name, block),
                        event.getWhoClicked());
                if (data.getColor(wrapped) != 0)
                    meta.addEnchant(Enchantment.DURABILITY, 1, true);
                else
                    meta.removeEnchant(Enchantment.DURABILITY);
                stack.setItemMeta(meta);
                gui.update();
            }));
        }

        for (int i = 0; i < (items.size() - 1) / 45 + 1; i++) {
            final List<GuiItem> itemStackList = extractPageItems(items, i);
            final StaticPane staticPane = new StaticPane(9, Math.min((itemStackList.size() - 1) / 9 + 1, 5));
            for (int itemIndex = 0; itemIndex < itemStackList.size(); itemIndex++)
                staticPane.addItem(itemStackList.get(itemIndex), itemIndex % 9, itemIndex / 9);
            pane.addPane(i, staticPane);
        }

        //page selection
        final StaticPane back = new StaticPane(2, 5, 1, 1);
        final StaticPane forward = new StaticPane(6, 5, 1, 1);
        final StaticPane exit = new StaticPane(4, 5, 1, 1);

        back.addItem(new GuiItem(new ItemStack(Material.ARROW), event -> {
            pane.setPage(pane.getPage() - 1);
            if (pane.getPage() == 0) back.setVisible(false);
            forward.setVisible(true);
            gui.update();
        }), 0, 0);
        back.setVisible(false);

        forward.addItem(new GuiItem(new ItemStack(Material.ARROW), event -> {
            pane.setPage(pane.getPage() + 1);
            if (pane.getPage() == pane.getPages() - 1) forward.setVisible(false);
            back.setVisible(true);
            gui.update();
        }), 0, 0);

        if (pane.getPages() <= 1)
            forward.setVisible(false);

        exit.addItem(new GuiItem(getItemStack(Material.BARRIER, Config.BACK.toSerializedString()),
                (event) -> mainGui.show(event.getWhoClicked())), 0, 0);

        gui.setOnTopClick(event -> event.setCancelled(true));
        gui.addPane(back);
        gui.addPane(forward);
        gui.addPane(exit);
        gui.addPane(pane);
        return gui;
    }

    private List<GuiItem> extractPageItems(final List<GuiItem> items, final int page) {
        final List<GuiItem> output = new ArrayList<>();
        for (int i = page * 45; i < (page + 1) * 45 && i < items.size(); i++) output.add(items.get(i));
        return output;
    }


    private void updateDisplayData(BlockWrapper wrapper, int color, HumanEntity player) {
        DisplayData data = XrayPlugin.get().getDisplayData(player);
        data.toggle(wrapper, color);
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
