package io.th0rgal.xray;

import dev.jorel.commandapi.CommandAPICommand;
import io.th0rgal.xray.overlay.Renderer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


public class XrayCommand {

    private final CommandAPICommand command;

    public XrayCommand() {
        command = new CommandAPICommand("xray")
                .withPermission("xray.command")
                .executes((sender, args) -> {
                    if (sender instanceof Player player) {
                        XrayPlugin.get().getMenu().show(player);
                        new Renderer(player,
                                50,
                                (Block block) -> XrayPlugin.get().getDisplayData(player).getColor(block.getType()))
                                .runTaskTimerAsynchronously(XrayPlugin.get(), 0, 25);
                    }
                });
    }

    public void register() {
        command.register();
    }


}
