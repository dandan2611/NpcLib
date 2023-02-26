package fr.codinbox.npclib.api.npc.skin;

import org.jetbrains.annotations.NotNull;

public interface Skin {

    @NotNull
    static LegacySkin fromValueAndSignature(@NotNull String value, @NotNull String signature) {
        return new LegacySkin(value, signature);
    }

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
