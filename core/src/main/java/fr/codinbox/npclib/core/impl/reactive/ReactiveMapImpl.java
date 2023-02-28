package fr.codinbox.npclib.core.impl.reactive;

import fr.codinbox.npclib.api.reactive.Reactive;
import fr.codinbox.npclib.api.reactive.ReactiveListener;
import fr.codinbox.npclib.api.reactive.ReactiveMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ReactiveMapImpl<K, V> implements ReactiveMap<K, V> {

    private final Map<K, V> map;
    private final Set<ReactiveListener<Map<K, V>>> listenerList;
    private final HashMap<Reactive<Map<K, V>>, ReactiveListener<Map<K, V>>> bindListenerMap;

    public ReactiveMapImpl(Map<K, V> map) {
        this.map = map;
        this.listenerList = new HashSet<>();
        this.bindListenerMap = new HashMap<>();
    }

    @Override
    public void addListener(ReactiveListener<Map<K, V>> listener) {
        this.listenerList.add(listener);
    }

    @Override
    public void removeListener(ReactiveListener<Map<K, V>> listener) {
        this.listenerList.remove(listener);
    }

    @Override
    public Map<K, V> get() {
        return this.map;
    }

    @Override
    public void set(Map<K, V> value) {
        Map<K, V> oldValue = this.map;
        this.map.clear();
        this.map.putAll(value);
        for (ReactiveListener<Map<K, V>> listener : listenerList) {
            listener.onChange(this, oldValue, value);
        }
    }

    @Override
    public void set(Map<K, V> value, boolean notify) {
        if (notify)
            set(value);
        else {
            this.map.clear();
            this.map.putAll(value);
        }
    }

    @Override
    public void bind(Reactive<Map<K, V>> other) {
        ReactiveListener<Map<K, V>> listener = (reactive, oldValue, newValue) -> this.set(newValue);
        this.bindListenerMap.put(other, listener);
        other.addListener(listener);
        set(other.get());
    }

    @Override
    public void unbind(Reactive<Map<K, V>> other) {
        ReactiveListener<Map<K, V>> listener = this.bindListenerMap.get(other);
        if (listener != null) {
            other.removeListener(listener);
            this.bindListenerMap.remove(other);
        }
    }

    @Override
    public void unbindAll() {
        for (Map.Entry<Reactive<Map<K, V>>, ReactiveListener<Map<K, V>>> entry : this.bindListenerMap.entrySet()) {
            entry.getKey().removeListener(entry.getValue());
        }
        this.bindListenerMap.clear();
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return this.map.get(key);
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        Map<K, V> newMap = new HashMap<>(this.map);
        V oldValue = newMap.put(key, value);
        this.set(newMap);
        return oldValue;
    }

    @Override
    public V remove(Object key) {
        Map<K, V> newMap = new HashMap<>(this.map);
        V oldValue = newMap.remove(key);
        this.set(newMap);
        return oldValue;
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        Map<K, V> newMap = new HashMap<>(this.map);
        newMap.putAll(m);
        this.set(newMap);
    }

    @Override
    public void clear() {
        this.set(new HashMap<>());
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return this.map.keySet();
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return this.map.values();
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }

}
