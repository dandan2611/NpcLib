package fr.codinbox.npclib.api.npc.skin.cache;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A generic cache provider.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface CacheProvider<K, V> {

    /**
     * Get a cached value.
     *
     * @param key the key
     * @return the cached value, or empty if not cached or expired
     */
    @NotNull Optional<V> get(@NotNull K key);

    /**
     * Store a value in the cache.
     *
     * @param key the key
     * @param value the value
     */
    void put(@NotNull K key, @NotNull V value);

    /**
     * Store an absent entry in the cache (negative caching).
     * Only has effect if the provider was created with {@code cacheAbsent = true}.
     *
     * @param key the key
     */
    void putAbsent(@NotNull K key);

    /**
     * Remove an entry from the cache.
     *
     * @param key the key
     */
    void invalidate(@NotNull K key);

    /**
     * Remove all entries from the cache.
     */
    void clear();

}
