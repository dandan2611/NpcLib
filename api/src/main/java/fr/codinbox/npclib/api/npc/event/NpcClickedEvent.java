package fr.codinbox.npclib.api.npc.event;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * An object containing information about a npc clicked event.
 */
public class NpcClickedEvent {

    @NotNull
    private final Npc npc;

    @NotNull
    private final Player player;

    @NotNull
    private final NpcHolder holder;

    @NotNull
    private final InteractionType interactionType;

    /**
     * Create a new npc clicked event.
     *
     * @param npc the npc
     * @param player the player
     * @param holder the holder
     * @param interactionType the interaction type
     */
    public NpcClickedEvent(@NotNull Npc npc, @NotNull Player player, @NotNull NpcHolder holder, @NotNull InteractionType interactionType) {
        this.npc = npc;
        this.player = player;
        this.holder = holder;
        this.interactionType = interactionType;
    }

    /**
     * Get the npc.
     *
     * @return the npc
     */
    public Npc getNpc() {
        return npc;
    }

    /**
     * Get the player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the holder.
     *
     * @return the holder
     */
    public NpcHolder getHolder() {
        return holder;
    }

    /**
     * Get the interaction type.
     *
     * @return the interaction type
     */
    public InteractionType getInteractionType() {
        return interactionType;
    }

    /**
     * The interaction type.
     */
    public enum InteractionType {

        /**
         * The player left clicked the npc.
         */
        ATTACK,

        /**
         * The player right clicked the npc.
         */
        INTERACT,

        /**
         * The player right clicked the npc with an item.
         */
        INTERACT_AT;
    }

}
