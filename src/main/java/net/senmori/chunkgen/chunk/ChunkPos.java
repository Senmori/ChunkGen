package net.senmori.chunkgen.chunk;

import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.Comparator;
import java.util.UUID;

public class ChunkPos {

    private final int x;
    private final int z;
    private final UUID world;

    public ChunkPos(int x, int z, UUID world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    public ChunkPos(Location location) {
        this.x = location.getBlockX() >> 4;
        this.z = location.getBlockZ() >> 4;
        this.world = location.getWorld().getUID();
    }

    public ChunkPos(Chunk chunk) {
        this.x = chunk.getX();
        this.z = chunk.getZ();
        this.world = chunk.getWorld().getUID();
    }

    public UUID getWorldUID() {
        return world;
    }

    public boolean equals(Object other) {
        if ( other == null || ! ( other instanceof ChunkPos ) ) {
            return false;
        }
        ChunkPos pos = ( ChunkPos ) other;
        return pos.getX() == this.getX() && pos.getZ() == this.getZ();
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String toString() {
        return "ChunkPos{X=" + this.x + ", Z=" + this.z + "}";
    }
}
