package fr.codinbox.npclib.api.npc;

import fr.codinbox.npclib.api.npc.skin.Skin;
import fr.codinbox.npclib.api.npc.viewer.NpcRenderLogic;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a NPC configuration.
 * <br>
 * A NPC configuration is used to create a {@link Npc}.
 */
public final class NpcConfig {

    /**
     * The NPC location.
     */
    @NotNull
    private final Location location;

    /**
     * The NPC skin.
     */
    private final @Nullable Skin skin;

    /**
     * Whether the NPC is global or not.
     * <br>
     * If the NPC is global, it will be visible to all players.
     */
    private boolean global = true;

    /**
     * The NPC render distance.
     */
    private Integer renderDistance = 32;

    /**
     * The NPC name (nameplate).
     */
    private String name = "§7";

    /**
     * Creates a new NPC configuration.
     *
     * @param location the NPC location
     * @param skin    the NPC skin
     */
    private NpcConfig(@NotNull Location location, @Nullable Skin skin) {
        this.location = location;
        this.skin = skin;
    }

    /**
     * Creates a new NPC configuration.
     *
     * @param location the NPC location
     * @param skin the NPC skin
     * @return the NPC configuration
     */
    public static NpcConfig create(@NotNull Location location, @Nullable Skin skin) {
        return new NpcConfig(location, skin);
    }

    /**
     * Set whether the NPC is global or not.
     *
     * @param global whether the NPC is global or not
     * @return the NPC configuration
     */
    public @NotNull NpcConfig setGlobal(boolean global) {
        this.global = global;
        return this;
    }

    /**
     * Set the NPC render distance.
     *
     * @param renderDistance the NPC render distance
     * @return the NPC configuration
     */
    public @NotNull NpcConfig setRenderDistance(int renderDistance) {
        this.renderDistance = renderDistance;
        return this;
    }

    /**
     * Set the NPC name (nameplate).
     *
     * @param name the NPC name
     * @return the NPC configuration
     */
    public @NotNull NpcConfig setName(@NotNull String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the NPC location.
     *
     * @return the NPC location
     */
    public @NotNull Location getLocation() {
        return location;
    }

    /**
     * Get the NPC skin.
     *
     * @return the NPC skin
     */
    public @Nullable Skin getSkin() {
        return skin;
    }

    /**
     * Get whether the NPC is global or not (see {@link NpcConfig#global}).
     *
     * @return whether the NPC is global or not
     */
    public boolean isGlobal() {
        return global;
    }

    /**
     * Get the NPC render distance.
     *
     * @return the NPC render distance
     */
    public @NotNull Integer getRenderDistance() {
        return renderDistance;
    }

    /**
     * Get the NPC name (nameplate).
     *
     * @return the NPC name
     */
    public @NotNull String getName() {
        return name;
    }

}
