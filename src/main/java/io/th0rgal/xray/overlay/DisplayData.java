package io.th0rgal.xray.overlay;

import io.th0rgal.xray.XrayPlugin;
import io.th0rgal.xray.blocks.BlockWrapper;
import io.th0rgal.xray.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DisplayData {

    private final Map<BlockWrapper, Integer> colorPerType;
    private final HumanEntity player;
    private final Renderer renderer;

    public DisplayData(HumanEntity player) {
        this.colorPerType = new ConcurrentHashMap<>();
        this.player = player;
        this.renderer = new Renderer((Player) player,
                (Integer) Config.RENDER_DISTANCE.getValue(),
                (Block block) -> XrayPlugin.get().getDisplayData(player).getColor(block));
        renderer.runTaskTimerAsynchronously(XrayPlugin.get(), 0,
                (Integer) Config.REFRESH_COOLDOWN.getValue());
    }

    public int getColor(BlockWrapper wrapper) {
        return colorPerType.getOrDefault(wrapper, 0);
    }

    public int getColor(Block block) {
        for (Map.Entry<BlockWrapper, Integer> entry : colorPerType.entrySet())
            if (entry.getKey().isSimilarTo(block))
                return entry.getValue();
        return 0;
    }

    public void toggle(BlockWrapper wrapper, Integer color) {
        if (colorPerType.containsKey(wrapper)) {
            colorPerType.remove(wrapper);
            BlockOverlay.clear((Player) player);
        } else
            colorPerType.put(wrapper, color);
        renderer.updateRendering();
    }


}
