package fr.codinbox.npclib.core.impl.reactive;

import fr.codinbox.npclib.api.reactive.Reactive;
import fr.codinbox.npclib.api.reactive.ReactiveListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ReactiveImpl<T> implements Reactive<T> {

    private T value;
    private final Set<ReactiveListener<T>> listenerList;
    private final HashMap<Reactive<T>, ReactiveListener<T>> bindListenerMap;

    public ReactiveImpl(T value) {
        this.value = value;
        this.listenerList = new HashSet<>();
        this.bindListenerMap = new HashMap<>();
    }

    public ReactiveImpl(Reactive<T> other) {
        this.value = other.get();
        this.listenerList = new HashSet<>();
        this.bindListenerMap = new HashMap<>();
        bind(other);
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(T value) {
        T oldValue = this.value;
        this.value = value;
        for (ReactiveListener<T> listener : listenerList) {
            listener.onChange(this, oldValue, value);
        }
    }

    @Override
    public void addListener(ReactiveListener<T> listener) {
        listenerList.add(listener);
    }

    @Override
    public void removeListener(ReactiveListener<T> listener) {
        listenerList.remove(listener);
    }

    @Override
    public void bind(Reactive<T> other) {
        ReactiveListener<T> listener = bindListenerMap.computeIfAbsent(other, k ->
                (reactive, previousValue, newValue) -> set(newValue));
        other.addListener(listener);
        set(other.get());
    }

    @Override
    public void unbind(Reactive<T> other) {
        ReactiveListener<T> listener = bindListenerMap.remove(other);
        if (listener != null)
            other.removeListener(listener);
    }

    @Override
    public void unbindAll() {
        for (Reactive<T> other : bindListenerMap.keySet())
            unbind(other);
    }
}
