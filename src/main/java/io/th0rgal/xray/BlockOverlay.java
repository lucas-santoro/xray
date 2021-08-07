package io.th0rgal.xray;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class BlockOverlay {

    private final Location location;
    private final int color;
    private final int lifetime;
    private final String title;

    public BlockOverlay(Location location, int color, int lifetime) {
        this.location = location;
        this.color = color;
        this.lifetime = lifetime;
        this.title = "";
    }

    public BlockOverlay(Location location, int color) {
        this.location = location;
        this.color = color;
        this.lifetime = 0xFFFFFF;
        this.title = "";
    }

    @SuppressWarnings("UnstableApiUsage")
    public void send(Player player) {
        PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.CUSTOM_PAYLOAD);
        packetContainer.getMinecraftKeys().write(0,
                new MinecraftKey("minecraft", "debug/game_test_add_marker"));
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeLong(getPosition(location));
        out.writeInt(color);
        writeVarInt(out, 0);
        out.writeInt(lifetime);
        packetContainer.getModifier().write(1,
                new PacketDataSerializer(Unpooled.wrappedBuffer(out.toByteArray())));
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(
                    "Cannot send packet " + packetContainer, e);
        }
    }

    private long getPosition(Location location) {
        return ((long) (location.getBlockX() & 0x3FFFFFF) << 38)
                | ((long) (location.getBlockZ() & 0x3FFFFFF) << 12)
                | (location.getBlockY() & 0xFFF);
    }

    private void writeVarInt(ByteArrayDataOutput out, int value) {
        do {
            byte currentByte = (byte) (value & 0b01111111);

            value >>>= 7;
            if (value != 0) currentByte |= 0b10000000;

            out.writeByte(currentByte);
        } while (value != 0);
    }

}
