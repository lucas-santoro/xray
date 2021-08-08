package io.th0rgal.xray;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import io.th0rgal.xray.menus.CategoryMenu;
import org.bukkit.plugin.java.JavaPlugin;

public class XrayPlugin extends JavaPlugin {

    private ChestGui menu;
    private static XrayPlugin instance;

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


}
