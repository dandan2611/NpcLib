package fr.codinbox.npclib.api.npc.skin.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.codinbox.npclib.api.npc.skin.Skin;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

/**
 * A file-based skin cache provider that stores one JSON file per UUID.
 * <p>
 * Each file contains: {@code {"value": "...", "signature": "...", "timestamp": 1234567890}}
 * <br>
 * Absent entries (when {@code cacheAbsent} is true): {@code {"absent": true, "timestamp": 1234567890}}
 */
public class FileCacheProvider implements SkinCacheProvider {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final Path directory;
    private final long ttlMillis;
    private final boolean cacheAbsent;

    /**
     * Create a new file-based cache provider.
     *
     * @param directory the directory to store cache files in
     * @param ttl the time-to-live for cache entries
     * @param cacheAbsent whether to cache absent (no skin found) results
     */
    public FileCacheProvider(@NotNull Path directory, @NotNull Duration ttl, boolean cacheAbsent) {
        this.directory = directory;
        this.ttlMillis = ttl.toMillis();
        this.cacheAbsent = cacheAbsent;
    }

    /**
     * Create a file-based cache provider using the plugin's data folder.
     * <p>
     * Files will be stored in {@code {dataFolder}/skins/}.
     *
     * @param plugin the plugin
     * @param ttl the time-to-live for cache entries
     * @param cacheAbsent whether to cache absent (no skin found) results
     * @return the cache provider
     */
    public static @NotNull FileCacheProvider forPlugin(@NotNull Plugin plugin, @NotNull Duration ttl, boolean cacheAbsent) {
        return new FileCacheProvider(plugin.getDataFolder().toPath().resolve("skins"), ttl, cacheAbsent);
    }

    private @NotNull Path fileFor(@NotNull UUID key) {
        return directory.resolve(key.toString() + ".json");
    }

    @Override
    public @NotNull Optional<Skin> get(@NotNull UUID key) {
        var file = fileFor(key);
        if (!Files.exists(file))
            return Optional.empty();

        try {
            var node = MAPPER.readTree(file.toFile());
            var timestamp = node.get("timestamp").asLong();
            if (System.currentTimeMillis() - timestamp > ttlMillis) {
                Files.deleteIfExists(file);
                return Optional.empty();
            }

            if (node.has("absent") && node.get("absent").asBoolean())
                return Optional.empty();

            var value = node.get("value").asText();
            var signatureNode = node.get("signature");
            var signature = signatureNode != null && !signatureNode.isNull() ? signatureNode.asText() : null;
            return Optional.of(Skin.fromValueAndSignature(value, signature));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Check if a key is cached, including absent entries.
     *
     * @param key the key
     * @return true if the key is in the cache and not expired
     */
    public boolean contains(@NotNull UUID key) {
        var file = fileFor(key);
        if (!Files.exists(file))
            return false;

        try {
            var node = MAPPER.readTree(file.toFile());
            var timestamp = node.get("timestamp").asLong();
            if (System.currentTimeMillis() - timestamp > ttlMillis) {
                Files.deleteIfExists(file);
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void put(@NotNull UUID key, @NotNull Skin value) {
        try {
            Files.createDirectories(directory);
            ObjectNode node = MAPPER.createObjectNode();
            node.put("value", value.getValue());
            node.put("signature", value.getSignature());
            node.put("timestamp", System.currentTimeMillis());
            MAPPER.writeValue(fileFor(key).toFile(), node);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write skin cache file", e);
        }
    }

    @Override
    public void putAbsent(@NotNull UUID key) {
        if (!cacheAbsent)
            return;

        try {
            Files.createDirectories(directory);
            ObjectNode node = MAPPER.createObjectNode();
            node.put("absent", true);
            node.put("timestamp", System.currentTimeMillis());
            MAPPER.writeValue(fileFor(key).toFile(), node);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write skin cache file", e);
        }
    }

    @Override
    public void invalidate(@NotNull UUID key) {
        try {
            Files.deleteIfExists(fileFor(key));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete skin cache file", e);
        }
    }

    @Override
    public void clear() {
        try (var stream = Files.list(directory)) {
            stream.filter(p -> p.toString().endsWith(".json"))
                  .forEach(p -> {
                      try {
                          Files.delete(p);
                      } catch (IOException e) {
                          throw new RuntimeException("Failed to delete skin cache file", e);
                      }
                  });
        } catch (IOException e) {
            // Directory may not exist yet
        }
    }

}
