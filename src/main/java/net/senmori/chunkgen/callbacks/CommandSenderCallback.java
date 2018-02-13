package net.senmori.chunkgen.callbacks;

import org.bukkit.command.CommandSender;

public class CommandSenderCallback implements SimpleCallback {

    private final CommandSender commandSender;

    public CommandSenderCallback(CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    public void accept(String message) {
        commandSender.sendMessage( message );
    }

    @Override
    public void accept(String message, String permission) {
        commandSender.sendMessage( message );
    }
}
