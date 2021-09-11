package me.fredthedoggy.chunkpacketwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.MultiBlockChangeInfo;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import me.fredthedoggy.chunkpacketwrapper.Events.ChunkPacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public final class ChunkPacketWrapper {
    private final ProtocolManager protocolManager;

    public ChunkPacketWrapper(JavaPlugin plugin) {
        protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(
                new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.MAP_CHUNK) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        // Map Chunk (id: 0x22)
                        if (event.getPacketType() == PacketType.Play.Server.MAP_CHUNK) {
                            PacketContainer packet = event.getPacket();
                            PacketContainer clone = packet.shallowClone();
                            Chunk chunk = Bukkit.getWorlds().get(0).getChunkAt(packet.getIntegers().read(0), packet.getIntegers().read(1));
                            ChunkPacketEvent packetEvent = new ChunkPacketEvent(chunk);
                            Bukkit.getPluginManager().callEvent(packetEvent);
                            MultiBlockChangeInfo[] changes = new MultiBlockChangeInfo[65536];
                            int i = 0;
                            for (int x = 0; x <= 15; x++) {
                                for (int y = 0; y <= 255; y++) {
                                    for (int z = 0; z <= 15; z++) {
                                        int finalZ = z;
                                        int finalY = y;
                                        int finalX = x;
                                        WrappedBlockData foundWrappedBlockData = packetEvent.getChangedMaterials().entrySet().stream().filter(e -> e.getKey().X == finalX && e.getKey().Y == finalY && e.getKey().Z == finalZ).map(e -> e.getValue().wrapBlock()).findFirst().orElse(null);
                                        changes[i++] = new MultiBlockChangeInfo(chunk.getBlock(x, y, z).getLocation(), foundWrappedBlockData);
                                    }
                                }
                            }
                            clone.getMultiBlockChangeInfoArrays().write(0, changes);
                            event.setPacket(clone);
                        }
                    }
                });
    }
}
