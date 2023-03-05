package fr.codinbox.npclib.core.impl.npc.viewer;

import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.animation.AnimationType;
import fr.codinbox.npclib.api.npc.viewer.NpcRenderLogic;
import fr.codinbox.npclib.api.npc.viewer.NpcViewer;
import fr.codinbox.npclib.api.packet.PacketStation;
import fr.codinbox.npclib.api.reactive.Reactive;
import fr.codinbox.npclib.core.impl.npc.viewer.render.WorldDistanceRenderLogic;
import fr.codinbox.npclib.core.impl.reactive.ReactiveImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NpcViewerImpl implements NpcViewer {

    private final Npc npc;

    private final UUID uuid;

    private final Reactive<Boolean> rendered;

    private final PacketStation<?> packetStation;

    public NpcViewerImpl(Npc npc, UUID uuid, PacketStation<?> packetStation) {
        this.npc = npc;
        this.uuid = uuid;
        this.packetStation = packetStation;
        this.rendered = new ReactiveImpl<>(false);
        this.rendered.addListener(this::renderInternal);
    }

    @Override
    public @NotNull Npc getNpc() {
        return this.npc;
    }

    @Override
    public @NotNull UUID getUuid() {
        return this.uuid;
    }

    @Override
    public @Nullable Player player() {
        return Bukkit.getPlayer(this.uuid);
    }

    @Override
    public @NotNull Reactive<Boolean> getRendered() {
        return this.rendered;
    }

    private void renderInternal(Reactive<Boolean> r, Boolean p, Boolean n) {
        if (p == n)
            return;
        var player = player();
        if (player == null) {
            // Player is not online
            this.rendered.set(false, false);
            return;
        }
        if (n) {
            // The NPC should be rendered
            this.packetStation.createPlayerInfoPacket(this.npc, null, player);
            this.packetStation.createPlayerSpawnPacket(this.npc, null, player);
        } else {
            // The NPC should be destroyed
            this.packetStation.createPlayerDespawnPacket(this.npc, null, player);
            this.packetStation.createPlayerInfoRemovePacket(this.npc, null, player);
        }
    }

    @Override
    public void render() {
        var player = player();
        if (player == null) {
            // Player is not online
            this.rendered.set(false, false);
            return;
        }

        if (!this.npc.getGlobalReactive().get()) {
            if (this.rendered.get() && !this.npc.hasViewer(this.uuid)) {
                this.rendered.set(false);
                return;
            }
        }

       if (!this.npc.getRenderLogic().shouldBeRendered(this.npc, player, this)) {
            // The NPC should not be rendered
            this.rendered.set(false);
            return;
       }

        // The player is close enough to the NPC
        this.rendered.set(true);
    }

    @Override
    public void playAnimation(@NotNull AnimationType animationType) {
        var player = player();
        if (player == null) {
            // Player is not online
            return;
        }
        this.packetStation.createEntityAnimationPacket(this.npc, animationType, null, player);
    }

}
