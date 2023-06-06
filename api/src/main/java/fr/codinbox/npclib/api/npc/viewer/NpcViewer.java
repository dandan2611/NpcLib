package fr.codinbox.npclib.api.npc.viewer;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.animation.AnimationType;
import fr.codinbox.npclib.api.npc.equipment.NpcEquipment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Represents a NPC viewer.
 * <br>
 * A NPC viewer is a player (online or not) that can see a NPC.
 */
public interface NpcViewer {

    /**
     * Get the NPC.
     *
     * @return the NPC
     */
    @NotNull Npc getNpc();

    /**
     * Get the viewer UUID.
     * <br>
     * Please use this method in order to identify a viewer. Do not use {@link #player()}.
     *
     * @return the viewer UUID
     */
    @NotNull UUID getUuid();

    /**
     * Get the viewer player.
     * <br>
     * If you wish to identify a viewer, please use {@link #getUuid()} instead, as the player may be offline.
     *
     * @return the viewer player or {@code null} if the player is offline
     */
    @Nullable Player player();

    /**
     * Get if the NPC is rendered (visible) for this viewer.
     * @return {@code true} if the NPC is rendered, {@code false} otherwise
     */
    boolean isRendered();

    /**
     * Update the rendered property of the viewer based on built-in logic.
     * <br>
     * This method calls the {@link NpcRenderLogic#shouldBeRendered(Npc, Player, NpcViewer)} method and runs further logic.
     * It is automatically called on few game events in order to update the NPC visibility for the viewer.
     */
    void render();

    /**
     * Set the rendered property of the viewer.
     * <br>
     * This method <b>does not</b> hide the NPC <b>permanently</b> for the viewer, as the built-in logic may override it.
     * If you wish to hide the NPC permanently, please use {@link Npc#removeViewer(UUID)} in order to remove the viewer from the NPC.
     * 
     * @param rendered {@code true} if the NPC should be rendered, {@code false} otherwise
     */
    void setRendered(boolean rendered);

    /**
     * Play an animation for the viewer.
     *
     * @param animationType the animation type
     */
    void playAnimation(@NotNull AnimationType animationType);

    /**
     * Update the equipment for the viewer.
     *
     * @param equipment the equipment
     */
    void updateEquipment(@NotNull NpcEquipment equipment);

}
