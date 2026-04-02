package fr.codinbox.npclib.core.impl.packet;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityHeadLook;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRotation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoRemove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.Action;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.PlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import fr.codinbox.npclib.api.npc.Npc;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface NpcPacket {

    NpcPacket PLAYER_INFO_ADD = (protocolManager, player, npc) -> {
        final List<TextureProperty> properties = new ArrayList<>();
        if (npc.getSkin() != null) {
            properties.add(new TextureProperty("textures", npc.getSkin().getValue(), npc.getSkin().getSignature()));
        }

        final WrapperPlayServerPlayerInfoUpdate packet = new WrapperPlayServerPlayerInfoUpdate(
            EnumSet.of(Action.ADD_PLAYER, Action.UPDATE_LISTED), List.of(new PlayerInfo(
            new UserProfile(npc.getUUID(), npc.getName(), properties),
            false,
            0,
            GameMode.CREATIVE,
            Component.text(npc.getName()),
            null,
            0,
            true
        ))
        );

        protocolManager.getPlayerManager().sendPacket(player, packet);
    };

    NpcPacket PLAYER_INFO_REMOVE = (protocolManager, player, npc) -> {
        final WrapperPlayServerPlayerInfoRemove packet = new WrapperPlayServerPlayerInfoRemove(List.of(npc.getUUID()));

        protocolManager.getPlayerManager().sendPacket(player, packet);
    };

    NpcPacket PLAYER_SPAWN = (protocolManager, player, npc) -> {
        final Location location = npc.getLocation();

        final WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(
            npc.getEntityId(),
            Optional.of(npc.getUUID()),
            EntityTypes.PLAYER,
            new Vector3d(location.getX(), location.getY(), location.getZ()),
            location.getPitch(),
            location.getYaw(),
            0.0f,
            0,
            Optional.empty()
        );

        protocolManager.getPlayerManager().sendPacket(player, packet);
    };

    NpcPacket PLAYER_DESPAWN = (protocolManager, player, npc) -> {
        final WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(npc.getEntityId());

        protocolManager.getPlayerManager().sendPacket(player, packet);
    };

    NpcPacket HEAD_ROTATION = (protocolManager, player, npc) -> {
        final WrapperPlayServerEntityHeadLook packet = new WrapperPlayServerEntityHeadLook(
            npc.getEntityId(),
            (byte) (npc.getLocation().getYaw() * 256.0F / 360.0F)
        );

        protocolManager.getPlayerManager().sendPacket(player, packet);

        final WrapperPlayServerEntityRotation bodyPacket = new WrapperPlayServerEntityRotation(
            npc.getEntityId(),
            (byte) (npc.getLocation().getYaw() * 256.0F / 360.0F),
            (byte) (npc.getLocation().getPitch() * 256.0F / 360.0F),
            true
        );

        protocolManager.getPlayerManager().sendPacket(player, bodyPacket);
    };

    NpcPacket ENTITY_METADATA = (protocolManager, player, npc) -> {
        final List<EntityData<?>> data = new ArrayList<>();
        data.add(new EntityData<>(16, EntityDataTypes.BYTE, npc.getDisplayedSkinParts().getValue()));

        final WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(
            npc.getEntityId(),
            data
        );

        protocolManager.getPlayerManager().sendPacket(player, packet);
    };

    void send(@NotNull PacketEventsAPI<?> protocolManager, @NotNull Player player, @NotNull Npc npc);

}
