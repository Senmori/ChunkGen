package net.senmori.chunkgen.language;

public enum LangKey {
    LANG_CODE("language.code"),
    LANG_REGION("language.region"),
    LANG_NAME("language.name"),
    INFO_GENERATION_COMPLETE("chunk.info.chunkGenerationComplete"),
    /** Chunk generated at [X:{0} Z:{1}][World:{2}] : {3}% completed. */
    INFO_UPDATE_MESSAGE("chunk.info.updateMessage"),
    INFO_PAUSE_FOR_PLAYERS("chunk.info.pauseForPlayers"),
    INFO_MAX_CHUNK_AMT_REACHED("chunk.info.maxChunkAmountReached"),
    INFO_UNLOAD_CHUNKS_FOR_WORLD("chunk.info.unloadChunksForWorld"),
    INFO_UNLOADED_CHUNKS_FOR_WORLD("chunk.info.unloadedChunksForWorld"),
    INFO_LOGIN_MESSAGE_DENY("login.message.deny"),
    INFO_CHUNK_GENERATION_KICK_MSG("chunk.info.chunkGenerationKickMessage"),
    ERROR_WORLD_LOAD_FAILURE("chunk.error.worldLoadFailure"),
    ERROR_CHUNK_LOAD_FAILURE("chunk.error.chunkLoadFailure"),
    ERROR_REGION_GEN_TASK("chunk.error.cancelTaskForRegion"),
    ERROR_INVALID_REGION_ARGS("command.error.invalidRegionArgs");

    private final String key;
    private LangKey(String code) {
        this.key = code;
    }

    public String getKey() {
        return this.key;
    }

    public String getTranslation() {
        return LanguageHandler.getInstance().getServerLocale().translate( this.key, true );
    }

    public String getTranslation(Object... args) {
        return LanguageHandler.getInstance().getServerLocale().translate( this.key, true, args );
    }
}
