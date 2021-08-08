package io.th0rgal.xray;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import io.th0rgal.xray.menus.CategoryMenu;
import io.th0rgal.xray.overlay.DisplayData;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XrayPlugin extends JavaPlugin {

    private ChestGui menu;
    private static XrayPlugin instance;
    private final Map<HumanEntity, DisplayData> displayData = new ConcurrentHashMap<>();

    @Override
    public void onLoad() {
        instance = this;
        CommandAPI.onLoad(new CommandAPIConfig().silentLogs(true));
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        menu = new CategoryMenu().create();
        CommandAPI.onEnable(this);
        new XrayCommand().register();
    }

    public static XrayPlugin get() {
        return instance;
    }

    public ChestGui getMenu() {
        return menu;
    }

    public DisplayData getDisplayData(HumanEntity player) {
        if (displayData.containsKey(player))
            return displayData.get(player);
        DisplayData data = new DisplayData(player);
        displayData.put(player, data);
        return data;
    }

}
