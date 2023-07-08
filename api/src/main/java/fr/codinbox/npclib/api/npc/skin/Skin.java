package fr.codinbox.npclib.api.npc.skin;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    // TODO: Add a method to create a skin from a player UUID.
    // TODO: Add a method to create a skin from a skin URL.

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
