package fr.codinbox.npclib.api.reactive;

/**
 * A Reactive Listener is a listener bound to a {@link Reactive} object.
 * @param <T> The type of the reactive object.
 */
public interface ReactiveListener<T> {

    /**
     * The onChange method is called when the reactive object value is changed.
     * @param reactive The reactive object the listener is bound to.
     * @param previousValue The previous value of the reactive object.
     * @param newValue The new value of the reactive object.
     */
    void onChange(Reactive<T> reactive, T previousValue, T newValue);

}
