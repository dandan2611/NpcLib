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
import fr.codinbox.npclib.api.npc.name.NpcName;
import fr.codinbox.npclib.api.npc.skin.Skin;
import fr.codinbox.npclib.api.npc.skin.SkinPart;
import fr.codinbox.npclib.api.npc.viewer.NpcRenderLogic;
import fr.codinbox.npclib.api.npc.viewer.NpcViewer;
import fr.codinbox.npclib.core.impl.npc.animation.NpcAnimationImpl;
import fr.codinbox.npclib.core.impl.npc.equipment.NpcEquipmentImpl;
import fr.codinbox.npclib.core.impl.npc.viewer.NpcViewerImpl;
import fr.codinbox.npclib.core.impl.npc.viewer.render.WorldDistanceRenderLogic;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public class NpcImpl implements Npc {

    private final NpcHolder holder;
    private final Location location;
    private final Skin skin;
    private int entityId;
    private final int nameEntityId;
    private final UUID nameEntityUuid;
    private UUID uuid;
    private final ConcurrentHashMap<UUID, NpcViewer> viewers;
    private final boolean global;
    private final CopyOnWriteArraySet<NpcClickedListener> clickedListeners;
    private final int renderDistance;
    private final String name;
    private final boolean nameVisible;
    private final Function<Player, NpcName> customNameProvider;
    private final NpcRenderLogic renderLogic;
    private final NpcEquipment equipment;
    private boolean lookAt;
    private double lookAtDistance;
    private SkinPart skinPart;
    private boolean displayInTablist;

    public NpcImpl(NpcHolder holder, int entityId, int nameEntityId, NpcConfig config) {
        this.holder = holder;
        this.renderLogic = new WorldDistanceRenderLogic();
        this.location = config.getLocation().clone();
        this.skin = config.getSkin();
        this.entityId = entityId;
        this.nameEntityId = nameEntityId;
        this.nameEntityUuid = UUID.randomUUID();
        this.uuid = config.getUuid();
        this.global = config.isGlobal();
        this.viewers = new ConcurrentHashMap<>();
        this.clickedListeners = new CopyOnWriteArraySet<>();
        this.renderDistance = config.getRenderDistance();
        this.name = config.getName();
        this.nameVisible = config.isNameVisible();
        this.customNameProvider = config.getCustomNameProvider();
        this.equipment = new NpcEquipmentImpl(this);
        this.lookAt = config.isLookAtPlayer();
        this.lookAtDistance = config.getLookAtPlayerRange();
        this.skinPart = config.getSkinPart();
        this.displayInTablist = config.isDisplayInTablist();
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

    public int getNameEntityId() {
        return this.nameEntityId;
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

    @Override
    public boolean isNameVisible() {
        return this.nameVisible;
    }

    @Override
    public @Nullable NpcName getCustomName(@NotNull Player player) {
        if (this.customNameProvider == null)
            return null;
        try {
            return this.customNameProvider.apply(player);
        } catch (RuntimeException exception) {
            this.holder.getPlugin().getLogger().warning(
                    "Could not resolve the custom name of NPC " + this.uuid + " for " + player.getName()
                            + ": " + exception.getMessage()
            );
            return null;
        }
    }

    @Override
    public boolean hasCustomName() {
        return this.customNameProvider != null;
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
    public @NotNull NpcViewer addViewer(@NotNull UUID uuid) {
        final NpcViewer viewer = new NpcViewerImpl(this, uuid, this.nameEntityId, this.nameEntityUuid);
        this.viewers.put(uuid, viewer);
        return viewer;
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

        animationViewers.forEach(v -> Objects.requireNonNull(this.getViewer(v))
                                             .playAnimation(animation.getAnimationType()));
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

    public boolean isDisplayInTablist() {
        return displayInTablist;
    }

    @Override
    public int hashCode() {
        return this.getWorld().getKey().hashCode() + this.uuid.hashCode() + this.entityId;
    }

}
