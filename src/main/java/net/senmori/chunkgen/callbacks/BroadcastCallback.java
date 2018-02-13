package net.senmori.chunkgen.callbacks;

import org.bukkit.Bukkit;

public class BroadcastCallback implements SimpleCallback {
    @Override
    public void accept(String message) {
        Bukkit.broadcastMessage( message );
    }

    @Override
    public void accept(String message, String permission) {
        Bukkit.broadcast( message, permission );
    }
}
