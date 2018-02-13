package net.senmori.chunkgen.configuration;

import net.senmori.senlib.configuration.ConfigManager;
import net.senmori.senlib.configuration.option.BooleanOption;
import net.senmori.senlib.configuration.option.NumberOption;
import net.senmori.senlib.configuration.option.StringOption;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Settings extends ConfigManager {

    public final BooleanOption PAUSE_FOR_PLAYERS;
    public final BooleanOption PREVENT_PLAYER_JOIN;
    public final NumberOption NUM_CHUNKS_PER_TICK;

    public final NumberOption UPDATE_DELAY;
    public final NumberOption MAX_LOADED_CHUNKS;
    public final BooleanOption DISABLE_WORLD_SAVING;
    public final BooleanOption CANCEL_TASK_ON_ERROR;

    public Settings(JavaPlugin plugin, File configFile) {
        super( plugin, configFile );
        // Players
        PAUSE_FOR_PLAYERS = this.registerOption( "Pause for Players", new BooleanOption( "players.pause-for-online-players", false, "Pause chunk generation when players are online." ) );
        PREVENT_PLAYER_JOIN = this.registerOption( "Prevent Player Joins", new BooleanOption( "players.prevent-joins", true, "Prevent players logging in while generating chunks." ) );
        NUM_CHUNKS_PER_TICK = this.registerOption( "Number of Chunks Per Tick", new NumberOption( "settings.chunks-per-tick", 1, "Number of chunks loaded per tick." ) );

        // Settings
        UPDATE_DELAY = this.registerOption( "Update Delay", new NumberOption( "settings.update-delay", 40, "Number of chunks between percentage updates." ) );
        MAX_LOADED_CHUNKS = this.registerOption( "Max Loaded Chunks", new NumberOption( "settings.max-chunks-loaded", 3000, "Pause generation if more chunks than this are loaded into memory." ) );
        DISABLE_WORLD_SAVING = this.registerOption( "Disable World Saving", new BooleanOption( "settings.disable-world-saving", true, "Disable world saving while generating chunks." ) );
        CANCEL_TASK_ON_ERROR = this.registerOption( "Cancel Task on Error", new BooleanOption( "settings.cancel-task-on-error", false, "Cancel the generation task when there is any error." ) );

        load( getConfig() );
    }

    @Override
    public boolean load(FileConfiguration configuration) {
        getOptions().values().stream().forEach( option -> option.load( configuration ) );
        return true;
    }

    @Override
    public boolean save(FileConfiguration configuration) {
        getOptions().values().stream().allMatch( option -> option.save( configuration ) );
        return true;
    }

    public boolean reload() {
        return this.save( getConfig() ) && this.load( getConfig() );
    }
}
