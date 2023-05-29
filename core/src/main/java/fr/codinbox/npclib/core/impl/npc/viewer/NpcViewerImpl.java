package fr.codinbox.npclib.core.impl.npc.viewer;

import com.comphenix.protocol.ProtocolLibrary;
import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.animation.AnimationType;
import fr.codinbox.npclib.api.npc.viewer.NpcViewer;
import fr.codinbox.npclib.core.impl.packet.NpcPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NpcViewerImpl implements NpcViewer {

    private final Npc npc;

    private final UUID uuid;

    private boolean rendered;

    public NpcViewerImpl(Npc npc, UUID uuid) {
        this.npc = npc;
        this.uuid = uuid;
        this.rendered = false;
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
    public boolean isRendered() {
        return this.rendered;
    }

    @Override
    public void setRendered(boolean rendered) {
        this.updateRender(rendered);
    }

    private void updateRender(boolean rendered) {
        var wasRendered = this.rendered;
        if (wasRendered == rendered)
            return;
        var player = player();
        if (player == null) {
            // Player is not online
            this.rendered = false;
            return;
        }
        if (rendered) {
            // The NPC should be rendered
            NpcPacket.PLAYER_INFO_ADD.send(ProtocolLibrary.getProtocolManager(), player, this.npc);
            NpcPacket.PLAYER_SPAWN.send(ProtocolLibrary.getProtocolManager(), player, this.npc);
            //this.packetStation.createEntityMetadataPacket(this.npc, null, player);
            NpcPacket.HEAD_ROTATION.send(ProtocolLibrary.getProtocolManager(), player, this.npc);
        } else {
            // The NPC should be destroyed
            NpcPacket.PLAYER_DESPAWN.send(ProtocolLibrary.getProtocolManager(), player, this.npc);
            NpcPacket.PLAYER_INFO_REMOVE.send(ProtocolLibrary.getProtocolManager(), player, this.npc);
        }
        this.rendered = rendered;
    }

    @Override
    public void render() {
        var player = player();
        if (player == null) {
            // Player is not online
            this.rendered = false;
            return;
        }

        if (!this.npc.isGlobal()) {
            if (this.rendered && !this.npc.hasViewer(this.uuid)) {
                this.updateRender(false);
                return;
            }
        }

       if (!this.npc.getRenderLogic().shouldBeRendered(this.npc, player, this)) {
            // The NPC should not be rendered
            this.updateRender(false);
            return;
       }

        // The player is close enough to the NPC
        this.updateRender(true);
    }

    @Override
    public void playAnimation(@NotNull AnimationType animationType) {
        var player = player();
        if (player == null || !this.rendered) {
            // Player is not online
            return;
        }
        //this.packetStation.createEntityAnimationPacket(this.npc, animationType, null, player);
        // TODO: Fix animations
    }

}
