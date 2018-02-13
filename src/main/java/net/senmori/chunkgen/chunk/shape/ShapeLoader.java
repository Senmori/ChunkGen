package net.senmori.chunkgen.chunk.shape;

import net.senmori.chunkgen.chunk.ChunkPos;

import java.util.Set;

public interface ShapeLoader {

    Set<ChunkPos> createShape(ChunkPos center, int radius);
}
