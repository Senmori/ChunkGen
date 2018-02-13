package net.senmori.chunkgen.listener;

import net.senmori.chunkgen.ChunkPreGen;
import net.senmori.chunkgen.Permissions;
import net.senmori.chunkgen.chunk.ChunkRegionHandler;
import net.senmori.chunkgen.configuration.Settings;
import net.senmori.chunkgen.language.LangKey;
import net.senmori.chunkgen.language.LanguageHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private ChunkPreGen plugin;
    private Settings settings;

    public PlayerListener(ChunkPreGen plugin) {
        this.plugin = plugin;
        this.settings = plugin.getSettings();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void prePlayerJoin(PlayerJoinEvent e) {
        if ( ChunkRegionHandler.getInstance().isRunning() && !e.getPlayer().hasPermission( Permissions.LOGIN_DURING_CHUNK_GEN )) {
            e.getPlayer().kickPlayer( LangKey.INFO_LOGIN_MESSAGE_DENY.getTranslation() );
        }
    }
}
