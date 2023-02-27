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

    private Integer renderDistance = null;

    private NpcConfig(@NotNull Location location, @NotNull Skin skin) {
        this.location = location;
        this.skin = skin;
    }

    public static NpcConfig create(@NotNull Location location, @NotNull Skin skin) {
        return new NpcConfig(location, skin);
    }

    public Location getLocation() {
        return location;
    }

    public Skin getSkin() {
        return skin;
    }

    public NpcConfig setGlobal(boolean global) {
        this.global = global;
        return this;
    }

    public NpcConfig setRenderDistance(Integer renderDistance) {
        this.renderDistance = renderDistance;
        return this;
    }

    public boolean isGlobal() {
        return global;
    }

    public Integer getRenderDistance() {
        return renderDistance;
    }

}
