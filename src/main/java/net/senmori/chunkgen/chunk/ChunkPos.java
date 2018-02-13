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
        return "[" + this.x + ", " + this.z + "]";
    }

    public static Comparator<ChunkPos> byAngleComparator(ChunkPos center) {
        final int centerX = center.getX();
        final int centerZ = center.getZ();
        return new Comparator<ChunkPos>() {
            @Override
            public int compare(ChunkPos o1, ChunkPos o2) {
                float angle0 = angleToX( centerX, centerZ, o1.getX(), o2.getZ() );
                float angle1 = angleToX( centerX, centerZ, o2.getX(), o2.getZ() );
                return Float.compare( angle0, angle1 );
            }
        };
    }

    private static float angleToX(int centerX, int centerZ, int x, int z) {
        float dx = x - centerX;
        float dz = z - centerZ;
        return ( float ) ( Math.atan2( dz, dx ) );
    }

}
