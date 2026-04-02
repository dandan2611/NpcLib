package fr.codinbox.npclib.api.npc.skin.cache;

import fr.codinbox.npclib.api.npc.skin.Skin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An in-memory skin cache provider backed by a {@link ConcurrentHashMap}.
 */
public class MemoryCacheProvider implements SkinCacheProvider {

    private final ConcurrentHashMap<UUID, CacheEntry> cache = new ConcurrentHashMap<>();
    private final long ttlMillis;
    private final boolean cacheAbsent;

    /**
     * Create a new in-memory cache provider.
     *
     * @param ttl the time-to-live for cache entries
     * @param cacheAbsent whether to cache absent (no skin found) results
     */
    public MemoryCacheProvider(@NotNull Duration ttl, boolean cacheAbsent) {
        this.ttlMillis = ttl.toMillis();
        this.cacheAbsent = cacheAbsent;
    }

    @Override
    public @NotNull Optional<Skin> get(@NotNull UUID key) {
        var entry = cache.get(key);
        if (entry == null)
            return Optional.empty();
        if (System.currentTimeMillis() - entry.timestamp > ttlMillis) {
            cache.remove(key);
            return Optional.empty();
        }
        if (entry.skin == null)
            return Optional.empty();
        return Optional.of(entry.skin);
    }

    /**
     * Check if a key is cached, including absent entries.
     *
     * @param key the key
     * @return true if the key is in the cache and not expired
     */
    public boolean contains(@NotNull UUID key) {
        var entry = cache.get(key);
        if (entry == null)
            return false;
        if (System.currentTimeMillis() - entry.timestamp > ttlMillis) {
            cache.remove(key);
            return false;
        }
        return true;
    }

    @Override
    public void put(@NotNull UUID key, @NotNull Skin value) {
        cache.put(key, new CacheEntry(value, System.currentTimeMillis()));
    }

    @Override
    public void putAbsent(@NotNull UUID key) {
        if (cacheAbsent)
            cache.put(key, new CacheEntry(null, System.currentTimeMillis()));
    }

    @Override
    public void invalidate(@NotNull UUID key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    private record CacheEntry(@Nullable Skin skin, long timestamp) {
    }

}
