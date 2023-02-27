package fr.codinbox.npclib.core.impl.npc;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.NpcConfig;
import fr.codinbox.npclib.api.npc.event.NpcClickedEvent;
import fr.codinbox.npclib.api.npc.event.NpcClickedListener;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import fr.codinbox.npclib.api.npc.skin.Skin;
import fr.codinbox.npclib.api.reactive.Reactive;
import fr.codinbox.npclib.api.reactive.ReactiveList;
import fr.codinbox.npclib.core.impl.reactive.ReactiveImpl;
import fr.codinbox.npclib.core.impl.reactive.ReactiveListImpl;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class NpcImpl implements Npc {

    private final NpcHolder holder;
    private final Reactive<Location> location;
    private final ReactiveImpl<Skin> skin;
    private int entityId;
    private UUID uuid;
    private final ReactiveList<UUID> viewers;
    private final Reactive<Boolean> global;
    private final ReactiveList<NpcClickedListener> clickedListeners;
    private final Reactive<Integer> renderDistance;

    public NpcImpl(NpcHolder holder,
                   int entityId,
                   UUID uuid,
                   NpcConfig config) {
        this.holder = holder;
        this.location = new ReactiveImpl<>(config.getLocation()); // TODO: Location change listener
        this.skin = new ReactiveImpl<>(config.getSkin()); // TODO: Skin change listener
        this.entityId = entityId;
        this.uuid = uuid;
        this.global = new ReactiveImpl<>(config.isGlobal()); // TODO: Global change listener

        this.viewers = new ReactiveListImpl<>(); // TODO: Viewers change listener
        this.clickedListeners = new ReactiveListImpl<>();
        this.renderDistance = config.getRenderDistance() != null ? new ReactiveImpl<>(config.getRenderDistance())
                : new ReactiveImpl<>(this.holder.getConfiguration().getNpcViewDistance()); // TODO: Render distance change listener
    }

    @Override
    public @NotNull Reactive<Location> getLocationReactive() {
        return this.location;
    }

    @Override
    public @NotNull Reactive<Skin> getSkinReactive() {
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
    public @NotNull ReactiveList<UUID> getViewersReactive() {
        return this.viewers;
    }

    @Override
    public @NotNull Reactive<Boolean> getGlobalReactive() {
        return this.global;
    }

    @Override
    public @NotNull ReactiveList<NpcClickedListener> getClickedListeners() {
        return this.clickedListeners;
    }

    @Override
    public void callClickedListeners(@NotNull NpcClickedEvent event) {
        this.clickedListeners.stream().forEach(listener -> listener.onNpcClicked(event));
    }

    @Override
    public Reactive<Integer> getRenderDistanceReactive() {
        return this.renderDistance;
    }

    @Override
    public int hashCode() {
        return this.getWorld().getKey().hashCode() + this.uuid.hashCode() + this.entityId;
    }

}
