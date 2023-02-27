package fr.codinbox.npclib.api.reactive;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * A Reactive List is a list that can be listened to.
 * @param <T> the type of the list
 */
public interface ReactiveList<T> extends Reactive<List<T>>, Cloneable, Listenable<List<T>> {

    /**
     * Add an element to the list.
     * @param t The element to add
     * @return true if this collection changed as a result of the call
     */
    boolean add(T t);

    /**
     * Remove an element from the list.
     * @param o element to be removed from this list, if present
     * @return true if this collection changed as a result of the call
     */
    boolean remove(Object o);

    /**
     * Add all elements from a collection to the list.
     * @param c collection containing elements to be added to this collection
     * @return true if this collection changed as a result of the call
     */
    boolean addAll(Collection<? extends T> c);

    /**
     * Add all elements from a collection from the list.
     * @param index index at which to insert the first element from the
     *              specified collection.
     * @param c collection containing elements to be added to this list
     * @return true if this collection changed as a result of the call
     */
    boolean addAll(int index, Collection<? extends T> c);

    /**
     * Remove all elements from a collection from the list.
     * @param c collection containing elements to be removed from this list
     * @return true if this collection changed as a result of the call
     */
    boolean removeAll(Collection<?> c);

    /**
     * Remove all elements from the list.
     */
    void clear();

    T set(int index, T element);

    /**
     * Add an element to the list at the specified index.
     * @param index the index at which the specified element is to be inserted
     * @param element the element to add
     */
    void add(int index, T element);

    /**
     * Remove the element at the specified index.
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     */
    T remove(int index);

    /**
     * Replace all elements of this list with the specified element.
     * @param operator the operator to apply to each element
     */
    void replaceAll(UnaryOperator<T> operator);

    /**
     * Sort the list using the specified comparator.
     * @param c the {@code Comparator} used to compare list elements.
     *          A {@code null} value indicates that the elements'
     *          {@linkplain Comparable natural ordering} should be used
     */
    void sort(Comparator<? super T> c);

    /**
     * Remove all elements that match the specified predicate.
     * @param filter a predicate which returns {@code true} for elements to be
     *        removed
     * @return {@code true} if any elements were removed
     */
    boolean removeIf(Predicate<? super T> filter);

    /**
     * Get a stream of the list.
     *
     * @return a stream of the list
     */
    @NotNull
    default Stream<T> stream() {
        return get().stream();
    }

}
