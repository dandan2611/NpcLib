package fr.codinbox.npclib.api.npc;

import com.destroystokyo.paper.SkinParts;
import fr.codinbox.npclib.api.npc.skin.Skin;
import fr.codinbox.npclib.api.npc.skin.SkinPart;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

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
    private String name = "";

    /**
     * Whether the NPC should look at the player or not.
     */
    private boolean lookAtPlayer = false;

    /**
     * The NPC look at player range.
     */
    private double lookAtPlayerRange = 32;

    /**
     * The NPC skin parts.
     */
    private SkinPart skinPart = SkinPart.all();

    /**
     * The NPC UUID.
     */
    private UUID uuid;

    /**
     * Creates a new NPC configuration.
     *
     * @param uuid the NPC UUID. If null, a random UUID will be generated
     * @param location the NPC location
     * @param skin the NPC skin
     */
    private NpcConfig(@Nullable UUID uuid, @NotNull Location location, @Nullable Skin skin) {
        this.location = location;
        this.skin = skin;
        this.uuid = (uuid != null) ? uuid : UUID.randomUUID();
    }

    /**
     * Creates a new NPC configuration.
     *
     * @param uuid the NPC UUID. If null, a random UUID will be generated
     * @param location the NPC location
     * @param skin the NPC skin
     * @return the NPC configuration
     */
    public static @NotNull NpcConfig create(@Nullable UUID uuid, @NotNull Location location, @Nullable Skin skin) {
        return new NpcConfig(uuid, location, skin);
    }

    /**
     * Creates a new NPC configuration.
     *
     * @param location the NPC location
     * @param skin the NPC skin
     * @return the NPC configuration
     */
    public static @NotNull NpcConfig create(@NotNull Location location, @Nullable Skin skin) {
        return create(UUID.randomUUID(), location, skin);
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
     * @param name the NPC name (should not be blank)
     * @return the NPC configuration
     */
    public @NotNull NpcConfig setName(@NotNull String name) {
        if (name.isBlank())
            throw new IllegalArgumentException("The NPC name cannot be blank");
        this.name = name;
        return this;
    }

    /**
     * Set whether the NPC should look at the player or not.
     *
     * @param lookAtPlayer whether the NPC should look at the player or not
     * @return the NPC configuration
     */
    public @NotNull NpcConfig setLookAtPlayer(boolean lookAtPlayer) {
        this.lookAtPlayer = lookAtPlayer;
        return this;
    }

    /**
     * Set the NPC look at player range.
     *
     * @param lookAtPlayerRange the NPC look at player range
     * @return the NPC configuration
     */
    public NpcConfig setLookAtPlayerRange(double lookAtPlayerRange) {
        this.lookAtPlayerRange = lookAtPlayerRange;
        return this;
    }

    /**
     * Set the NPC skin parts.
     *
     * @param skinPart the NPC skin parts
     * @return the NPC configuration
     */
    public @NotNull NpcConfig setSkinPart(SkinPart skinPart) {
        this.skinPart = skinPart;
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

    /**
     * Get whether the NPC should look at the player or not (see {@link NpcConfig#lookAtPlayer}).
     *
     * @return whether the NPC should look at the player or not
     */
    public boolean isLookAtPlayer() {
        return lookAtPlayer;
    }

    /**
     * Get the NPC look at player range.
     *
     * @return the NPC look at player range
     */
    public double getLookAtPlayerRange() {
        return lookAtPlayerRange;
    }

    /**
     * Get the NPC skin parts.
     *
     * @return the NPC skin parts
     */
    public @NotNull SkinPart getSkinPart() {
        return skinPart;
    }

    /**
     * Get the NPC UUID.
     *
     * @return the NPC UUID
     */
    public @NotNull UUID getUuid() {
        return uuid;
    }

}
