package net.senmori.chunkgen.chunk.shape;

import net.senmori.chunkgen.chunk.ChunkPos;

import java.util.Collection;

public interface ShapeLoader {

    Collection<ChunkPos> createShape(ChunkPos center, int radius);
}
