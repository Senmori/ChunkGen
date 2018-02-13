package net.senmori.chunkgen;

public final class Permissions {

    private Permissions() {}

    /** Base command permission */
    public static final String CMD = "cg.command";

    /** Help command permission */
    public static final String CMD_HELP = "cg.command.help";

    /** Create a chunk region permission */
    public static final String CMD_CREATE_REGION = "cg.command.create";

    /** Start chunk generation permission */
    public static final String CMD_START = "cg.command.start";

    /** Base permission for config subcommand */
    public static final String CMD_CONFIG = "cg.command.config";

    /** Permission that the player can receive error messages. Error messages are always displayed in the console. */
    public static final String RECEIVE_ERROR_MSG = "cg.messages.error";

    /** Permission that the player can receive information messages. Information messages are always displayed in the console */
    public static final String RECEIVE_INFO_MSG = "cg.messages.info";

    public static final String LOGIN_DURING_CHUNK_GEN = "cg.login.generation.allow";
}
