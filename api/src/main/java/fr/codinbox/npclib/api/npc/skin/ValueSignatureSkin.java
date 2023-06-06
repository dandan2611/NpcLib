package fr.codinbox.npclib.api.npc.skin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Skin with a value and a signature.
 */
public class ValueSignatureSkin implements Skin {

    private final @NotNull String value;
    private final @Nullable String signature;

    /**
     * Create a new skin from a value and a signature.
     *
     * @param value the value
     * @param signature the signature. Can be null (Notchian client does not seem to care about it).
     */
    protected ValueSignatureSkin(@NotNull String value, @Nullable String signature) {
        this.value = value;
        this.signature = signature;
    }

    /**
     * Get the skin value.
     *
     * @return the skin value
     */
    @Override
    public @NotNull String getValue() {
        return this.value;
    }

    /**
     * Get the skin signature.
     *
     * @return the skin signature
     */
    @Override
    public @Nullable String getSignature() {
        return this.signature;
    }

}
