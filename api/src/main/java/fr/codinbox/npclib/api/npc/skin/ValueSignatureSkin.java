package fr.codinbox.npclib.api.npc.skin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ValueSignatureSkin implements Skin {

    private final @NotNull String value;
    private final @Nullable String signature;

    protected ValueSignatureSkin(@NotNull String value, @Nullable String signature) {
        this.value = value;
        this.signature = signature;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getSignature() {
        return this.signature;
    }

}
