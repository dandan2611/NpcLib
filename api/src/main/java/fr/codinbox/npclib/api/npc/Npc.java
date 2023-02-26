package fr.codinbox.npclib.api.npc;

import fr.codinbox.npclib.api.npc.skin.Skin;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public interface Npc {

    /**
     * Get the NPC location.
     *
     * @return the NPC location
     */
    @NotNull
    Location getLocation();

    /**
     * Get the NPC world.
     *
     * @return the NPC world
     */
    @NotNull
    default World getWorld() {
        return this.getLocation().getWorld();
    }

    /**
     * Get the NPC skin.
     *
     * @return the NPC skin
     */
    @NotNull
    Skin getSkin();

    @NotNull
    int getEntityId();

    @NotNull
    UUID getUUID();

    @NotNull Set<UUID> getViewers();

    boolean isGlobal();

}
