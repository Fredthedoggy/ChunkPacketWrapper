package me.fredthedoggy.chunkpacketwrapper;

import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.Material;

public class PacketBlock {
    public Object data;
    public Material material;
    public int blockData;
    public int type;

    public PacketBlock(Object data) {
        this.data = data;
        type = 0;
    }

    public PacketBlock(Material material) {
        this.material = material;
        type = 1;
    }

    public PacketBlock(Material material, int blockData) {
        this.material = material;
        this.blockData = blockData;
        type = 2;
    }

    public WrappedBlockData wrapBlock() {
        if (type == 0) return WrappedBlockData.createData(data);
        else if (type == 1) return WrappedBlockData.createData(material);
        else return WrappedBlockData.createData(material, blockData);
    }
}
