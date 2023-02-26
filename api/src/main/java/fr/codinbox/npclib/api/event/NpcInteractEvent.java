package fr.codinbox.npclib.api.event;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NpcInteractEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;

    private final Npc npc;
    private final NpcHolder holder;

    public NpcInteractEvent(Npc npc, NpcHolder holder) {
        this.npc = npc;
        this.holder = holder;
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

    public Npc getNpc() {
        return npc;
    }

    public NpcHolder getHolder() {
        return holder;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
