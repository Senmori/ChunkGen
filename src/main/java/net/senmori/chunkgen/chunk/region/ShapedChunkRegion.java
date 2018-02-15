package net.senmori.chunkgen.chunk.region;

import com.google.common.collect.Sets;
import net.senmori.chunkgen.chunk.ChunkPos;
import net.senmori.chunkgen.chunk.shape.Shape;
import net.senmori.chunkgen.chunk.shape.ShapeHandler;

import java.util.Set;

public class ShapedChunkRegion implements ChunkRegion {

    private final String name;
    private final Shape shape;
    private final ChunkPos center;
    private final int radius;
    private final Set<ChunkPos> regions = Sets.newHashSet();

    public ShapedChunkRegion(String name, Shape shape, ChunkPos center, int radius) {
        this.name = name;
        this.shape = shape;
        this.center = center;
        this.radius = radius;
        this.regions.addAll( ShapeHandler.getLoader(shape).createShape( center, radius ) );
    }

    @Override
    public Set<ChunkPos> getRegion() {
        return regions;
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public ChunkPos getCenter() {
        return center;
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public String getName() {
        return name;
    }
}
