package net.senmori.chunkgen.language;

import com.google.common.collect.Maps;
import net.senmori.senlib.translation.AbstractLanguageMap;
import net.senmori.senlib.translation.FileType;
import net.senmori.senlib.translation.LanguageLoader;
import net.senmori.senlib.translation.LanguageMap;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public class LanguageHandler {
    private static final LanguageHandler INSTANCE = new LanguageHandler();
    private AbstractLanguageMap rootLocale; // en_US
    private AbstractLanguageMap currentServerLocale;
    private Map<String, LanguageMap> loadedLocales = Maps.newHashMap();

    private LanguageHandler() {
    }

    public static LanguageHandler getInstance() {
        return INSTANCE;
    }

    public void init() {
        rootLocale = LanguageLoader.loadLanguage( FileType.JSON );
        rootLocale.loadLanguage( this.getClass().getResourceAsStream( "/lang/en_US.json" ) );
    }

    public void setCurrentServerLocale(File localeFile) {
        this.currentServerLocale = loadLangMap( localeFile );
        currentServerLocale.loadLanguage( currentServerLocale.parseInput( localeFile ) );
    }

    public void setServerLocale(AbstractLanguageMap locale) {
        this.currentServerLocale = locale;
    }

    public AbstractLanguageMap getServerLocale() {
        return currentServerLocale != null ? currentServerLocale : rootLocale;
    }

    public void loadLocale(File localeFile) {
        String fileName = FilenameUtils.removeExtension( localeFile.getName() );
        if ( fileName.isEmpty() ) {
            throw new IllegalArgumentException( "Locale file cannot have empty file name." );
        }
        LanguageMap map = loadLangMap( localeFile );
        map.loadLanguage( map.parseInput( localeFile ) );
        loadedLocales.put( FilenameUtils.removeExtension( localeFile.getName() ), map );
    }

    public boolean removeLocale(String name) {
        return loadedLocales.remove( name ) != null;
    }

    private AbstractLanguageMap loadLangMap(File file) {
        return LanguageLoader.loadLanguage( getFileType( file ) );
    }

    private FileType getFileType(File file) {
        FileType type = LanguageLoader.getLanguageFileType( FilenameUtils.getExtension( file.getName() ) );
        if(type == null) {
            throw new IllegalArgumentException( "Unknown file type for LanguageFileType (" + file.getAbsolutePath() + ")");
        }
        return type;
    }
}
