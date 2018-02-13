package net.senmori.chunkgen.chunk.shape;

import net.senmori.chunkgen.chunk.ChunkRegion;

/**
 * This enum determines what shape a {@link ChunkRegion} is.
 */
public final class Shape {

    public static final Shape SQUARE = new Shape( "square" );

    private final String name;
    public Shape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
