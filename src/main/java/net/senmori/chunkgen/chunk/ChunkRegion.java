package net.senmori.chunkgen.chunk;

import net.senmori.chunkgen.chunk.shape.Shape;

import java.util.Set;

public interface ChunkRegion {

    public Set<ChunkPos> getRegion();

    Shape getShape();

    ChunkPos getCenter();

    int getRadius();

    String getName();
}
