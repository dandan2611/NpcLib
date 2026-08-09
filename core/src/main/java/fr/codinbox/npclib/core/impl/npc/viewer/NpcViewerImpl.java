package fr.codinbox.npclib.core.impl.npc.viewer;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation.EntityAnimationType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.CollisionRule;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.NameTagVisibility;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.OptionData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.ScoreBoardTeamInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.TeamMode;
import fr.codinbox.npclib.api.npc.Npc;
import fr.codinbox.npclib.api.npc.animation.AnimationType;
import fr.codinbox.npclib.api.npc.equipment.NpcEquipment;
import fr.codinbox.npclib.api.npc.name.NpcName;
import fr.codinbox.npclib.api.npc.viewer.NpcViewer;
import fr.codinbox.npclib.core.impl.packet.NpcNamePacket;
import fr.codinbox.npclib.core.impl.packet.NpcPacket;
import java.util.List;
import java.util.UUID;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NpcViewerImpl implements NpcViewer {

    private final Npc npc;

    private final UUID uuid;

    private final int nameEntityId;

    private final UUID nameEntityUuid;

    private boolean rendered;

    private @Nullable NpcName customName;

    private int customNameFrame;

    private int customNameFrameTicks;

    private boolean customNameRendered;

    public NpcViewerImpl(Npc npc, UUID uuid, int nameEntityId, UUID nameEntityUuid) {
        this.npc = npc;
        this.uuid = uuid;
        this.nameEntityId = nameEntityId;
        this.nameEntityUuid = nameEntityUuid;
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
            this.customName = null;
            this.customNameRendered = false;
            return;
        }
        if (rendered) {
            // The NPC should be rendered
            if (!this.npc.isNameVisible()) {
                this.updateVanillaNameVisibility(player, true);
            }
            NpcPacket.PLAYER_INFO_ADD.send(PacketEvents.getAPI(), player, this.npc);
            NpcPacket.PLAYER_SPAWN.send(PacketEvents.getAPI(), player, this.npc);
            NpcPacket.ENTITY_METADATA.send(PacketEvents.getAPI(), player, this.npc);
            NpcPacket.HEAD_ROTATION.send(PacketEvents.getAPI(), player, this.npc);
            this.rendered = true;
            this.updateEquipment(this.npc.getEquipment());
            this.updateCustomName();
        } else {
            // The NPC should be destroyed
            this.destroyCustomName(player);
            NpcPacket.PLAYER_DESPAWN.send(PacketEvents.getAPI(), player, this.npc);
            NpcPacket.PLAYER_INFO_REMOVE.send(PacketEvents.getAPI(), player, this.npc);
            if (!this.npc.isNameVisible()) {
                this.updateVanillaNameVisibility(player, false);
            }
            this.rendered = false;
        }
    }

    @Override
    public void render() {
        var player = player();
        if (player == null) {
            // Player is not online
            this.rendered = false;
            this.customName = null;
            this.customNameRendered = false;
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

        final WrapperPlayServerEntityAnimation packet = new WrapperPlayServerEntityAnimation(
            npc.getEntityId(), EntityAnimationType.values()[animationType.ordinal()]
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    @Override
    public void updateEquipment(@NotNull NpcEquipment equipment) {
        var player = player();
        if (player == null || !this.rendered) {
            return;
        }
        List<Equipment> slots = List.of(
                new Equipment(EquipmentSlot.HELMET, item(equipment.getHelmet())),
                new Equipment(EquipmentSlot.CHEST_PLATE, item(equipment.getChestplate())),
                new Equipment(EquipmentSlot.LEGGINGS, item(equipment.getLeggings())),
                new Equipment(EquipmentSlot.BOOTS, item(equipment.getBoots())),
                new Equipment(EquipmentSlot.MAIN_HAND, item(equipment.getMainHand())),
                new Equipment(EquipmentSlot.OFF_HAND, item(equipment.getOffHand()))
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(
                player,
                new WrapperPlayServerEntityEquipment(this.npc.getEntityId(), slots)
        );
    }

    @Override
    public void updateCustomName() {
        var player = player();
        if (player == null || !this.rendered) {
            this.customName = null;
            this.customNameRendered = false;
            return;
        }
        this.destroyCustomName(player);
        this.customName = this.npc.getCustomName(player);
        this.customNameFrame = 0;
        this.customNameFrameTicks = 0;
        if (this.customName == null) {
            return;
        }
        NpcNamePacket.spawn(player, this.npc, this.nameEntityId, this.nameEntityUuid, this.customName);
        this.customNameRendered = true;
    }

    @Override
    public void tickCustomName() {
        if (!this.rendered || !this.customNameRendered || this.customName == null
                || this.customName.getFrames().size() < 2) {
            return;
        }
        this.customNameFrameTicks++;
        if (this.customNameFrameTicks < this.customName.getFrames().get(this.customNameFrame).durationTicks()) {
            return;
        }
        this.customNameFrameTicks = 0;
        this.customNameFrame = (this.customNameFrame + 1) % this.customName.getFrames().size();
        var player = player();
        if (player != null) {
            NpcNamePacket.updateFrame(
                    player,
                    this.nameEntityId,
                    this.customName,
                    this.customName.getFrames().get(this.customNameFrame)
            );
        }
    }

    private void destroyCustomName(Player player) {
        if (this.customNameRendered) {
            NpcNamePacket.destroy(player, this.nameEntityId);
        }
        this.customName = null;
        this.customNameRendered = false;
        this.customNameFrame = 0;
        this.customNameFrameTicks = 0;
    }

    private void updateVanillaNameVisibility(Player player, boolean hide) {
        String teamName = "nl" + Integer.toUnsignedString(this.npc.getEntityId(), 16);
        WrapperPlayServerTeams packet;
        if (hide) {
            var teamInfo = new ScoreBoardTeamInfo(
                    Component.empty(),
                    Component.empty(),
                    Component.empty(),
                    NameTagVisibility.NEVER,
                    CollisionRule.ALWAYS,
                    NamedTextColor.WHITE,
                    OptionData.NONE
            );
            packet = new WrapperPlayServerTeams(teamName, TeamMode.CREATE, teamInfo, NpcPacket.profileName(this.npc));
        } else {
            packet = new WrapperPlayServerTeams(teamName, TeamMode.REMOVE, (ScoreBoardTeamInfo) null, List.of());
        }
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    private static ItemStack item(@Nullable org.bukkit.inventory.ItemStack item) {
        return item == null ? ItemStack.EMPTY : SpigotConversionUtil.fromBukkitItemStack(item);
    }

}
