package io.th0rgal.xray.overlay;

import org.bukkit.Material;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DisplayData {

    private final Map<Material, Integer> colorPerType;

    public DisplayData() {
        colorPerType = new ConcurrentHashMap<>();
    }

    public int getColor(Material type) {
        return colorPerType.getOrDefault(type, 0);
    }

    public void toggle(Material type, Integer color) {
        if (colorPerType.containsKey(type))
            colorPerType.remove(type);
        else
            colorPerType.put(type, color);
    }


}
