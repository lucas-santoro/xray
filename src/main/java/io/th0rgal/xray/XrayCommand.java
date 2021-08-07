package io.th0rgal.xray;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Player;


public class XrayCommand {

    private final CommandAPICommand command;

    public XrayCommand() {
        command = new CommandAPICommand("xray")
                .withPermission("xray.command")
                .executes((sender, args) -> {
                    if (sender instanceof Player player) {
                        new BlockOverlay(player.getLocation(), 0xFFFFFFFF).send(player);
                    }
                });
    }

    public void register() {
        command.register();
    }


}
