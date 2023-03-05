package fr.codinbox.npclib.core.impl.npc.animation;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.animation.AnimationType;
import fr.codinbox.npclib.api.npc.animation.NpcAnimation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NpcAnimationImpl implements NpcAnimation {

    private final Npc npc;
    private final AnimationType animationType;
    private final Set<UUID> viewers;

    public NpcAnimationImpl(Npc npc, AnimationType animationType) {
        this.npc = npc;
        this.animationType = animationType;
        this.viewers = new HashSet<>();
    }

    @Override
    public @NotNull Npc getNpc() {
        return this.npc;
    }

    @Override
    public @NotNull AnimationType getAnimationType() {
        return this.animationType;
    }

    @Override
    public @NotNull NpcAnimation withAllViewers() {
        this.viewers.addAll(this.npc.getRenderers());
        return this;
    }

    @Override
    public @NotNull NpcAnimation withViewers(@NotNull UUID... viewers) {
        this.viewers.addAll(Set.of(viewers));
        return this;
    }

    @Override
    public @NotNull NpcAnimation withViewers(@NotNull Player... viewers) {
        this.viewers.addAll(Arrays.stream(viewers).map(Player::getUniqueId).toList());
        return this;
    }

    @Override
    public @NotNull NpcAnimation withViewer(@NotNull UUID viewer) {
        this.viewers.add(viewer);
        return this;
    }

    @Override
    public @NotNull NpcAnimation withViewer(@NotNull Player viewer) {
        this.viewers.add(viewer.getUniqueId());
        return this;
    }

    @Override
    public @NotNull NpcAnimation withoutViewers(@NotNull UUID... viewers) {
        this.viewers.removeAll(Set.of(viewers));
        return this;
    }

    @Override
    public @NotNull NpcAnimation withoutViewers(@NotNull Player... viewers) {
        Arrays.stream(viewers).map(Player::getUniqueId).toList().forEach(this.viewers::remove);
        return this;
    }

    @Override
    public @NotNull NpcAnimation withoutViewer(@NotNull UUID viewer) {
        this.viewers.remove(viewer);
        return this;
    }

    @Override
    public @NotNull NpcAnimation withoutViewer(@NotNull Player viewer) {
        this.viewers.remove(viewer.getUniqueId());
        return this;
    }

    @Override
    public @NotNull Set<UUID> getViewers() {
        return this.viewers;
    }

}
