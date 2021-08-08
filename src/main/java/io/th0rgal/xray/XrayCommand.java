package io.th0rgal.xray;

import dev.jorel.commandapi.CommandAPICommand;
import io.th0rgal.xray.overlay.Renderer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class XrayCommand {

    private final CommandAPICommand command;

    public XrayCommand(JavaPlugin plugin) {
        command = new CommandAPICommand("xray")
                .withPermission("xray.command")
                .executes((sender, args) -> {
                    if (sender instanceof Player player) {
                        new Renderer(player,
                                50,
                                (Block block) -> block.getType() == Material.DIAMOND_ORE)
                                .runTaskTimerAsynchronously(plugin, 0, 25);
                    }
                });
    }

    public void register() {
        command.register();
    }


}
