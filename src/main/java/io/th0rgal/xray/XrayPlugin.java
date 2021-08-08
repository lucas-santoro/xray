package io.th0rgal.xray;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class XrayPlugin extends JavaPlugin {

    private static XrayPlugin instance;

    @Override
    public void onLoad() {
        instance = this;
        CommandAPI.onLoad(new CommandAPIConfig().silentLogs(true));
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable(this);
        new XrayCommand(this).register();
    }

    public static XrayPlugin get() {
        return instance;
    }


}
