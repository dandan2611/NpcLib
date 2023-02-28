package fr.codinbox.npclib.core.impl.reactive;

import fr.codinbox.npclib.api.reactive.Reactive;
import fr.codinbox.npclib.api.reactive.ReactiveList;
import fr.codinbox.npclib.api.reactive.ReactiveListener;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class ReactiveListImpl<T> extends ArrayList<T> implements ReactiveList<T> {

    private final Set<ReactiveListener<List<T>>> listenerList;

    public ReactiveListImpl() {
        this.listenerList = new HashSet<>();
        HashMap<Reactive<List<T>>, ReactiveListener<List<T>>> bindListenerMap = new HashMap<>();
    }

    @Override
    public boolean add(T t) {
        ArrayList<T> oldValue = new ArrayList<>(this);
        boolean result = super.add(t);
        for (ReactiveListener<List<T>> listener : listenerList) {
            listener.onChange(this, oldValue, this);
        }
        return result;
    }

    @Override
    public boolean remove(Object o) {
        ArrayList<T> oldValue = new ArrayList<>(this);
        boolean result = super.remove(o);
        for (ReactiveListener<List<T>> listener : listenerList) {
            listener.onChange(this, oldValue, this);
        }
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        ArrayList<T> oldValue = new ArrayList<>(this);
        boolean result = super.addAll(c);
        for (ReactiveListener<List<T>> listener : listenerList) {
            listener.onChange(this, oldValue, this);
        }
        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        ArrayList<T> oldValue = new ArrayList<>(this);
        boolean result = super.addAll(index, c);
        for (ReactiveListener<List<T>> listener : listenerList) {
            listener.onChange(this, oldValue, this);
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        ArrayList<T> oldValue = new ArrayList<>(this);
        boolean result = super.removeAll(c);
        for (ReactiveListener<List<T>> listener : listenerList) {
            listener.onChange(this, oldValue, this);
        }
        return result;
    }

    @Override
    public void clear() {
        ArrayList<T> oldValue = new ArrayList<>(this);
        super.clear();
        for (ReactiveListener<List<T>> listener : listenerList) {
            listener.onChange(this, oldValue, this);
        }
    }

    @Override
    public T set(int index, T element) {
        ArrayList<T> oldValue = new ArrayList<>(this);
        T result = super.set(index, element);
        for (ReactiveListener<List<T>> listener : listenerList) {
            listener.onChange(this, oldValue, this);
        }
        return result;
    }

    @Override
    public void add(int index, T element) {
        ArrayList<T> oldValue = new ArrayList<>(this);
        super.add(index, element);
        for (ReactiveListener<List<T>> listener : listenerList) {
            listener.onChange(this, oldValue, this);
        }
    }

    @Override
    public T remove(int index) {
        ArrayList<T> oldValue = new ArrayList<>(this);
        T result = super.remove(index);
        for (ReactiveListener<List<T>> listener : listenerList) {
            listener.onChange(this, oldValue, this);
        }
        return result;
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        ArrayList<T> oldValue = new ArrayList<>(this);
        super.replaceAll(operator);
        for (ReactiveListener<List<T>> listener : listenerList) {
            listener.onChange(this, oldValue, this);
        }
    }

    @Override
    public void sort(Comparator<? super T> c) {
        ArrayList<T> oldValue = new ArrayList<>(this);
        super.sort(c);
        for (ReactiveListener<List<T>> listener : listenerList) {
            listener.onChange(this, oldValue, this);
        }
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        ArrayList<T> oldValue = new ArrayList<>(this);
        boolean result = super.removeIf(filter);
        for (ReactiveListener<List<T>> listener : listenerList) {
            listener.onChange(this, oldValue, this);
        }
        return result;
    }

    @Override
    public void addListener(ReactiveListener<List<T>> listener) {
        listenerList.add(listener);
    }

    @Override
    public void removeListener(ReactiveListener<List<T>> listener) {
        listenerList.remove(listener);
    }

    @Override
    public List<T> get() {
        return this;
    }

    @Override
    public void set(List<T> value) {
        ArrayList<T> oldValue = new ArrayList<>(this);
        super.clear();
        super.addAll(value);
        for (ReactiveListener<List<T>> listener : listenerList) {
            listener.onChange(this, oldValue, this);
        }
    }

    @Override
    public T set(int index, T element, boolean notify) {
        if (notify) {
            return set(index, element);
        } else {
            return super.set(index, element);
        }
    }

    @Override
    public void set(List<T> value, boolean notify) {
        if (notify) {
            set(value);
        } else {
            super.clear();
            super.addAll(value);
        }
    }

    @Override
    public void bind(Reactive<List<T>> other) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void unbind(Reactive<List<T>> other) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void unbindAll() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Stream<T> stream() {
        return super.stream();
    }

}
