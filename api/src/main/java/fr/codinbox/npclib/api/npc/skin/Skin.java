package fr.codinbox.npclib.api.npc.skin;

import org.jetbrains.annotations.NotNull;

public interface Skin {

    /**
     * Create a new skin from a value and a signature.
     *
     * @param value the value
     * @param signature the signature
     * @return the skin
     */
    @NotNull
    static ValueSignatureSkin fromValueAndSignature(@NotNull String value, @NotNull String signature) {
        return new ValueSignatureSkin(value, signature);
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
