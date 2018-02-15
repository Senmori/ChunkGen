package net.senmori.chunkgen.chunk.comparator;

import net.senmori.chunkgen.chunk.ChunkPos;

import java.util.Comparator;

/**
 * This class is used to sort {@link ChunkPos} collections according to their 'z' and 'x' values.
 * Z takes priority before X.
 */
public class DefaultChunkPosComparator implements Comparator<ChunkPos> {

    @Override
    public int compare(ChunkPos o1, ChunkPos o2) {
        if(o1.getZ() >= o2.getX()) {
            // Z before X
            return Integer.compare( o1.getZ(), o2.getZ() );
        }
        return Integer.compare( o1.getX(), o2.getX() );
    }

}
