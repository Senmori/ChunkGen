package net.senmori.chunkgen.command;

import net.senmori.acf.BaseCommand;
import net.senmori.acf.InvalidCommandArgument;
import net.senmori.acf.annotation.CommandAlias;
import net.senmori.acf.annotation.CommandPermission;
import net.senmori.acf.annotation.Default;
import net.senmori.acf.annotation.Description;
import net.senmori.acf.annotation.Optional;
import net.senmori.acf.annotation.Single;
import net.senmori.acf.annotation.Subcommand;
import net.senmori.acf.annotation.Values;
import net.senmori.chunkgen.ChunkPreGen;
import net.senmori.chunkgen.Permissions;
import net.senmori.chunkgen.chunk.ChunkPos;
import net.senmori.chunkgen.chunk.ChunkRegion;
import net.senmori.chunkgen.chunk.ChunkRegionHandler;
import net.senmori.chunkgen.chunk.ShapedChunkRegion;
import net.senmori.chunkgen.chunk.shape.Shape;
import net.senmori.chunkgen.configuration.Settings;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias( "cg|chunkgen" )
@CommandPermission( Permissions.CMD )
public class ChunkGenCommand extends BaseCommand {

    private ChunkPreGen plugin = ChunkPreGen.instance();
    private Settings settings = plugin.getSettings();

    public ChunkGenCommand() {}

    @Default
    @Subcommand( "help" )
    @CommandPermission( Permissions.CMD_HELP )
    @Description( "Displays all the sub-commands for the chunkgen command" )
    public void onDefault(CommandSender sender) {

    }

    /*
     * Create a persistent region.
     * /cg create <regionName> <x> <z> <radius> <shape>
     */
    @Subcommand( "create" )
    @CommandPermission( Permissions.CMD_CREATE_REGION )
    public void createRegion(CommandSender sender, String regionName, String chunkX, String chunkZ, int radius, Shape shape, @Optional World world) {

        int cx = 0;
        int cz = 0;
        if( NumberUtils.isParsable( chunkX ) ) {
            cx = NumberUtils.toInt( chunkX );
        }
        if( NumberUtils.isParsable( chunkZ ) ) {
            cz = NumberUtils.toInt( chunkZ );
        }
        if(sender instanceof Player) {
            Player player = (Player)sender;
            if ( chunkX.equals( "~" ) ) {
                cx = player.getLocation().getBlockX() >> 4;
            }
            if ( chunkZ.equals( "~" ) ) {
                cz = player.getLocation().getBlockZ() >> 4;
            }
        }

        UUID worldUUID = null;
        if(world != null ) {
            worldUUID = world.getUID();
        } else if(sender instanceof Player) {
            worldUUID = ((Player)sender).getWorld().getUID();
        } else {
            worldUUID = Bukkit.getWorlds().get( 0 ).getUID();
        }

        ChunkPos center = new ChunkPos( cx, cz, worldUUID );

        ChunkRegion region = new ShapedChunkRegion( regionName, shape, center, radius);
        ChunkRegionHandler.getInstance().addChunkRegion( region );

        sender.sendMessage( "Created region " + region.getName() + " starting from [" + region.getCenter().getX() + ", " + region.getCenter().getZ() +  "] with a radius of " + region.getRadius() + ".");
        sender.sendMessage( "Region " + region.getName() + " has " + region.getRegion().size() + " chunk(s).");
    }

    @Subcommand( "load" )
    @CommandPermission( Permissions.CMD_START )
    public void startLoad(CommandSender sender) {
        ChunkRegionHandler.getInstance().start();
    }

}

