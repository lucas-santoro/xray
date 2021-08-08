package io.th0rgal.xray.overlay;

import io.netty.util.internal.ConcurrentSet;
import io.th0rgal.xray.XrayPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RendererListener implements Listener {


    private final ConcurrentSet<Location> blocks;
    private final BukkitRunnable runnable;
    private final Player player;

    public RendererListener(BukkitRunnable runnable, Player player) {
        this.runnable = runnable;
        this.blocks = new ConcurrentSet<>();
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, XrayPlugin.get());
    }

    public void addLocation(Location location) {
        blocks.add(location);
    }

    @EventHandler
    public void onBreakEvent(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        if (blocks.contains(location)) {
            BlockOverlay.clear(player);
            blocks.remove(location);
            for (Location block : blocks)
                new BlockOverlay(block, 0xFFFFFFFF).send(player);
        }
    }

    public void onPlayerDisconnect(PlayerQuitEvent event) {
        if (event.getPlayer().getUniqueId() == player.getUniqueId())
            runnable.cancel();
    }

}
