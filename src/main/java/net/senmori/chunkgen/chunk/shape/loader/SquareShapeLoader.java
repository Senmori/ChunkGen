package net.senmori.chunkgen.chunk.shape.loader;

import com.google.common.collect.Sets;
import net.senmori.chunkgen.chunk.ChunkPos;
import net.senmori.chunkgen.chunk.shape.ShapeLoader;

import java.util.Set;

public class SquareShapeLoader implements ShapeLoader {
    @Override
    public Set<ChunkPos> createShape(ChunkPos center, int radius) {
        Set<ChunkPos> result = Sets.newHashSet();
        int xMin = center.getX() + -radius;
        int xMax = center.getX() + radius;

        int zMin = center.getZ() + -radius;
        int zMax = center.getZ() + radius;

        // every increment of x/z is a new chunk position
        for ( int x = xMin; x <= xMax; x++ ) {
            for ( int z = zMin; z <= zMax; z++ ) {
                result.add( new ChunkPos( x, z, center.getWorldUID() ) );
            }
        }
        return result;
    }
}
