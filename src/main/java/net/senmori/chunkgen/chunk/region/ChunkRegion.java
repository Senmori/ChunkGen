package net.senmori.chunkgen.chunk.region;

import net.senmori.chunkgen.chunk.ChunkPos;
import net.senmori.chunkgen.chunk.shape.Shape;

import java.util.Set;

public interface ChunkRegion {

    public Set<ChunkPos> getRegion();

    Shape getShape();

    ChunkPos getCenter();

    int getRadius();

    String getName();
}
