package fr.codinbox.npclib.api.npc;

import com.google.common.collect.ImmutableMap;
import fr.codinbox.npclib.api.npc.animation.AnimationType;
import fr.codinbox.npclib.api.npc.animation.NpcAnimation;
import fr.codinbox.npclib.api.npc.event.NpcClickedEvent;
import fr.codinbox.npclib.api.npc.event.NpcClickedListener;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import fr.codinbox.npclib.api.npc.skin.Skin;
import fr.codinbox.npclib.api.npc.viewer.NpcRenderLogic;
import fr.codinbox.npclib.api.npc.viewer.NpcViewer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Map;
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

    @NotNull Location getLocation();

    @NotNull default World getWorld() {
        return this.getLocation().getWorld();
    }

    @NotNull Skin getSkin();

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

    @NotNull ImmutableMap<UUID, NpcViewer> getViewers();

    boolean isGlobal();

    /**
     * Get the NPC clicked listeners.
     *
     * @return the NPC clicked listeners
     */
    @NotNull Set<NpcClickedListener> getClickedListeners();

    /**
     * Call the NPC clicked listeners.
     *
     * @param event the NPC clicked event
     */
    void callClickedListeners(@NotNull NpcClickedEvent event);

    @Range(from = 0, to = Integer.MAX_VALUE) int getRenderDistance();

    @NotNull String getName();

    default boolean isRenderedFor(@NotNull UUID uuid) {
        return this.getViewers().get(uuid) != null;
    }

    /**
     * Check if the NPC is rendered for a player.
     *
     * @param player the player
     * @return if the NPC is rendered for the player
     */
    default boolean isRenderedFor(@NotNull Player player) {
        return this.isRenderedFor(player.getUniqueId());
    }

    /**
     * Check if the NPC is rendered for a player.
     *
     * @param uuid the player UUID
     * @return if the NPC is rendered for the player
     */
    default boolean hasViewer(@NotNull UUID uuid) {
        return this.getViewers().containsKey(uuid);
    }

    /**
     * Get the viewer object of a player.
     *
     * @param uuid the player UUID
     * @return the viewer object of the player
     */
    default @Nullable NpcViewer getViewer(@NotNull UUID uuid) {
        return this.getViewers().get(uuid);
    }

    /**
     * Get the rendered viewers UUIDs.
     * The rendered viewers are the players that can see the NPC.
     *
     * @return the rendered viewers UUIDs
     */
    @NotNull
    default Set<UUID> getRenderers() {
        return this.getViewers().values().stream()
                .filter(viewer -> viewer.isRendered())
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
        var player = Bukkit.getPlayer(uuid);
        if (viewer == null) {
            // Viewer is not in the cache
            if (!this.isGlobal())
                return; // NPC is not global
            // Check if the NPC should be rendered for the viewer
            if (player == null)
                return; // Player is not online
            if (player.getWorld() != this.getWorld())
                return; // Player is not in the same world as the NPC
            if (getRenderLogic().shouldBeRendered(this, player, null)) {
                this.addViewer(uuid);
            }
        } else {
            if (player == null) // Player is not online, set the rendered property to false
                viewer.setRendered(false, false);
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
