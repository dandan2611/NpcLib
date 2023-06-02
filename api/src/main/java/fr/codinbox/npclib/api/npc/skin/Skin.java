package fr.codinbox.npclib.api.npc.skin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    // TODO: Add a method to create a skin from a player name.
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
