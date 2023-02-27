package fr.codinbox.npclib.api.npc;

import fr.codinbox.npclib.api.npc.event.NpcClickedEvent;
import fr.codinbox.npclib.api.npc.event.NpcClickedListener;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import fr.codinbox.npclib.api.npc.skin.Skin;
import fr.codinbox.npclib.api.reactive.Reactive;
import fr.codinbox.npclib.api.reactive.ReactiveList;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface Npc {

    /**
     * Get the NPC holder.
     * The holder is the object that created the NPC and is responsible for its management.
     *
     * @return the NPC holder
     */
    @NotNull
    NpcHolder getHolder();

    /**
     * Get the NPC location reactive.
     *
     * @return the NPC location reactive
     */
    @NotNull
    Reactive<Location> getLocationReactive();

    /**
     * Get the NPC world.
     *
     * @return the NPC world
     */
    @NotNull
    default World getWorld() {
        return this.getLocationReactive().get().getWorld();
    }

    /**
     * Get the NPC skin reactive.
     *
     * @return the NPC skin reactive
     */
    @NotNull
    Reactive<Skin> getSkinReactive();

    /**
     * Get the NPC entity id.
     * The entity ID is NOT stored in the server as a regular entity. You can't use it to get the entity from the server.
     *
     * @return the NPC entity id
     */
    @NotNull
    int getEntityId();

    /**
     * Get the NPC UUID.
     * The UUID is NOT stored in the server as a regular entity. You can't use it to get the entity from the server.
     *
     * @return the NPC UUID
     */
    @NotNull
    UUID getUUID();

    /**
     * Get the NPC viewers reactive.
     *
     * @return the NPC viewers reactive
     */
    @NotNull ReactiveList<UUID> getViewersReactive();

    /**
     * Get if the NPC is global.
     * If the NPC is global, it will be visible for all players within its range.
     *
     * @return if the NPC is global
     */
    @NotNull Reactive<Boolean> getGlobalReactive();

    @NotNull ReactiveList<NpcClickedListener> getClickedListeners();

    /**
     * Call the NPC clicked listeners.
     *
     * @param event the NPC clicked event
     */
    void callClickedListeners(@NotNull NpcClickedEvent event);

    /**
     * Get the NPC render distance reactive.
     * The render distance is the distance in blocks at which the NPC will be visible.
     *
     * @return the NPC render distance reactive
     */
    @NotNull Reactive<Integer> getRenderDistanceReactive();

    /**
     * Get the NPC name reactive.
     * The name is the name of the NPC. It is displayed above the NPC's head.
     *
     * @return the NPC name reactive
     */
    @NotNull Reactive<String> getNameReactive();

    @NotNull
    default Set<UUID> getRenderedFor() {
        return this.getHolder().getRenderedPlayers(this);
    }

}
