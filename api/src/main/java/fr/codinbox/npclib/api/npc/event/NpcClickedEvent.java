package fr.codinbox.npclib.api.npc.event;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NpcClickedEvent {

    @NotNull
    private final Npc npc;

    @NotNull
    private final Player player;

    @NotNull
    private final NpcHolder holder;

    @NotNull
    private final InteractionType interactionType;

    public NpcClickedEvent(@NotNull Npc npc, @NotNull Player player, @NotNull NpcHolder holder, @NotNull InteractionType interactionType) {
        this.npc = npc;
        this.player = player;
        this.holder = holder;
        this.interactionType = interactionType;
    }

    public Npc getNpc() {
        return npc;
    }

    public Player getPlayer() {
        return player;
    }

    public NpcHolder getHolder() {
        return holder;
    }

    public InteractionType getInteractionType() {
        return interactionType;
    }

    public enum InteractionType {
        ATTACK,
        INTERACT,
        INTERACT_AT;
    }

}
