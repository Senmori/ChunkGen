package net.senmori.chunkgen.callbacks;

public interface Callback<T> {

    void accept(T message);
}
