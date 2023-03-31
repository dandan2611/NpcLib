package fr.codinbox.npclib.core.impl.npc;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.NpcConfig;
import fr.codinbox.npclib.api.npc.animation.AnimationType;
import fr.codinbox.npclib.api.npc.animation.NpcAnimation;
import fr.codinbox.npclib.api.npc.event.NpcClickedEvent;
import fr.codinbox.npclib.api.npc.event.NpcClickedListener;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import fr.codinbox.npclib.api.npc.skin.Skin;
import fr.codinbox.npclib.api.npc.viewer.NpcRenderLogic;
import fr.codinbox.npclib.api.npc.viewer.NpcViewer;
import fr.codinbox.npclib.api.reactive.Reactive;
import fr.codinbox.npclib.api.reactive.ReactiveList;
import fr.codinbox.npclib.api.reactive.ReactiveMap;
import fr.codinbox.npclib.core.NpcLibPlugin;
import fr.codinbox.npclib.core.impl.npc.animation.NpcAnimationImpl;
import fr.codinbox.npclib.core.impl.npc.viewer.NpcViewerImpl;
import fr.codinbox.npclib.core.impl.npc.viewer.render.WorldDistanceRenderLogic;
import fr.codinbox.npclib.core.impl.reactive.ReactiveImpl;
import fr.codinbox.npclib.core.impl.reactive.ReactiveListImpl;
import fr.codinbox.npclib.core.impl.reactive.ReactiveMapImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NpcImpl implements Npc {

    private final NpcHolder holder;
    private final Reactive<Location> location;
    private final ReactiveImpl<Skin> skin;
    private int entityId;
    private UUID uuid;
    private final ReactiveMap<UUID, NpcViewer> viewers;
    private final Reactive<Boolean> global;
    private final ReactiveList<NpcClickedListener> clickedListeners;
    private final Reactive<Integer> renderDistance;
    private final Reactive<String> name;
    private final NpcRenderLogic renderLogic;

    public NpcImpl(NpcHolder holder,
                   int entityId,
                   UUID uuid,
                   NpcConfig config) {
        this.holder = holder;
        this.renderLogic = new WorldDistanceRenderLogic();

        this.location = new ReactiveImpl<>(config.getLocation());
        this.location.addListener((r, p, n) -> this.update());

        this.skin = new ReactiveImpl<>(config.getSkin());
        this.skin.addListener((r, p, n) -> this.update());

        this.entityId = entityId;
        this.uuid = uuid;
        this.global = new ReactiveImpl<>(config.isGlobal());
        this.global.addListener((r, p, n) -> this.update());

        this.viewers = new ReactiveMapImpl<>(new ConcurrentHashMap<>());
        this.viewers.addListener((r, p, n) -> n.values().forEach(NpcViewer::render));

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
    public @NotNull ReactiveMap<UUID, NpcViewer> getViewersReactive() {
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
        this.getViewersReactive().values()
                .forEach(NpcViewer::render);
    }

    @Override
    public @NotNull NpcRenderLogic getRenderLogic() {
        return this.renderLogic;
    }

    @Override
    public void addViewer(@NotNull UUID uuid) {
        this.viewers.put(uuid, new NpcViewerImpl(this, uuid, NpcLibPlugin.station()));
    }

    @Override
    public void removeViewer(@NotNull UUID uuid) {
        var viewer = this.getViewer(uuid);

        if (viewer == null)
            return;
        viewer.getRendered().set(false); // Delete the rendered NPC for the player
        this.viewers.remove(uuid);
    }

    @Override
    public @NotNull NpcAnimation createAnimation(@NotNull AnimationType animationType) {
        return new NpcAnimationImpl(this, animationType);
    }

    @Override
    public void playAnimation(@NotNull NpcAnimation animation) {
        var animationViewers = animation.getViewers();

        animationViewers.stream()
                .filter(this::isRenderedFor)
                .forEach(v -> Objects.requireNonNull(this.getViewer(v)).playAnimation(animation.getAnimationType()));
    }

    @Override
    public int hashCode() {
        return this.getWorld().getKey().hashCode() + this.uuid.hashCode() + this.entityId;
    }

}
