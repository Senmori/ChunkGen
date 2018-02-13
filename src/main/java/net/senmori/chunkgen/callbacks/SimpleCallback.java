package net.senmori.chunkgen.callbacks;

public interface SimpleCallback extends Callback<String> {

    void accept(String message);

    void accept(String message, String permission);
}
