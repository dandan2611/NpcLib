package fr.codinbox.npclib.api.npc.skin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.codinbox.npclib.api.npc.skin.cache.SkinCacheProvider;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a NPC skin.
 */
public interface Skin {

    /**
     * Create a new skin from a value and a signature.
     *
     * @param value the value
     * @param signature the signature. Can be null (Notchian client does not seem to care about it).
     * @return the skin
     */
    static @NotNull ValueSignatureSkin fromValueAndSignature(@NotNull String value, @Nullable String signature) {
        return new ValueSignatureSkin(value, signature);
    }

    /**
     * A default skin.
     * <br>
     * Skin is calculated from the player name and default textures.
     *
     * @return the default skin
     */
    static @Nullable ValueSignatureSkin defaultSkin() {
        return null;
    }

    /**
     * Create a skin from a player.
     *
     * @param player the player
     * @return the skin
     */
    static @Nullable ValueSignatureSkin fromPlayer(@NotNull OfflinePlayer player) {
        for (com.destroystokyo.paper.profile.ProfileProperty property : player.getPlayerProfile().getProperties()) {
            if (property.getName().equals("textures")) {
                return fromValueAndSignature(property.getValue(), property.getSignature());
            }
        }
        return null;
    }
    /**
     * Create a skin from a player UUID by querying the Mojang session server.
     * <p>
     * This method performs an HTTP request asynchronously. The provided cache is always consulted
     * first and updated after a successful fetch.
     *
     * @param uuid  the player UUID
     * @param cache the skin cache provider
     * @return a future that completes with the skin, or empty if the player has no custom skin
     */
    static @NotNull CompletableFuture<@NotNull Optional<Skin>> fromUUID(@NotNull UUID uuid, @NotNull SkinCacheProvider cache) {
        var cached = cache.get(uuid);
        if (cached.isPresent())
            return CompletableFuture.completedFuture(cached);

        return CompletableFuture.supplyAsync(() -> {
            var uuidString = uuid.toString().replace("-", "");
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder()
                    .uri(URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidString + "?unsigned=false"))
                    .GET()
                    .build();

            try {
                var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    cache.putAbsent(uuid);
                    return Optional.empty();
                }

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                JsonNode properties = root.get("properties");

                if (properties == null || !properties.isArray() || properties.isEmpty()) {
                    cache.putAbsent(uuid);
                    return Optional.empty();
                }

                for (JsonNode property : properties) {
                    if ("textures".equals(property.get("name").asText())) {
                        var value = property.get("value").asText();
                        var signatureNode = property.get("signature");
                        var signature = signatureNode != null && !signatureNode.isNull() ? signatureNode.asText() : null;
                        var skin = fromValueAndSignature(value, signature);
                        cache.put(uuid, skin);
                        return Optional.<Skin>of(skin);
                    }
                }

                cache.putAbsent(uuid);
                return Optional.empty();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch skin for UUID " + uuid, e);
            }
        });
    }

    // Note: Creating a skin from an arbitrary URL requires a third-party signing service
    // (e.g., mineskin.org) because the Notchian client verifies Mojang's signature.
    // Use such a service and pass the result to fromValueAndSignature().

    /**
     * Get the skin value.
     *
     * @return the skin value
     */
    String getValue();

    /**
     * Get the skin signature.
     *
     * @return the skin signature
     */
    String getSignature();

}
