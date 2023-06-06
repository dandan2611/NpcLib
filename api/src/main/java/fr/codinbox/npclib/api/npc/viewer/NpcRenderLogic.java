package fr.codinbox.npclib.api.npc.viewer;

import fr.codinbox.npclib.api.npc.Npc;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a NPC render logic.
 * <br>
 * A render logic is used to determine whether a NPC should be rendered or not.
 */
public interface NpcRenderLogic {

    /**
     * Check if the NPC should be rendered.
     *
     * @param npc the NPC
     * @param player the player
     * @param viewer the viewer
     * @return {@code true} if the NPC should be rendered, {@code false} otherwise
     */
    boolean shouldBeRendered(@NotNull Npc npc, @NotNull Player player, @Nullable NpcViewer viewer);

}
