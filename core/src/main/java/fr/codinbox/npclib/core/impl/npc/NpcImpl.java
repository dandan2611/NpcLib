package fr.codinbox.npclib.core.impl.npc;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.NpcConfig;
import fr.codinbox.npclib.api.npc.event.NpcClickedEvent;
import fr.codinbox.npclib.api.npc.event.NpcClickedListener;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import fr.codinbox.npclib.api.npc.skin.Skin;
import fr.codinbox.npclib.api.reactive.Reactive;
import fr.codinbox.npclib.api.reactive.ReactiveList;
import fr.codinbox.npclib.api.reactive.ReactiveListener;
import fr.codinbox.npclib.core.impl.reactive.ReactiveImpl;
import fr.codinbox.npclib.core.impl.reactive.ReactiveListImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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
    private final Reactive<String> name;

    public NpcImpl(NpcHolder holder,
                   int entityId,
                   UUID uuid,
                   NpcConfig config) {
        this.holder = holder;

        this.location = new ReactiveImpl<>(config.getLocation());
        this.location.addListener((r, p, n) -> this.update());

        this.skin = new ReactiveImpl<>(config.getSkin());
        this.skin.addListener((r, p, n) -> this.update());

        this.entityId = entityId;
        this.uuid = uuid;
        this.global = new ReactiveImpl<>(config.isGlobal());
        this.global.addListener((r, p, n) -> this.update());

        this.viewers = new ReactiveListImpl<>();
        this.viewers.addListener((r, p, n) -> n.forEach(i -> {
            var pl = Bukkit.getPlayer(i);
            if (pl == null)
                return;
            if (!this.holder.getShownNpcs(i).contains(this))
                this.holder.performChecks(this, pl);
        }));

        this.clickedListeners = new ReactiveListImpl<>();

        this.renderDistance = config.getRenderDistance() != null ? new ReactiveImpl<>(config.getRenderDistance())
                : new ReactiveImpl<>(this.holder.getConfiguration().getNpcViewDistance());
        this.renderDistance.addListener((r, p, n) -> this.update());

        this.name = new ReactiveImpl<>(config.getName());
        this.name.addListener((r, p, n) -> this.update());
    }

    @Override
    public @NotNull NpcHolder getHolder() {
        return this.holder;
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
    public @NotNull Reactive<Integer> getRenderDistanceReactive() {
        return this.renderDistance;
    }

    @Override
    public @NotNull Reactive<String> getNameReactive() {
        return this.name;
    }

    private void update() {
        var renderers = this.getRenderedFor();
        for (UUID renderer : renderers) {
            var pl = Bukkit.getPlayer(renderer);
            if (pl == null) continue;
            this.holder.setRendered(this, pl, false);
            this.holder.setRendered(this, pl, true);
        }
    }

    @Override
    public int hashCode() {
        return this.getWorld().getKey().hashCode() + this.uuid.hashCode() + this.entityId;
    }

}
