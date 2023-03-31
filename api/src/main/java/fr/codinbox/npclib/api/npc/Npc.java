package fr.codinbox.npclib.api.npc;

import fr.codinbox.npclib.api.npc.animation.AnimationType;
import fr.codinbox.npclib.api.npc.animation.NpcAnimation;
import fr.codinbox.npclib.api.npc.event.NpcClickedEvent;
import fr.codinbox.npclib.api.npc.event.NpcClickedListener;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import fr.codinbox.npclib.api.npc.skin.Skin;
import fr.codinbox.npclib.api.npc.viewer.NpcRenderLogic;
import fr.codinbox.npclib.api.npc.viewer.NpcViewer;
import fr.codinbox.npclib.api.reactive.Reactive;
import fr.codinbox.npclib.api.reactive.ReactiveList;
import fr.codinbox.npclib.api.reactive.ReactiveMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @NotNull ReactiveMap<UUID, NpcViewer> getViewersReactive();

    /**
     * Get if the NPC is global.
     * If the NPC is global, it will be visible for all players within its range.
     *
     * @return if the NPC is global
     */
    @NotNull Reactive<Boolean> getGlobalReactive();

    /**
     * Get the NPC clicked listeners.
     *
     * @return the NPC clicked listeners
     */
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

    /**
     * Check if the NPC is rendered for a player.
     *
     * @param player the player
     * @return if the NPC is rendered for the player
     */
    default boolean isRenderedFor(@NotNull Player player) {
        return this.getViewersReactive().get(player.getUniqueId()) != null;
    }

    default boolean isRenderedFor(@NotNull UUID uuid) {
        return this.getViewersReactive().get(uuid) != null;
    }

    /**
     * Check if the NPC is rendered for a player.
     *
     * @param uuid the player UUID
     * @return if the NPC is rendered for the player
     */
    default boolean hasViewer(@NotNull UUID uuid) {
        return this.getViewersReactive().containsKey(uuid);
    }

    /**
     * Get the viewer object of a player.
     *
     * @param uuid the player UUID
     * @return the viewer object of the player
     */
    default @Nullable NpcViewer getViewer(@NotNull UUID uuid) {
        return this.getViewersReactive().get(uuid);
    }

    /**
     * Get the rendered viewers UUIDs.
     * The rendered viewers are the players that can see the NPC.
     *
     * @return the rendered viewers UUIDs
     */
    @NotNull
    default Set<UUID> getRenderers() {
        return this.getViewersReactive().values().stream()
                .filter(viewer -> viewer.getRendered().get())
                .map(NpcViewer::getUuid)
                .collect(Collectors.toSet());
    }

    /**
     * Perform the check to see if the NPC should be rendered for a player, and if so, render it.
     *
     * @param uuid the player UUID
     */
    default void renderFor(@NotNull UUID uuid) {
        var viewer = this.getViewer(uuid);
        if (viewer == null) {
            // Viewer is not in the cache
            if (!this.getGlobalReactive().get())
                return; // NPC is not global
            // Check if the NPC should be rendered for the viewer
            var player = Bukkit.getPlayer(uuid);
            if (player == null)
                return; // Player is not online
            if (player.getWorld() != this.getWorld())
                return; // Player is not in the same world as the NPC
            if (getRenderLogic().shouldBeRendered(this, player, null)) {
                this.addViewer(uuid);
            }
        } else {
            // Viewer exists in the cache
            // Just call the viewer render logic
            viewer.render();
        }
    }

    /**
     * Get the render logic of the NPC.
     * The render logic is the object that is responsible for checking if the NPC should be rendered for a player.
     *
     * @return the render logic of the NPC
     */
    @NotNull NpcRenderLogic getRenderLogic();

    /**
     * Add a viewer to the NPC.
     *
     * @param uuid the viewer UUID
     */
    void addViewer(@NotNull UUID uuid);

    /**
     * Remove a viewer from the NPC.
     *
     * @param uuid the viewer UUID
     */
    void removeViewer(@NotNull UUID uuid);

    /**
     * Create a new NPC animation from the current NPC.
     *
     * @param animationType the animation type
     * @return the NPC animation
     */
    @NotNull NpcAnimation createAnimation(@NotNull AnimationType animationType);

    /**
     * Play an animation on the NPC.
     *
     * @param animation the animation
     */
    void playAnimation(@NotNull NpcAnimation animation);

}
