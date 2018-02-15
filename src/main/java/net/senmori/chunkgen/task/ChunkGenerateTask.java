package net.senmori.chunkgen.task;

import com.google.common.collect.Lists;
import net.senmori.chunkgen.Permissions;
import net.senmori.chunkgen.ChunkPreGen;
import net.senmori.chunkgen.callbacks.SimpleCallback;
import net.senmori.chunkgen.chunk.ChunkPos;
import net.senmori.chunkgen.chunk.region.ChunkRegion;
import net.senmori.chunkgen.chunk.comparator.DefaultChunkPosComparator;
import net.senmori.chunkgen.configuration.Settings;
import net.senmori.chunkgen.language.LangKey;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.stream.Stream;

public class ChunkGenerateTask extends BukkitRunnable {

    private final ChunkPreGen owner;
    private final Server bukkitServer;
    private final Settings settings;
    private final SimpleCallback callback;

    LinkedList<ChunkPos> queuedRegions = Lists.newLinkedList();
    private final ChunkRegion chunkRegion;

    int chunkQueue = 0;
    int chunksGenerated = 0;
    int startingSize = 0;
    int pausedTicks = 0;
    int pausedUpdateInterval;

    public ChunkGenerateTask(ChunkPreGen plugin, Server server, Settings settings, ChunkRegion region, SimpleCallback callback) {
        this.owner = plugin;
        this.bukkitServer = server;
        this.settings = settings;
        this.chunkRegion = region;
        queuedRegions.addAll( region.getRegion() );
        this.startingSize = queuedRegions.size();
        this.callback = callback;
        this.pausedUpdateInterval = settings.UPDATE_DELAY.getValue().intValue();

        queuedRegions.sort( new DefaultChunkPosComparator() );
    }

    @Override
    public void run() {
        if( settings.PAUSE_FOR_PLAYERS.getValue() && bukkitServer.getOnlinePlayers().size() > 0 ) {
            if(pausedTicks == 0 || pausedTicks % pausedUpdateInterval == 0) {
                sendCallback( LangKey.INFO_PAUSE_FOR_PLAYERS.getTranslation() );
            }
            pausedTicks++;
            return;
        }

        if( !queuedRegions.isEmpty() ) {
            chunkQueue += settings.NUM_CHUNKS_PER_TICK.getValue().intValue();
            while( chunkQueue > 1 ) {
                chunkQueue--;
                chunksGenerated++;

                ChunkPos chunkPos = queuedRegions.poll();
                if( chunkPos != null ) {
                    final World world = bukkitServer.getWorld( chunkPos.getWorldUID() );
                    if( world == null ) {
                        sendCallback( LangKey.ERROR_WORLD_LOAD_FAILURE.getTranslation( chunkPos.getWorldUID() ), Permissions.RECEIVE_ERROR_MSG );
                        this.cancel();
                        return;
                    }

                    world.setKeepSpawnInMemory( true ); // load spawn chunks before checking for loaded chunks

                    // check for too many chunks loaded
                    if( getLoadedChunks( bukkitServer ) > settings.MAX_LOADED_CHUNKS.getValue().intValue() ) {
                        Object[] args = new Object[] { settings.MAX_LOADED_CHUNKS.getValue().intValue(), world.getLoadedChunks().length };
                        sendCallback( LangKey.INFO_MAX_CHUNK_AMT_REACHED.getTranslation( args ), Permissions.RECEIVE_INFO_MSG );
                        if( settings.CANCEL_TASK_ON_ERROR.getValue() ) {
                            sendCallback( LangKey.ERROR_REGION_GEN_TASK.getTranslation( chunkRegion.getName() ), Permissions.RECEIVE_ERROR_MSG );
                            this.cancel();
                            return;
                        } else {
                            bukkitServer.getWorlds().forEach( (serverWorld -> {
                                sendCallback( LangKey.INFO_UNLOAD_CHUNKS_FOR_WORLD.getTranslation( serverWorld.getName() ), Permissions.RECEIVE_INFO_MSG );
                                int numLoaded = serverWorld.getLoadedChunks().length;
                                Stream.of( serverWorld.getLoadedChunks() ).forEach( chunk -> {
                                    chunk.unload( true ); // unload all loaded chunks
                                } );
                                sendCallback( LangKey.INFO_UNLOADED_CHUNKS_FOR_WORLD.getTranslation( numLoaded, serverWorld.getName() ), Permissions.RECEIVE_INFO_MSG );
                            }) );
                        }
                    }

                    world.setAutoSave( settings.DISABLE_WORLD_SAVING.getValue() );


                    // load chunk
                    if( !world.isChunkLoaded( chunkPos.getX(), chunkPos.getZ() ) || !world.isChunkInUse( chunkPos.getX(), chunkPos.getZ() ) ) {
                        boolean loaded = world.loadChunk( chunkPos.getX(), chunkPos.getZ(), true );
                        if( !loaded ) {
                            sendCallback( LangKey.ERROR_CHUNK_LOAD_FAILURE.getTranslation( chunkPos.getX(), chunkPos.getZ() ), Permissions.RECEIVE_ERROR_MSG );
                            return;
                        }
                    }

                    double completedPercentage = 100 - ( (double)queuedRegions.size() / (double)startingSize ) * 100;

                    if( chunksGenerated % settings.UPDATE_DELAY.getValue().intValue() == 0 ) {
                        Object[] args = new Object[] { chunkPos.getX(), chunkPos.getZ(), world.getName(), String.format( "%.2f", completedPercentage ) };
                        //bukkitServer.getConsoleSender().sendMessage( LangKey.INFO_UPDATE_MESSAGE.getTranslation( args ) );
                        sendCallback( LangKey.INFO_UPDATE_MESSAGE.getTranslation( args ), Permissions.RECEIVE_INFO_MSG );
                    }

                    if( queuedRegions.peek() == null ) {
                        // completed the region; send success message
                        Object[] args = new Object[] { chunksGenerated, chunkRegion.getName() };
                        sendCallback( LangKey.INFO_GENERATION_COMPLETE.getTranslation( args ), Permissions.RECEIVE_INFO_MSG );
                        world.setAutoSave( true );
                        this.cancel();
                        return;
                    }
                }
            }
        }
    }

    private int getLoadedChunks(Server server) {
        int total = 0;
        for ( World world : server.getWorlds() ) {
            total += world.getLoadedChunks().length;
        }
        return total;
    }

    private void sendCallback(String message) {
        ChunkPreGen.log().info( message );
        if ( callback != null ) {
            callback.accept( message );
        }
    }

    private void sendCallback(String message, String permission) {
        if(callback != null ) {
            callback.accept( message, permission );
        }
    }
}
