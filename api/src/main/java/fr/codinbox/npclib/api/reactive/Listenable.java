package fr.codinbox.npclib.api.reactive;

/**
 * A Listenable is an object that can be listened to.
 * @param <T> The type of the listenable object.
 */
public interface Listenable<T> {

    void addListener(ReactiveListener<T> listener);

    void removeListener(ReactiveListener<T> listener);

}
