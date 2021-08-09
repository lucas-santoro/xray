package io.th0rgal.xray.overlay;

import io.th0rgal.xray.XrayPlugin;
import io.th0rgal.xray.config.Config;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DisplayData {

    private final Map<Material, Integer> colorPerType;
    private final HumanEntity player;
    private final Renderer renderer;

    public DisplayData(HumanEntity player) {
        this.colorPerType = new ConcurrentHashMap<>();
        this.player = player;
        this.renderer = new Renderer((Player) player,
                (Integer) Config.RENDER_DISTANCE.getValue(),
                (Block block) -> XrayPlugin.get().getDisplayData(player).getColor(block.getType()));
        renderer.runTaskTimerAsynchronously(XrayPlugin.get(), 0,
                (Integer) Config.REFRESH_COOLDOWN.getValue());
    }

    public int getColor(Material type) {
        return colorPerType.getOrDefault(type, 0);
    }

    public void toggle(Material type, Integer color) {
        if (colorPerType.containsKey(type)) {
            colorPerType.remove(type);
            BlockOverlay.clear((Player) player);
        } else
            colorPerType.put(type, color);
        renderer.updateRendering();
    }


}
