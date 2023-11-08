package io.th0rgal.xray.config;

import io.th0rgal.xray.XrayPlugin;
import io.th0rgal.xray.blocks.BlockWrapper;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public enum Config {
    RENDER_DISTANCE("settings.render_distance"),
    REFRESH_COOLDOWN("settings.refresh_cooldown"),
    EXIT("menu.exit"),
    BACK("menu.back"),
    MENU_CATEGORIES("menu.categories"),
    MENU_CATEGORY_ITEM("menu.categories.%s.item"),
    MENU_CATEGORY_NAME("menu.categories.%s.name"),
    MENU_CATEGORY_BLOCKS("menu.categories.%s.blocks"),
    MENU_CATEGORY_BLOCKS_NAME("menu.categories.%s.blocks.%s.name"),
    MENU_CATEGORY_BLOCKS_TYPE("menu.categories.%s.blocks.%s.type"),
    MENU_CATEGORY_BLOCKS_COLOR("menu.categories.%s.blocks.%s.color");

    private final String path;
    public static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    Config(String path) {
        this.path = path;
    }

    public String getPath(String... placeholders) {
        return path.formatted((Object[]) placeholders);
    }

    public Object getValue(String... placeholders) {
        return XrayPlugin.get().getConfig().get(getPath(placeholders));
    }

    @Override
    public String toString() {
        return (String) getValue();
    }

    public String toString(String... placeholders) {
        return (String) getValue(placeholders);
    }

    public ConfigurationSection toConfigSection(String... placeholders) {
        return XrayPlugin.get().getConfig().getConfigurationSection(getPath(placeholders));
    }

    public @NotNull String toSerializedString(String... placeholders) {
        return null;
    }

    public BlockWrapper toWrappedBlock(String... placeholders) {
        return BlockWrapper.wrap(toString(placeholders));
    }

    public int toColor(String... placeholders) {
        String[] values = toString(placeholders).split(", ");
        int encoded = 0;
        encoded = encoded | Integer.parseInt(values[2]);
        encoded = encoded | (Integer.parseInt(values[1]) << 8);
        encoded = encoded | (Integer.parseInt(values[0]) << 16);
        encoded = encoded | (Integer.parseInt(values[3]) << 24);
        return encoded;
    }


}
