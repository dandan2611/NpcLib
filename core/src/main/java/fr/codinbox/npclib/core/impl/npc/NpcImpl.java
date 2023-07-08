package fr.codinbox.npclib.core.impl.npc;

import com.google.common.collect.ImmutableMap;
import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.NpcConfig;
import fr.codinbox.npclib.api.npc.animation.AnimationType;
import fr.codinbox.npclib.api.npc.animation.NpcAnimation;
import fr.codinbox.npclib.api.npc.equipment.NpcEquipment;
import fr.codinbox.npclib.api.npc.event.NpcClickedEvent;
import fr.codinbox.npclib.api.npc.event.NpcClickedListener;
import fr.codinbox.npclib.api.npc.holder.NpcHolder;
import fr.codinbox.npclib.api.npc.skin.Skin;
import fr.codinbox.npclib.api.npc.skin.SkinPart;
import fr.codinbox.npclib.api.npc.viewer.NpcRenderLogic;
import fr.codinbox.npclib.api.npc.viewer.NpcViewer;
import fr.codinbox.npclib.core.impl.npc.animation.NpcAnimationImpl;
import fr.codinbox.npclib.core.impl.npc.equipment.NpcEquipmentImpl;
import fr.codinbox.npclib.core.impl.npc.viewer.NpcViewerImpl;
import fr.codinbox.npclib.core.impl.npc.viewer.render.WorldDistanceRenderLogic;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;

public class NpcImpl implements Npc {

    private final NpcHolder holder;
    private final Location location;
    private final Skin skin;
    private int entityId;
    private UUID uuid;
    private final HashMap<UUID, NpcViewer> viewers;
    private final boolean global;
    private final HashSet<NpcClickedListener> clickedListeners;
    private final int renderDistance;
    private final String name;
    private final NpcRenderLogic renderLogic;
    private final NpcEquipment equipment;
    private boolean lookAt;
    private double lookAtDistance;
    private SkinPart skinPart;

    public NpcImpl(NpcHolder holder,
                   int entityId,
                   NpcConfig config) {
        this.holder = holder;
        this.renderLogic = new WorldDistanceRenderLogic();
        this.location = config.getLocation().clone();
        this.skin = config.getSkin();
        this.entityId = entityId;
        this.uuid = config.getUuid();
        this.global = config.isGlobal();
        this.viewers = new HashMap<>();
        this.clickedListeners = new HashSet<>();
        this.renderDistance = config.getRenderDistance();
        this.name = config.getName();
        this.equipment = new NpcEquipmentImpl(this);
        this.lookAt = config.isLookAtPlayer();
        this.lookAtDistance = config.getLookAtPlayerRange();
        this.skinPart = config.getSkinPart();
    }

    @Override
    public @NotNull NpcHolder getHolder() {
        return this.holder;
    }

    @Override
    public @NotNull Location getLocation() {
        return this.location;
    }

    @Override
    public @Nullable Skin getSkin() {
        return this.skin;
    }

    @Override
    public int getEntityId() {
        return this.entityId;
    }

    @Override
    public @NotNull UUID getUUID() {
        return this.uuid;
    }

    @Override
    public @NotNull ImmutableMap<UUID, NpcViewer> getViewers() {
        return ImmutableMap.copyOf(this.viewers);
    }

    @Override
    public boolean isGlobal() {
        return this.global;
    }

    @Override
    public @NotNull Set<NpcClickedListener> getClickedListeners() {
        return this.clickedListeners;
    }

    @Override
    public void callClickedListeners(@NotNull NpcClickedEvent event) {
        // TODO: This is not thread safe (ConcurrentModificationException)
        this.clickedListeners.forEach(listener -> listener.onNpcClicked(event));
    }

    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int getRenderDistance() {
        return this.renderDistance;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    private void update() {
        this.viewers.values().forEach(viewer -> {
            if (viewer.isRendered()) {
                viewer.setRendered(false);
                viewer.setRendered(true);
            }
        });
    }

    @Override
    public @NotNull NpcRenderLogic getRenderLogic() {
        return this.renderLogic;
    }

    @Override
    public void addViewer(@NotNull UUID uuid) {
        this.viewers.put(uuid, new NpcViewerImpl(this, uuid));
    }

    @Override
    public void removeViewer(@NotNull UUID uuid) {
        var viewer = this.getViewer(uuid);

        if (viewer == null)
            return;
        viewer.setRendered(false); // Delete the rendered NPC for the player
        this.viewers.remove(uuid);
    }

    @Override
    public @NotNull NpcAnimation createAnimation(@NotNull AnimationType animationType) {
        return new NpcAnimationImpl(this, animationType);
    }

    @Override
    public void playAnimation(@NotNull NpcAnimation animation) {
        var animationViewers = animation.getViewers();

        animationViewers
                .forEach(v -> Objects.requireNonNull(this.getViewer(v)).playAnimation(animation.getAnimationType()));
    }

    @Override
    public @NotNull NpcEquipment getEquipment() {
        return this.equipment;
    }

    @Override
    public boolean isLookingAtPlayer() {
        return this.lookAt;
    }

    @Override
    public void setLookingAtPlayer(boolean lookingAtPlayer) {
        this.lookAt = lookingAtPlayer;
    }

    @Override
    public double getLookingAtPlayerRange() {
        return this.lookAtDistance;
    }

    @Override
    public void setLookingAtPlayerRange(double lookingAtPlayerRange) {
        this.lookAtDistance = lookingAtPlayerRange;
    }

    public @NotNull SkinPart getDisplayedSkinParts() {
        return skinPart;
    }

    public void setSkinPart(SkinPart skinPart) {
        this.skinPart = skinPart;
    }

    @Override
    public int hashCode() {
        return this.getWorld().getKey().hashCode() + this.uuid.hashCode() + this.entityId;
    }

}
