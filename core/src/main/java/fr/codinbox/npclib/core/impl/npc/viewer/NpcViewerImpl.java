package fr.codinbox.npclib.core.impl.npc.viewer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.animation.AnimationType;
import fr.codinbox.npclib.api.npc.equipment.NpcEquipment;
import fr.codinbox.npclib.api.npc.viewer.NpcViewer;
import fr.codinbox.npclib.core.impl.packet.NpcPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
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
            NpcPacket.ENTITY_METADATA.send(ProtocolLibrary.getProtocolManager(), player, this.npc);
            NpcPacket.HEAD_ROTATION.send(ProtocolLibrary.getProtocolManager(), player, this.npc);
            this.updateEquipment(this.npc.getEquipment());
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

        var protocolManager = ProtocolLibrary.getProtocolManager();
        var packet = new PacketContainer(PacketType.Play.Server.ANIMATION);
        packet.getIntegers().write(0, npc.getEntityId());
        packet.getIntegers().write(1, animationType.getId());
        protocolManager.sendServerPacket(player, packet);
    }

    @Override
    public void updateEquipment(@NotNull NpcEquipment equipment) {
        var protocolManager = ProtocolLibrary.getProtocolManager();
        var player = player();
        if (player == null || !this.rendered) {
            // Player is not online
            return;
        }
        var packet = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0, npc.getEntityId());
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
        list.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, equipment.getHelmet()));
        list.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, equipment.getChestplate()));
        list.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, equipment.getLeggings()));
        list.add(new Pair<>(EnumWrappers.ItemSlot.FEET, equipment.getBoots()));
        list.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, equipment.getMainHand()));
        list.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, equipment.getOffHand()));
        packet.getSlotStackPairLists().write(0, list);
        protocolManager.sendServerPacket(player, packet);
    }

}
