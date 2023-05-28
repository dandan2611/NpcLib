package fr.codinbox.npclib.core.impl.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import fr.codinbox.npclib.api.npc.Npc;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public interface NpcPacket {

    NpcPacket PLAYER_INFO = (player, npc) -> {
        var profile = new WrappedGameProfile(npc.getUUID(), npc.getName());
        profile.getProperties().clear();
        profile.getProperties().put("textures",
                new WrappedSignedProperty("textures",
                        npc.getSkin().getValue(),
                        npc.getSkin().getSignature()));

        var packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoActions().write(0,
                Set.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER, EnumWrappers.PlayerInfoAction.UPDATE_LISTED));
        packet.getPlayerInfoDataLists().write(1, Collections.singletonList(
                new PlayerInfoData(
                        profile,
                        0,
                        EnumWrappers.NativeGameMode.CREATIVE,
                        null,
                        null
                )
        ));
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    };

    NpcPacket PLAYER_SPAWN = (player, npc) -> {
        var location = npc.getLocation();
        var packet = new PacketContainer(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        packet.getIntegers().write(0, npc.getEntityId());
        packet.getUUIDs().write(0, npc.getUUID());
        packet.getDoubles().write(0 ,location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());
        packet.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
        packet.getBytes().write(1, (byte) (location.getPitch() * 256.0F / 360.0F));
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    };

    void send(@NotNull Player player, @NotNull Npc npc);

}
