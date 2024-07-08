package fr.codinbox.npclib.api.npc.holder;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.NpcConfig;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * A NPC Holder is a manager object able to create, manage and destroy NPCs.
 */
public interface NpcHolder {

    /**
     * Create a new npc.
     *
     * @param config the npc config
     * @return the npc
     */
    @NotNull Npc createNpc(@NotNull NpcConfig config);

    /**
     * Destroy a npc.
     *
     * @param npc the npc
     */
    void destroyNpc(@NotNull Npc npc);

    /**
     * Get all the npcs of the holder.
     *
     * @return the npcs
     */
    @NotNull Set<@NotNull Npc> getNpcs();

    /**
     * Get all the npcs in a world.
     *
     * @param world the world
     * @return the npcs
     */
    @NotNull Set<@NotNull Npc> getNpcsInWorld(@NotNull World world);

    /**
     * Check if a npc is rendered for a player (visible).
     *
     * @param npc the npc
     * @param player the player
     * @return true if the npc is rendered, false otherwise
     */
    boolean isRendered(@NotNull Npc npc, @NotNull Player player);

    /**
     * Update a npc for a player.
     * <br>
     * This method check if the NPC should be rendered or not for the player.
     *
     * @param npc the npc
     * @param player the player
     */
    void updateVisibility(@NotNull Npc npc, @NotNull Player player);

    /**
     * Get all the npcs rendered for a player.
     *
     * @param player the player
     * @return the npcs
     */
    @NotNull Set<@NotNull Npc> getRenderedNpcs(@NotNull Player player);

    /**
     * Get all the npcs rendered for a player.
     *
     * @param player the player
     * @return the npcs
     */
    @NotNull Set<@NotNull Npc> getRenderedNpcs(@NotNull UUID player);

    /**
     * Get all the players rendered for a npc.
     *
     * @param npc the npc
     * @return the players
     */
    @NotNull Set<@NotNull UUID> getRenderedPlayers(@NotNull Npc npc);

    /**
     * Get the plugin of the holder.
     *
     * @return the plugin
     */
    @NotNull Plugin getPlugin();

}
