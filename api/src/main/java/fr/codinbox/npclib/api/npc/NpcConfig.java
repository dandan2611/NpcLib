package fr.codinbox.npclib.api.npc;

import fr.codinbox.npclib.api.npc.skin.Skin;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public final class NpcConfig {

    /**
     * The NPC location.
     */
    @NotNull
    private final Location location;

    /**
     * The NPC skin.
     */
    @NotNull
    private final Skin skin;

    private boolean global = true;

    private Integer renderDistance = 32;

    private String name = "§7";

    private NpcConfig(@NotNull Location location, @NotNull Skin skin) {
        this.location = location;
        this.skin = skin;
    }

    public static NpcConfig create(@NotNull Location location, @NotNull Skin skin) {
        return new NpcConfig(location, skin);
    }

    public @NotNull NpcConfig setGlobal(boolean global) {
        this.global = global;
        return this;
    }

    public @NotNull NpcConfig setRenderDistance(int renderDistance) {
        this.renderDistance = renderDistance;
        return this;
    }

    public @NotNull NpcConfig setName(@NotNull String name) {
        this.name = name;
        return this;
    }

    public @NotNull Location getLocation() {
        return location;
    }

    public @NotNull Skin getSkin() {
        return skin;
    }

    public boolean isGlobal() {
        return global;
    }

    public @NotNull Integer getRenderDistance() {
        return renderDistance;
    }

    public @NotNull String getName() {
        return name;
    }

}
