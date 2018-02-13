package net.senmori.chunkgen;

import net.senmori.acf.BukkitCommandManager;
import net.senmori.chunkgen.chunk.shape.Shape;
import net.senmori.chunkgen.chunk.shape.ShapeHandler;
import net.senmori.chunkgen.command.ChunkGenCommand;
import net.senmori.chunkgen.configuration.Settings;
import net.senmori.chunkgen.language.LangKey;
import net.senmori.chunkgen.language.LanguageHandler;
import net.senmori.chunkgen.listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ChunkPreGen extends JavaPlugin {
    public static final boolean DEBUG = true;

    private static ChunkPreGen INSTANCE;

    private Settings settings;
    private BukkitCommandManager manager;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        debug( "Debug mode enabled! Was this done on purpose? If not then you really messed up." );

        getConfig().options().copyDefaults( true );
        getConfig().options().copyHeader( true );
        this.saveDefaultConfig();

        settings = new Settings( this, new File( this.getDataFolder(), "config.yml" ) );

        LanguageHandler.getInstance().init(); // load translations

        this.getServer().getPluginManager().registerEvents( new PlayerListener( this ), this );

        manager = new BukkitCommandManager( this );
        registerCommands();
        manager.registerCommand( new ChunkGenCommand() );

        debug( LangKey.INFO_UPDATE_MESSAGE.getTranslation( 12, -7, "world", "12.34" ) );
    }


    private void registerCommands() {
        manager.getCommandContexts().registerIssuerAwareContext( Shape.class, (c) -> {
            String name = c.getFirstArg();
            if(name != null) {
                   return ShapeHandler.getShape(name);
            }
            return Shape.SQUARE;
        } );

        manager.getCommandCompletions().registerAsyncCompletion( "shapes", (c) -> {
            return ShapeHandler.getShapes().stream().map( Shape::getName ).collect( Collectors.toList() );
        } );
    }

    public Settings getSettings() {
        return  settings;
    }

    public static Logger log() {
        return ChunkPreGen.instance().getLogger();
    }

    public static void debug(String message) {
        if(DEBUG) {
            log().info( "[Debug] " + "[" + instance().getDescription().getPrefix() + "] " + message);
        }
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    public static ChunkPreGen instance() {
        return INSTANCE;
    }
}
