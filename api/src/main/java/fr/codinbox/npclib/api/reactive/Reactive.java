package fr.codinbox.npclib.api.reactive;

/**
 * When working on a project where you need to keep track of changes in a value, the usage of a reactive object is
 * recommended.
 * Reactive objects helps you to keep track of changes in a value and to notify {@link Listenable} when the value
 * changed.
 * @param <T> The type of the value.
 */
public interface Reactive<T> extends Listenable<T> {

    /**
     * Retrieve the current value of the reactive object.
     * This value can be null if there is no value set.
     * @return The current value of the reactive object.
     */
    T get();

    /**
     * Set the value of the reactive object.
     * @param value The new value of the reactive object.
     */
    void set(T value);

    /**
     * Bind the reactive object to another reactive object.
     * When the value of provided reactive object is changed, the value of this reactive object will be changed too with
     * the same value.
     * @param other The reactive object to bind to.
     */
    void bind(Reactive<T> other);

    /**
     * Unbind the reactive object from another reactive object.
     * @param other The reactive object to unbind from.
     */
    void unbind(Reactive<T> other);

    /**
     * Unbind the reactive object from all bound reactive objects.
     */
    void unbindAll();

}
