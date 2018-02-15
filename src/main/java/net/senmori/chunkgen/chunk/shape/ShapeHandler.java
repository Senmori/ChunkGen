package net.senmori.chunkgen.chunk.shape;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.senmori.chunkgen.chunk.shape.loader.SquareShapeLoader;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class ShapeHandler {

    private static final Map<Shape, ShapeLoader> shapeMap = Maps.newHashMap();

    private ShapeHandler() {}

    public static ShapeLoader getLoader(Shape shape) {
        return shapeMap.get( shape );
    }

    public static boolean registerShape(Shape shape, ShapeLoader loader) {
        return shapeMap.putIfAbsent( shape, loader ) == null;
    }

    public static Shape getShape(String name) {
        return shapeMap.keySet().stream().filter( key -> key.getName().equalsIgnoreCase( name ) ).limit( 1 ).collect( Collectors.toList() ).get( 0 );
    }

    public static Set<Shape> getShapes() {
        return ImmutableSet.copyOf( shapeMap.keySet() );
    }

    static {
        shapeMap.put( Shape.SQUARE, new SquareShapeLoader() );
    }
}
