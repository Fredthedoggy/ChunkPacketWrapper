package me.fredthedoggy.chunkpacketwrapper.Events;

import me.fredthedoggy.chunkpacketwrapper.PacketBlock;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkEvent;

import java.util.HashMap;
import java.util.Map;

public class ChunkPacketEvent extends ChunkEvent {
    private final HandlerList HANDLERS = new HandlerList();
    private final Map<ChunkLocation, PacketBlock> changedMaterials = new HashMap<>();

    public ChunkPacketEvent(Chunk chunk) {
        super(chunk);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public HandlerList getHandlerList() {
        return HANDLERS;
    }

    public void setBlock(int x, int y, int z, Material material) {
        changedMaterials.put(new ChunkLocation(x, y, z), new PacketBlock(material));
    }

    public void setBlock(int x, int y, int z, Material material, int data) {
        changedMaterials.put(new ChunkLocation(x, y, z), new PacketBlock(material, data));
    }

    public void setBlock(int x, int y, int z, Object data) {
        changedMaterials.put(new ChunkLocation(x, y, z), new PacketBlock(data));
    }

    public Map<ChunkLocation, PacketBlock> getChangedMaterials() {
        return changedMaterials;
    }

    public static class ChunkLocation {
        public int X;
        public int Y;
        public int Z;
        public ChunkLocation(int X, int Y, int Z) {
            this.X = X;
            this.Y = Y;
            this.Z = Z;
        }
    }
}
