package fr.codinbox.npclib.core.impl.npc;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.event.NpcClickedEvent;
import fr.codinbox.npclib.api.npc.event.NpcClickedListener;
import fr.codinbox.npclib.api.npc.skin.Skin;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NpcImpl implements Npc {

    private Location location;

    private Skin skin;

    private int entityId;

    private UUID uuid;

    private final Set<UUID> viewers;

    private boolean global;

    private final Set<NpcClickedListener> listeners = new HashSet<>();

    public NpcImpl(Location location, Skin skin, int entityId, UUID uuid, boolean global) {
        this.location = location;
        this.skin = skin;
        this.entityId = entityId;
        this.uuid = uuid;
        this.global = global;
        this.viewers = new HashSet<>();
    }

    @Override
    public @NotNull Location getLocation() {
        return this.location;
    }

    @Override
    public @NotNull Skin getSkin() {
        return this.skin;
    }

    @Override
    public @NotNull int getEntityId() {
        return this.entityId;
    }

    @Override
    public @NotNull UUID getUUID() {
        return this.uuid;
    }

    @Override
    public @NotNull Set<UUID> getViewers() {
        return this.viewers;
    }

    @Override
    public boolean isGlobal() {
        return this.global;
    }

    @Override
    public void addClickedListener(@NotNull NpcClickedListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void callClickedListeners(@NotNull NpcClickedEvent event) {
        this.listeners.forEach(listener -> listener.onNpcClicked(event));
    }

    @Override
    public int hashCode() {
        return this.location.getWorld().hashCode() + this.uuid.hashCode() + this.entityId;
    }

}
