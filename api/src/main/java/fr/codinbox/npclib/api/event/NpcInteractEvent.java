package fr.codinbox.npclib.api.event;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NpcInteractEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;

    @NotNull
    private final Npc npc;

    @NotNull
    private final NpcHolder holder;

    @NotNull
    private final Player player;

    @NotNull
    private final InteractionType interactionType;

    public NpcInteractEvent(@NotNull Npc npc,
                            @NotNull NpcHolder holder,
                            @NotNull Player player,
                            @NotNull InteractionType interactionType) {
        this.npc = npc;
        this.holder = holder;
        this.player = player;
        this.interactionType = interactionType;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public Npc getNpc() {
        return npc;
    }

    @NotNull
    public NpcHolder getHolder() {
        return holder;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public enum InteractionType {
        PUNCH,
        INTERACT,
        INTERACT_AT
    }

}
