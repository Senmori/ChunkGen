package net.senmori.chunkgen.chunk;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.senmori.chunkgen.ChunkPreGen;
import net.senmori.chunkgen.callbacks.BroadcastCallback;
import net.senmori.chunkgen.chunk.region.ChunkRegion;
import net.senmori.chunkgen.chunk.region.ShapedChunkRegion;
import net.senmori.chunkgen.chunk.shape.Shape;
import net.senmori.chunkgen.configuration.Settings;
import net.senmori.chunkgen.task.ChunkGenerateTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class ChunkRegionHandler {
    private static final ChunkRegionHandler INSTANCE = new ChunkRegionHandler();

    private final Map<String, ChunkRegion> cachedChunkRegions = Maps.newHashMap();
    private final LinkedList<ChunkRegion> queuedRegions = new LinkedList<>();


    private boolean running = false;

    private BukkitRunnable loader;
    private BukkitTask currentTask;
    private ChunkGenerateTask currentGenerateTask = null;
    private ChunkRegion currentRegion = null;

    private ChunkPreGen owner = ChunkPreGen.instance();
    private Server server = Bukkit.getServer();
    private Settings settings = owner.getSettings();

    private ChunkRegionHandler() {
    }

    public static ChunkRegionHandler getInstance() {
        return INSTANCE;
    }

    public boolean start() {
        if( isRunning() || ( currentTask != null && !currentTask.isCancelled() ) ) {
            // only start task if we are already running or if the current task is cancelled
            return false;
        }
        running = true;
        loader = new BukkitRunnable() {
            @Override
            public void run() {
                if( queuedRegions.peek() == null ) {
                    // no more regions; we loaded them all! :D
                    running = false;
                    this.cancel();
                    return;
                }

                if( currentRegion == null ) {
                    currentRegion = queuedRegions.poll();
                }

                if( currentGenerateTask == null && currentRegion != null) {
                    // first run
                    currentGenerateTask = new ChunkGenerateTask( owner, server, settings, currentRegion, new BroadcastCallback());

                    currentGenerateTask.runTaskTimer( owner, 1L, 1L );
                } else if ( currentGenerateTask != null && currentGenerateTask.isCancelled() ) {
                    // task is complete, move to next region
                    currentRegion = queuedRegions.poll();
                    currentGenerateTask = new ChunkGenerateTask( owner, server, settings, currentRegion, new BroadcastCallback());
                    currentGenerateTask.runTaskTimer( owner, 1L, 1L );
                    running = true;
                }
            }
        };

        currentTask = loader.runTaskTimer( owner, 1L, 1L );
        return true;
    }

    public boolean isRunning() {
        return running;
    }

    public void addChunkRegion(ChunkRegion region) {
        queuedRegions.add( region );
        cachedChunkRegions.put( region.getName(), region );
    }

    public void createChunkRegion(String name, Location location, int radius) {
        ChunkPos center = new ChunkPos( location );
        queuedRegions.add( new ShapedChunkRegion( name, Shape.SQUARE, center, radius ) );
        cachedChunkRegions.put( name, new ShapedChunkRegion( name, Shape.SQUARE, center, radius ) );
    }

    public void createChunkRegion(String name, int chunkX, int chunkZ, UUID worldUUID, int radius, Shape shape) {
        ChunkPos center = new ChunkPos( chunkX, chunkZ, worldUUID );
        queuedRegions.add( new ShapedChunkRegion( name, shape, center, radius ) );
        cachedChunkRegions.put( name, new ShapedChunkRegion( name, shape, center, radius ) );
    }

    public Collection<ChunkRegion> getRegions() {
        return Sets.newHashSet( cachedChunkRegions.values() );
    }
}
