package io.th0rgal.xray.overlay;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

public class Renderer extends BukkitRunnable {

    private final Player player;
    private final int length;
    private Location previousLocation;
    private final BlockFilter filter;
    private final RendererListener listener;

    public Renderer(Player player, int length, BlockFilter blockFilter) {
        this.player = player;
        this.length = length;
        this.filter = blockFilter;
        this.listener = new RendererListener(this, player);
    }

    @FunctionalInterface
    public interface BlockFilter {
        boolean isFiltered(Block block);
    }

    @Override
    public void run() {
        if (previousLocation == null
                || previousLocation.distanceSquared(player.getLocation()) > length * length * 0.5) {
            updateRendering();
        }
    }

    public void updateRendering() {
        previousLocation = player.getLocation();
        World world = player.getWorld();
        for (int i = -length / 2; i < length / 2; i++)
            for (int j = -length / 2; j < length / 2; j++)
                for (int k = -length / 2; k < length / 2; k++) {
                    Block block = world.getBlockAt(previousLocation.clone().add(i, j, k));
                    if (filter.isFiltered(block)) {
                        listener.addLocation(block.getLocation());
                        new BlockOverlay(block.getLocation(), 0xFFFFFFFF).send(player);
                    }
                }
    }

}
